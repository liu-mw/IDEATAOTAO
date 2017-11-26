package com.taotao.web.httpClient;

/**
 * @author liu_mw
 * @date 2017/11/23 21:42
 */
public class HttpResultOld {
    private Integer code;
    private String body;

    public HttpResultOld(){

    }
    public HttpResultOld(Integer code, String body) {
        this.code = code;
        this.body = body;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
