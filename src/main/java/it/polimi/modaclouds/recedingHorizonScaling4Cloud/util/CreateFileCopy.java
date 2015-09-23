package it.polimi.modaclouds.recedingHorizonScaling4Cloud.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateFileCopy {
	
//	@SuppressWarnings("unused")
	private static final Logger journal = LoggerFactory.getLogger(CreateFileCopy.class);

	public static Path print(File orig, String relativePath, Object... substitutions) throws IOException {
		if (orig == null || (!orig.toString().contains(".jar!") && (!orig.exists() || orig.isDirectory())))
			throw new RuntimeException("Your file doesn't exist or is not a file at all!");
		
		Path newFile;
		if (relativePath != null && relativePath.length() > 0)
			newFile = Paths.get(ConfigManager.getLocalTmp().toString(), relativePath, orig.getName());
		else
			newFile = Paths.get(ConfigManager.getLocalTmp().toString(), orig.getName());
		newFile.toFile().getParentFile().mkdirs();
		
		String path = orig.toString();
		StringBuilder sb = new StringBuilder();
		
		if (path.contains(".jar!")) {
			path = path.substring(path.lastIndexOf(".jar!") + 5);
		}
		
		journal.trace("{} -> {}", path, newFile.toString());
		
		try (Scanner sc =
				new Scanner(ConfigManager.getInputStream(path))) {
			while (sc.hasNextLine()) {
				sb.append(sc.nextLine());
				sb.append("\n");
			}
		} catch (Exception e) {
			return null;
		}
		
		String baseFile = sb.toString();
			
		try (PrintWriter out = new PrintWriter(newFile.toFile())) {
			out.printf(baseFile, substitutions);
			out.flush();
		}
		
		return newFile;
	}

}
