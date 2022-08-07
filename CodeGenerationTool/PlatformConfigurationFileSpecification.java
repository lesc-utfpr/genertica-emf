package CodeGenerationTool;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class represents a source code file inside a platform.
 * A file is compound of several fragments. 
 * The file, as well as each fragment, can be associated with an aspect name,
 * which indicates that the file (or fragment) is included in the platform
 * configuration only if the associated aspect appears in the UML model.
 * @author Marco Aurelio Wehrmeister
 *
 */
public class PlatformConfigurationFileSpecification {

	private PlatformMappingRules m_Owner;
	private String m_FileName;
	private String m_OutputDirectory;
	private boolean ScriptsLoaded;
	
	private ArrayList<String> m_Fragments = new ArrayList<String>();
	private ArrayList<String> m_AspectsOfFragments = new ArrayList<String>();
	private ArrayList<String> m_Aspects = new ArrayList<String>();
	
	/**
	 * Creates an instance of PlatformConfigurationFileSpecification class.
	 * This class manages the loading of <PlatformConfiguration> node inside
	 * the XML file of mapping rules used as input to GenERTiCA.
	 * @param platformMappingRules XML node that contains the information of the file.
	 * @param owner Object representing the platform configuration that owns the file.
	 */
	public PlatformConfigurationFileSpecification(Node platformMappingRules, 
			PlatformMappingRules owner) {
		m_Owner = owner;
		ScriptsLoaded = loadScripts(platformMappingRules);
	}
	
	/**
	 * Get the PlatformMappingRules objects that owns the file.
	 * @return The PlatformMappingRules object.
	 */
	public PlatformMappingRules getOwner() {
		return m_Owner;
	}

//	/**
//	 * Set the PlatformMappingRules objects that owns the file.
//	 * @param owner Owner of the file.
//	 */
//	public void setOwner(PlatformMappingRules owner) {
//		m_Owner = owner;
//	}

	/**
	 * Get the name of the file that will be used when writing the file in the
	 * platform configuration.
	 * @return The file name.
	 */
	public String getFileName() {
		return m_FileName;
	}

//	/**
//	 * Set the name of the file that will be used when writing the file in the
//	 * platform configuration.
//	 * @param fileName New name for the file.
//	 */
//	public void setFileName(String fileName) {
//		m_FileName = fileName;
//	}

	/**
	 * Get the name of the directory where the file will be written.
	 * @return The name of the output directory. 
	 */
	public String getOutputDirectory() {
		return m_OutputDirectory;
	}

//	/**
//	 * Set the name of the directory where the file will be written.
//	 * @param outputDirectory Name of the new output directory.
//	 */
//	public void setOutputDirectory(String outputDirectory) {
//		m_OutputDirectory = outputDirectory;
//	}
	
	public String getCompleteFileName() {
		String result = m_OutputDirectory;
		if (!result.equals(""))
			result += File.separator;
		result += m_FileName;
		return result;
	}
	
	// Loads the fragments stored into the XML file representing the mapping rules
	// for the file configuration.
	// It returns true if the mappingRules has at least one child node, and this
	// node was successfully loaded.
	private boolean loadScripts(Node mappingRules) {
		boolean result = false;
		if (mappingRules.getNodeName().compareTo(MappingRulesScripts.FILE) == 0) {
			// reading information from node's attributes
			Element fileNode = (Element)mappingRules;
			m_FileName = fileNode.getAttribute(MappingRulesScripts.NAME).trim();
			m_OutputDirectory = fileNode.getAttribute(MappingRulesScripts.OUTPUT_DIRECTORY).trim();
			String tmp = fileNode.getAttribute(MappingRulesScripts.ASPECTS).trim();
			if (!tmp.equals("")) {
				while (tmp.indexOf(",") >= 0) {
					String aspect = tmp.substring(0, tmp.indexOf(",")).trim();
					m_Aspects.add(aspect);
					tmp = tmp.substring(tmp.indexOf(",")+1).trim();
				}
				m_Aspects.add(tmp);
			}
			// indentation of the first fragment valid character in the first fragment
			int startPos = -1;
			// reading fragment's information
			NodeList childElements = mappingRules.getChildNodes();
			for(int i=0; i < childElements.getLength(); i++) {
				if (childElements.item(i).getNodeName().compareTo(MappingRulesScripts.FRAGMENT) == 0) {
					// reading aspects that affect the fragment
					tmp = ((Element)childElements.item(i)).getAttribute(MappingRulesScripts.ASPECTS).trim();
					// the aspects list of a fragment are described as aspect1, aspect2, aspect3, ...
					// The approach is not similar to aspects of files because 
					// it must have a 1-to-1 relation with m_Fragments array.
					m_AspectsOfFragments.add(tmp);
					
					// reading the fragment
					tmp = childElements.item(i).getFirstChild().getNodeValue();
					if (tmp.startsWith("\n"))
						tmp = tmp.substring(1);
					// correct indentation of the text
					// get the first 5 characters
					String s = tmp.trim();
					if (s.length() >= 5)
						s = s.substring(0, 5);
					if (startPos == -1)
						// getting the first position where appears a character
						startPos = tmp.indexOf(s);
					// getting the indentation string
					s = tmp.substring(0, startPos);
					// removing the indentation from the string
					//tmp = tmp.substring(startPos);
					// the "real" text fragment
					String fragment = "";
					String line = "";
					int newLine = tmp.indexOf("\n"); 
					while (newLine >= 0) {
						// getting the first line
						line = tmp.substring(0, newLine);
						if (line.startsWith(s))
							line = line.substring(startPos);
						// append the line with the "real" text fragment
						fragment += line + "\n";
						tmp = tmp.substring(newLine + 1);
						newLine = tmp.indexOf("\n");
					}
					line = tmp;
					if (line.startsWith(s))
						line = line.substring(startPos);
					// append the line with the "real" text fragment
					fragment += line + "\n";
					m_Fragments.add(fragment);
				}
			}
			result = true;
		}
		return result;
	}

	/**
	 * Get the list of text fragments of a file.
	 * @return The list of text fragments.
	 */
	public ArrayList<String> getFragments() {
		return m_Fragments;
	}

	/**
	 * Get the list of aspects associated with the fragment.
	 * @return The list of aspects associated with a fragment.
	 */
	public ArrayList<String> getAspectsOfFragments() {
		return m_AspectsOfFragments;
	}

	/**
	 * Get the list of aspects associated with the file.
	 * @return The list of aspects associated with the file.
	 */
	public ArrayList<String> getAspects() {
		return m_Aspects;
	}
	
	/**
	 * Indicate if the platform configuration scripts were successful loaded.
	 * @return true if the script where successfull loaded, false otherwise.
	 */
	public boolean scriptsSuccessfullLoaded() {
		return ScriptsLoaded;
	}
}
