// file proxy
package main

import (
	"io"
	"os"
	"fmt"
	"flag"
	"path"
	"regexp"
	"strings"
	"net/http"
)

var urlFileNameRegexp, _ = regexp.Compile("[^0-9a-zA-Z\\.]")

func DownloadGet(url, basePath string) (int64, error) {
	newFileName := GetRealFullFileName(basePath, GetFileName(url))
	fmt.Println("Download file from: ", url, " -> " + newFileName)
	resp, respError := http.Get(url)
	if (respError != nil) {
		return 0, respError
	}
	defer resp.Body.Close()
	newFile, newFileError := os.Create(newFileName)
	if newFileError != nil {
		return 0, newFileError
	}
	defer newFile.Close()
	copyCount, copyError := io.Copy(newFile, resp.Body)
	if copyError != nil {
		return 0, copyError
	}
	return copyCount, nil
}

func GetRealFullFileName(basePath, fileName string) string {
	fullFileName := path.Join(basePath, fileName)
	currentFullFileName := fullFileName
	currentFileIndex := 0
	for {
		openFile, openFileError := os.Open(currentFullFileName)
		if os.IsNotExist(openFileError) {
			return currentFullFileName
		}
		openFile.Close()
		currentFullFileName = fullFileName + fmt.Sprintf(".%v", currentFileIndex)
		currentFileIndex = currentFileIndex + 1
	}
	return "" // make the compile happy
}

func GetFileName(url string) string {
	lastIndexOfSlash := strings.LastIndex(url, "/")
	fileName := url
	if lastIndexOfSlash > -1 {
		fileName = url[lastIndexOfSlash + 1:]
	}
	if fileName == "" {
		fileName = url
	}
	fileName = urlFileNameRegexp.ReplaceAllString(fileName, "_")
	return fileName
}

type HttpServerHandle struct {}

func (h HttpServerHandle) ServeHTTP (
	w http.ResponseWriter,
	r *http.Request) {
	requestIsGet := (r.Method == "GET")
	w.Header().Set("Content-Type", "text/html")
	if requestIsGet {
		fmt.Fprint(w, "<html>\n")
		fmt.Fprint(w, "<head>", "<title>", "Hatter's Remote File Downloader", "</title>", "</head>")
		fmt.Fprint(w, "<body>\n")
		fmt.Fprint(w, "<form action=\"/\" method=\"POST\">\n")
		fmt.Fprint(w, "URL: ", "<input type=\"text\" name=\"url\" style=\"width: 600px;\">\n")
		fmt.Fprint(w, "<input type=\"submit\" value=\"Download\">\n")
		fmt.Fprint(w, "</form>\n")
		fmt.Fprint(w, "</body>\n")
		fmt.Fprint(w, "</html>\n")
		return
	}
	parseError := r.ParseForm()
	formUrl := r.FormValue("url")
	if parseError != nil {
		fmt.Fprint(w, "Error: ", fmt.Sprintf("%T %v", parseError, parseError), "\n")
		fmt.Fprint(w, "<br>", "<a href=\"/\">&lt;&lt;&lt;&lt;BACK&lt;&lt;&lt;&lt;</a>")
		return
	}
	if ! strings.HasPrefix(formUrl, "http://") {
		fmt.Fprint(w, "Error: ", "Url is not starts with http://: ", formUrl, "\n")
		fmt.Fprint(w, "<br>", "<a href=\"/\">&lt;&lt;&lt;&lt;BACK&lt;&lt;&lt;&lt;</a>")
		return
	}
	DownloadGet(formUrl, appHttpServerPath)
}

var appHttpServerPort = flag.Int("port", 8000, "listen port")
var appHttpServerPath, _ = os.Getwd()

func main() {
	flag.Parse()
	args := flag.Args()
	if len(args) > 0 {
		appHttpServerPath = args[0]
	}
	fmt.Println("Base path: ", appHttpServerPath)
	fmt.Println("Start up at port:", *appHttpServerPort)
	var h HttpServerHandle
	listenAndServeError := http.ListenAndServe(fmt.Sprintf(":%v", *appHttpServerPort), h)
	if (listenAndServeError != nil) {
		fmt.Println(listenAndServeError)
	}
}
