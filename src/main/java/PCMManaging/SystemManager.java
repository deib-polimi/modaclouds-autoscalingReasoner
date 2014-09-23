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

public class SystemManager extends GenericXMLHelper {


	public  SystemManager(String Filepath) {
		super(Filepath);
	}



	public String getNameFromId(String containerId) {

		Element container;
		List<Element> containerList = getElements(
				"assemblyContexts__ComposedStructure");
		String toReturn=null;

		for (int i = 0; i < containerList.size(); i++) {
			container = containerList.get(i);
			if(container.getAttribute("id").equals(containerId))
				toReturn=container.getAttribute("entityName");
		}
		
		return toReturn;

	}
	
	public Map<String,String> getAssemblyMapping(){
		
		Element assembly;
		
		Map<String,String> toReturn= new HashMap<>();
		
		List<Element> assemblyList = getElements(
				"assemblyContexts__ComposedStructure");
		
		for (int i = 0; i < assemblyList.size(); i++) {
			assembly = assemblyList.get(i);

			toReturn.put(assembly.getAttribute("id"), assembly.getAttribute("entityName").split(" ")[1].replaceAll("<|>", ""));
		}
		
		return toReturn;

		
		
	}

}
