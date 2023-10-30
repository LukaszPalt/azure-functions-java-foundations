package com.udemy;

import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;

import java.util.function.Supplier;

class LanguageValidator {

    private enum Result {
        VALID, INVALID, UNKNOWN
    }

    private final HttpRequestMessage<String> question;
    private Result validationResult;
    private Supplier<HttpResponseMessage> replySupplier;

    private LanguageValidator(HttpRequestMessage<String> question) {
        this.question = question;
        this.validationResult = Result.UNKNOWN;
    }

    static LanguageValidator forQuestion(HttpRequestMessage<String> question) {
        return new LanguageValidator(question);
    }

    LanguageValidator validate() {
        var questionHeaders = question.getHeaders();
        if (questionHeaders != null && "en".equals(questionHeaders.get("accept-language"))) {
            validationResult = Result.VALID;
        } else if (questionHeaders != null && questionHeaders.get("accept-language") != null) {
            validationResult = Result.INVALID;
        }
        return this;
    }

    LanguageValidator onValid(Supplier<HttpResponseMessage> replySupplier) {
        if (validationResult == Result.VALID) {
            this.replySupplier = replySupplier;
        }
        return this;
    }

    LanguageValidator onInvalid(Supplier<HttpResponseMessage> replySupplier) {
        if (validationResult == Result.INVALID) {
            this.replySupplier = replySupplier;
        }
        return this;
    }

    LanguageValidator onUnknown(Supplier<HttpResponseMessage> replySupplier) {
        if (validationResult == Result.UNKNOWN) {
            this.replySupplier = replySupplier;
        }
        return this;
    }

    HttpResponseMessage reply() {
        return replySupplier.get();
    }
}