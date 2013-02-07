package main

import (
	"./lib"
	"io"
	"os"
	"fmt"
	"log"
	"flag"
	"path"
	"time"
	"bytes"
	"strconv"
	"strings"
	"os/exec"
	"io/ioutil"
	"net/url"
	"net/http"
)

const (
	CACHE_SECONDS = 60 * 60 * 24
)

const (
	REDIRECT_URL = "redirect.url"
)

var (
	serverSysPath, _ = os.Getwd()
	serverPath = flag.String("path", "serverSysPath", "server path")
	serverPort = flag.Int("port", 8888, "listen port")
	serverUseTLS = flag.Bool("usetls", false, "use tls")
	serverTLSPort = flag.Int("tlsport", 443, "tls port")
	serverTLSCert = flag.String("tlscert", "cert.pem", "tls cert")
	serverTLSKey = flag.String("tlskey", "key.pem", "tls key")
	serverListDir = flag.Bool("listdir", true, "list dir")
	serverUseDefault = flag.Bool("usedefault", true, "list dir")
	
	indexPages = []string {
		"index.htm",
		"index.html",
	}
)

const (
	REDIRECT = 0
	LOCATION = 1
	PROXY =2
)

type DomainSettingType int

type DomainSetting struct {
	SettingType DomainSettingType
	Target string
	Charset string
}

var defaultDomainSetting = DomainSetting {
	LOCATION, "", "", // default startup path
}

var hatterMeDomainSetting = DomainSetting {
	LOCATION, "/root/hatter.me", "utf-8",
}

var jiangchenhaoDomainSetting = DomainSetting {
	LOCATION, "/root/hatter.me/jiangchenhao", "utf-8",
}

var hatterMeRedirectDomainSetting = DomainSetting {
	REDIRECT, "http://hatter.me/", "",
}

var quickDomainSettingMap = map[string]*DomainSetting {
	"hatter.me": &hatterMeDomainSetting,
	"aprilsoft.cn": &hatterMeRedirectDomainSetting,
	"www.aprilsoft.cn": &hatterMeRedirectDomainSetting,
	"iwebsky.com": &hatterMeRedirectDomainSetting,
	"www.iwebsky.com": &hatterMeRedirectDomainSetting,
	"jiangchenhao.me": &jiangchenhaoDomainSetting,
	"www.jiangchenhao.me": &jiangchenhaoDomainSetting,
	"jiangchenhao.com": &jiangchenhaoDomainSetting,
	"www.jiangchenhao.com": &jiangchenhaoDomainSetting,
	"blog.hatter.me": &DomainSetting {
		REDIRECT, "http://aprilsoft.cn/blog/", "",
	},
	"code.hatter.me": &DomainSetting {
		REDIRECT, "https://code.google.com/p/hatter-source-code/", "",
	},
	"svn.hatter.me": &DomainSetting {
		PROXY, "https://hatter-source-code.googlecode.com/svn/trunk/", "",
	},
	"go.hatter.me": &DomainSetting {
		PROXY, "http://golang.org", "",
	},
	"playgo.hatter.me": &DomainSetting {
		PROXY, "http://play.golang.org", "",
	},
	"mail.hatter.me": &DomainSetting {
		REDIRECT, "https://www.google.com/a/hatterjiang.com", "",
	},
	"tinyencrypt.hatter.me": &DomainSetting {
		REDIRECT, "https://jshtaframework.googlecode.com/svn/trunk/jshtaframework/src/application/TinyEncrypt/EmtpyApplication.hta", "",
	},
}

type RequestCallFunc func (w http.ResponseWriter, r *http.Request) bool

var domainPathHandlerMap = map[string]RequestCallFunc {
	"hatter.me/redirect": DomainPathRedirectHandle,
	"hatter.me/uphatterme": DomainPathSvnUpHandle,
	"hatter.me/gocompile": DomainPathGoCompileHandle,
	"hatter.me/goformat": DomainPathGoFormatHandle,
	"hatter.me/apps": DomainPathAppsHandle,
	"hatter.me/p": DomainPathPHandle,
}

var domainFilters = map[string][]RequestCallFunc {
	"hatter.me": []RequestCallFunc {
		DomainPathWikiFilter,
	},
	"aprilsoft.cn": []RequestCallFunc {
		HatterJiangHeadFilter,
	},
	"www.aprilsoft.cn": []RequestCallFunc {
		HatterJiangHeadFilter,
	},
}


func HatterJiangHeadFilter(w http.ResponseWriter, r *http.Request) bool {
	if r.Method != "GET" {
		return false
	}
	if r.URL.Path == "/hatterjiang_head.jpg" {
		return HandleFileDomainSetting(w, r, "/root/hatter.me/hatterjiang_head.jpg", "")
	}
	return false
}

func DomainPathWikiFilter(w http.ResponseWriter, r *http.Request) bool {
	// http://code.google.com/p/hatter-source-code/wiki/Study_Java_HotSpot_Arguments?show=content
	if r.Method != "GET" {
		return false
	}
	if strings.HasPrefix(r.URL.Path, "/p/hatter-source-code/w/") {
		lib.RedirectURL(w, lib.JoinURLPath("https://code.google.com", r.RequestURI))
		return true
	}
	if strings.HasPrefix(r.URL.Path, "/u/") {
		lib.RedirectURL(w, lib.JoinURLPath("https://code.google.com", r.RequestURI))
		return true
	}
	if strings.HasPrefix(r.URL.Path, "/p/hatter-source-code/wiki/") {
		pWikiName := r.URL.Path[len("/p/hatter-source-code/wiki/"):]
		lib.RedirectURL(w, lib.JoinURLPath("/wiki/", pWikiName))
		return true
	}
	isWiki := false
	wikiName := ""
	if strings.HasPrefix(r.URL.Path, "/wiki/") {
		isWiki = true
		wikiName = r.URL.Path[len("/wiki/"):]
	}
	if !isWiki {
		if r.URL.Path == "/wiki" {
			lib.RedirectURL(w, "/wiki/")
			return true
		}
		return false
	}
	if wikiName == "" {
		wikiName = "WikiIndex"
	}
	if strings.Contains(wikiName, "/") {
		return false;
	}
	proxyFullURL := "http://code.google.com/p/hatter-source-code/wiki/" + wikiName + "?show=content"
	return HandleProxyDomainURL(w, r, proxyFullURL)
}

func DomainPathPHandle(w http.ResponseWriter, r *http.Request) bool {
	parseError := r.ParseForm()
	if parseError != nil {
		log.Println("Parse form failed:", parseError)
		return false
	}
	proxyUrl := r.FormValue("url")
	if proxyUrl == "" {
		log.Println("Form url is empty.")
		return false;
	}
	return HandleProxyDomainURL(w, r, proxyUrl)
}

func DomainPathAppsHandle(w http.ResponseWriter, r *http.Request) bool {
	return HandleProxyDomainURL(w, r, "http://hatter-source-code.googlecode.com/svn/trunk/apps.txt")
}

func DomainPathSvnUpHandle(w http.ResponseWriter, r *http.Request) bool {
	DoSvnUpHatterMe(w, r)
	return true
}

func DomainPathRedirectHandle(w http.ResponseWriter, r *http.Request) bool {
	parseError := r.ParseForm()
	if parseError != nil {
		log.Println("Parse form failed:", parseError)
		return false
	}
	formUrl := r.FormValue("url")
	if formUrl == "" {
		log.Println("Form url is empty.")
		return false;
	}
	lib.RedirectURL(w, formUrl)
	return true
}

func DomainPathGoFormatHandle(w http.ResponseWriter, r *http.Request) bool {
	parseError := r.ParseForm()
	if parseError != nil {
		log.Println("Parse form failed:", parseError)
		return false
	}
	body := r.FormValue("body")
	postResponse, postResponseError := http.PostForm("http://play.golang.org/fmt",
	                                url.Values{"body": {body}})
	if postResponseError != nil {
		fmt.Fprint(w, "Post form formt failed:", postResponseError)
	} else {
		defer postResponse.Body.Close()
		postResponseBody, postResponseBodyError := ioutil.ReadAll(postResponse.Body)
		if postResponseBodyError != nil {
			fmt.Fprint(w, "Read from response failed:", postResponseBodyError)
		} else {
			fmt.Fprint(w, string(postResponseBody))
		}
	}
	return true;
}

func DomainPathGoCompileHandle(w http.ResponseWriter, r *http.Request) bool {
	parseError := r.ParseForm()
	if parseError != nil {
		log.Println("Parse form failed:", parseError)
		return false
	}
	body := r.FormValue("body")
	postResponse, postResponseError := http.PostForm("http://play.golang.org/compile",
	                                url.Values{"body": {body}})
	if postResponseError != nil {
		fmt.Fprint(w, "Post form compile failed:", postResponseError)
	} else {
		defer postResponse.Body.Close()
		postResponseBody, postResponseBodyError := ioutil.ReadAll(postResponse.Body)
		if postResponseBodyError != nil {
			fmt.Fprint(w, "Read from response failed:", postResponseBodyError)
		} else {
			fmt.Fprint(w, string(postResponseBody))
		}
	}
	return true;
}

func HandleRedirectDomainSetting(w http.ResponseWriter, r *http.Request, setting *DomainSetting) bool {
	log.Println("Redirect to url:", setting.Target)
	lib.RedirectURL(w, setting.Target)
	return true
}

func HandleListDirDomainSetting(w http.ResponseWriter, r *http.Request, dirPath string) bool {
	readDirFileInfos, readDirError := ioutil.ReadDir(dirPath)
	if readDirError != nil {
		log.Println("Read dir failed:", readDirError)
		return false
	}
	w.Header().Set(lib.CONTENT_TYPE, lib.TEXT_HTML)
	fmt.Fprint(w, "<html>")
	fmt.Fprint(w, "<head>")
	fmt.Fprint(w, "<title>", "Listing dir: ", r.URL.Path, "</title>")
	fmt.Fprint(w, "</head>")
	fmt.Fprint(w, "<body>")
	fmt.Fprint(w, "<h1>Listing dir:</h1>")
	fmt.Fprint(w, "<ul>")
	if r.URL.Path != "/" {
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
	fmt.Fprint(w, "Powered by Hatter's Private Server, Version: 0.0.1<br/>")
	fmt.Fprint(w, "</body>")
	fmt.Fprint(w, "</html>")
	return true
}

func HandleDirDomainSetting(w http.ResponseWriter, r *http.Request, dirPath string, charset string) bool {
	indexRedirectPath := path.Join(dirPath, REDIRECT_URL)
	indexRedirectPathFileInfo, indexRedirectPathFileInfoError := os.Stat(indexRedirectPath)
	if indexRedirectPathFileInfoError == nil && (!indexRedirectPathFileInfo.IsDir()) {
		indexRedirectPathFileBytes,indexRedirectPathFileBytesError := ioutil.ReadFile(indexRedirectPath)
		if indexRedirectPathFileBytesError != nil {
			log.Println("Read file failed:", indexRedirectPathFileBytesError)
			return false
		}
		indexRedirectUrl := strings.TrimSpace(string(indexRedirectPathFileBytes))
		lib.RedirectURL(w, indexRedirectUrl)
		return true
	}
	for _, indexPage := range indexPages {
		filePath := path.Join(dirPath, indexPage)
		_, statFileInfoError := os.Stat(filePath)
		if statFileInfoError == nil {
			return HandleFileDomainSetting(w, r, filePath, charset)
		}
	}
	if *serverListDir {
		return HandleListDirDomainSetting(w, r, dirPath)
	}
	fmt.Fprint(w, "List dir is forbiden.")
	return true
}


func HandleFileDomainSetting(w http.ResponseWriter, r *http.Request, filePath string, charset string) bool {
	openFile, openFileError := os.Open(filePath)
	if openFileError != nil {
		log.Println("Open file failed:", openFileError)
		return false
	}
	defer openFile.Close()
	openFileInfo, openFileInfoError := openFile.Stat()
	if openFileInfoError != nil {
		log.Println("Stat file failed:", openFileInfoError)
		return false
	}
	etag := lib.CalcETag(openFileInfo)
	rEtag := r.Header.Get(lib.IF_NONE_MATCH)
	isNotModified := false
	if (rEtag != "") && (etag == rEtag) {
		w.WriteHeader(304)
		isNotModified = true
	}
	w.Header().Set(lib.CONTENT_TYPE, lib.GetContentType(lib.GetSuffix(filePath), charset))
	if !isNotModified {
		w.Header().Set(lib.CONTENT_LENGTH, strconv.FormatInt(openFileInfo.Size(), 10))
	}
	w.Header().Set(lib.ETAG, etag)
	now := time.Now()
	exp := now.Add(time.Duration(1000000 * 1000 * CACHE_SECONDS))
	w.Header().Set(lib.CACHE_CONTROL, "max-age=" + strconv.Itoa(CACHE_SECONDS))
	w.Header().Set(lib.DATE, now.UTC().Format(lib.RFC1123))
	w.Header().Set(lib.EXPIRES, exp.UTC().Format(lib.RFC1123))
	w.Header().Set(lib.LAST_MODIFIED, openFileInfo.ModTime().UTC().Format(lib.RFC1123))
	if isNotModified {
		return true
	}
	io.Copy(w, openFile)
	return true
}

func HandleDirFileDomainSetting(w http.ResponseWriter, r *http.Request, setting *DomainSetting) bool {
	locationPath := setting.Target
	charset := setting.Charset
	if locationPath == "" {
		locationPath = *serverPath
	}
	accessPath := path.Join(locationPath, r.URL.Path)
	accessFileInfo, accessFileInfoError := os.Stat(accessPath)
	if accessFileInfoError != nil {
		log.Println("OS Stat file/path failed:", accessFileInfoError)
		return false
	}
	if accessFileInfo.IsDir() && !strings.HasSuffix(r.URL.Path, "/") {
		lib.RedirectURL(w, r.URL.Path + "/")
		return true
	}
	if accessFileInfo.IsDir() {
		return HandleDirDomainSetting(w, r, accessPath, charset)
	}
	return HandleFileDomainSetting(w, r, accessPath, charset)
}


func HandleProxyDomainSetting(w http.ResponseWriter, r *http.Request, setting *DomainSetting) bool {
	proxyFullURL := lib.JoinURLPath(setting.Target, r.RequestURI)
	return HandleProxyDomainURL(w, r, proxyFullURL)
}

func HandleProxyDomainURL(w http.ResponseWriter, r *http.Request, proxyFullURL string) bool {
	log.Println("Proxy to url:", proxyFullURL)
	var requestBody io.Reader = nil
	if r.Method == "POST" {
		requestBodyReadAll, requestBodyReadAllError := ioutil.ReadAll(r.Body)
		if requestBodyReadAllError != nil {
			log.Println("Read post body failed:", requestBodyReadAllError)
		} else {
			requestBody = bytes.NewReader(requestBodyReadAll)
		}
	}
	client := &http.Client{}
	getRequest, getRequestError := http.NewRequest(r.Method, proxyFullURL, requestBody)
	if r.Method == "POST" {
		getRequest.Header.Set(lib.CONTENT_TYPE, lib.APPLICATION_WWW_FORM_URLENCODED)
	}
	remoteAddr := r.RemoteAddr
	if remoteAddr != "" {
		xForwardFor := r.Header.Get(lib.X_FORWARDED_FOR)
		if xForwardFor == "" {
			xForwardFor = lib.GetRemoteAddrIP(remoteAddr)
		} else {
			xForwardFor = xForwardFor + ", " + lib.GetRemoteAddrIP(remoteAddr)
		}
		getRequest.Header.Set(lib.X_FORWARDED_FOR, xForwardFor)
	}
	if getRequestError != nil {
		w.WriteHeader(500)
		fmt.Fprint(w, "New request failed:", getRequestError)
		return true
	}
	getResponse, getResponseError := client.Do(getRequest)
	if getResponseError != nil {
		w.WriteHeader(500)
		fmt.Fprint(w, "Get request failed:", getResponseError)
		return true
	}
	defer getResponse.Body.Close()
	getResponseBody, getResponseBodyError := ioutil.ReadAll(getResponse.Body)
	if getResponseBodyError != nil {
		w.WriteHeader(500)
		fmt.Fprint(w, "Read from response failed:", getResponseBodyError)
		return true
	}
	for headerKey, headerValues := range getResponse.Header {
		if headerKey != "" && headerKey != "Server" && headerKey != "X-Powered-By" && headerKey != "Transfer-Encoding" {
			for headerValueIndex, headerValue := range headerValues {
				if headerValueIndex == 0 {
					w.Header().Set(headerKey, headerValue)
				} else {
					w.Header().Add(headerKey, headerValue)
				}
			}
		}
	}
	w.WriteHeader(getResponse.StatusCode)
	w.Write(getResponseBody)
	return true
}

func HandleDomainSetting(w http.ResponseWriter, r *http.Request, setting *DomainSetting) bool {
	if setting.SettingType == REDIRECT {
		return HandleRedirectDomainSetting(w, r, setting)
	}
	if setting.SettingType == LOCATION {
		return HandleDirFileDomainSetting(w, r, setting)
	}
	if setting.SettingType == PROXY {
		return HandleProxyDomainSetting(w, r, setting)
	}
	return false
}

func HandleNotFound(w http.ResponseWriter, r *http.Request, requestURL string) bool {
	w.WriteHeader(404)
	/*
	fmt.Fprint(w, "Resource not found: ")
	fmt.Fprint(w, requestURL)
	*/
	// GONGYI
	fmt.Fprint(w, "<html>\n")
	fmt.Fprint(w, "<head>")
	fmt.Fprint(w, "<title>404 - Page not found!</title>")
	fmt.Fprint(w, "</head>\n")
	fmt.Fprint(w, "<body>\n")
	fmt.Fprint(w, "<center>")
	fmt.Fprint(w, "<h1>404 - Page not found!</h1>\n")
	fmt.Fprint(w, "<iframe scrolling='no' frameborder='0'")
	fmt.Fprint(w, " src='http://yibo.iyiyun.com/js/yibo404'")
	fmt.Fprint(w, " width='735' height='700' style='display:block;'></iframe>")
	fmt.Fprint(w, "</center>\n")
	fmt.Fprint(w, "</body>\n")
	fmt.Fprint(w, "</html>\n")
	log.Println("Resource not found: ", requestURL)
	return false
}

func DoSvnUpHatterMe(w http.ResponseWriter, r *http.Request) bool {
	log.Println("Exec: svn up /root/hatter.me")
	cmd := exec.Command("svn", "up", "/root/hatter.me")
	var out bytes.Buffer
	cmd.Stdout = &out
	err := cmd.Run()
	if err != nil {
	    fmt.Fprint(w, fmt.Sprintf("Error: %T %v", err, err))
	} else {
		fmt.Fprint(w, out.String())
	}
	return true
}

func HandleRequest(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Server", "HatterPrivateServer/0.0.1")
	w.Header().Set("X-Powered-By", "GoLang")
	hostDomain, hostPort, hostError := lib.ParseHost(r.Host)
	if hostError != nil {
		log.Println(fmt.Sprintf("Parse host failed: %T %v", hostError, hostError))
		return
	}
	domainAndPort := lib.ToDomainAndPort(hostDomain, hostPort)
	domainAndPortPath := domainAndPort + r.URL.Path
	requestURL := fmt.Sprintf("http://%v%v", domainAndPort, r.RequestURI)
	log.Println("Request url:", requestURL)
	if r.Referer() != "" {
		log.Println("---- Referer:", r.Referer())
	}
	if r.RemoteAddr != "" {
		log.Println("---- Remote addr:", lib.GetRemoteAddrIP(r.RemoteAddr))
	}
	requestCallFuncs := domainFilters[domainAndPort]
	if requestCallFuncs != nil {
		for _, requestCallFunc := range requestCallFuncs {
			if requestCallFunc(w, r) { // call filter
				return
			}
		}
	}
	domainPathHandler := domainPathHandlerMap[domainAndPortPath]
	if domainPathHandler != nil {
		domainPathHandler(w, r)
		return
	}
	setting := quickDomainSettingMap[domainAndPort]
	handleResult := false
	if setting != nil {
		handleResult = HandleDomainSetting(w, r, setting)
		if handleResult {
			return
		}
	} else {
		if *serverUseDefault {
			handleResult = HandleDirFileDomainSetting(w, r, &defaultDomainSetting)
			if handleResult {
				return
			}
		}
	}
	HandleNotFound(w, r, requestURL)
}

func main() {
	flag.Parse()
	log.Println("Sarting server at port:", *serverPort)
	http.HandleFunc("/", HandleRequest)
	
	log.Println("Check use tls:", *serverUseTLS)
	if *serverUseTLS {
		go func() {
			log.Println("Sarting server at port:", *serverTLSPort)
			tlsErr := http.ListenAndServeTLS(fmt.Sprintf(":%v", *serverTLSPort), *serverTLSCert, *serverTLSKey, nil)
			if tlsErr != nil {
				log.Fatal("Listen and serve faled:", tlsErr)
			}
		}()
	}
	
	err := http.ListenAndServe(fmt.Sprintf(":%v", *serverPort), nil)
	if err != nil {
		log.Fatal("Listen and serve faled:", err)
	}
}
