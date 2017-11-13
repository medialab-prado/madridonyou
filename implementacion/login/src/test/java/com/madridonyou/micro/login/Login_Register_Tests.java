package com.madridonyou.micro.login;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.madridonyou.micro.domain.inputs.LoginInput;
import com.madridonyou.micro.domain.inputs.RegisterInput;
import com.madridonyou.micro.domain.outputs.LoginOutput;
import com.madridonyou.micro.domain.outputs.RegisterOutput;
import com.madridonyou.micro.login.App;
import com.madridonyou.micro.login.api.MicroLoginAPI;
import com.madridonyou.micro.login.api.MicroLoginController;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Login_Register_Tests 
extends TestCase
{

	private MicroLoginAPI api = new MicroLoginController();

	public Login_Register_Tests( String testName ) {super( testName );
	}

	public static Test suite() { 	
		App.main(new String [] {"aws.accessKeyId","AKIAJGVO7MPXSUPSKEDA","aws.secretAccessKey","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"});
		return new TestSuite( Login_Register_Tests.class );
	}

	public void testLoginBadInput1()
	{
		LoginInput input = null;
		ResponseEntity<LoginOutput> out;

		out = api.login(input);
		assertTrue(out.getStatusCode() == HttpStatus.BAD_REQUEST && out.getBody().getStatus().equals("Credenciales incorrectas"));

		input = new LoginInput();
		out = api.login(input);
		assertTrue(out.getStatusCode() == HttpStatus.BAD_REQUEST && out.getBody().getStatus().equals("Credenciales incorrectas"));

		input.setUsername("unknown");
		input.setPassword("");
		out = api.login(input);
		assertTrue(out.getStatusCode() == HttpStatus.BAD_REQUEST && out.getBody().getStatus().equals("Credenciales incorrectas"));

		input.setUsername("");
		input.setPassword("asd");
		out = api.login(input);
		assertTrue(out.getStatusCode() == HttpStatus.BAD_REQUEST && out.getBody().getStatus().equals("Credenciales incorrectas"));

		input.setUsername("unknown2");
		input.setPassword("unknown2");
		out = api.login(input);
		assertTrue(out.getStatusCode() == HttpStatus.BAD_REQUEST && out.getBody().getStatus().equals("Credenciales incorrectas"));

	}

	public void testLoginGoodInput1()
	{
		LoginInput input = new LoginInput();
		ResponseEntity<LoginOutput> out;
		input.setUsername("jesus");
		input.setPassword("test");
		out = api.login(input);
		assertTrue(out.getStatusCode() == HttpStatus.OK && out.getBody().getStatus().equals("Login OK"));
	}

	public void testRegisterBadInput1()
	{

		RegisterInput input = null;
		ResponseEntity<RegisterOutput> out;

		out = api.register(input);
		assertTrue(out.getStatusCode() == HttpStatus.BAD_REQUEST && out.getBody().getStatus().equals("Error en los campos de entrada"));

		input = new RegisterInput();
		out = api.register(input);
		assertTrue(out.getStatusCode() == HttpStatus.BAD_REQUEST && out.getBody().getStatus().equals("Error en los campos de entrada"));

		input.setUsername("unknown");
		input.setPassword("");
		input.setName("sdas");
		input.setLastName("asda");
		input.setMail("asd");
		out = api.register(input);
		assertTrue(out.getStatusCode() == HttpStatus.BAD_REQUEST && out.getBody().getStatus().equals("Error en los campos de entrada"));

		input.setUsername("unknown");
		input.setPassword("");
		input.setLastName("asda");
		input.setMail("asd");
		out = api.register(input);
		assertTrue(out.getStatusCode() == HttpStatus.BAD_REQUEST && out.getBody().getStatus().equals("Error en los campos de entrada"));

		input.setUsername("unknown");
		out = api.register(input);
		assertTrue(out.getStatusCode() == HttpStatus.BAD_REQUEST && out.getBody().getStatus().equals("Error en los campos de entrada"));

		input.setUsername("jesus");
		input.setPassword("asd");
		input.setLastName("asda");
		input.setMail("asd");
		input.setName("unknown");
		out = api.register(input);
		assertTrue(out.getStatusCode() == HttpStatus.BAD_REQUEST && out.getBody().getStatus().equals("Ya existe ese nombre de usuario"));
	}

	public void testRegisterGoodInput1()
	{
//		RegisterInput input = new RegisterInput();
//		ResponseEntity<RegisterOutput> out;
//		
//		input.setUsername("unknown4");
//		input.setPassword("        ");
//		input.setLastName("asda");
//		input.setName("unknown");
//		input.setMail("asd");
//		out = api.register(input);
//		assertTrue(out.getStatusCode() == HttpStatus.OK && out.getBody().getStatus().equals("Register OK"));

	}
}
