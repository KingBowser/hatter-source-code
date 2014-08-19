package me.hatter.test.owasp;

import com.google.json.JsonSanitizer;

// https://code.google.com/p/json-sanitizer/
public class JSONTest {

    public static void main(String[] args) {
        System.out.println(JsonSanitizer.sanitize(""));
        System.out.println(JsonSanitizer.sanitize("[]"));
        System.out.println(JsonSanitizer.sanitize("{}"));
        System.out.println(JsonSanitizer.sanitize("{a:b}"));
        System.out.println(JsonSanitizer.sanitize("{a:\"<script>alert(1); \r\n</script>\"}"));
    }
}
