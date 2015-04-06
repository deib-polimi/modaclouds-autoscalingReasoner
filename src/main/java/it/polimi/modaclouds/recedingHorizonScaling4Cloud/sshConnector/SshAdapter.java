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
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Containers;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigDictionary;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

import java.io.File;
import java.util.List;

//this class is used to create connection to AMPL server (wrapper)
public class SshAdapter {

	// main execution function
	public static void executeOptimization(Container c) {
		
		
		// this block uploads files data.dat and AMPL.run on AMPL server
		ScpTo newScpTo = new ScpTo();
		
		
		
			
			
			File dir = new File("executions/execution_"+c.getId()+"/IaaS_1");
			File[] directoryListing = dir.listFiles();
				
				
			if (directoryListing != null) {
				for (File child : directoryListing) {
					
					if(!child.getAbsolutePath().contains("output")){
						System.out.println("sending file: "+child.toString());
						newScpTo.sendfile(child.getAbsolutePath(), ConfigManager.getConfig(ConfigDictionary.OPTIMIZATION_INPUT_FOLDER));
						
						try {
						    Thread.sleep(10000);                 //1000 milliseconds is one second.
						} catch(InterruptedException e) {
						    Thread.currentThread().interrupt();
						}	
					}
				}
			} else {
				System.out.println("Some error occurred: no files finded in the INPUT directory of the project file system");
			}
			
			// this block runs bash-script on AMPL server
			ExecSSH newExecSSH = new ExecSSH();
			newExecSSH.mainExec();
	
			// this block downloads logs and results of AMPL
			
			ScpFrom newScpFrom = new ScpFrom();
			
			newScpFrom.receivefile("executions/execution_"+c.getId()+"/IaaS_1/output.out",
					ConfigManager.getConfig(ConfigDictionary.OPTIMIZATION_OUTPUT_FILE));			
	

			
		}
	

}
