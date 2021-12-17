package com.amazonaws.lambda.dictionarytrie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

public class TreeNode {
	private HashMap<Character, TreeNode> letters = new HashMap<Character, TreeNode>(); //stores future letters
	private boolean terminal = false; //false if no word ends on this character, true if word does end
	private char myletter; //stores the letter the node represents
	
	//Constructor for letter node
	public TreeNode(char _myletter) {
		//Uppercase letter passed in means it is terminal, end of string
		if(Character.isUpperCase(_myletter)) {
			myletter = Character.toLowerCase(_myletter);
			terminal = true;
		}
		else myletter = _myletter;
	}
	
	//Empty constructor for a root node
	public TreeNode() {
		myletter = '*'; //indicates root, start of file
	}
	
	//Adds a string to the trie through a series of calls down nodes in the tree
	//word: char array representation of string to be added
	//index: next index of the string trying to be stored
	public void addString(char[] word, int index) {
		if(word.length == index) terminal = true;
		else{
			//Create a new letter node if it does not exist
			if(!letters.containsKey(word[index])) {
				letters.put(word[index], new TreeNode(word[index]));
			}
			letters.get(word[index]).addString(word, index+1);
		}
	}
	
	 /* Finds solution words that can be constructed from a set of characters
	 * Only the root node's instance of this method should be called */
	//word: array containing characters
	//requirement: required character or character sequence in the solution words
	//minlength: minimum length of a solution word
	public void findNYTCombinations(char[] word, CharSequence requirement, int minlength) {
		//Overloaded methods to save some runtime and memory usage
		if(requirement.length()==0){ //no character required
			combinationRecurse(TreeNode.filterDuplicates(word), "", minlength);
		}
		else if(requirement.length() == 1){ //single chracter requirement, true to the original NYT-spelling bee game
			combinationRecurse(TreeNode.filterDuplicates(word), "", requirement.charAt(0), minlength, false);
		}
		else if(requirement.length() > 1){ //for multi character subword requirements
			combinationRecurse(TreeNode.filterDuplicates(word), "", requirement.toString(), minlength, false);
		}
		else return; //this should not happen, can add error logging here
	}
	
	//combinationRecurse: Traversal helper methods where no special character required
	//Overloaded to accomodate different parameters
	//word: array containing characters
	//req(optional): required character or character sequence in the solution words
	//previous: keeps track of previous nodes characters for solution words
	//minlength: minimum length of a solution word
	//found(optional): boolean that keeps track whether the required character has been included
	private void combinationRecurse(char[] word, String previous, int minlength) {
		//Solution word if the character is terminal and reaches minimum length
		if(terminal && previous.length() >= minlength) 
			InputHandler.addToString(previous);
		
		//Calls this method for every existing child node with a key equal to a letter in the word
		for(int i = 0 ; i < word.length ; i++) 
			if(letters.containsKey(word[i])) 
				letters.get(word[i]).combinationRecurse(word, previous + word[i], minlength);
	}

	private void combinationRecurse(char[] word, String previous, char req, int minlength, boolean found) {
		//If required character hasn't been found, and if current char is required character, change found status
		if(!found && myletter == req)
			found = true;
		
		//Solution word if the character is terminal, required is found, and reaches minimum length
		if(terminal && found && previous.length() >= minlength)
			InputHandler.addToString(previous);
			
		for(int i = 0 ; i < word.length ; i++) 
			if(letters.containsKey(word[i])) 
				letters.get(word[i]).combinationRecurse(word, previous + word[i], req, minlength, found);
	}
	
	private void combinationRecurse(char[] word, String previous, CharSequence requirement, int minlength, boolean found) {
		//If terminal character and found, or if terminal, contains required sequence, and has minimum length
		if(terminal){
			if(found)
				InputHandler.addToString(previous);
			else if(previous.length() >= minlength && previous.contains(requirement)){
				InputHandler.addToString(previous);
				found = true;
			}
		}
			
		for(int i = 0 ; i < word.length ; i++) 
			if(letters.containsKey(word[i])) 
				letters.get(word[i]).combinationRecurse(word, previous + word[i], requirement, minlength, found);
	}
	
	//Checks whether a certain string is present in the dictionary
	//Not used for the dictionary trie word finder
	//s: string to be searched for
	//index: string index currently searching for, starts at 0
	//returns: true if dicitonary contains string, false otherwise
	public boolean containsString(String s, int index) {
		if(index >= s.length()) return true; //only needs to be ==, but >= added for readability
		
		//Returns false the first time the node's character doesn't match string's index character
		if(!letters.containsKey(s.charAt(index))) {
			System.out.println("Cannot find " + s.charAt(index) + " at slot " + index);
			return false;
		}
		
		return letters.get(s.charAt(index)).containsString(s, index + 1);
	}
	
	//Helps serialize this particular node into a text file
	//bw: buffered writer for setup by DictionaryManager
	public void serializeTree(BufferedWriter bw) throws IOException {
		//Writes this node's character, uppercase if character creates a word
		if(terminal) bw.write(Character.toUpperCase(myletter));
		else bw.write(myletter);
		
		//Extract all keys and call this method for all of them
		Object[] c = letters.keySet().toArray();
		for(int i = 0 ; i < c.length ; i++) 
			letters.get(c[i]).serializeTree(bw);
		
		//Write the end node character, represents going up the tree
		bw.write(']');
	}

	//Helps deserialize part of the text file into child nodes
	//br: buffered reader for setup by DictionaryManager
	public void deserializeTree(BufferedReader br) throws IOException {
		char temp;
		//repeat until this node is ended
		while(true) {	
			//read in next character
			temp = (char) br.read();
			//if end node, break and return
			if(temp == ']') break;
			//otherwise add child node whose characters is the next character
			letters.put(Character.toLowerCase(temp), new TreeNode(temp));
			letters.get(Character.toLowerCase(temp)).deserializeTree(br); 
		}
	}
	
	//Method used to remove duplicates of a character array
	//original: array to have duplicates removed from
	//returns: filtered array
	private static char[] filterDuplicates(char[] original){
		String removed = "";
		for(int i = 0 ; i < original.length ; i++)
			if(removed.indexOf(original[i]) == -1) 
				removed = removed + original[i];
		
		return removed.toCharArray();
	}
	
}
