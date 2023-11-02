package com.udemy;

final class Prompt {

    private static final String PROMPT_TEMPLATE = "You are a CEO. Given a strategic question, you will create one " +
            "futuristic, hypothetical scenarios that happen 5 years from now. Each scenario must be an optimistic " +
            "version of the future. Each scenario must be realistic. The strategic question is: {question}";

    private final String question;

    private Prompt(String question) {
        this.question = question;
    }

    static Prompt from(String question) {
        return new Prompt(question);
    }

    String create() {
        return PROMPT_TEMPLATE.replace("{question}", question);
    }
}