package com.amazonaws.lambda.dictionarytrie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

public class TreeNode {
	private HashMap<Character, TreeNode> letters = new HashMap<Character, TreeNode>();
	private boolean terminal = false;
	private char myletter;
	
	//uppercase letter means it is terminal, end of string
	public TreeNode(char _myletter) {
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
	
	public void addString(char[] word, int index) {
		if(word.length == index) terminal = true;
		else{
			if(!letters.containsKey(word[index])) {
				letters.put(word[index], new TreeNode(word[index]));
			}
			letters.get(word[index]).addString(word, index+1);
		}
	}
	
	/*
	//Previous could be an immutable string or a mutable array/arraylist on the heap. First method is easier, little more memory used. 
	public void findNYTCombinationsOld(char[] word, String previous, CharSequence requirement) {
		if(terminal && previous.length() >= 6 && previous.contains(requirement)) System.out.println(previous);
		
		for(int i = 0 ; i < word.length ; i++) 
			if(letters.containsKey(word[i])) 
				letters.get(word[i]).findNYTCombinationsOld(word, previous + word[i], requirement);
	}
	*/
	//Previous could be an immutable string or a mutable array/arraylist on the heap. First method is easier, little more memory used. 
	
	public void findNYTCombinations(char[] word, CharSequence requirement, int minlength) {
		//if(requirement.length()==0){ }
		if(requirement.length() == 1){
			combinationRecurse(TreeNode.filterDuplicates(word), "", requirement.charAt(0), minlength, false);
		}
		else if(requirement.length() > 1){
			combinationRecurse(TreeNode.filterDuplicates(word), "", requirement.toString(), minlength, false);
		}
		else return; //this should not happen
	}
	
	private void combinationRecurse(char[] word, String previous, char req, int minlength, boolean found) {
		if(!found && myletter == req)
			found = true;
		
		if(terminal && found && previous.length() >= minlength)
			InputHandler.addToString(previous);
			
		for(int i = 0 ; i < word.length ; i++) 
			if(letters.containsKey(word[i])) 
				letters.get(word[i]).combinationRecurse(word, previous + word[i], req, minlength, found);
	}
	
	private void combinationRecurse(char[] word, String previous, CharSequence requirement, int minlength, boolean found) {
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
	
	//Index always starts 0
	public boolean containsString(String s, int index) {
		if(index >= s.length()) return true; //only needs to be ==, but >= added for readability
		
		if(!letters.containsKey(s.charAt(index))) {
			System.out.println("Cannot find " + s.charAt(index) + " at slot " + index);
			return false;
		}
		
		return letters.get(s.charAt(index)).containsString(s, index + 1);
	}
	
	public void serializeTree(BufferedWriter bw) throws IOException {
		if(terminal) bw.write(Character.toUpperCase(myletter));
		else bw.write(myletter);
		
		Object[] c = letters.keySet().toArray();
		for(int i = 0 ; i < c.length ; i++) 
			letters.get(c[i]).serializeTree(bw);
		
		bw.write(']');
	}

	public void deserializeTree(BufferedReader br) throws IOException {
		char temp;
		while(true) {	
			temp = (char) br.read();
			if(temp == ']') break;
			letters.put(Character.toLowerCase(temp), new TreeNode(temp));
			letters.get(Character.toLowerCase(temp)).deserializeTree(br); 
		}
	}
	
	private static char[] filterDuplicates(char[] original){
		String removed = "";
		for(int i = 0 ; i < original.length ; i++)
			if(removed.indexOf(original[i]) == -1) 
				removed = removed + original[i];
		
		return removed.toCharArray();
	}
	
}
