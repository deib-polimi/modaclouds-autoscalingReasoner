package staticInputProcessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import util.ConfigDictionary;
import util.ConfigManager;

import com.hp.hpl.jena.rdf.model.Container;
import com.hp.hpl.jena.sparql.algebra.Op;

import PCMManaging.PCMManager;
import model.ApplicationTier;
import model.ModelManager;
import model.OptimizationExecution;

public class StaticInputWriter {

	private StaticInputBuilder inputBuilder;

	public StaticInputWriter() {	
		this.inputBuilder = new StaticInputBuilder();
	}

	public void writeStaticInput(List<OptimizationExecution> executions) {
			
		double speedNorm=Double.parseDouble(ConfigManager.getConfig(ConfigDictionary.speedNorm));
		this.inputBuilder.build(Double.parseDouble(ConfigManager.getConfig(ConfigDictionary.speedNorm)),
				ConfigManager.getConfig(ConfigDictionary.pathToLineResult),
				executions );

		
		int i=1;
		for (OptimizationExecution ex : executions) {

			this.writeFile("C.dat", ex.toString(), "let C:=\n"
					+ ex.getCapacity() + "\n;");
			this.writeFile("W.dat", ex.toString(), "let W:=\n"
					+ ex.getW() + "\n;");
			this.writeFile("delta.dat", ex.toString(),
					"let delta:=\n" + ex.getOnDemandCost() + "\n;");
			this.writeFile("rho.dat", ex.toString(), "let rho:=\n"
					+ ex.getReservedCost() + "\n;");
			
			/*
			for(ApplicationTier c: ex.getContainers()){
				this.writeFile("Rcross.dat", "Execution "+i, Float.toString(c.getRcross()));
			}
			*/			
			i++;
		}
	}

	// problema: sovrascrive i file conteneti parametri vettoriale: modificare per eseguire solo un "append"
	private void writeFile(String fileName, String execution,
			String fileContent) {
		FileWriter fw = null;
		File file = null;
		
		
			try {
				file = new File("executions/execution_"+execution+"/IaaS_1/" + fileName);
				file.createNewFile();
				fw = new FileWriter(file);
				fw.append(fileContent);
				fw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

	}
	
}
