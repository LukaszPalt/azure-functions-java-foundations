package com.udemy;

import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LanguageValidatorTest {

    @Mock
    private HttpRequestMessage<String> question;
    private Supplier<HttpResponseMessage> onValidSupplier;
    private Supplier<HttpResponseMessage> onInvalidSupplier;
    private Supplier<HttpResponseMessage> onUnknownSupplier;

    @BeforeEach
    void setUpSuppliers() {
        onValidSupplier = () -> {
            var reply = mock(HttpResponseMessage.class);
            when(reply.getBody()).thenReturn("AI industry will revolutionize financial industry.");
            return reply;
        };
        onInvalidSupplier = () -> {
            var reply = mock(HttpResponseMessage.class);
            when(reply.getBody()).thenReturn("I'm sorry, but I can only speak English.");
            return reply;
        };
        onUnknownSupplier = () -> {
            var reply = mock(HttpResponseMessage.class);
            when(reply.getBody()).thenReturn("I'm sorry, an error occurred. Your chat software don't send all the " +
                    "required information.");
            return reply;
        };
    }

    @Test
    void shouldReturnOnValidReplyWhenQuestionLanguageIsEnglish() {
        when(question.getHeaders()).thenReturn(Map.of("accept-language", "en"));

        var reply = LanguageValidator.forQuestion(question)
                .validate()
                .onValid(onValidSupplier)
                .onInvalid(onInvalidSupplier)
                .onUnknown(onUnknownSupplier)
                .reply();

        assertThat(reply.getBody()).isInstanceOf(String.class);
        assertThat(reply.getBody()).isEqualTo("AI industry will revolutionize financial industry.");
    }

    @Test
    void shouldReturnOnInvalidReplyWhenQuestionLanguageIsNorwegian() {
        when(question.getHeaders()).thenReturn(Map.of("accept-language", "no"));

        var reply = LanguageValidator.forQuestion(question)
                .validate()
                .onValid(onValidSupplier)
                .onInvalid(onInvalidSupplier)
                .onUnknown(onUnknownSupplier)
                .reply();

        assertThat(reply.getBody()).isInstanceOf(String.class);
        assertThat(reply.getBody()).isEqualTo("I'm sorry, but I can only speak English.");
    }

    @Test
    void shouldReturnOnUnknownReplyWhenQuestionLanguageIsNotDefined() {
        when(question.getHeaders()).thenReturn(Map.of());

        var reply = LanguageValidator.forQuestion(question)
                .validate()
                .onValid(onValidSupplier)
                .onInvalid(onInvalidSupplier)
                .onUnknown(onUnknownSupplier)
                .reply();

        assertThat(reply.getBody()).isInstanceOf(String.class);
        assertThat(reply.getBody()).isEqualTo("I'm sorry, an error occurred. Your chat software don't send " +
                "all the required information.");
    }
}