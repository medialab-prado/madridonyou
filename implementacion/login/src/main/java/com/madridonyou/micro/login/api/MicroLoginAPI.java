package com.madridonyou.micro.login.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.madridonyou.micro.domain.inputs.LoginInput;
import com.madridonyou.micro.domain.inputs.RegisterInput;
import com.madridonyou.micro.domain.outputs.LoginOutput;
import com.madridonyou.micro.domain.outputs.RegisterOutput;

@Controller
public interface MicroLoginAPI {

	@RequestMapping(
			value ="/login", 
			method = RequestMethod.POST,
			consumes = {"application/json"},
			produces = {"application/json"}
			)
	public ResponseEntity<LoginOutput> login (@RequestBody LoginInput input);
	
	
	@RequestMapping(
			value ="/register", 
			method = RequestMethod.POST,
			consumes = {"application/json"},
			produces = {"application/json"}
			)
	public ResponseEntity<RegisterOutput> register (@RequestBody RegisterInput input);
	
	@RequestMapping("/test")
	public String test();
}
