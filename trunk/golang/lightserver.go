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
	CONTENT_TYPE = "Content-Type"
	TEXT_PLAIN = "text/plain"
	TEXT_HTML = "text/html"
	TEXT_CSS = "text/css"
	IMAGE_JPEG = "image/jpeg"
	IMAGE_PNG = "image/png"
	IMAGE_GIF = "image/gif"
	IMAGE_ICON = "image/vnd.microsoft.icon"
	IMAGE_PSD = "image/vnd.adobe.photoshop"
	AUDIO_WAV = "audio/wav"
	VEDIO_MP4 = "video/mp4"
	VEDIO_MPEG = "video/mpeg"
	VEDIO_QUICKTIME = "video/quicktime"
	VEDIO_ASF = "video/x-ms-asf"
	VEDIO_WMV = "video/x-ms-wmv"
	VEDIO_AVI = "video/x-msvideo"
	VEDIO_MOVIE = "video/x-sgi-movie"
	APPLICATION_PDF = "application/pdf"
	APPLICATION_XML = "application/xml"
	APPLICATION_JS = "application/javascript"
	APPLICATION_RM = "application/vnd.rn-realmedia"
	APPLICATION_OCTET_STREAM = "application/octet-stream"
)

var (
	lightHttpServerPort = flag.Int("port", 8888, "listen port")
	lightHttpServerPath = ""
	lightHttpServerPrintVerbose = true
	mimeTypeMap = map[string]string {
	            		"go": TEXT_PLAIN, 
			    		"java": TEXT_PLAIN, 
						"txt": TEXT_PLAIN, 
						"properties": TEXT_PLAIN,
						"yaml": TEXT_PLAIN,
						"css": TEXT_CSS,
						"htm": TEXT_HTML,
						"html": TEXT_HTML,
						"jpg": IMAGE_JPEG,
						"jpe": IMAGE_JPEG,
						"jpeg": IMAGE_JPEG,
						"png": IMAGE_PNG,
						"gif": IMAGE_GIF,
						"ico": IMAGE_ICON,
						"pdf": APPLICATION_PDF,
						"psd": IMAGE_PSD,
						"xml": APPLICATION_XML,
						"js": APPLICATION_JS,
						"rm": APPLICATION_RM,
						"wav": AUDIO_WAV,
						"mp4": VEDIO_MP4,
						"mp4v": VEDIO_MP4,
						"mpg4": VEDIO_MP4,
						"mpeg": VEDIO_MPEG,
						"mpg": VEDIO_MPEG,
						"mpe": VEDIO_MPEG,
						"m1v": VEDIO_MPEG,
						"m2v": VEDIO_MPEG,
						"qt": VEDIO_QUICKTIME,
						"mov": VEDIO_QUICKTIME,
						"asf": VEDIO_ASF,
						"asx": VEDIO_ASF,
						"wmv": VEDIO_WMV,
						"avi": VEDIO_AVI,
						"movie": VEDIO_MOVIE,
	}
)

func DisplayUsage() {
	fmt.Println(LIGHT_HTTP_SERVER_NAME, "(Version: ", LIGHT_HTTP_SERVER_VERSION, ")")
	fmt.Println("Author: Hatter Jiang")
	fmt.Println("URL: http://hatter.in")
	fmt.Println("Usage:")
	fmt.Println("  lightserver [options] [dir]")
	fmt.Println("    -port               http listen port")
	os.Exit(0);
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
		openFileMimeType := mimeTypeMap[openFileSuffix]
		if openFileMimeType == "" {
			openFileMimeType = APPLICATION_OCTET_STREAM
		}
		w.WriteHeader(200)
		w.Header().Set(CONTENT_TYPE, openFileMimeType)
		
		err := CopyBytes(openFile, w);
		if err != nil {
			fmt.Println("[ERROR] Error in write file to response: ", fmt.Sprintf("%T %v", err, err))
		}
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
			fileSize = " (" + lib.ToSize(fileInfo.Size()) + ")"
		}
		fmt.Fprint(w, "<li>", "<a href=\"", basePath, fileInfo.Name(), "\">", fName, "</a>", fileSize, "</li>");
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
				break;
			}
			return err
		}
	}
	return nil
}

func main() {
	// init
	flag.Usage = DisplayUsage
	flag.Parse();
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
