package com.udemy.answer;

import com.microsoft.azure.functions.OutputBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnswerHandlerTest {

    @Mock
    private Function<String, String> answer;
    @Mock
    private OutputBinding<String> answerQueue;

    private AnswerHandler answerHandler;

    @BeforeEach
    void setUp() {
        when(answer.apply(anyString())).thenReturn("Mock answer");

        answerHandler = new AnswerHandler(answer);
    }

    @Test
    void answerIsSentToOutputQueue() {
        answerHandler.answer("Test prompt", answerQueue);

        verify(answerQueue, times(1)).setValue(anyString());
    }
}