package main

import (
	"./lib"
	"io"
	"os"
	"fmt"
	"log"
	"flag"
	"path"
	"bytes"
	"strings"
	"os/exec"
	"io/ioutil"
	"net/url"
	"net/http"
)

const (
	REDIRECT_URL = "redirect.url"
)

var (
	serverSysPath, _ = os.Getwd()
	serverPath = flag.String("path", "serverSysPath", "server path")
	serverPort = flag.Int("port", 8888, "listen port")
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
	RedirectURL string
	LocationPath string
}

var defaultDomainSetting = DomainSetting {
	LOCATION, "", "", // default startup path
}

var hatterMeDomainSetting = DomainSetting {
	LOCATION, "", "/root/hatter.me",
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
	"blog.hatter.me": &DomainSetting {
		REDIRECT, "http://aprilsoft.cn/blog/", "",
	},
	"code.hatter.me": &DomainSetting {
		PROXY, "", "http://code.google.com/p/hatter-source-code/wiki/Study_Java_HotSpot_Thread_VMThread",
	},
	"mail.hatter.me": &DomainSetting {
		REDIRECT, "https://www.google.com/a/hatterjiang.com", "",
	},
	"tinyencrypt.hatter.me": &DomainSetting {
		REDIRECT, "https://jshtaframework.googlecode.com/svn/trunk/jshtaframework/src/application/TinyEncrypt/EmtpyApplication.hta", "",
	},
}

var domainPathHandlerMap = map[string]func (w http.ResponseWriter, r *http.Request) bool {
	"hatter.me/redirect": DomainPathRedirectHandle,
	"hatter.me/uphatterme": DomainPathSvnUpHandle,
	"hatter.me/gocompile": DomainPathGoCompileHandle,
	"hatter.me/goformat": DomainPathGoFormatHandle,
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
	log.Println("Redirect to url:", setting.RedirectURL)
	lib.RedirectURL(w, setting.RedirectURL)
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
	fmt.Fprint(w, "Powered by Hatter's Private Server, Version: 0.0.1<br/>")
	fmt.Fprint(w, "</body>")
	fmt.Fprint(w, "</html>")
	return true
}

func HandleDirDomainSetting(w http.ResponseWriter, r *http.Request, dirPath string) bool {
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
			return HandleFileDomainSetting(w, r, filePath)
		}
	}
	if *serverListDir {
		return HandleListDirDomainSetting(w, r, dirPath)
	}
	fmt.Fprint(w, "List dir is forbiden.")
	return true
}


func HandleFileDomainSetting(w http.ResponseWriter, r *http.Request, filePath string) bool {
	openFile, openFileError := os.Open(filePath)
	if openFileError != nil {
		log.Println("Open file failed:", openFileError)
		return false
	}
	defer openFile.Close()
	w.Header().Set(lib.CONTENT_TYPE, lib.GetContentType(lib.GetSuffix(filePath)))
	io.Copy(w, openFile)
	return true
}

func HandleDirFileDomainSetting(w http.ResponseWriter, r *http.Request, setting *DomainSetting) bool {
	locationPath := setting.LocationPath
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
		return HandleDirDomainSetting(w, r, accessPath)
	}
	return HandleFileDomainSetting(w, r, accessPath)
}

func HandleDomainSetting(w http.ResponseWriter, r *http.Request, setting *DomainSetting) bool {
	if setting.SettingType == REDIRECT {
		return HandleRedirectDomainSetting(w, r, setting)
	}
	if setting.SettingType == LOCATION {
		return HandleDirFileDomainSetting(w, r, setting)
	}
	return false
}

func HandleNotFound(w http.ResponseWriter, r *http.Request, requestURL string) bool {
	w.WriteHeader(404)
	fmt.Fprint(w, "Resource not found: ")
	fmt.Fprint(w, requestURL)
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
	err := http.ListenAndServe(fmt.Sprintf(":%v", *serverPort), nil)
	if err != nil {
		log.Fatal("Listen and serve faled:", err)
	}
}
