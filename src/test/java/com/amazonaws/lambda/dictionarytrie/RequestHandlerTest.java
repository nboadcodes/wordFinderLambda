package com.amazonaws.lambda.dictionarytrie;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class RequestHandlerTest {

	 private static JSONObject input;

	    @BeforeClass
	    public static void createInput() throws IOException {
	        //***JSON Object no longer appropriate for testing the Input Handler***
	        JSONObject temp = new JSONObject();
	        temp.put("letters", "sadbn");
	        temp.put("required","");
	        temp.put("minlength",4);
	        temp.put("inputtype","serialized");
	        temp.put("inputsource","serialization");
	        input = new JSONObject();
	        input.put("queryStringParameters", temp);
	    }

	    private Context createContext() {
	        TestContext ctx = new TestContext();
	        ctx.setFunctionName("Your Function Name");
	        return ctx;
	    }

	    @Test
	    public void testInputHandler() {
	        InputHandler handler = new InputHandler();
	        Context ctx = createContext();

	        APIGatewayProxyResponseEvent output = handler.handleRequest(input, ctx);
	        System.out.println(output);
	        Assert.assertEquals("Hello from Lambda!", output);
	    }
}
