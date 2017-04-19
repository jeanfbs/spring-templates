package br.com.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {

	@GetMapping(value="echo/{msg}")
	public String echo(@PathVariable String msg){
		
		return msg;
	}
	
	
	
}
