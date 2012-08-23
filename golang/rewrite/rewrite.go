package rewrite

import (
	"fmt"
)

type ReWriteRule struct {
	config string // L, R=301, P ...
}

type ReWriteRuleGroup struct {
	domain string
}

type ReWriteRuleAll struct {
}

