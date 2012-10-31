package main

import (
        "os"
        "fmt"
        "path"
        "io/ioutil"
)

const (
	FIND_MESSAGE = "osascript -e 'tell Application \"Finder\" to display dialog \"$MESSAGE$\" '"
	X11_MESSAGE = "xmessage -nearmouse -print \"$MESSAGE$\""
)

func processMessage(msg string) string {
}

func main() {

}

