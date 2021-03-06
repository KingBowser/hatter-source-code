// lightserver.go
package main

import (
	"./lib"
	"os"
	"io"
	"fmt"
	"flag"
	"io/ioutil"
	"net/http"
)

const (
	LIGHT_HTTP_SERVER_NAME = "Light http server"
	LIGHT_HTTP_SERVER_VERSION = "0.0.1"
)

var (
	lightHttpServerPort = flag.Int("port", 8888, "listen port")
	lightHttpServerPath = ""
	lightHttpServerPrintVerbose = true
)

func DisplayUsage() {
	fmt.Println(LIGHT_HTTP_SERVER_NAME, "(Version: ", LIGHT_HTTP_SERVER_VERSION, ")")
	fmt.Println("Author: Hatter Jiang")
	fmt.Println("URL: http://hatter.in")
	fmt.Println("Usage:")
	fmt.Println("  lightserver [options] [dir]")
	fmt.Println("    -port               http listen port")
	os.Exit(0)
}

func DisplayStartmessage() {
	fmt.Println("Server base path: ", lightHttpServerPath)
	fmt.Println(LIGHT_HTTP_SERVER_NAME, "start up at:", *lightHttpServerPort)
}

type HttpServerHandle struct {}

func (h HttpServerHandle) ServeHTTP (
	w http.ResponseWriter,
	r *http.Request) {
	if (lightHttpServerPrintVerbose) {
		fmt.Println("Request url:", r.URL)
	}
	w.Header().Set("Server", "LightHttpServer/" + LIGHT_HTTP_SERVER_VERSION)
	filePath := lightHttpServerPath + r.URL.Path
	openFile, openFileError := os.Open(filePath)
	if openFileError != nil {
		if os.IsNotExist(openFileError) {
			w.WriteHeader(404)
		} else {
			w.WriteHeader(500)
			fmt.Println("[ERROR] ", fmt.Sprintf("%T %v", openFileError, openFileError))
		}
		fmt.Fprint(w, "Error: File or Directory not exits.")
		return
	}
	defer openFile.Close()
	openFileInfo, _ := openFile.Stat()
	openFileIsDir := openFileInfo.Mode().IsDir()
	
	if !openFileIsDir {
		openFileSuffix := lib.GetSuffix(openFileInfo.Name())
		openFileMimeType := lib.GetContentType(openFileSuffix)
		w.WriteHeader(200)
		w.Header().Set(lib.CONTENT_TYPE, openFileMimeType)
		
		err := CopyBytes(openFile, w)
		if err != nil {
			fmt.Println("[ERROR] Error in write file to response: ", fmt.Sprintf("%T %v", err, err))
		}
		return
	}
	
	readDirFileInfos, readDirError := ioutil.ReadDir(lightHttpServerPath + r.URL.Path)
	if readDirError != nil {
		w.WriteHeader(500)
		fmt.Fprint(w, "Error: " + fmt.Sprintf("%T %v", readDirError, readDirError))
		return
	}
	
	w.Header().Set(lib.CONTENT_TYPE, lib.TEXT_HTML)
	fmt.Fprint(w, "<h1>Listing dir:</h1>")
	fmt.Fprint(w, "<ul>")
	if (r.URL.Path != "/") {
		fmt.Fprint(w, "<li>", "<a href=\"..\">[..]</a>", "</li>")
	}
	for _, fileInfo := range readDirFileInfos {
		isDir := fileInfo.Mode().IsDir()
		fName := fileInfo.Name()
		if isDir {
			fName = "[" + fName + "]"
		}
		basePath := r.URL.Path
		if basePath != "/" {
			basePath = basePath + "/"
		}
		fileSize := ""
		if !isDir {
			fileSize = " (" + lib.ToSize(fileInfo.Size()) + ")"
		}
		fmt.Fprint(w, "<li>", "<a href=\"", basePath, fileInfo.Name(), "\">", fName, "</a>", fileSize, "</li>")
	}
	fmt.Fprint(w, "</ul>")
	fmt.Fprint(w, "<br/>", "<br/>")
	fmt.Fprint(w, "Powered by ", LIGHT_HTTP_SERVER_NAME, ", Version: ", LIGHT_HTTP_SERVER_VERSION, "<br/>")
}

func CopyBytes(file *os.File, writer http.ResponseWriter) error {
	buffer := make([]byte, lib.KB * 4)
	for {
		count, err := file.Read(buffer[0:])
		writer.Write(buffer[0:count])
		if err != nil {
			if err == io.EOF {
				break
			}
			return err
		}
	}
	return nil
}

func main() {
	// init
	flag.Usage = DisplayUsage
	flag.Parse()
	lightHttpServerPath, _ = os.Getwd()
	
	args := flag.Args()
	if len(args) > 0 {
		assignedPath := args[0]
		assignedFileInfo, assignedFileInfoError := os.Stat(assignedPath)
		if assignedFileInfoError != nil {
			fmt.Println("Directory not exists: ", assignedPath)
			os.Exit(0)
		}
		if !assignedFileInfo.IsDir() {
			fmt.Println("Target is file: " + assignedPath)
			os.Exit(0)
		}
		lightHttpServerPath = assignedPath
	}
	
	DisplayStartmessage()
	var h HttpServerHandle
	listenAndServeError := http.ListenAndServe(fmt.Sprintf(":%v", *lightHttpServerPort), h)
	if (listenAndServeError != nil) {
		fmt.Println(listenAndServeError)
	}
}
