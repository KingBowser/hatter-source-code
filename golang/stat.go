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
	WALKED = "W"
)

var (
	maxDirDepth = 0
	totalSize int64 = 0
	walkedCount = 1
	walkedMap = map[string]string { }
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

func ListDir(dir string, dirDepth *int) error {
	*dirDepth += 1
	fileInfos, fileInfosError := ioutil.ReadDir(dir)
	if fileInfosError != nil {
		return fileInfosError
	}
	for _, fileInfo := range fileInfos {
		walkedCount += 1
		if walkedCount % 1000 == 0 {
			fmt.Print(".")
		}
		isDir := fileInfo.Mode().IsDir()
		totalSize += fileInfo.Size()
		// fmt.Println(fileInfo.Name(), isDir, fileInfo.Size(), fileInfo.Mode()) // LOG
		fullDir := path.Join(dir, fileInfo.Name())
		isWalked := true // detech if has loop link
		if walkedMap[fullDir] != WALKED {
			isWalked = false
			walkedMap[fullDir] = WALKED
		}
		if isDir && (! isWalked) {
			ListDir(fullDir, dirDepth)
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
	fmt.Println()
	fmt.Println(fmt.Sprintf("File or dir count: %v", walkedCount) + ", " + "total size: " + fmt.Sprintf("%v bytes", totalSize) + ", " + ToSize(totalSize))
}

