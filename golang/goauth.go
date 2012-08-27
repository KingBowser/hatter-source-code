// goauth
package main

import (
	"./googleauth"
	"fmt"
)


func main() {
	challenge := googleauth.GetChallenge()
	fmt.Println(googleauth.GenerateResponseCode("aaaaaaaaaaaaaaaaaaaa", challenge - 1))
	fmt.Println(googleauth.GenerateResponseCode("aaaaaaaaaaaaaaaaaaaa", challenge))
	fmt.Println(googleauth.GenerateResponseCode("aaaaaaaaaaaaaaaaaaaa", challenge + 1))
}
