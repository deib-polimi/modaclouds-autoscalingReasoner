/**
 * Copyright ${year} deib-polimi
 * Contact: deib-polimi <giovannipaolo.gibilisco@polimi.it>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package it.polimi.modaclouds.recedingHorizonScaling4Cloud.sshConnector;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizationInputWriter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.CreateFileCopy;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ModelManager;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//this class is used to create connection to AMPL server (wrapper)
public class SshAdapter {

	private static final Logger journal = LoggerFactory
			.getLogger(SshAdapter.class);
	
	private Shell shell;
	
	public SshAdapter() {
		ConfigManager.initFolders();
		
		shell = new Shell();
	}
	
	private void sendFile(String localFile, String remoteFile) throws Exception {
		shell.sendFile(localFile, remoteFile);
	}
	
	public void sendFileToWorkingDir(String file) throws Exception {
		sendFile(Paths.get(ConfigManager.LOCAL_TEMPORARY_FOLDER.toString(), file).toString(), ConfigManager.RUN_WORKING_DIRECTORY + "/" + file);
		fixFile(ConfigManager.RUN_WORKING_DIRECTORY, file);
	}
	
	public void exec(String command) throws Exception {
		shell.exec(command);
	}
	
	public void exec() throws Exception {
		switch (ConfigManager.MATH_SOLVER) {
		case AMPL:
			shell.exec(String.format("bash %s/%s %d", ConfigManager.RUN_WORKING_DIRECTORY, "run_Short.sh", 1));
			break;
		case CMPL:
			journal.error("CMPL not supported at the moment.");
//			shell.exec();
			break;
		}
	}
	
	private void receiveFile(String localFile, String remoteFile) throws Exception {
		shell.receiveFile(localFile, remoteFile);
	}
	
	public void receiveFileFromWorkingDir(String file) throws Exception {
		receiveFile(Paths.get(ConfigManager.LOCAL_TEMPORARY_FOLDER.toString(), file).toString(), ConfigManager.RUN_WORKING_DIRECTORY + "/" + file);
	}
	
	private void fixFile(String folder, String file) throws Exception {
		exec(String.format("cd %1$s && tr -d '\r' < %2$s > %2$s-bak && mv %2$s-bak %2$s",
						folder,
						file));
	}
	
	public void sendAllFiles() throws Exception {
		switch (ConfigManager.MATH_SOLVER) {
		case AMPL:
			sendAllFilesAMPL();
			break;
		case CMPL:
			journal.error("CMPL not supported at the moment.");
//			sendAllFilesCMPL();
			break;
		}
	}
	
	private void sendAllFilesAMPL() throws Exception {
		sendAllFilesInFolder("AMPL",
				ConfigManager.RUN_AMPL_EXECUTABLE,
				ConfigManager.RUN_AMPL_SOLVER,
				ConfigManager.RUN_WORKING_DIRECTORY,
				"log.out");
	}
	
	@SuppressWarnings("unused")
	private void sendAllFilesCMPL() throws Exception {
		sendAllFilesInFolder("CMPL",
				ConfigManager.RUN_CMPL_EXECUTABLE,
				ConfigManager.RUN_CMPL_SOLVER);
	}
	
	private void sendAllFilesInFolder(String pathToFolder, Object... substitutions) throws Exception {
		Path folder = ConfigManager.getPathToFile(pathToFolder);
		if (folder == null)
			throw new RuntimeException(pathToFolder + " folder not found!");
		
		exec(String.format("mkdir -p %s", ConfigManager.RUN_WORKING_DIRECTORY));
		
		List<File> files = getAllFiles(folder.toFile());
		for (File f : files) {
			journal.trace("Considering {}...", f);
			String relativePath = f.toString().substring(folder.toString().length() + 1, f.toString().indexOf(f.getName()));
			CreateFileCopy.print(f,
					relativePath,
					substitutions);
			
			if (relativePath != null && relativePath.length() > 0)
				exec(String.format("mkdir -p %s", ConfigManager.RUN_WORKING_DIRECTORY + "/" + relativePath));
			
			sendFileToWorkingDir(f.toString().substring(folder.toString().length() + 1));
		}
		
		journal.info("All files in the folder {} sent!", pathToFolder);
	}
	
	private static List<File> getAllFiles(File dir) throws Exception {
		if (dir == null || !dir.exists() || !dir.isDirectory())
			throw new RuntimeException("The file argument isn't valid.");
		
		List<File> files = new ArrayList<>();
		
		List<File> dirs = new ArrayList<>();
		dirs.add(dir);
		for (int i = 0; i < dirs.size(); ++i) {
			File d = dirs.get(i);
			for (File f : d.listFiles()) {
				if (f.isDirectory())
					dirs.add(f);
				else
					files.add(f);
			}
		}
		
		return files;
	}
	
	public static void main(String[] args) throws Exception {
		ConfigManager.loadConfiguration();
				
		List<Container> containers = ModelManager.getModel().getContainer();
		for (Container toAdapt : containers) {
			journal.info("Writing the dynamic input files for the current timestep");
			OptimizationInputWriter siw= new OptimizationInputWriter();
			siw.writeDynamicInput(toAdapt);
		}
		
//		SshAdapter adapter = new SshAdapter();
//		adapter.sendAllFiles();
		
		List<File> files = getAllFiles(Paths.get(ConfigManager.LOCAL_TEMPORARY_FOLDER.toString(), "executions").toFile());
		for (File f : files) {
			String fileName = f.toString();
			journal.info("{}, second/data/{}", fileName, fileName.substring(fileName.lastIndexOf("IaaS_")));
		}
	}

	// main execution function
	public static void executeOptimization(Container c) {
		SshAdapter adapter = new SshAdapter();
		
		try {
			journal.info("Sending all the files for solving the problem...");
			adapter.sendAllFiles();
			
//			File dir = new File("executions/execution_" + c.getId() + "/IaaS_1");
//			File[] directoryListing = dir.listFiles();
//
//			journal.info("Start sending all the optimization input files");
//
//			if (directoryListing != null) {
//				for (File child : directoryListing) {
//
//					if (!child.getAbsolutePath().contains("output")) {
//						journal.info("Sending file: " + child.toString());
//						adapter.sendFile(child.getAbsolutePath(),
//								ConfigManager.OPTIMIZATION_INPUT_FOLDER);
//
//						try {
//							Thread.sleep(10000); // 1000 milliseconds is one
//													// second.
//						} catch (InterruptedException e) {
//							Thread.currentThread().interrupt();
//						}
//					}
//				}
//			} else {
//				journal.info("Some error occurred: no files finded in the INPUT directory of the project file system");
//			}

			// this block runs bash-script on AMPL server
			journal.info("Solving the optimization problem...");
			adapter.exec();

			// this block downloads logs and results of AMPL
			journal.info("Retrieving the optimization output file {}...", ConfigManager.OPTIMIZATION_OUTPUT_FILE);
			adapter.receiveFile("executions/execution_" + c.getId()
					+ "/IaaS_1/output.out",
					ConfigManager.OPTIMIZATION_OUTPUT_FILE);
		} catch (Exception e) {
			journal.error("Error while performing the optimization.", e);
		}

	}

}
