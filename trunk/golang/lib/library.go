package lib

import (
	"fmt"
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
	IMAGE_GIF = "image/gif"
	IMAGE_ICON = "image/vnd.microsoft.icon"
	APPLICATION_PDF = "application/pdf"
	APPLICATION_XML = "application/xml"
	APPLICATION_JS = "application/javascript"
	APPLICATION_OCTET_STREAM = "application/octet-stream"
)

const (
	KB = 1024
	MB = KB * KB
	GB = KB * KB * KB
)

func ToSize(size int64) string {
	if size > GB {
		return fmt.Sprintf("%0.2fGB", float64(size) / GB)
	} else if size > MB {
		return fmt.Sprintf("%0.2fMB", float64(size) / MB)
	} else if size > KB {
		return fmt.Sprintf("%0.2fKB", float64(size) / KB)
	}
	return fmt.Sprintf("%v bytes", size)
}

func GetSuffix(s string) string {
	lastIndexOfDot := strings.LastIndex(s, ".")
	if lastIndexOfDot > -1 {
		return s[lastIndexOfDot + 1:]
	}
	return ""
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

func RedirectURL(w http.ResponseWriter, url string) {
	w.Header().Set("Location", url)
	w.Header().Set(CONTENT_TYPE, TEXT_HTML)
	w.WriteHeader(301)
	fmt.Fprint(w, "Redirect to: <a href=\"" + url + "\">", url, "</a>")
}
