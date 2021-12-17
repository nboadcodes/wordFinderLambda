package com.amazonaws.lambda.dictionarytrie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class DictionaryManager {

	//Serializes an existing trie that stores a dictionary
	//root: the root of a dictionary trie
	//writeTo: the filename that the serialized dictionary will be written to
	public static void serializeDictionary(TreeNode root, String writeTo){
		try {
			BufferedWriter bw = FileMaster.write(writeTo);
			root.serializeTree(bw);
			bw.close();
		} catch (IOException e) {}
	}
	
	//Constructs a dictionary storing trie from a serialization file
	//readFrom: the filename of a file that contains a serialized trie
	//Easiest to get serialization file from serializeDictionary() above
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

	//Creates a trie structure to store a dictionary
	//readFrom: file name of a file that contains a list of newline separated words
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
}
