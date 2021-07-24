package com.web.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.json.Account;

@Controller
public class WebController {

	private static final Logger logger = LoggerFactory.getLogger(WebController.class);
	
	
	@GetMapping("/html")
	public String index() {
		return "index.html";
	}
	
	
	@PostMapping("/request")
	@ResponseBody
	public ResponseEntity<String> request(@RequestParam String url, @RequestParam String method ,@RequestParam String content) {
		//get request content
		//response rest api response
		RestTemplate template = new RestTemplate();
		logger.info("url: "+ url +"method: "+ method);
		ResponseEntity<String> response = null;
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(content,header);
		
		switch(method)
		{
		case "GET":
			
			logger.info("GETTING");
			response=template.getForEntity(url, String.class,request);
			//below code for mapping a request to object
			if(url.contains("getAll")) {
				ObjectMapper mapper = new ObjectMapper();
				Account[] list;
				try {
					String json= response.getBody();
					list = mapper.readValue(json,Account[].class);
					List<Account> arrList = Arrays.asList(list);
					String res ="";
					for(Account ac:arrList) {
						res+=ac.getName();
						res+=ac.getBalance();
						res+=",";
					}
					return ResponseEntity.ok(res);
				
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
			}
		
			break;
		case "POST":
			logger.info("POSTING");
			
		
			response = template.postForEntity(url, request, String.class);
			break;
		}
		
		return response;
		
	}
	
}
