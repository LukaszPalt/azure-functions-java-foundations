package com.udemy;

import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.HttpStatusType;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseMessageMock implements HttpResponseMessage {
    private final int httpStatusCode;
    private final HttpStatusType httpStatus;
    private final Object body;
    private final Map<String, String> headers;

    public HttpResponseMessageMock(HttpStatusType status, Map<String, String> headers, Object body) {
        this.httpStatus = status;
        this.httpStatusCode = status.value();
        this.headers = headers;
        this.body = body;
    }

    @Override
    public HttpStatusType getStatus() {
        return this.httpStatus;
    }

    @Override
    public int getStatusCode() {
        return httpStatusCode;
    }

    @Override
    public String getHeader(String key) {
        return this.headers.get(key);
    }

    @Override
    public Object getBody() {
        return this.body;
    }

    public static class HttpResponseMessageBuilderMock implements Builder {
        private Object body;
        private final Map<String, String> headers = new HashMap<>();
        private HttpStatusType httpStatus;

        public Builder status(HttpStatus status) {
            this.httpStatus = status;
            return this;
        }

        @Override
        public Builder status(HttpStatusType httpStatusType) {
            this.httpStatus = httpStatusType;
            return this;
        }

        @Override
        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        @Override
        public Builder body(Object body) {
            this.body = body;
            return this;
        }

        @Override
        public HttpResponseMessage build() {
            return new HttpResponseMessageMock(this.httpStatus, this.headers, this.body);
        }
    }
}