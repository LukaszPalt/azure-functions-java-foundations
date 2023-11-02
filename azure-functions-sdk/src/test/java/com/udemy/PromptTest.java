package com.udemy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class PromptTest {

    private static final String PROMPT_TEMPLATE = "You are a CEO. Given a strategic question, you will create one " +
            "futuristic, hypothetical scenarios that happen 5 years from now. Each scenario must be an optimistic " +
            "version of the future. Each scenario must be realistic. The strategic question is: ";

    @ParameterizedTest
    @ValueSource(strings = {
            "Will this test work?",
            "To be or not to be?",
            "Will AI take my job?",
            ""
    })
    void promptIsCorrectlyEngineeredFromQuestion(String question) {
        var prompt = Prompt.from(question).create();
        assertThat(prompt).isEqualTo(PROMPT_TEMPLATE + question);
    }
}