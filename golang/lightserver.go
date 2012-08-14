// lightserver.go
package main

import (
	"os"
	"fmt"
	"io/ioutil"
	"net/http"
)

const (
	LIGHT_HTTP_SERVER_NAME = "Light http server"
	LIGHT_HTTP_SERVER_VERSION = "0.0.1"
)

var (
	lightHttpServerPort = 8888
	lightHttpServerPath = ""
	lightHttpServerPrintVerbose = true
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
	readDirFileInfos, readDirError := ioutil.ReadDir(lightHttpServerPath + r.URL.Path)
	if (readDirError != nil) {
		w.WriteHeader(500)
		fmt.Fprint(w, "Error: " + fmt.Sprintf("%T %v", readDirError, readDirError))
	}
	
	w.Header().Set("Content-Type", "text/html")
	fmt.Fprint(w, "Listing dir:<br/>")
	for _, fileInfo := range readDirFileInfos {
		fmt.Fprint(w, fileInfo.Name(), "<br/>");
	}
	fmt.Fprint(w, "<br/>", "<br/>")
	fmt.Fprint(w, "Powered by ", LIGHT_HTTP_SERVER_NAME, "Version: ", LIGHT_HTTP_SERVER_VERSION, "<br/>")
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
