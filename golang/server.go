package main

import (
	"./lib"
	"io"
	"os"
	"fmt"
	"log"
	"flag"
	"path"
	"io/ioutil"
	"net/http"
)

const (
	CONTENT_TYPE = "Content-Type"
	TEXT_PLAIN = "text/plain"
	TEXT_HTML = "text/html"
	TEXT_CSS = "text/css"
	IMAGE_JPEG = "image/jpeg"
	IMAGE_PNG = "image/png"
	APPLICATION_JS = "application/javascript"
)

var (
	serverSysPath, _ = os.Getwd()
	serverPath = flag.String("path", serverSysPath, "server path")
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
)

type DomainSettingType int

type DomainSetting struct {
	SettingType DomainSettingType
	RedirectURL string
	LocationPath string
}

var defaultDomainSetting = DomainSetting {
	LOCATION, "", *serverPath,
}

var quickDomainSettingMap = map[string]*DomainSetting {
	"hatter.me": &DomainSetting {
		REDIRECT, "http://aprilsoft.cn/blog/", "",
	},
	"blog.hatter.me": &DomainSetting {
		REDIRECT, "http://aprilsoft.cn/blog/", "",
	},
	"mail.hatter.me": &DomainSetting {
		REDIRECT, "https://www.google.com/a/hatterjiang.com", "",
	},
	"tinyencrypt.hatter.me": &DomainSetting {
		REDIRECT, "http://jshtaframework.googlecode.com/svn/trunk/jshtaframework/src/application/TinyEncrypt/EmtpyApplication.hta", "",
	},
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
	w.Header().Set(CONTENT_TYPE, TEXT_HTML)
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
		fmt.Fprint(w, "<li>", "<a href=\"", basePath, fileInfo.Name(), "\">", fName, "</a>", fileSize, "</li>");
	}
	fmt.Fprint(w, "</ul>")
	fmt.Fprint(w, "<br/>", "<br/>")
	fmt.Fprint(w, "Powered by Hatter's Private Server, Version: 0.0.1<br/>")
	fmt.Fprint(w, "</body>")
	fmt.Fprint(w, "</html>")
	return true
}

func HandleDirDomainSetting(w http.ResponseWriter, r *http.Request, dirPath string) bool {
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
	io.Copy(w, openFile)
	return true
}

func HandleDirFileDomainSetting(w http.ResponseWriter, r *http.Request, setting *DomainSetting) bool {
	accessPath := path.Join(setting.LocationPath, r.URL.Path)
	accessFileInfo, accessFileInfoError := os.Stat(accessPath)
	if accessFileInfoError != nil {
		log.Println("OS Stat file/path failed:", accessFileInfoError)
		return false;
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

func HandleRequest(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Server", "HatterPrivateServer/0.0.1")
	w.Header().Set("X-Powered-By", "GoLang")
	hostDomain, hostPort, hostError := lib.ParseHost(r.Host)
	if hostError != nil {
		log.Println(fmt.Sprintf("Parse host failed: %T %v", hostError, hostError))
		return
	}
	domainAndPort := lib.ToDomainAndPort(hostDomain, hostPort)
	requestURL := fmt.Sprintf("http://%v%v", domainAndPort, r.RequestURI)
	log.Println("Request url: ", requestURL)
	setting := quickDomainSettingMap[domainAndPort]
	handleResult := false
	if setting != nil {
		handleResult = HandleDomainSetting(w, r, setting)
		if handleResult {
			return
		}
	}
	if *serverUseDefault {
		handleResult = HandleDirFileDomainSetting(w, r, &defaultDomainSetting)
		if handleResult {
			return
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
