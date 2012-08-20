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

var quickDomainSettingMap = map[string]DomainSetting {
	"blog.hatter.me": DomainSetting {
		true, "http://aprilsoft.cn/blog", "",
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

func HandleRequest(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Server", "HatterPrivateServer/0.0.1")
	w.Header().Set("X-Powered-By", "GoLang")
	hostDomain, hostPort, hostError := ParseHost(r.Host)
	if hostError != nil {
		log.Println(fmt.Sprintf("Parse host failed: %T %v", hostError, hostError))
		return
	}
	domainAndPort := hostDomain
	if hostPort != 80 {
		domainAndPort = hostDomain + ":" + strconv.Itoa(hostPort)
	}
	setting := quickDomainSettingMap[domainAndPort]
	if setting.IsRedirect {
		w.WriteHeader(301)
		w.Header().Set("Location", setting.RedirectURL)
		return
	}
	w.WriteHeader(404)
	fmt.Fprint(w, "Path cannot found: ")
	fmt.Fprint(w, fmt.Sprintf("http://%v%v", domainAndPort, r.RequestURI))
	log.Println("Unparsed URL: ", r.RequestURI)
}

func main() {
	flag.Parse()
	log.Println("Sarting server at port: ", *serverPort)
	http.HandleFunc("/", HandleRequest)
	err := http.ListenAndServe(fmt.Sprintf(":%v", *serverPort), nil)
	if err != nil {
		log.Fatal("Listen and serve faled: ", err)
	}
}
