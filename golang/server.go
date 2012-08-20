package main

import (
	"fmt"
	"log"
	"flag"
	"net/http"
)

var (
	serverPort = flag.Int("port", 8888, "listen port")
)

func HandleRequest(w http.ResponseWriter, req *http.Request) {
	
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
