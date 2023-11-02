package com.udemy;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromptEngineeringFunctionTest {

    @Mock
    private OutputBinding<String> promptQueue;
    @Mock
    ExecutionContext executionContext;
    @Mock
    Logger logger;

    private PromptEngineeringFunction function;

    @BeforeEach
    void setUp() {
        when(executionContext.getLogger()).thenReturn(logger);

        function = new PromptEngineeringFunction();
    }

    @Test
    void promptIsSentToPromptQueue() {
        var question = "Will this test work?";

        function.sendPrompt(question, promptQueue, executionContext);

        verify(promptQueue, times(1)).setValue(anyString());
    }
}