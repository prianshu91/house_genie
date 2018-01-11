package com.promelle.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.UUID;

import com.promelle.exception.AbstractException;

/**
 * This class is intended for providing file functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class FileUtils {

	public static File writeToTempFile(InputStream inputStream, String extension)
			throws IOException, AbstractException {
		File tempFile = File.createTempFile(UUID.randomUUID().toString(),
				extension);
		int read = 0;
		byte[] bytes = new byte[1024];
		try (OutputStream out = new FileOutputStream(tempFile)) {
			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
		}
		tempFile.deleteOnExit();
		inputStream.close();
		return tempFile;
	}
	
	public static File createFileAndWriteContent(String fileName, 
			String extension, String content) throws IOException { 
		BufferedWriter bufferedWriter = null;
		File tempFile = File.createTempFile(fileName, extension);
		if(tempFile.exists()){
			tempFile.delete();
		}
		tempFile.createNewFile();		
		
		Writer writer;
		try {
			writer = new FileWriter(tempFile);
			bufferedWriter = new BufferedWriter(writer);
			bufferedWriter.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {		
			if(bufferedWriter != null)
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return tempFile;	
	}

}
