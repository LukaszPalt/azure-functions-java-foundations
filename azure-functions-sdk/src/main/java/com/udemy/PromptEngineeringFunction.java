package com.udemy;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.QueueOutput;
import com.microsoft.azure.functions.annotation.QueueTrigger;

public class PromptEngineeringFunction {

    @FunctionName("PromptEngineeringFunction")
    public void sendPrompt(
            @QueueTrigger(
                    name = "question",
                    queueName = "question-queue",
                    connection = "AzureWebJobsStorage")
            String question,
            @QueueOutput(
                    name = "promptQueue",
                    queueName = "prompt-queue",
                    connection = "AzureWebJobsStorage")
            OutputBinding<String> promptQueue,
            ExecutionContext executionContext) {
        var prompt = Prompt.from(question).create();
        promptQueue.setValue(prompt);
        executionContext.getLogger().info(() -> "Sent prompt [" + prompt + "] to the AI model.");
    }
}
