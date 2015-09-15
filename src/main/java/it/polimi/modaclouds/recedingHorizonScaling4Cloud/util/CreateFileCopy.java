package it.polimi.modaclouds.recedingHorizonScaling4Cloud.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateFileCopy {
	
	@SuppressWarnings("unused")
	private static final Logger journal = LoggerFactory.getLogger(CreateFileCopy.class);

	public static Path print(File orig, String relativePath, Object... substitutions) throws IOException {
		if (orig == null || !orig.exists() || orig.isDirectory())
			throw new RuntimeException("Your file doesn't exist or is not a file at all!");
		
		Path newFile;
		if (relativePath != null && relativePath.length() > 0)
			newFile = Paths.get(ConfigManager.getLocalTmp().toString(), relativePath, orig.getName());
		else
			newFile = Paths.get(ConfigManager.getLocalTmp().toString(), orig.getName());
		newFile.toFile().getParentFile().mkdirs();
		
		try (PrintWriter out = new PrintWriter(newFile.toFile())) {
			
			String baseFile = new String(Files.readAllBytes(orig.toPath())); //, Charset.defaultCharset()); // StandardCharsets.UTF_8);
			
			out.printf(baseFile, substitutions);
			
			out.flush();
			out.close();
		}
		
		return newFile;
	}

}
