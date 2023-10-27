package com.udemy;

import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.annotation.QueueOutput;

import java.util.HashMap;
import java.util.Map;

public class InternationalisationFunction {

    private static final Map<String, String> messages = new HashMap<>();

    static {
        messages.put("en", "Hello!");
        messages.put("fr", "Bonjour !");
        messages.put("es", "¡Hola!");
        messages.put("de", "Hallo!");
    }

    @FunctionName("greet")
    public HttpResponseMessage greet(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<String> request,
            @QueueOutput(name = "queueOutput",
                    queueName = "greetings-queue",
                    connection = "AzureWebJobsStorage")
            OutputBinding<String> queueOutput) {
        var headers = request.getHeaders();
        var acceptLanguage = headers.get("accept-language");

        var message = messages.getOrDefault(acceptLanguage, "Hello!");

        queueOutput.setValue(message);

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "text/plain")
                .body(message)
                .build();
    }
}