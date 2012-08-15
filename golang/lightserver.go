// lightserver.go
package main

import (
	"os"
	"io"
	"fmt"
	"strings"
	"io/ioutil"
	"net/http"
)

const (
	LIGHT_HTTP_SERVER_NAME = "Light http server"
	LIGHT_HTTP_SERVER_VERSION = "0.0.1"
	CONTENT_TYPE = "Content-Type"
	TEXT_PLAIN = "text/plain"
	TEXT_HTML = "text/html"
	IMAGE_JPEG = "image/jpeg"
	IMAGE_PNG = "image/png"
	APPLICATION_PDF = "application/pdf"
	APPLICATION_XML = "application/xml"
	APPLICATION_OCTET_STREAM = "application/octet-stream"
	
	KB = 1024
	MB = KB * KB
	GB = KB * KB * KB
)

var (
	lightHttpServerPort = 8888
	lightHttpServerPath = ""
	lightHttpServerPrintVerbose = true
	mimeTypeMap = map[string]string {
	            		"go": TEXT_PLAIN, 
			    		"java": TEXT_PLAIN, 
						"txt": TEXT_PLAIN, 
						"properties": TEXT_PLAIN,
						"yaml": TEXT_PLAIN,
						"htm": TEXT_HTML,
						"html": TEXT_HTML,
						"jpg": IMAGE_JPEG,
						"png": IMAGE_PNG,
						"pdf": APPLICATION_PDF,
						"xml": APPLICATION_XML,
	}
)


func display_version() {
	fmt.Println(LIGHT_HTTP_SERVER_NAME, "(Version: ", LIGHT_HTTP_SERVER_VERSION, ")")
	fmt.Println("Author: Hatter Jiang")
	fmt.Println("URL: http://hatter.in")
	fmt.Println("Usage:")
	fmt.Println("  lightserver [options] [dir]")
	os.Exit(0);
}

func display_startmessage() {
	fmt.Println(LIGHT_HTTP_SERVER_NAME, "start up at:", lightHttpServerPort)
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
		lastIndexOfDot := strings.LastIndex(openFileInfo.Name(), ".")
		var openFileMimeType string = ""
		if lastIndexOfDot > -1 {
			openFileSuffix := openFileInfo.Name()[lastIndexOfDot + 1:]
			openFileMimeType = mimeTypeMap[openFileSuffix]
		}
		if openFileMimeType == "" {
			openFileMimeType = APPLICATION_OCTET_STREAM
		}
		w.Header().Set(CONTENT_TYPE, openFileMimeType)
		//readFileContent, readFileContentError := ioutil.ReadFile(filePath)
		//if readFileContentError != nil {
		//	fmt.Println("[ERROR] Read file ", fmt.Sprintf("%T %v", readFileContentError))
		//}
		buffer := make([]byte, KB * 4)
		for {
			count, err := openFile.Read(buffer[0:])
			w.Write(buffer[0:count])
			if err != nil {
				if err == io.EOF {
					break;
				}
				fmt.Println("[ERROR] Error in write file to response: ", fmt.Sprintf("%T %v", err, err))
			}
		}
		//w.Write(readFileContent)
		return
	}
	
	readDirFileInfos, readDirError := ioutil.ReadDir(lightHttpServerPath + r.URL.Path)
	if readDirError != nil {
		w.WriteHeader(500)
		fmt.Fprint(w, "Error: " + fmt.Sprintf("%T %v", readDirError, readDirError))
		return;
	}
	
	w.Header().Set(CONTENT_TYPE, TEXT_HTML)
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
			fileSize = " (" + toSize(fileInfo.Size()) + ")"
		}
		fmt.Fprint(w, "<li>", "<a href=\"", basePath, fileInfo.Name(), "\">", fName, "</a>", fileSize, "</li>");
	}
	fmt.Fprint(w, "</ul>")
	fmt.Fprint(w, "<br/>", "<br/>")
	fmt.Fprint(w, "Powered by ", LIGHT_HTTP_SERVER_NAME, ", Version: ", LIGHT_HTTP_SERVER_VERSION, "<br/>")
}

func toSize(size int64) string {
	if size > GB {
		return fmt.Sprintf("%vGB", size / GB)
	} else if size > MB {
		return fmt.Sprintf("%vMB", size / MB)
	} else if size > KB {
		return fmt.Sprintf("%vKB", size / KB)
	}
	return fmt.Sprintf("%v bytes", size)
}

func main() {
	// init
	lightHttpServerPath, _ = os.Getwd()
	
	display_startmessage()
	var h HttpServerHandle
	listenStr := ":" + fmt.Sprintf("%v", lightHttpServerPort)
	listenAndServeError := http.ListenAndServe(listenStr, h)
	if (listenAndServeError != nil) {
		fmt.Println(listenAndServeError)
	}
}
