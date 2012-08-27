package googleauth

/*
Use sample:
challenge := googleauth.GetChallenge()
currPasscode, err := googleauth.GenerateResponseCode(keys, challenge) // current passcode
nextPasscode, err := googleauth.GenerateResponseCode(keys, challenge + 1) // next passcode
*/

import (
	"fmt"
	"time"
	"errors"
	"strconv"
	"strings"
	"crypto/hmac"
	"crypto/sha1"
)

const (
	INTERVAL = 30
	PASS_CODE_LENGTH = 6
	PIN_MODULO = 1000000
)

var (
	// Integer.numberOfTrailingZeros
	// http://stackoverflow.com/questions/5471129/number-of-trailing-zeros
	// http://graphics.stanford.edu/~seander/bithacks.html#ZerosOnRightModLookup
	ZerosOnRightModLookup = []int {
		32, 0, 1, 26, 2, 23, 27, 0, 3, 16, 24, 30, 28, 11, 0, 13, 4, 7, 17,
		0, 25, 22, 31, 15, 29, 10, 12, 6, 0, 21, 14, 9, 5, 20, 8, 19, 18,
	}
)

type Base32Decode struct {
	encode string
	decodeMap []byte
}

func DefaultNewBase32Decode() *Base32Decode {
	return NewBase32Decode("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567")
}

func NewBase32Decode(encode string) *Base32Decode {
	d := new(Base32Decode)
	d.encode = encode
	d.decodeMap = make([]byte, 256)
	for i := 0; i < len(d.decodeMap); i++ {
		d.decodeMap[i] = 0xFF
	}
	for i := 0; i < len(encode); i++ {
		d.decodeMap[encode[i]] = byte(i)
	}
	return d
}

func (dec *Base32Decode) Decode(encoded string) ([]byte, error) {
	encoded = strings.TrimSpace(encoded)
	encoded = strings.Replace(encoded, "-", "", -1)
	encoded = strings.Replace(encoded, " ", "", -1)
	encoded = strings.ToUpper(encoded)
	if encoded == "" {
		return []byte{}, nil
	}
	MASK := len(dec.encode) - 1
	SHIFT := uint(numberOfTrailingZeros(len(dec.encode)))
	encodedLength := len(encoded)
	outLength := encodedLength * int(SHIFT) / 8
	result := make([]byte, outLength)
	buffer := 0
	next := 0
	bitsLeft := 0
	for _, c := range encoded {
		x := dec.decodeMap[c]
		if x == 0xFF {
			return nil, errors.New(fmt.Sprintf("Char illegal: ", c))
		}
		buffer <<= SHIFT
		buffer |= int(x) & MASK
		bitsLeft += int(SHIFT)
		if bitsLeft >= 8 {
			result[next] = byte(buffer >> uint(bitsLeft - 8))
			next += 1
			bitsLeft -= 8
		}
	}
	return result, nil
}

func numberOfTrailingZeros(i int) int {
	return ZerosOnRightModLookup[(i&-i)%37];
}

func int64ToBytes(v int64) []byte {
	result := make([]byte, 8)
	for i := 0; i < 8; i++ {
		result[7 - i] = byte(v & 0xFF)
		v >>= 8
	}
	return result
}

func hashToInt(bytes []byte, start int) uint32 {
	var result uint32 = 0
	for i := start ; i < len(bytes) && i < (start + 4); i++ {
		result |= uint32(bytes[i])
		if i < (start + 4 -1) {
			result <<= 8
		}
	}
	return result
}

func getCurrentTimeMillis() int64 {
	return time.Now().UnixNano() / 1000 / 1000
}

func GenerateResponseCode(seed string, challenge int64) (string, error) {
	dec := DefaultNewBase32Decode()
	decode, decodeError := dec.Decode(seed)
	if decodeError != nil {
		return "", decodeError
	}
	challengeBytes := int64ToBytes(challenge)
	hmacSha1 := hmac.New(sha1.New, decode)
	hmacSha1.Write(challengeBytes)
	hash := hmacSha1.Sum(nil)
	offset := int(hash[len(hash) - 1] & 0x0F)
	truncatedHash := hashToInt(hash, offset) & 0x7FFFFFFF
	pinValue := int(truncatedHash % PIN_MODULO)
	code := strconv.Itoa(pinValue)
	if len(code) >= PASS_CODE_LENGTH {
		return code, nil
	}
	return strings.Repeat("0", PASS_CODE_LENGTH - len(code)) + code, nil
}

func GetChallenge() int64 {
	return getCurrentTimeMillis() / 1000 / INTERVAL
}
