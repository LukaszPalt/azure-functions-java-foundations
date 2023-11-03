package com.udemy;

import com.udemy.model.ModelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class FunctionConfiguration {

	@Bean
	public Function<String, String> answer(ModelClient modelClient) {
		return modelClient::ask;
	}
}