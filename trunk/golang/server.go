package main

import (
	"./lib"
	"fmt"
	"log"
	"flag"
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

var (
	serverPort = flag.Int("port", 8888, "listen port")
)

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

func HandleDirFileDomainSetting(w http.ResponseWriter, r *http.Request, setting *DomainSetting) bool {
	return false
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
	log.Println("Unparsed URL: ", requestURL)
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
	if setting != nil {
		handleResult := HandleDomainSetting(w, r, setting)
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
