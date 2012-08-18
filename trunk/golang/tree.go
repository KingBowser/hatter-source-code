package main

import (
	"os"
	"fmt"
	"path"
	"io/ioutil"
)

const (
	KB = 1024
	MB = KB * KB
	GB = KB * KB * KB
)

func ToSize(size int64) string {
	if size > GB {
		return fmt.Sprintf("%vGB", size / GB)
	} else if size > MB {
		return fmt.Sprintf("%vMB", size / MB)
	} else if size > KB {
		return fmt.Sprintf("%vKB", size / KB)
	}
	return fmt.Sprintf("%v bytes", size)
}

func MakeTabIn(tabIn int) string {
	tabs := ""
	for i := 0; i < tabIn - 1; i++ {
		tabs += "    "
	}
	return tabs
}

func ListDir(dir string, dirDepth *int) error {
	*dirDepth += 1
	fileInfos, fileInfosError := ioutil.ReadDir(dir)
	if fileInfosError != nil {
		return fileInfosError
	}
	for _, fileInfo := range fileInfos {
		isDir := fileInfo.Mode().IsDir()
		name := fileInfo.Name()
		printName := name
		if isDir {
			printName = "[" + name + "]"
		}
		fmt.Println(MakeTabIn(*dirDepth) + printName)
		if isDir {
			err := ListDir(path.Join(dir, name), dirDepth)
			if err != nil {
				return err
			}
		}
	}
	*dirDepth -= 1
	return nil
}

func main() {
	appDir, _ := os.Getwd()
	dirDepth := 0
	err := ListDir(appDir, &dirDepth)
	if err != nil {
		fmt.Println(fmt.Sprintf("Error %T %v", err, err))
	}
}

