package com.udemy.answer;

import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.QueueOutput;
import com.microsoft.azure.functions.annotation.QueueTrigger;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AnswerHandler {

    private final Function<String, String> answer;

    public AnswerHandler(Function<String, String> answer) {
        this.answer = answer;
    }

    @FunctionName("answer")
    public void answer(
            @QueueTrigger(
                    name = "promptQueue",
                    queueName = "prompt-queue",
                    connection = "AzureWebJobsStorage")
            String prompt,
            @QueueOutput(
                    name = "answerQueue",
                    queueName = "answer-queue",
                    connection = "AzureWebJobsStorage")
            OutputBinding<String> answerQueue
    ) {
        answerQueue.setValue(answer.apply(prompt));
    }
}