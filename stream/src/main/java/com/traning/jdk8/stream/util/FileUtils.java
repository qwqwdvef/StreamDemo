package com.traning.jdk8.stream.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileUtils {
	
	public static Stream<String> load(String path) {
		
		try {
			return Files.lines(Paths.get(path),Charset.forName("GB2312"));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Stream<String> load(String path,Charset charset) {
		
		try {
			return Files.lines(Paths.get(path),charset);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
