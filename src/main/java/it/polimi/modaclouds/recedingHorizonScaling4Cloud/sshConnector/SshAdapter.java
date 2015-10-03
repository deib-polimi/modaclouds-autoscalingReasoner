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

import it.polimi.modaclouds.qos_models.schema.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizationInputWriter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.CreateFileCopy;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ModelManager;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//this class is used to create connection to AMPL server (wrapper)
public class SshAdapter {

	private static final Logger journal = LoggerFactory
			.getLogger(SshAdapter.class);
	
	private Shell shell;
	
	public SshAdapter() {
		shell = new Shell();
	}
	
	private void sendFile(String localFile, String remoteFile) throws Exception {
		shell.sendFile(localFile, remoteFile);
	}
	
	public void sendFileToWorkingDir(String file) throws Exception {
		sendFile(Paths.get(ConfigManager.getLocalTmp().toString(), file).toString(), ConfigManager.RUN_WORKING_DIRECTORY + "/" + file);
		fixFile(ConfigManager.RUN_WORKING_DIRECTORY, file);
	}
	
	public void exec(String command) throws Exception {
		shell.exec(command);
	}
	
	public int getNumberOfProviders() {
		return 1;
	}
	
	public int getNumberOfClasses() {
		return 1;
	}
	
	public void exec() throws Exception {
		switch (ConfigManager.MATH_SOLVER) {
		case AMPL:
			shell.exec(String.format("bash %s/%s", ConfigManager.RUN_WORKING_DIRECTORY, "run_Short.sh"));
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
		receiveFile(Paths.get(ConfigManager.getLocalTmp().toString(), file).toString(), ConfigManager.RUN_WORKING_DIRECTORY + "/" + file);
	}
	
	private void fixFile(String folder, String file) throws Exception {
		exec(String.format("cd %1$s && tr -d '\r' < %2$s > %2$s-bak && mv %2$s-bak %2$s",
						folder,
						file));
	}
	
	public void sendAllModelFiles() throws Exception {
		switch (ConfigManager.MATH_SOLVER) {
		case AMPL:
			sendAllModelFilesAMPL();
			break;
		case CMPL:
			journal.error("CMPL not supported at the moment.");
//			sendAllFilesCMPL();
			break;
		}
	}
	
	private void sendAllModelFilesAMPL() throws Exception {
		sendAllFilesInFolder("AMPL",
				ConfigManager.RUN_AMPL_EXECUTABLE,
				ConfigManager.RUN_AMPL_SOLVER,
				ConfigManager.RUN_WORKING_DIRECTORY,
				"log.out",
				getNumberOfProviders(),
				getNumberOfClasses());
	}
	
	@SuppressWarnings("unused")
	private void sendAllModelFilesCMPL() throws Exception {
		sendAllFilesInFolder("CMPL",
				ConfigManager.RUN_CMPL_EXECUTABLE,
				ConfigManager.RUN_CMPL_SOLVER,
				ConfigManager.RUN_WORKING_DIRECTORY,
				"log.out",
				getNumberOfProviders(),
				getNumberOfClasses());
	}
	
	private void sendAllFilesInFolder(String pathToFolder, Object... substitutions) throws Exception {
		Path folder = ConfigManager.getPathToFile(pathToFolder);
		
		List<File> files = getAllFiles(pathToFolder);
		
		List<File> folders = getAllUniqueFolders(files);
		for (File f : folders) {
			if (f.toString().equals(folder.toString()))
				continue;
			
			String relativePath = f.toString().substring(folder.toString().length() + 1);
			if (relativePath != null && relativePath.length() > 0)
				exec(String.format("mkdir -p %s/%s", ConfigManager.RUN_WORKING_DIRECTORY, relativePath));
		}
		
		for (File f : files) {
//			String relativePath = f.toString().substring(folder.toString().length() + 1, f.toString().indexOf(f.getName()));
			
			String relativePath = f.toString().substring(folder.toString().length());
			if (relativePath.length() == 0)
				continue;
			int i = relativePath.lastIndexOf(File.separator);
			if (i > 0)
				relativePath = relativePath.substring(1, i);
			else
				relativePath = "";
			
			CreateFileCopy.print(f,
					relativePath,
					substitutions);
			
			sendFileToWorkingDir(f.toString().substring(folder.toString().length() + 1));
		}
		
		journal.info("All files in the folder {} sent!", pathToFolder);
	}
	
	private static List<File> getAllFiles(String pathToFolder) throws Exception {
		URL url = ConfigManager.getURLToFile(pathToFolder);
		if (url == null)
			throw new RuntimeException(pathToFolder + " folder not found!");
		Path folder = Paths.get(url.getPath());
		
		List<File> files;
		if (url.toURI().getScheme().equals("jar")) {
			files = getAllFilesFromJar(pathToFolder);
		} else {
			 files = getAllFiles(folder.toFile());
		}
		return files;
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
				if (f.isDirectory()) {
					dirs.add(f);
				} else {
					if (f.getName().startsWith("."))
						continue;
					files.add(f);
				}
			}
		}
		
		return files;
	}
	
	private static List<File> getAllUniqueFolders(List<File> files) throws Exception {
		List<File> res = new ArrayList<>();
		
		for (File f : files) {
			File folder = f.getParentFile();
			if (!res.contains(folder))
				res.add(folder);
		}
		
		for (int i = 0; i < res.size(); ++i) {
			File folder = res.get(i);
			boolean goOn = true;
			for (int j = i+1; j < res.size() && goOn; ++j) {
				File otherFolder = res.get(j);
				if (otherFolder.toString().contains(folder.toString())) {
					res.remove(i);
					i--;
					goOn = false;
				}
			}
		}
		
		return res;
	}
	
	private static List<File> getAllFilesFromJar(String filePath) throws Exception {
		List<File> res = new ArrayList<>();
		
		URL url = ConfigManager.getURLToFile(filePath);
			
		if (!url.toURI().getScheme().equals("jar"))
			return res;
        
        String jarFile = url.getPath();
        jarFile = jarFile.substring("file:".length(), jarFile.lastIndexOf(".jar!") + 4);
        
        try (JarInputStream in = new JarInputStream(new FileInputStream(jarFile))) {
        	for (JarEntry entry = in.getNextJarEntry(); entry != null; entry = in.getNextJarEntry()) {
        		if (entry.isDirectory() || !entry.getName().startsWith(filePath))
        			continue;
        		res.add(ConfigManager.getPathToFile(entry.toString()).toFile());
        	}
        }
		
		return res;
	}
	
	public static void main(String[] args) throws Exception {
		ConfigManager.loadConfiguration();
				
		List<Container> containers = ModelManager.getModel().getContainer();
		for (Container toAdapt : containers) {
			journal.info("Writing the dynamic input files for the current timestep");
			OptimizationInputWriter siw= new OptimizationInputWriter();
			siw.writeDynamicInput(toAdapt);
		}
		
		journal.info("Writing the static input files for the current timestep");
		OptimizationInputWriter siw= new OptimizationInputWriter();
		siw.writeStaticInput(ModelManager.getModel());
		
		SshAdapter adapter = new SshAdapter();
		
		adapter.sendAllModelFiles();
		adapter.sendAllDataFiles("static");
		adapter.sendAllDataFiles("dynamic");
		
		adapter.exec();
		
		for (Container c : containers)
			adapter.receiveResults(c);
		
		journal.info("End!");
	}
	
	private void sendAllDataFiles(String type) throws Exception {
		List<File> files = getAllFiles(Paths.get(ConfigManager.getLocalTmp().toString(), "executions").toFile());
		
		List<File> folders = getAllUniqueFolders(files);
		for (File f : folders) {
			String fileName = f.toString();
			String relativePath = fileName.substring(fileName.lastIndexOf("IaaS_"));
			if (relativePath != null && relativePath.length() > 0)
				exec(String.format("mkdir -p %s/second/data/%s", ConfigManager.RUN_WORKING_DIRECTORY, relativePath));
		}
		
		for (File f : files) {
			String fileName = f.toString();
			if (!fileName.contains(type))
				continue;
			
			String relativePath = fileName.substring(fileName.lastIndexOf("IaaS_"));
			
			sendFile(fileName, ConfigManager.RUN_WORKING_DIRECTORY + "/second/data/" + relativePath);
			fixFile(ConfigManager.RUN_WORKING_DIRECTORY, "second/data/" + relativePath);
		}
	}
	
	public static void sendFixedFiles() throws Exception {
		SshAdapter adapter = new SshAdapter();
		adapter.sendAllModelFiles();
		adapter.sendAllDataFiles("static");
	}
	
	public void receiveResults(Container c) throws Exception {
		switch (ConfigManager.MATH_SOLVER) {
		case AMPL:
			receiveResultsAMPL(c);
			break;
		case CMPL:
			journal.error("CMPL not supported at the moment.");
//			receiveResultsCMPL(c);
			break;
		}
	}
	
	public void receiveResultsAMPL(Container c) throws Exception {
		for (int i = 1; i <= getNumberOfProviders(); ++i) {
			try {
				receiveFile(
						Paths.get(ConfigManager.getLocalTmp().toString(), "executions", "execution_" + c.getId(), "IaaS_" + i, "output.out").toString(),
						ConfigManager.RUN_WORKING_DIRECTORY + "/second/data/IaaS_" + i + "/rawGlobal/dati.txt");
			} catch (Exception e) {
				journal.error("Error while retrieving the results.", e);
			}
		}
	}

	// main execution function
	public static void executeOptimization(Container c) {
		SshAdapter adapter = new SshAdapter();
		
		try {
			journal.info("Sending all the optimization dynamic input files...");
			adapter.sendAllDataFiles("dynamic");

			// this block runs bash-script on AMPL server
			journal.info("Solving the optimization problem...");
			adapter.exec();

			// this block downloads logs and results of AMPL
			journal.info("Retrieving the optimization output files...");
			adapter.receiveResults(c);
		} catch (Exception e) {
			journal.error("Error while performing the optimization.", e);
		}

	}

}
