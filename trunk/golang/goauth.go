// goauth
package main

import (
	"./googleauth"
	"fmt"
)


func main() {
	challenge := googleauth.GetChallenge()
	fmt.Println("===1111====")
	fmt.Println(googleauth.GenerateResponseCode("aaaaaaaaaaaaaaaaaaaa", challenge))
	fmt.Println(googleauth.GenerateResponseCode("aaaaaaaaaaaaaaaaaaaa", challenge + 1))
	fmt.Println("===2222====")
	fmt.Println(googleauth.GenerateResponseCode("bbbbbbbbbbcccccccccc", challenge))
	fmt.Println(googleauth.GenerateResponseCode("bbbbbbbbbbcccccccccc", challenge + 1))
}
