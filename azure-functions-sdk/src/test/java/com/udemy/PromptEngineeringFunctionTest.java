package com.udemy;

import com.microsoft.azure.functions.OutputBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PromptEngineeringFunctionTest {

    @Mock
    private OutputBinding<String> promptQueue;

    private PromptEngineeringFunction function;

    @BeforeEach
    void setFunction() {
        function = new PromptEngineeringFunction();
    }

    @Test
    void promptIsSentToPromptQueue() {
        var question = "Will this test work?";

        function.sendPrompt(question, promptQueue);

        verify(promptQueue, times(1)).setValue(anyString());
    }
}