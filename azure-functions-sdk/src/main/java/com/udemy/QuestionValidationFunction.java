package com.udemy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.annotation.QueueOutput;

public class QuestionValidationFunction {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FunctionName("QuestionValidationFunction")
    public HttpResponseMessage validate(
            @HttpTrigger(name = "question",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.FUNCTION)
            HttpRequestMessage<String> question,
            @QueueOutput(name = "questionQueue",
                    queueName = "question-queue",
                    connection = "AzureWebJobsStorage")
            OutputBinding<String> questionQueue) {
        return LanguageValidator.forQuestion(question)
                .validate()
                .onValid(() -> {
                    try {
                        var questionPayload = question.getBody();
                        questionQueue.setValue(questionPayload);

                        var replyPayload = objectMapper.writeValueAsString(new AskConfirmation(questionPayload));
                        return question.createResponseBuilder(HttpStatus.ACCEPTED)
                                .header(CONTENT_TYPE, APPLICATION_JSON)
                                .body(replyPayload)
                                .build();
                    } catch (JsonProcessingException e) {
                        return question.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                                .header(CONTENT_TYPE, APPLICATION_JSON)
                                .body("{\"error\":\"Could not process the question\"}")
                                .build();
                    }
                })
                .onInvalid(() ->
                        question.createResponseBuilder(HttpStatus.BAD_REQUEST)
                            .header(CONTENT_TYPE, APPLICATION_JSON)
                            .body("{\"error\":\"The question must be asked in English\"}")
                            .build())
                .onUnknown(() ->
                        question.createResponseBuilder(HttpStatus.BAD_REQUEST)
                            .header(CONTENT_TYPE, APPLICATION_JSON)
                            .body("{\"error\":\"Missing required information in the request\"}")
                            .build())
                .reply();
    }
}