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
package ssh;

import java.io.File;
import java.util.List;

import model.ModelManager;
import model.OptimizerExecution;
import util.ConfigDictionary;
import util.ConfigManager;

//this class is used to create connection to AMPL server (wrapper)
public class SshConnector {

	// main execution function
	public static void run(List<OptimizerExecution> executions) {
		// this block uploads files data.dat and AMPL.run on AMPL server
		ScpTo newScpTo = new ScpTo();
		
		
		
		for(OptimizerExecution ex: executions){
			
			File dir = new File("executions/execution_"+ex.toString()+"/");
			File[] directoryListing = dir.listFiles();
				
				
			if (directoryListing != null) {
				for (File child : directoryListing) {
					newScpTo.sendfile(child.getAbsolutePath(), ConfigManager.getConfig(ConfigDictionary.RUN_WORKING_DIRECTORY)
							+ ConfigManager.getConfig(ConfigDictionary.INPUT_FOLDER));
				}
			} else {
				System.out.println("Some error occurred: no files finded in the INPUT directory of the project file system");
			}
	

	
			// this block runs bash-script on AMPL server
			ExecSSH newExecSSH = new ExecSSH();
			newExecSSH.mainExec();
	
			// this block downloads logs and results of AMPL
			/*
			ScpFrom newScpFrom = new ScpFrom();
			
			newScpFrom.receivefile(Configuration.RUN_LOG,
			Configuration.RUN_WORKING_DIRECTORY + "/" + Configuration.RUN_LOG);
			newScpFrom.receivefile(Configuration.RUN_RES,
			Configuration.RUN_WORKING_DIRECTORY + "/" + Configuration.RUN_RES);
			*/
		}
	}

}
