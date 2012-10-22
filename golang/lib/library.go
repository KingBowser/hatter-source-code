package lib

import (
	"os"
	"fmt"
	"time"
	"strconv"
	"strings"
	"net/http"
)

// port
const (
	DEFAULT_FTP_PORT = 21
	DEFAULT_SMTP_PORT = 25
	DEFAULT_HTTP_PORT = 80
)

// status code
const (
	HTTP_STATUS_CODE_200 = 200 // OK
	HTTP_STATUS_CODE_201 = 201 // Created
	HTTP_STATUS_CODE_202 = 202 // Accepted
	HTTP_STATUS_CODE_204 = 204 // No Content
	HTTP_STATUS_CODE_301 = 301 // Moved permanently
	HTTP_STATUS_CODE_302 = 302 // Moved temporarily
	HTTP_STATUS_CODE_304 = 304 // Not modified
	HTTP_STATUS_CODE_400 = 400 // Bad request
	HTTP_STATUS_CODE_401 = 401 // Unauthorised
	HTTP_STATUS_CODE_403 = 403 // Forbidden
	HTTP_STATUS_CODE_404 = 404 // Not found
	HTTP_STATUS_CODE_500 = 500 // Internal server error
	HTTP_STATUS_CODE_501 = 501 // Not implemented
	HTTP_STATUS_CODE_502 = 502 // Bad gateway
	HTTP_STATUS_CODE_503 = 503 // Service unavailable
)

// string constants
const (
	EMPTY = ""
	DOT = "."
	COLON = ":"
	SLASH = "/"
)

// http header
const (
	X_FORWARDED_FOR = "X-Forwarded-For"
	LOCATION = "Location"
	CONTENT_TYPE = "Content-Type"
	CONTENT_LENGTH = "Content-Length"
	ETAG = "ETag"
	DATE = "Date"
	EXPIRES = "Expires"
	LAST_MODIFIED = "Last-Modified"
)

// format
const (
	RFC1123 = "Mon, 02 Jan 2006 15:04:05 GMT"
)

// mime type
const (
	TEXT_PLAIN = "text/plain"
	TEXT_HTML = "text/html"
	TEXT_CSS = "text/css"
	IMAGE_JPEG = "image/jpeg"
	IMAGE_PNG = "image/png"
	IMAGE_GIF = "image/gif"
	IMAGE_ICON = "image/vnd.microsoft.icon"
	IMAGE_PSD = "image/vnd.adobe.photoshop"
	AUDIO_WAV = "audio/wav"
	VEDIO_MP4 = "video/mp4"
	VEDIO_MPEG = "video/mpeg"
	VEDIO_QUICKTIME = "video/quicktime"
	VEDIO_ASF = "video/x-ms-asf"
	VEDIO_WMV = "video/x-ms-wmv"
	VEDIO_AVI = "video/x-msvideo"
	VEDIO_MOVIE = "video/x-sgi-movie"
	APPLICATION_PDF = "application/pdf"
	APPLICATION_XML = "application/xml"
	APPLICATION_JS = "application/javascript"
	APPLICATION_RM = "application/vnd.rn-realmedia"
	APPLICATION_OCTET_STREAM = "application/octet-stream"
	APPLICATION_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded"
)

// size
const (
	KB = 1024
	MB = KB * KB
	GB = KB * KB * KB
)

var (
	ContentTypeMap = map[string]string {
	            		"go": TEXT_PLAIN, 
			    		"java": TEXT_PLAIN, 
						"txt": TEXT_PLAIN, 
						"properties": TEXT_PLAIN,
						"yaml": TEXT_PLAIN,
						"css": TEXT_CSS,
						"htm": TEXT_HTML,
						"html": TEXT_HTML,
						"jpg": IMAGE_JPEG,
						"jpe": IMAGE_JPEG,
						"jpeg": IMAGE_JPEG,
						"png": IMAGE_PNG,
						"gif": IMAGE_GIF,
						"ico": IMAGE_ICON,
						"pdf": APPLICATION_PDF,
						"psd": IMAGE_PSD,
						"xml": APPLICATION_XML,
						"js": APPLICATION_JS,
						"rm": APPLICATION_RM,
						"wav": AUDIO_WAV,
						"mp4": VEDIO_MP4,
						"mp4v": VEDIO_MP4,
						"mpg4": VEDIO_MP4,
						"mpeg": VEDIO_MPEG,
						"mpg": VEDIO_MPEG,
						"mpe": VEDIO_MPEG,
						"m1v": VEDIO_MPEG,
						"m2v": VEDIO_MPEG,
						"qt": VEDIO_QUICKTIME,
						"mov": VEDIO_QUICKTIME,
						"asf": VEDIO_ASF,
						"asx": VEDIO_ASF,
						"wmv": VEDIO_WMV,
						"avi": VEDIO_AVI,
						"movie": VEDIO_MOVIE,
	}
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
	lastIndexOfDot := strings.LastIndex(s, DOT)
	if lastIndexOfDot > -1 {
		return s[lastIndexOfDot + 1:]
	}
	return EMPTY
}

func GetContentType(suffix string) string {
	contentType := ContentTypeMap[suffix]
	if contentType == EMPTY {
		contentType = APPLICATION_OCTET_STREAM
	}
	return contentType
}

func ParseHost(host string) (string, int, error) {
	// format: domain(or ip) [+ ":" + port]
	commaIndex := strings.Index(host, COLON)
	if commaIndex > -1 {
		strPort := host[commaIndex + 1:]
		intPort, err := strconv.Atoi(strPort)
		if err != nil {
			return EMPTY, 0, err
		}
		return host[:commaIndex], intPort, nil
	}
	return host, DEFAULT_HTTP_PORT, nil
}

func GetRemoteAddrIP(remoteAddr string) string {
	// format: ip [+ ":" + port]
	commaIndex := strings.Index(remoteAddr, COLON)
	if commaIndex > -1 {
		return remoteAddr[:commaIndex]
	}
	return remoteAddr
}

func ToDomainAndPort(hostDomain string, hostPort int) string {
	domainAndPort := hostDomain
	if hostPort != DEFAULT_HTTP_PORT {
		domainAndPort = hostDomain + ":" + strconv.Itoa(hostPort)
	}
	return domainAndPort
}

func RedirectURL(w http.ResponseWriter, url string) {
	w.Header().Set(LOCATION, url)
	w.Header().Set(CONTENT_TYPE, TEXT_HTML)
	w.WriteHeader(HTTP_STATUS_CODE_301)
	fmt.Fprint(w, "Redirect to: <a href=\"" + url + "\">", url, "</a>")
}

func JoinURLPath(url, path string) string {
	if path == EMPTY {
		return url
	}
	hasSlashSuffixOfUrl := strings.HasSuffix(url, SLASH)
	hasSlashPrefoxOfPath := strings.HasPrefix(path, SLASH)
	if !hasSlashSuffixOfUrl {
		url = url + SLASH
	}
	if hasSlashPrefoxOfPath {
		path = path[1:]
	}
	return url + path
}

func GetCurrentTimeMillis() int64 {
	return time.Now().UnixNano() / 1000 / 1000
}

func CalcETag(fileInfo os.FileInfo) string {
	size := strconv.FormatInt(fileInfo.Size(), 16)
	time := strconv.FormatInt(fileInfo.ModTime().UnixNano(), 16)
	return "S" + size + "T" + time
}
