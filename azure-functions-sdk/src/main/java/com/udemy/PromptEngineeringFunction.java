package com.udemy;

import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.QueueOutput;
import com.microsoft.azure.functions.annotation.QueueTrigger;

public class PromptEngineeringFunction {

    @FunctionName("PromptEngineeringFunction")
    public void sendPrompt(
            @QueueTrigger(
                    name = "question",
                    queueName = "questions-queue",
                    connection = "AzureWebJobsStorage")
            String question,
            @QueueOutput(
                    name = "promptQueue",
                    queueName = "prompt-queue",
                    connection = "AzureWebJobsStorage")
            OutputBinding<String> promptQueue) {
        var prompt = Prompt.from(question).create();
        promptQueue.setValue(prompt);
    }
}
