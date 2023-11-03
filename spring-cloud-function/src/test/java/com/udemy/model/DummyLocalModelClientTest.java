package com.udemy.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class DummyLocalModelClientTest {

    private DummyLocalModelClient client;

    @BeforeEach
    void setClient() {
        client = new DummyLocalModelClient();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "How AI will affect the financial industry?",
            "Is this a dumb quesiton?",
            "To be or not to be?"
    })
    void clientAlwaysGivesPredefinedAnswer(String prompt) {
        var answer = client.ask(prompt);

        assertThat(answer).isEqualTo("AI will completely revolutionise the financial industry.");
    }
}