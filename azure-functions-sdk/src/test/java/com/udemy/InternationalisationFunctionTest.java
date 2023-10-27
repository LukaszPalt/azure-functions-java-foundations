package com.udemy;

import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.OutputBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InternationalisationFunctionTest {

    @Mock
    private HttpRequestMessage<String> requestMessage;
    @Mock
    private OutputBinding<String> outputBinding;

    private InternationalisationFunction function;

    @BeforeEach
    void setFunction() {
        function = new InternationalisationFunction();
    }

    @ParameterizedTest
    @MethodSource("testParameters")
    void functionReturnsCorrectMessageForAcceptedLanguage(String acceptLanguage, String expectedMessage) {
        // Arrange
        doReturn(Map.of("accept-language", acceptLanguage)).when(requestMessage).getHeaders();
        doAnswer((Answer<HttpResponseMessage.Builder>) invocation -> {
            HttpStatus status = (HttpStatus) invocation.getArguments()[0];
            return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
        }).when(requestMessage).createResponseBuilder(any(HttpStatus.class));

        // Act
        final HttpResponseMessage response = function.greet(requestMessage, outputBinding);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(expectedMessage, response.getBody());
        assertEquals("text/plain", response.getHeader("Content-Type"));
        verify(outputBinding, times(1)).setValue(anyString());
    }

    private static Stream<Arguments> testParameters() {
        return Stream.of(
                Arguments.of("en", "Hello!"),
                Arguments.of("fr", "Bonjour !"),
                Arguments.of("es", "¡Hola!"),
                Arguments.of("de", "Hallo!"),
                Arguments.of("no", "Hello!")
        );
    }
}