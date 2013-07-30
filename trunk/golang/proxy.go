package main

import (
	"fmt"
	"flag"
	"sync"
	"net/url"
	"net/http"
	"io/ioutil"
	"encoding/json"
	"net/http/httputil"
)

// ---------------------------------------------------------------------------
var (
	listenPort = flag.Int("port", 80, "listen port")
	proxyURL = flag.String("url", "http://localhost:8080/", "proxy url")
)
// ---------------------------------------------------------------------------

const (
	CONFIG_FILE = "config.json"
)

type ProxyItem struct {
	Host string
	Port int
	Target string
}

type ProxyConfig struct {
	ProxyItems []ProxyItem
}

var proxyMapMutex sync.Mutex
var proxyMap = map[string]*httputil.ReverseProxy {
}

func getReverseProxy(target *url.URL, rawTarget string) *httputil.ReverseProxy {
	proxyMapMutex.Lock()
	defer proxyMapMutex.Unlock()
	proxy := proxyMap[rawTarget]
	if proxy == nil {
		proxy = httputil.NewSingleHostReverseProxy(target)
	}
	return proxy
}

func readConfigFile() (*ProxyConfig, error) {
	config, configErr := ioutil.ReadFile(CONFIG_FILE)
	if configErr != nil {
		return nil, configErr
	}
	var proxyConfig ProxyConfig
	jsonErr := json.Unmarshal(config, &proxyConfig)
	if jsonErr != nil {
		return nil, jsonErr
	}
	// TODO
	return nil, nil
}

func main() {
	flag.Parse()
	targetUrl, targetUrlErr := url.Parse(*proxyURL)
	if targetUrlErr != nil {
		fmt.Println("[ERROR]", targetUrlErr)
		return
	}
	fmt.Printf("[INFO] \tProxy server start up:\n\tPort: %v\n\tTarget URL: %v\n", *listenPort, targetUrl)
	var proxy = httputil.NewSingleHostReverseProxy(targetUrl)
	listenErr := http.ListenAndServe(fmt.Sprintf(":%v", *listenPort), proxy)
	if listenErr != nil {
		fmt.Println("[ERROR]", listenErr)
		return
	}
} 
