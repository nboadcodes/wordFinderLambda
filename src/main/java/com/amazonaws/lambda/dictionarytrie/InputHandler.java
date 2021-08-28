package com.amazonaws.lambda.dictionarytrie;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class InputHandler implements RequestHandler<Object, APIGatewayProxyResponseEvent> {

	 @SuppressWarnings("unchecked")
	 // *Can specify the input and output type
		private static String result = "";
		@Override
	    public APIGatewayProxyResponseEvent handleRequest(Object input, Context context) {
	        context.getLogger().log("Input: " + input + '\n');
	        result = "";
	       return decodeJSON(input, new JSONObject((Map<String, Object>) input), context);
	             
	        
	        /*
	        JSONObject headers = new JSONObject();
	    	//headers.put("ContentType", "applicationJson");
	    	JSONObject body = new JSONObject();
	    	body.put("words", "ehehehe");
	    	
	    	JSONObject response = new JSONObject();
	    	response.put("statusCode", 200);
	    	response.put("headers", headers);
	    	response.put("body", body);
	    	response.put("isBase64Encoded", false);
	    	
	    	context.getLogger().log(response.toJSONString());
	    	
			return response.toJSONString();     
	        */
	        
	        //finddirectories("/var/task/resources");
	        //  return null;
	    }
	/*
		public void finddirectories(String path){
			File dir = new File(path);
			
			try {
				InputHandler.c.getLogger().log("\n Inside of: " + dir.getCanonicalPath());
				String[] filesList = dir.list();
				if(filesList == null) return;
				for (String s : filesList) {
					//File f;
					//f = new File(path + s);
					InputHandler.c.getLogger().log("\n Sub: " + s);
					//if(f.isDirectory()){
						finddirectories(path + s);
					//}
				    
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		public APIGatewayProxyResponseEvent decodeJSON(Object i, JSONObject fullInput, Context context) {
	    	String letters;
			CharSequence req;
			int minlength;
			String iType;
			String iSource;
			
	    	//try{
	    		context.getLogger().log("smallinput: " + fullInput.get("queryStringParameters"));
	    		JSONObject input = new JSONObject((Map<String, Object>) fullInput.get("queryStringParameters"));
	    		context.getLogger().log(input.entrySet().toString());
	    		letters = (String)input.get("letters");
	    		req = (CharSequence)input.get("required");
	    		minlength = Integer.parseInt((String)input.get("minlength"));
	    		iType = (String) input.get("inputtype");
	    		iSource = (String) input.get("inputsource");
	    		
	    	//}catch(Exception e){
	    		context.getLogger().log("Bad sub-json, but why though?");
	    		//return "Bad JSON input, so this is what you send instead huh??? \n ";
	    	//d}
	    	
	    	context.getLogger().log("letters: " + letters);
	    	context.getLogger().log("required: " + req);
	    	context.getLogger().log("minlength: " + minlength);
	    	context.getLogger().log("inputtype: " + iType);
	    	context.getLogger().log("inputsource: " + iSource);
	    	
	    	TreeNode root = null;
	    	if(iType.equals("text")){
	    		root = DictionaryManager.createDictionary(iSource);
	    	}
	    	else if(iType.equals("serialized")){
	    		
	    		root = DictionaryManager.deserializeDictionary(iSource);
	    	}
	    	if(root != null){
	    		root.findNYTCombinations(letters.toCharArray(), req, minlength);
	    	}
	    	else return null;

	    	//JSONObject headers = new JSONObject();
	    	//headers.put("ContentType", "applicationJson");
	    	//JSONObject body = new JSONObject();
	    	//body.put("words", result);
	    	/*
	    	JSONObject response = new JSONObject();
	    	response.put("statusCode", 200);
	    	response.put("headers", headers);
	    	response.put("body", result);
	    	//response.put("body", body);
	    	response.put("isBase64Encoded", false);
	    	
	    	context.getLogger().log(response.toJSONString());
	    	context.getLogger().log(response.toString());
	    	*/
	    	APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
	    	HashMap<String, String> headers = new HashMap<String, String>();
	    	headers.put("Access-Control-Allow-Origin", "*");
	    	headers.put("Testing-Testing", "blahblah");
	    	response.setHeaders(headers);
		    response.setBody(result);
		    context.getLogger().log(result);
		    response.setIsBase64Encoded(false);
		    response.setStatusCode(200);
	    	
		    context.getLogger().log(response.toString());
		    
			return response;
	    }
	    
	    public static void addToString(String word){
	    	result = result + word + " ";
	    }
	}

