package staticInputProcessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import PCMManaging.PCMManager;
import util.GenericXMLHelper;

public class LineResultManager extends GenericXMLHelper {

	public LineResultManager(String FilePath){
		super(FilePath);
	}
	
	public List<Element> getContainerSEEF(String containerName, Map<String,String> allocation){
		
		List<Element> containerList = getElements("SEFF");
		List<Element> toReturn=new ArrayList<Element>();
		
		List< String> containerAllocation=new ArrayList<>();
		
		for(String key: allocation.keySet())
			if(allocation.get(key).equals(containerName))
				containerAllocation.add(key);
		
		for(Element e:containerList){
			for(String component: containerAllocation )
				if(e.getAttribute("name").contains(component))
					toReturn.add(e);
		}

		
				
		return toReturn;
	}
}
