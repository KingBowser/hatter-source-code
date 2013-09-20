package me.hatter.tools.resourceproxy.jsspserver.util;

public class HttpConstants {

    public static String METHOD_GET           = "GET";
    public static String METHOD_POST          = "POST";
    public static String METHOD_HEAD          = "HEAD";
    public static String METHOD_OPTIONS       = "OPTIONS";
    public static String METHOD_PUT           = "PUT";
    public static String METHOD_DELETE        = "DELETE";
    public static String METHOD_TRACE         = "TRACE";

    public static String HEADER_HOST          = "Host";
    public static String HEADER_USER_AGENT    = "User-Agent";

    public static int    STATUS_SUCCESS       = 200;
    public static int    STATUS_NOT_FOUND     = 404;
    public static int    STATUS_TEMP_REDIRECT = 302;
    public static int    STATUS_PERM_REDIRECT = 301;
    public static int    STATUS_SERVICE_ERROR = 500;
}
