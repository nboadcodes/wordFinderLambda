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
	        // TODO: set up your sample input object here.
	        JSONObject temp = new JSONObject();
	        temp.put("letters", "sadbn");
	        temp.put("required","b");
	        temp.put("minlength",4);
	        temp.put("inputtype","serialized");
	        temp.put("inputsource","serialization");
	        input = new JSONObject();
	        input.put("queryStringParameters", temp);
	    }

	    private Context createContext() {
	        TestContext ctx = new TestContext();

	        // TODO: customize your context here if needed.
	        ctx.setFunctionName("Your Function Name");

	        return ctx;
	    }

	    @Test
	    public void testInputHandler() {
	        InputHandler handler = new InputHandler();
	        Context ctx = createContext();

	        APIGatewayProxyResponseEvent output = handler.handleRequest(input, ctx);
	        System.out.println(output);
	        // TODO: validate output here if needed.
	        Assert.assertEquals("Hello from Lambda!", output);
	    }
}
