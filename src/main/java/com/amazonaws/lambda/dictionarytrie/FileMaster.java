package com.amazonaws.lambda.dictionarytrie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;

public class FileMaster {
	
	public static BufferedWriter write(String fileName) {
		try {
            FileOutputStream outputStream = new FileOutputStream(fileName + ".txt");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            return new BufferedWriter(outputStreamWriter);
        } catch (IOException e) {
           return null;
        }
	}
	
	public static BufferedReader read(String fileName) {
		try {
			FileInputStream inputS = new FileInputStream("/var/task/resources/" +fileName + ".txt");
			InputStreamReader isr = new InputStreamReader(inputS, "UTF-8");
			return new BufferedReader(isr);
		}catch (IOException e) {
			return null;
		}
	}
}
