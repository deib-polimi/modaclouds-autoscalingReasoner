package staticInputProcessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Container;
import com.hp.hpl.jena.sparql.algebra.Op;

import PCMManaging.PCMManager;
import model.ApplicationTier;
import model.ModelManager;
import model.OptimizerExecution;

public class StaticInputWriter {

	private StaticInputBuilder inputBuilder;

	public StaticInputWriter() {	
		this.inputBuilder = new StaticInputBuilder();
	}

	public void writeStaticInput(String pathToLineResult, String sn, List<OptimizerExecution> executions) {
			
		double speedNorm=Double.parseDouble(sn);
		this.inputBuilder.build(speedNorm, pathToLineResult, executions );

		
		int i=1;
		for (OptimizerExecution ex : executions) {

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
				file = new File("executions/execution_"+execution+"/" + fileName);
				file.createNewFile();
				fw = new FileWriter(file);
				fw.append(fileContent);
				fw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

	}
	
}
