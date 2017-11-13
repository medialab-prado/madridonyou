package com.madridonyou.micro.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App 
{
	public static void main( String[] args )
	{
		String akField = args[0];
		String akValue = args[1];
		String skField = args[2];
		String skValue = args[3];

		System.setProperty(akField, akValue);
		System.setProperty(skField, skValue);
		
		SpringApplication.run(App.class, args);
	}
}
