package main

import (
	"fmt"
	"flag"
	"net/url"
	"net/http"
	"net/http/httputil"
)

var (
	listenPort = flag.Int("port", 80, "listen port")
	proxyURL = flag.String("url", "http://localhost:8080/", "proxy url")
)

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
