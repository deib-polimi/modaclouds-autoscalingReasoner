package PCMManaging;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.GenericXMLHelper;

public class AllocationManager extends GenericXMLHelper {
	
	
	public  AllocationManager(String Filepath) {
		super(Filepath);
	}


	public String getNameFromId(String containerId) {

		Element container;
		List<Element> containerList = getElements(
				"allocationContexts_Allocation");
		String toReturn=null;

		for (int i = 0; i < containerList.size(); i++) {
			container = containerList.get(i);
			if(container.getAttribute("id").equals(containerId))
				toReturn=container.getAttribute("entityName");
		}
		
		return toReturn;

	}
	
	public Map<String,String> getComponentAllocation(){
		
		Element allocation;
		
		Map<String,String> toReturn= new HashMap<>();
		
		List<Element> allocationList = getElements(
				"allocationContexts_Allocation");
		
		for (int i = 0; i < allocationList.size(); i++) {
			allocation = allocationList.get(i);
			String tempComponent=null;
			String tempContainer=null;
			
			
			NodeList list = allocation.getChildNodes();
			
			for(int j=0;j< list.getLength(); j++){
				

				
				Element temp=null;
				
				if (list.item(j).getNodeType() == Node.ELEMENT_NODE)
					temp= (Element) list.item(j);
				


				
				
				if(temp!=null){
					if(temp.getNodeName().equals("resourceContainer_AllocationContext")){
						tempContainer= temp.getAttribute("href");
						//System.out.println(temp.getNodeName()+" "+temp.getAttribute("href"));
					}
					else if(temp.getNodeName().equals("assemblyContext_AllocationContext")){
						tempComponent= temp.getAttribute("href");
						//System.out.println(temp.getNodeName()+" "+temp.getAttribute("href"));

					}
					
					if(tempComponent!=null && tempContainer!= null)
						toReturn.put(tempComponent.split("#")[1], tempContainer.split("#")[1]);
				}
			}

		}
		
		return toReturn;

		
		
	}

}
