package com.amazonaws.lambda.dictionarytrie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class DictionaryManager {

	/*
	public static void main(String[] args) throws IOException {
		saveDictionary("words", "serialization");
		TreeNode root;
		try {
			root = loadDictionary("serialization");
			root.findNYTCombinations("caploht".toCharArray(), "", "t");
		} catch (IOException e) {}
		
	}	
	*/
	
	public static void serializeDictionary(TreeNode root, String writeTo){
		try {
			BufferedWriter bw = FileMaster.write(writeTo);
			root.serializeTree(bw);
			bw.close();
		} catch (IOException e) {}
	}
	
	public static TreeNode createDictionary(String readFrom){
		try {
			BufferedReader br = FileMaster.read(readFrom);
			TreeNode root = new TreeNode();
			String str;
			while((str = br.readLine()) != null) 
				root.addString(str.toLowerCase().toCharArray(), 0);
			return root;
			
		} catch (IOException e) {return null;}
		
	}
	
	public static TreeNode deserializeDictionary(String readFrom){
		try {
			TreeNode root = new TreeNode();
			BufferedReader br = FileMaster.read(readFrom);
			if(br.read() != '*') {
				return null;
			}
			root.deserializeTree(br);
			br.close();
			return root;
		} catch (IOException e) {
			
			return null;
		}
	}
	
}
