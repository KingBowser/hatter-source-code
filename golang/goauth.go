// goauth
package main

import (
	"./googleauth"
	"fmt"
)


func main() {
	challenge := googleauth.GetChallenge()
	for i := challenge; i < (challenge + 10); i++ {
		fmt.Println(googleauth.GenerateResponseCode("aaaaaaaaaaaaaaaaaaaa", i))
	}
}
