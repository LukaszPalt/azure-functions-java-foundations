package com.udemy;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.OutputBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionValidationFunctionTest {

    @Mock
    private HttpRequestMessage<String> questionRequest;
    @Mock
    OutputBinding<String> outputBinding;
    @Mock
    ExecutionContext executionContext;
    @Mock
    Logger logger;

    private QuestionValidationFunction function;

    @BeforeEach
    void setUp() {
        doAnswer((Answer<HttpResponseMessage.Builder>) invocation -> {
            HttpStatus status = (HttpStatus) invocation.getArguments()[0];
            return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
        }).when(questionRequest).createResponseBuilder(any(HttpStatus.class));

        when(executionContext.getLogger()).thenReturn(logger);

        function = new QuestionValidationFunction();
    }

    @ParameterizedTest
    @MethodSource("testParameters")
    void functionReturnsCorrectMessageForAcceptedLanguage(Map<String, String> actualHeaders,
                                                          HttpStatus expectedStatus,
                                                          String expectedMessage) {
        // Arrange
        doReturn(actualHeaders).when(questionRequest).getHeaders();
        if (expectedStatus.equals(HttpStatus.ACCEPTED)) {
            doReturn("How will the ongoing AI revolution affect the financial industry?")
                    .when(questionRequest).getBody();
        }

        // Act
        var response = function.validate(questionRequest, outputBinding, executionContext);

        // Assert
        assertEquals(expectedStatus, response.getStatus());
        assertEquals(expectedMessage, response.getBody());
        assertEquals("application/json", response.getHeader("Content-Type"));
    }

    @Test
    void validatedQuestionIsSentDownPipeline() {
        // Arrange
        doReturn(Map.of("accept-language", "en")).when(questionRequest).getHeaders();
        doReturn("How will the ongoing AI revolution affect the financial industry?")
                .when(questionRequest).getBody();

        // Act
        function.validate(questionRequest, outputBinding, executionContext);

        // Assert
        verify(outputBinding, times(1)).setValue("How will the ongoing AI revolution affect " +
                "the financial industry?");
    }

    private static Stream<Arguments> testParameters() {
        return Stream.of(
                Arguments.of(Map.of("accept-language", "en"), HttpStatus.ACCEPTED,
                        "{\"asked\":\"How will the ongoing AI revolution affect the financial industry?\"}"),
                Arguments.of(Map.of("accept-language", "fr"), HttpStatus.BAD_REQUEST,
                        "{\"error\":\"The question must be asked in English\"}"),
                Arguments.of(Map.of(), HttpStatus.BAD_REQUEST, "{\"error\":\"Missing required information " +
                        "in the request\"}")
        );
    }
}