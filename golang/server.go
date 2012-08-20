package main

import (
	"fmt"
	"log"
	"flag"
	"strconv"
	"strings"
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

	KB = 1024
	MB = KB * KB
	GB = KB * KB * KB
)

var (
	serverPort = flag.Int("port", 8888, "listen port")
)

type DomainSetting struct {
	IsRedirect bool
	RedirectURL string
	LocationPath string
}

var quickDomainSettingMap = map[string]*DomainSetting {
	"hatter.me": &DomainSetting {
		true, "http://aprilsoft.cn/blog/", "",
	},
	"blog.hatter.me": &DomainSetting {
		true, "http://aprilsoft.cn/blog/", "",
	},
	"mail.hatter.me": &DomainSetting {
		true, "https://www.google.com/a/hatterjiang.com", "",
	},
	"tinyencrypt.hatter.me": &DomainSetting {
		true, "http://jshtaframework.googlecode.com/svn/trunk/jshtaframework/src/application/TinyEncrypt/EmtpyApplication.hta", "",
	},
}

func ParseHost(host string) (string, int, error) {
	commaIndex := strings.Index(host, ":")
	if commaIndex > -1 {
		strPort := host[commaIndex + 1:]
		intPort, err := strconv.Atoi(strPort)
		if err != nil {
			return "", 0, err
		}
		return host[:commaIndex], intPort, nil
	}
	return host, 80, nil
}

func ToDomainAndPort(hostDomain string, hostPort int) string {
	domainAndPort := hostDomain
	if hostPort != 80 {
		domainAndPort = hostDomain + ":" + strconv.Itoa(hostPort)
	}
	return domainAndPort
}

func HandleRedirectDomainSetting(w http.ResponseWriter, r *http.Request, setting *DomainSetting) bool {
	log.Println("Redirect to url:", setting.RedirectURL)
	w.Header().Set("Location", setting.RedirectURL)
	w.Header().Set(CONTENT_TYPE, TEXT_HTML)
	w.WriteHeader(301)
	fmt.Fprint(w, "Redirect to: <a href=\"" + setting.RedirectURL + "\">", setting.RedirectURL, "</a>")
	return true
}

func HandleDirFileDomainSetting(w http.ResponseWriter, r *http.Request, setting *DomainSetting) bool {
	return false
}

func HandleDomainSetting(w http.ResponseWriter, r *http.Request, setting *DomainSetting) bool {
	if setting.IsRedirect {
		return HandleRedirectDomainSetting(w, r, setting)
	}
	return HandleDirFileDomainSetting(w, r, setting)
}

func HandleRequest(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Server", "HatterPrivateServer/0.0.1")
	w.Header().Set("X-Powered-By", "GoLang")
	hostDomain, hostPort, hostError := ParseHost(r.Host)
	if hostError != nil {
		log.Println(fmt.Sprintf("Parse host failed: %T %v", hostError, hostError))
		return
	}
	domainAndPort := ToDomainAndPort(hostDomain, hostPort)
	requestURL := fmt.Sprintf("http://%v%v", domainAndPort, r.RequestURI)
	log.Println("Request url: ", requestURL)
	setting := quickDomainSettingMap[domainAndPort]
	if setting != nil {
		handleResult := HandleDomainSetting(w, r, setting)
		if handleResult {
			return
		}
	}
	// cannot find resource
	w.WriteHeader(404)
	fmt.Fprint(w, "Resource not found: ")
	fmt.Fprint(w, requestURL)
	log.Println("Unparsed URL: ", r.RequestURI)
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
