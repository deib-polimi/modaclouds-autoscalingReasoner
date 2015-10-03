/**
 * Copyright (C) 2014 Politecnico di Milano (michele.guerriero@mail.polimi.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.polimi.modaclouds.recedingHorizonScaling4Cloud.sshConnector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalShell {

	protected static final Logger journal = LoggerFactory.getLogger(LocalShell.class);

	public static List<String> exec(String command) throws Exception {
		final List<String> res = new ArrayList<String>();
		
		ProcessBuilder pb = new ProcessBuilder(new String[] { "bash", "-c", command });
		pb.redirectErrorStream(true);

		final Process p = pb.start();
		
		Thread in = new Thread() {
			public void run() {
				try (Scanner sc = new Scanner(p.getInputStream())) {
					while (sc.hasNextLine()) {
						String line = sc.nextLine();
						journal.trace(line);
						res.add(line);
					}
				}
			}
		};
		in.start();
		
		Thread err = new Thread() {
			public void run() {
				try (Scanner sc = new Scanner(p.getErrorStream())) {
					while (sc.hasNextLine()) {
						String line = sc.nextLine();
						journal.trace(line);
						res.add(line);
					}
				}
			}
		};
		err.start();

		in.join();
		err.join();
		
		res.add(String.format("exit-status: %d", p.waitFor()));
		
		return res;
	}
	
	public static void sendFile(String lfile, String rfile) throws Exception {
		if (!new File(lfile).exists())
			throw new FileNotFoundException("File " + lfile + " not found!");
		
		if (new File(rfile).exists() && new File(rfile).isDirectory() && !rfile.endsWith(File.separator))
			rfile = rfile + File.separator;
		
		String command = String.format("cp %s %s", lfile, rfile);
		exec(command);
	}
	
	public static void receiveFile(String lfile, String rfile) throws Exception {
		if (!new File(rfile).exists())
			throw new FileNotFoundException("File " + rfile + " not found!");
		
		if (new File(lfile).exists() && new File(lfile).isDirectory() && !lfile.endsWith(File.separator))
			lfile = lfile + File.separator;
		
		String command = String.format("cp %s %s", rfile, lfile);
		exec(command);
	}

}
