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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//this class is used to create connection to AMPL server (wrapper)
public class SshAdapter {

	private static final Logger journal = LoggerFactory
			.getLogger(SshAdapter.class);	
	
	// main execution function
	public static void executeOptimization(Container c) {
		
		
		// this block uploads files data.dat and AMPL.run on AMPL server
		ScpTo newScpTo = new ScpTo();
		
		
		
			
			
			File dir = new File("executions/execution_"+c.getId()+"/IaaS_1");
			File[] directoryListing = dir.listFiles();
				
			journal.info("Start sending all the optimization input files");

			if (directoryListing != null) {
				for (File child : directoryListing) {
					
					if(!child.getAbsolutePath().contains("output")){
						journal.info("Sending file: "+child.toString());
						newScpTo.sendfile(child.getAbsolutePath(), ConfigManager.OPTIMIZATION_INPUT_FOLDER);
						
						try {
						    Thread.sleep(10000);                 //1000 milliseconds is one second.
						} catch(InterruptedException e) {
						    Thread.currentThread().interrupt();
						}	
					}
				}
			} else {
				journal.info("Some error occurred: no files finded in the INPUT directory of the project file system");
			}
			
			// this block runs bash-script on AMPL server
			journal.info("Solving the optimization problem");
			ExecSSH newExecSSH = new ExecSSH();
			newExecSSH.mainExec();
	
			
			// this block downloads logs and results of AMPL
			ScpFrom newScpFrom = new ScpFrom();
			journal.info("Retrieving the optimization output file");
			journal.info(ConfigManager.OPTIMIZATION_OUTPUT_FILE);
			newScpFrom.receivefile("executions/execution_"+c.getId()+"/IaaS_1/output.out",
					ConfigManager.OPTIMIZATION_OUTPUT_FILE);			
	

			
		}
	

}
