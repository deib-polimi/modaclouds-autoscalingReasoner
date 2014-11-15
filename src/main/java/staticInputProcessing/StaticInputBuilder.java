package staticInputProcessing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import model.ApplicationTier;
import model.ModelManager;
import model.OptimizerExecution;
import model.Resource;

import org.eclipse.emf.common.util.EList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import util.GenericXMLHelper;
import util.MILPInputDatParsed;
import util.MILPInputDatParser;
import PCMManaging.PCMManager;
import PCMManaging.ResEnvManager;
import RHS4CloudExceptions.PalladioHelpingException;
import RHS4CloudExceptions.StaticInputBuildingException;
import SPACE4CLOUD_DBaccess.DataHandler;
import it.polimi.modaclouds.qos_models.util.XMLHelper;
import it.polimi.modaclouds.qos_models.schema.ResourceContainer;
import it.polimi.modaclouds.qos_models.schema.ResourceModelExtension;
import it.polimi.modaclouds.resourcemodel.cloud.CloudResource;
import it.polimi.modaclouds.resourcemodel.cloud.Cost;
import it.polimi.modaclouds.resourcemodel.cloud.Link;
import it.polimi.modaclouds.resourcemodel.cloud.VirtualHWResource;
import it.polimi.modaclouds.resourcemodel.cloud.VirtualHWResourceType;

public class StaticInputBuilder {


	
	private DataHandler dbHandler;
	

	public StaticInputBuilder() {
			
		try {
			this.dbHandler = new DataHandler();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	
	}
	
	public void build(double speedNorm, String pathToLineResult, List <OptimizerExecution> executions){
	
		int i=1;
		
		for (OptimizerExecution ex : executions) {
			
			Resource resource= ex.getResource();
			
			CloudResource temp=this.dbHandler.getCloudResource(resource.getProvider(), resource.getName(), resource.getSize());
			
			LineResultManager lineHelper= new LineResultManager(pathToLineResult);
			
			try {
				ex.setCapacity(this.getResourceCapacity(temp, speedNorm));
				ex.setOnDemandCost(this.getResourceOnDemandCost(temp, ex.getResource().getRegion()));
				ex.setReservedCost(this.getResourceReservedCost(temp, ex.getResource().getRegion()));
				
				for(ApplicationTier container: ex.getContainers()){
					
					container.setRcross(this.getContainerRcross(container.getContainerName(), lineHelper));
					
				}
				
				ex.setW(1);

				
			} catch (StaticInputBuildingException e) {
				e.printStackTrace();
			}
			
			
			System.out.println("execution: "+i);
			System.out.println("capacity: "+ex.getCapacity());
			System.out.println("on demand cost: "+ex.getOnDemandCost());
			System.out.println("reserved cost: "+ex.getReservedCost());
			System.out.println("W: "+ex.getW());
			for(ApplicationTier c:ex.getContainers())
				System.out.println("container: "+ c.getContainerName()+" response time threshold: "+ c.getRcross());
			
			i++;
		}
		
	}
	
	private float getContainerRcross(String containerName, LineResultManager lineHelper) throws StaticInputBuildingException{
		
		//caso calcolo del response time direttamente da quello globale al container riportato nell'output di line (caso più immediato)
		
		List<Element> stations= lineHelper.getElements("station");
		float toReturn=-1;

		int i=0;
		
		while(toReturn==-1){
			
			Element station= stations.get(i);
			if(station.getAttribute("name").contains(containerName))
				toReturn=Float.parseFloat(station.getAttribute("responseTime"));
			
			i++;
		}
		
		if(toReturn!=-1)
			return toReturn;
		else
			throw new StaticInputBuildingException("Error calculating the container respone time threshold: response time of a container cannot be lesser than 0 (-1). "
					+ "element station in LINE result mathcing with the container:"+containerName+"not found");
			
	}

	private double getResourceCapacity(CloudResource resource, double speedNorm) throws StaticInputBuildingException{
		
		double toReturn=0;
		
		for (VirtualHWResource virtualResource : resource.getComposedOf()) {
			
			//il parametro CPU in realtà andrebbe letto da soluzione amazon ovvero è il serviceType che già memorizziamo e che può variare ad esempio storage ecc
			//per ora teniamo così...
			if (virtualResource.getType().equals(VirtualHWResourceType.CPU)) {

				toReturn= virtualResource
						.getNumberOfReplicas()
						* virtualResource.getProcessingRate()
						/ speedNorm;
			}
		}
		if(toReturn==0)
			throw new StaticInputBuildingException("Error calculating execution capacity: capacity cannot be equal to 0. Check the resource retrived from the CloudDB");	
		else
			return toReturn;
		
	}
	
	private double getResourceOnDemandCost(CloudResource resource, String region){
		
		EList<Cost> cl = resource.getHasCost();
		double toReturn=0;
		
		for (Cost c : cl) {
			
			// di tutte le opzioni per l'ondemand della macchina
			// individuata ipotizzo di selezionare la più economica
			// nel caso in cui l'opzione on demand fosse unica non c è
			// bisogno di questo frammento nel ciclo e l'individuazione
			// del costo può essere effettuata in una singola
			// chiamata all esterno
			//this.getResource().getRegion()
			if (c.getRegion().equals(region) && c.getDescription().contains("On-Demand")) {
									
					if (toReturn == 0)
						toReturn=c.getValue();
					else if (c.getValue() < toReturn)
						toReturn=c.getValue();			
			}

		}
		
		return toReturn;
		
	}
	
	private double getResourceReservedCost(CloudResource resource, String region){
		EList<Cost> cl = resource.getHasCost();
		double toReturn=0;

		for (Cost c : cl) {
			if (c.getRegion().equals(region) && c.getDescription().contains("Reserved 3year")) {
						
				// di tutte le opzioni per il reserved della macchina
				// individuata ipotizzo di selezionare la più economica tra
				// quelle a 3 anni
					if (toReturn == 0)
						toReturn=c.getValue();
					
					else if (c.getValue() < toReturn)
						toReturn=c.getValue();
				
			}

		}
		
		return toReturn;
	}

	
}
