package com.madridonyou.micro.login.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.madridonyou.micro.domain.dynamoTables.MoyUsers;
import com.madridonyou.micro.domain.inputs.LoginInput;
import com.madridonyou.micro.domain.inputs.RegisterInput;
import com.madridonyou.micro.domain.outputs.LoginOutput;
import com.madridonyou.micro.domain.outputs.RegisterOutput;

import software.amazon.awssdk.auth.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDBClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

@Controller
public class MicroLoginController implements MicroLoginAPI{

	private DynamoDBClient dynamo;

	public ResponseEntity<LoginOutput> login(@Valid @RequestBody LoginInput input) {

		LoginOutput responseBody = new LoginOutput();
		HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
		initDynamoClient();

		try {
			if (checkInput(input))
			{
				List<String> attrs = new LinkedList<String>();
				attrs.add("userId");
				attrs.add("password");
				Map<String,AttributeValue> key = new HashMap<String,AttributeValue>();
				key.put(MoyUsers.USER_ID.getField(), AttributeValue.builder().s(""+input.getUsername().hashCode()).build());

				GetItemResponse item = this.dynamo.getItem(GetItemRequest.builder().tableName("moy-users").key(key).attributesToGet(attrs).build());

				if (item != null && item.item() != null 
						&& !item.item().isEmpty() 
						&& item.item().get("password").equals(AttributeValue.builder().s(""+input.getPassword().hashCode()).build()))
				{
					responseStatus = HttpStatus.OK;
					responseBody.setStatus("Login OK");
				}
				else responseBody.setStatus("Credenciales incorrectas");

			}
			else responseBody.setStatus("Credenciales incorrectas");

		}
		catch (Exception e) {
			responseBody.setStatus(e.getMessage());
		}
		return new ResponseEntity<LoginOutput>(responseBody,responseStatus);
	}

	public ResponseEntity<RegisterOutput> register(@Valid @RequestBody RegisterInput input) {

		HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
		RegisterOutput responseBody = new RegisterOutput();
		initDynamoClient();

		try {
			if (checkInput(input))
			{
				Map<String,AttributeValue> newUser = new HashMap<String,AttributeValue>();
				newUser.put(MoyUsers.USERNAME.getField(), AttributeValue.builder().s(input.getUsername()).build());
				newUser.put(MoyUsers.PASSWORD.getField(), AttributeValue.builder().s(""+input.getPassword().hashCode()).build());
				newUser.put(MoyUsers.NAME.getField(), AttributeValue.builder().s(input.getUsername()).build());
				newUser.put(MoyUsers.LAST_NAME.getField(), AttributeValue.builder().s(input.getLastName()).build());
				newUser.put(MoyUsers.USER_ID.getField(), AttributeValue.builder().s(""+input.getUsername().hashCode()).build());
				this.dynamo.putItem(PutItemRequest.builder().tableName("moy-users").conditionExpression("attribute_not_exists(userId)").item(newUser).build());
				responseStatus = HttpStatus.OK;
				responseBody.setStatus("Register OK");
			}
			else responseBody.setStatus("Error en los campos de entrada");

		} 
		catch (ConditionalCheckFailedException e) {
			responseBody.setStatus("Ya existe ese nombre de usuario");
		}
		catch (Exception e) {
			responseBody.setStatus(e.getMessage());
		}
		return new ResponseEntity<RegisterOutput>(responseBody, responseStatus);
	}

	private boolean checkInput(Object input) {

		if (input != null)
		{
			boolean correct = true;
			if (input instanceof LoginInput)
			{
				Iterator<String> props = ((LoginInput) input).getProperties();
				while (props.hasNext() && correct)
				{
					String prop = props.next();
					correct = prop != null && !prop.equals("") && prop.length() > 2;

				}
			}
			else if (input instanceof RegisterInput)
			{
				Iterator<String> props = ((RegisterInput) input).getProperties();
				while (props.hasNext() && correct)
				{
					String prop = props.next();
					correct = prop != null && !prop.equals("") && prop.length() > 2;
				}
			}
			return correct;
		}
		else return false;
	}

	private void initDynamoClient() {
		if (this.dynamo == null)
		{
			this.dynamo = DynamoDBClient.builder().region(Region.US_EAST_2)
					.credentialsProvider(new SystemPropertyCredentialsProvider()).build();
		}
	}

	public String test() {
		return "OK";
	}
}
