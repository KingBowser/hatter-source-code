package main

import (
	"os"
	"fmt"
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

func MaxInt(a, b int) int {
	if a > b {
		return a
	}
	return b
}

var (
	maxDirDepth = 0
	totalSize = 0
	walkMap map[string]string = new(map[strig]string)
)

func ListDir(dir string, dirDepth *int) error {
	*dirDepth += 1
	fileInfos, fileInfosError := ioutil.ReadDir(dir)
	if fileInfosError != nil {
		return fileInfosError
	}
	for _, fileInfo := range fileInfos {
		isDir := fileInfo.Mode().IsDir()
		totalSize += fileInfo.Size()
		fmt.Println(fileInfo.Name(), isDir, fileInfo.Size(), fileInfo.Mode())
		if isDir {
			ListDir(path.Join(dir, fileInfo.Name()), dirDepth)
		}
	}
	maxDirDepth = MaxInt(maxDirDepth, *dirDepth)
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

