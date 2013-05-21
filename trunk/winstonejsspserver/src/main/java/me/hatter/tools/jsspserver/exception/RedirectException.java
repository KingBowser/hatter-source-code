package me.hatter.tools.jsspserver.exception;

public class RedirectException extends BusinessException {

    private static final long serialVersionUID = 1L;

    private String            url;

    public RedirectException(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
