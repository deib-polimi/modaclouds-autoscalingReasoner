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


public class ResEnvManager extends GenericXMLHelper {
	

		public ResEnvManager(String Filepath) {
			super(Filepath);
		}


		public String getNameFromId(String containerId) {

			Element container;
			List<Element> containerList = getElements(
					"resourceContainer_ResourceEnvironment");
			String toReturn=null;

			for (int i = 0; i < containerList.size(); i++) {
				container = containerList.get(i);
				if(container.getAttribute("id").equals(containerId))
					toReturn=container.getAttribute("entityName");
			}
			
			return toReturn;

		}
		
		public Map<String,String> getContainerCredentials(){
			
			Element container;
			
			Map<String,String> toReturn= new HashMap<>();
			
			List<Element> containerList = getElements(
					"resourceContainer_ResourceEnvironment");
			
			for (int i = 0; i < containerList.size(); i++) {
				container = containerList.get(i);

				toReturn.put(container.getAttribute("id"), container.getAttribute("entityName"));
			}
			
			return toReturn;

			
			
		}


}
