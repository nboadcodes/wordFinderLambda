package com.amazonaws.lambda.dictionarytrie;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class InputHandler implements RequestHandler<Object, APIGatewayProxyResponseEvent> {

	 	//Result string
		private static String result = "";
		@Override
		//Initial method called
		//input should be a JSON object of API gateway request
		//returns APIGatewayProxyResponseEvent which is JSON object that helps output conform to AWS APIGateway standards
	    public APIGatewayProxyResponseEvent handleRequest(Object input, Context context) {
	       context.getLogger().log("Input: " + input + '\n');
	       result = "";
	       try{
	    	   return decodeJSON(new JSONObject((Map<String, Object>) input), context);
	       }
	       catch(ClassCastException e){
	    	   context.getLogger().log("Unable to convert to Map and JSON: " + input);
	    	   return null;
	       }
		}
		
		//Decodes the JSON passed in by API Gateway and uses dictionary manager to handle request
		//returns APIGatewayProxyResponseEvent which helps output conform to API Gateaway standards
		public APIGatewayProxyResponseEvent decodeJSON(JSONObject fullInput, Context context) {
			//5 query string parameters that affect the program
	    	String letters;
			CharSequence req;
			int minlength;
			String iType;
			String iSource;
			
			//Isolate query string parameters from full JSON
	    	context.getLogger().log("QueryStringInputs: " + fullInput.get("queryStringParameters"));
	    	JSONObject input = new JSONObject((Map<String, Object>) fullInput.get("queryStringParameters"));
	    		
	    	//Attempt to cast query string parameters to their proper types
	    	letters = (String)input.get("letters");
	    	req = (CharSequence)input.get("required");
	    	minlength = Integer.parseInt((String)input.get("minlength"));
	    	iType = (String) input.get("inputtype");
	    	iSource = (String) input.get("inputsource");
	    	
	    	context.getLogger().log("NEWEST VERSION!!!!!!!!");
	    	context.getLogger().log("letters: " + letters);
	    	context.getLogger().log("required: " + req);
	    	context.getLogger().log("minlength: " + minlength);
	    	context.getLogger().log("inputtype: " + iType);
	    	context.getLogger().log("inputsource: " + iSource);
	    	
	    	//Creates empty root for the trie
	    	TreeNode root = null;
	    	//Decides whether to read dictionary from serialized file or plain text
	    	if(iType.equals("text")){
	    		root = DictionaryManager.createDictionary(iSource);
	    	}
	    	else if(iType.equals("serialized")){
	    		root = DictionaryManager.deserializeDictionary(iSource);
	    	}
	    	
	    	//If successful, has the root find the words
	    	if(root != null){
	    		root.findNYTCombinations(letters.toCharArray(), req, minlength);
	    	}
	    	else return null;

	    	
	    	APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
	    	
	    	HashMap<String, String> headers = new HashMap<String, String>();
	    	//Enables CORS
	    	headers.put("Access-Control-Allow-Origin", "*");
	    	
	    	//Prepare response event
	    	response.setHeaders(headers);
		    response.setBody(result);
		    response.setIsBase64Encoded(false);
		    response.setStatusCode(200);
	    	
		    context.getLogger().log(result);
		    context.getLogger().log(response.toString());
		    
			return response;
	    }
	    
		//Not the most efficient
		//Concatenates the new word onto result string
		//string: word to be concatenated
	    public static void addToString(String word){
	    	result = result + word + " ";
	    }
	}

