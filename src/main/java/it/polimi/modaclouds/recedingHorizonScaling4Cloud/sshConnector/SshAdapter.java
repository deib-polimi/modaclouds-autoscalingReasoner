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
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

//this class is used to create connection to AMPL server (wrapper)
public class SshAdapter {

	private static final Logger journal = Logger
			.getLogger(SshAdapter.class.getName());	
	
	// main execution function
	public static void executeOptimization(Container c) {
		
		
		// this block uploads files data.dat and AMPL.run on AMPL server
		ScpTo newScpTo = new ScpTo();
		
		
		
			
			
			File dir = new File("executions/execution_"+c.getId()+"/IaaS_1");
			File[] directoryListing = dir.listFiles();
				
			journal.log(Level.INFO,"Start sending all the optimization input files");

			if (directoryListing != null) {
				for (File child : directoryListing) {
					
					if(!child.getAbsolutePath().contains("output")){
						journal.log(Level.INFO,"Sending file: "+child.toString());
						newScpTo.sendfile(child.getAbsolutePath(), ConfigManager.OPTIMIZATION_INPUT_FOLDER);
						
						try {
						    Thread.sleep(10000);                 //1000 milliseconds is one second.
						} catch(InterruptedException e) {
						    Thread.currentThread().interrupt();
						}	
					}
				}
			} else {
				journal.log(Level.INFO,"Some error occurred: no files finded in the INPUT directory of the project file system");
			}
			
			// this block runs bash-script on AMPL server
			journal.log(Level.INFO,"Solving the optimization problem");
			ExecSSH newExecSSH = new ExecSSH();
			newExecSSH.mainExec();
	
			
			// this block downloads logs and results of AMPL
			ScpFrom newScpFrom = new ScpFrom();
			journal.log(Level.INFO,"Retrieving the optimization output file");
			journal.log(Level.INFO,ConfigManager.OPTIMIZATION_OUTPUT_FILE);
			newScpFrom.receivefile("executions/execution_"+c.getId()+"/IaaS_1/output.out",
					ConfigManager.OPTIMIZATION_OUTPUT_FILE);			
	

			
		}
	

}
