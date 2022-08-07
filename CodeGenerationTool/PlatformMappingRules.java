package CodeGenerationTool;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dercs.util.OutputLog;

public class PlatformMappingRules {

	// directory where the files will be generated
	private String m_OutputDirectory;
	// files that configure the platform
	private ArrayList<PlatformConfigurationFileSpecification> m_Files = new ArrayList<PlatformConfigurationFileSpecification>();
	// indicate that the scripts that configure the platform were successful loaded
	private boolean ScriptsLoaded;
	protected OutputLog m_Log;
	private String m_MappingRulesFileDir;
	
	public PlatformMappingRules(String mappingRulesFileDir, Node platformMappingRules, OutputLog log) {
		m_Log = log;
		m_MappingRulesFileDir = mappingRulesFileDir;
		ScriptsLoaded = loadScripts(platformMappingRules);
	}
	
	private boolean loadScripts(Node mappingRules) {
		boolean result = false;
		// reading platform information
		NodeList childElements = mappingRules.getChildNodes();
		for(int i=0; i < childElements.getLength(); i++) {
			if (childElements.item(i).getNodeName().compareTo(MappingRulesScripts.SOURCE_OPTIONS) == 0) {
				m_OutputDirectory = ((Element)childElements.item(i)).getAttribute(MappingRulesScripts.OUTPUT_DIRECTORY).trim();
				if (!m_OutputDirectory.endsWith(File.separator))
					m_OutputDirectory += File.separator;
			}
			else if (childElements.item(i).getNodeName().compareTo(MappingRulesScripts.FILES) == 0) {
				NodeList files = childElements.item(i).getChildNodes();
				for(int j=0; j < files.getLength(); j++) {
					if (files.item(j).getNodeName().compareTo(MappingRulesScripts.FILE) == 0)
						m_Files.add(new PlatformConfigurationFileSpecification(files.item(j), this));
					else if (files.item(j).getNodeName().compareTo(MappingRulesScripts.XI_INCLUDE) == 0) {
						// the JVM does not support <xi:include>
						String fileToInclude = ((Element)files.item(j)).getAttribute("href").trim();
						if (fileToInclude.startsWith("."+File.separator))
							fileToInclude = m_MappingRulesFileDir + fileToInclude.substring(2);
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						try {
							// open the new file
							DocumentBuilder builder = factory.newDocumentBuilder();
							Document newFile = builder.parse(fileToInclude);
							// parse the file
							NodeList childNodes = newFile.getChildNodes();
							for(int x = 0; x < childNodes.getLength(); x++) {
								//if (childElements.item(x).getNodeName().compareTo(MappingRulesScripts.FILE) == 0)
								if (childNodes.item(x).getNodeType() == Node.ELEMENT_NODE)
									m_Files.add(new PlatformConfigurationFileSpecification(childNodes.item(x), this));
							}
						} 
						catch (Exception e) {
							m_Log.println(CodeGenerator.CODE_GENERATION_ERROR_1012 + " [" + fileToInclude + "]", false);
							m_Log.println("\t"+e.getMessage(), false);
						}
					}
				}
			}
		}
		result = m_Files.size() > 0;
		for(Iterator<PlatformConfigurationFileSpecification> itpcf = m_Files.iterator(); 
								result && itpcf.hasNext(); 
								result &= itpcf.next().scriptsSuccessfullLoaded());
		return result;
	}

	/**
	 * Get the directory in which the generated configuration should be written.
	 * @return The name of the output directory.
	 */
	public String getOutputDirectory() {
		return m_OutputDirectory;
	}
	
	/**
	 * Get the iterator for the files list.
	 * @return The iterator.
	 */
	public Iterator<PlatformConfigurationFileSpecification> getFilesIterator() {
		return m_Files.iterator();
	}
	
	/**
	 * Indicate if the platform configuration scripts were successful loaded.
	 * @return TRUE if the script where successfull loaded, FALSE otherwise.
	 */
	public boolean scriptsSuccessfullLoaded() {
		return ScriptsLoaded;
	}
}
