package com.web.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.common.Config;
import com.web.json.Account;

@Controller
public class WebController {

	private static final Logger logger = LoggerFactory.getLogger(WebController.class);
	
	@Autowired
	private Config config;
	
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
			if(url.equals("http://sendrequestclient.us-east-2.elasticbeanstalk.com/getAll")) {
				ObjectMapper mapper = new ObjectMapper();
				Account[] list;
				try {
					String json= response.getBody();
					list = mapper.readValue(json,Account[].class);
					
					String res ="Custom Response from entity object: ";
					for(Account ac:list) {
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
			
			
			response = template.exchange(url, HttpMethod.POST, request, String.class);
			break;
		}
		
		return response;
		
	}
	
	@PostMapping("/create")
	@ResponseBody
	public ResponseEntity<String> create(@RequestParam String name,@RequestParam Long balance) {
		ResponseEntity<String> response= null;
		String url = config.getCreateURL(); 
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
		map.add("name", name);
		map.add("balance", balance);
		HttpHeaders header = new HttpHeaders();
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String,Object>>(map,header);
		
		response = restTemplate.postForEntity(url, request, String.class);
		
		
		StringBuilder sb = new StringBuilder();
		return response;
		
		
				
		
	}
	
	
}
