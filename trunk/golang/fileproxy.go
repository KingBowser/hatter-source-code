// file proxy
package main

import (
	"./lib"
	"os"
	"fmt"
	"flag"
	"path"
	"regexp"
	"strings"
	"net/http"
	"sync/atomic"
)

var urlFileNameRegexp, _ = regexp.Compile("[^0-9a-zA-Z\\.]")

var downloadingCount int64 = 0
var downloadedAllCount int64 = 0
var downloadedSuccCount int64 = 0
var downloadedSize int64 = 0
var downloadedTime int64 = 0

var downloadingTime int64 = 0
var downloadingSize int64 = 0

type CopingCallbackImpl struct {
	downloadTimeStart int64
	downloadingSize int64
	downloadingTime int64
}

func NewCopingCallbackImpl() *CopingCallbackImpl {
	callback := new(CopingCallbackImpl)
	callback.downloadTimeStart = 0
	callback.downloadingSize = 0
	callback.downloadingTime = 0
	return callback
}

func (callback *CopingCallbackImpl) CopyStart() {
	callback.downloadTimeStart = lib.GetCurrentTimeMillis()
}

func (callback *CopingCallbackImpl) Coping(len int64) {
	currTimeMillis := lib.GetCurrentTimeMillis()
	diffTimeMillis := currTimeMillis - callback.downloadTimeStart - downloadingTime
	atomic.AddInt64(&downloadingTime, diffTimeMillis)
	atomic.AddInt64(&downloadingSize, len)
	callback.downloadingTime += diffTimeMillis;
	callback.downloadingSize += len
}

func (callback *CopingCallbackImpl) CopyEnd(err error) {
	atomic.AddInt64(&downloadingTime, -callback.downloadingTime)
	atomic.AddInt64(&downloadingSize, -callback.downloadingSize)
}

func DoDownloadGet(url, basePath string) {
	size, err := DownloadGet(url, basePath)
	if err != nil {
		fmt.Println(fmt.Sprintf("[ERROR] Download error: %T %v", err, err))
		return
	}
	fmt.Println(fmt.Sprintf("Download url: %v success, bytes: %v", url, size))
}

func DownloadGet(url, basePath string) (int64, error) {
	atomic.AddInt64(&downloadingCount, 1)
	atomic.AddInt64(&downloadedAllCount, 1)
	defer atomic.AddInt64(&downloadingCount, -1)
	timeStart := lib.GetCurrentTimeMillis()
	newFileName := GetRealFullFileName(basePath, GetFileName(url))
	fmt.Println("Download file from: ", url, " -> " + newFileName)
	resp, respError := http.Get(url)
	if respError != nil {
		return 0, respError
	}
	defer resp.Body.Close()
	newFile, newFileError := os.Create(newFileName)
	if newFileError != nil {
		return 0, newFileError
	}
	defer newFile.Close()
	copyCount, copyError := lib.CopyWithCallback(newFile, resp.Body, NewCopingCallbackImpl())
	if copyError != nil {
		return 0, copyError
	}
	timeEnd := lib.GetCurrentTimeMillis();
	atomic.AddInt64(&downloadedSize, copyCount)
	atomic.AddInt64(&downloadedSuccCount, 1)
	atomic.AddInt64(&downloadedTime, (timeEnd - timeStart))
	return copyCount, nil
}

func GetRealFullFileName(basePath, fileName string) string {
	fullFileName := path.Join(basePath, fileName)
	currentFullFileName := fullFileName
	currentFileIndex := 0
	for {
		_, openFileError := os.Stat(currentFullFileName)
		if os.IsNotExist(openFileError) {
			return currentFullFileName
		}
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
		fmt.Fprint(w, "Downloading count: ", fmt.Sprintf("%v", downloadingCount))
		fmt.Fprint(w, ", total count: ", downloadedAllCount)
		fmt.Fprint(w, ", success count: ", downloadedSuccCount)
		fmt.Fprint(w, "<br>")
		fmt.Fprint(w, "Downloaded size: ", lib.ToSize(downloadedSize))
		fmt.Fprint(w, ", downloaded time: ", downloadedTime, "ms")
		if downloadedTime > 0 {
			fmt.Fprint(w, ", average: ", lib.ToSize(downloadedSize * 1000 / downloadedTime), "/s")
		} else {
			fmt.Fprint(w, ", average: ", lib.ToSize(0), "/s")
		}
		fmt.Fprint(w, "<br>")
		fmt.Fprint(w, "Downloading size: ", lib.ToSize(downloadingSize))
		fmt.Fprint(w, ", downloading time: ", downloadingTime, "ms")
		fmt.Fprint(w, ", downloading average: ")
		if downloadingTime > 0 {
			fmt.Fprint(w, lib.ToSize(downloadingSize * 1000 / downloadingTime), "/s")
		} else {
			fmt.Fprint(w, lib.ToSize(0), "/s")
		}
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
	if ! (strings.HasPrefix(formUrl, "http://") || strings.HasPrefix(formUrl, "https://")) {
		fmt.Fprint(w, "Error: ", "Url is not starts with http://: ", formUrl, "\n")
		fmt.Fprint(w, "<br>", "<a href=\"/\">&lt;&lt;&lt;&lt;BACK&lt;&lt;&lt;&lt;</a>")
		return
	}
	go DoDownloadGet(formUrl, appHttpServerPath)
	fmt.Fprint(w, "Download started: ", formUrl)
	fmt.Fprint(w, "<br>", "<a href=\"/\">&lt;&lt;&lt;&lt;BACK&lt;&lt;&lt;&lt;</a>")
}

var (
	appHttpServerPort = flag.Int("port", 8000, "listen port")
	serverUseTLS = flag.Bool("usetls", false, "use tls")
	serverTLSPort = flag.Int("tlsport", 8443, "tls port")
	serverTLSCert = flag.String("tlscert", "cert.pem", "tls cert")
	serverTLSKey = flag.String("tlskey", "key.pem", "tls key")
	)
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
	
	fmt.Println("Check use tls:", *serverUseTLS)
	if *serverUseTLS {
		go func() {
			fmt.Println("Sart up at port:", *serverTLSPort)
			tlsErr := http.ListenAndServeTLS(fmt.Sprintf(":%v", *serverTLSPort), *serverTLSCert, *serverTLSKey, h)
			if tlsErr != nil {
				fmt.Println("Listen and serve faled:", tlsErr)
			}
		}()
	}
	
	listenAndServeError := http.ListenAndServe(fmt.Sprintf(":%v", *appHttpServerPort), h)
	if listenAndServeError != nil {
		fmt.Println("Listen and serve faled:", listenAndServeError)
	}
}
