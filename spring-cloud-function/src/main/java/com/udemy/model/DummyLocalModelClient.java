package com.udemy.model;

import org.springframework.stereotype.Service;

@Service
public class DummyLocalModelClient implements ModelClient {

    @Override
    public String ask(String prompt) {
        return "AI will completely revolutionise the financial industry.";
    }
}