package com.web.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
	
	@Value("${url.create}")
	private String createUrl;
	
	@Bean
	public String getCreateURL() {
		return createUrl;
	}
}
