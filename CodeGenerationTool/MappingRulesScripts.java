package CodeGenerationTool;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dercs.util.OutputLog;

/**
 * This class represents all VTL script, which are stored into XML files. These
 * scripts describe how an element of DERCS should be transformed to the target
 * source code. There are scripts for the following items:
 * <OL>
 * <LI><b>Source code related scripts:</b></LI>
 * <OL>
 *    <LI><i><b>File name:</b></i> genetare the file name for the generated source code;</LI>
 *    <LI><i><b>Package declaration:</b></i> generate the package declaration;</LI>
 *    <LI><i><b>Source reference:</b></i> genetare declarations to other source code 
 *           files, whose elements are refered into the generated source code;</LI>
 *    <LI><i><b>Organization of the declaration file:</b></i> organizes the different parts of 
 *           a declaration file, such as C header (*.h) file;</LI>
 *    <LI><i><b>Organization of the implementation file:</b></i> organizes the different parts
 *           of a implmentation file, such as C++ (*.cc) or Java (*.java) files;</LI>
 * </OL>
 * <LI><b>Primary elements related scripts:</b></LI>
 * <OL>
 *   <LI><i><b>Data Types:</b></i> generate the string representing data types:</LI>
 *   <OL>
 *      <LI>Array</LI>
 *      <LI>Boolean</LI>
 *      <LI>Byte</LI>
 *      <LI>Char</LI>
 *      <LI>Class</LI>
 *      <LI>DateTime</LI>
 *      <LI>Enumeration</LI>
 *      <LI>Integer</LI>
 *      <LI>Long</LI>
 *      <LI>Short</LI>
 *      <LI>String</LI>
 *      <LI>Void</LI>
 *      <LI>Double</LI>
 *      <LI>Float</LI>
 *   </OL>
 *   <LI><i><b>Visibilities:</b></i> generate the string representing the following
 *   visibility within a class:</LI>
 *   <OL>
 *     <LI>Private</LI>
 *     <LI>Protected</LI>
 *     <LI>Public</LI>
 *   </OL>
 *   <LI><i><b>Parameter kinds:</b></i> generate the string representing the following
 *   parameter kinds which can be used to specify parameters of methods:</LI>
 *   <OL>
 *     <LI>In</LI>
 *     <LI>Out</LI>
 *     <LI>In/Out</LI>
 *   </OL>
 * </OL>
 * <LI><b>Class related scripts:</b></LI>
 * <OL>
 *    <LI><i><b>Class declaration:</b></i> genetare the declaration of a class;</LI>
 *    <LI><i><b>Attribute declaration:</b></i> genetare the declaration of a attribute;</LI>
 *    <LI><i><b>Message declaration:</b></i> genetare the declaration of a message (i.e.
 *           a method);</LI>
 * </OL>
 * <LI><b>Behavior related scripts:</b></LI>
 * <OL>
 *    <LI><i><b>Branch:</b></i> genetare the source code equivalent to a branch 
 *           (e.g. if(){});</LI>
 *    <LI><i><b>Loop:</b></i> genetare the source code equivalent to a loop 
 *           (e.g. for(;;), while(){});</LI>
 *    <LI><i><b>Assignment:</b></i> genetare the source code to an assignment 
 *           (e.g. i = 0);</LI>
 *    <LI><i><b>Object creation:</b></i> genetare the source code to an object 
 *           instantiation;</LI>
 *    <LI><i><b>Object destruction:</b></i> genetare the source code to an object 
 *           destruction;</LI>
 *    <LI><i><b>Expression:</b></i> genetare the source code equivalent to an 
 *           expression into the target languange;</LI>
 *    <LI><i><b>Return:</b></i> genetare the source code to a return from a method
 *           or a function;</LI>
 *    <LI><i><b>State change:</b></i> genetare the source code equivalente to a 
 *           state change, normally the state is represented as an attribute of the class
 *           and a state change is simply the assignment of a new value to this attribute;
 *    <LI><i><b>Message:</b></i> genetare the source code for a message send.
 *           The message can be sent from local to local objects (SW or HW objects) or 
 *           from local to remote objects (also SW or HW objects);</LI>
 * </OL>
 * <LI><b>Interruption handling related scripts:</b></LI>
 * <OL>
 *    <LI><i><b>Interrupt handling:</b></i> genetare the source code to link a
 *           interruption with its handling behavior;</LI>
 * </OL>
 * <LI><b>Aspect related scripts:</b></LI>
 * <OL>
 *    <LI><i><b>Aspect declaration:</b></i> genetare the declaration of an aspect.
 *           It is used only if the tager language is an Aspect-Oriented Language, such as
 *           AspectJ;</LI>
 *    <LI><i><b>Behavioral adaptation:</b></i> modifies the generated code fragment
 *           by modifying it according the aspect adaptation semantics;</LI>
 *    <LI><i><b>Structural adaptation:</b></i> modifies the generated code fragment
 *           by modifying it according the aspect adaptation semantics.</LI>
 * </OL>
 * </OL>
 * @author Marco Aurelio Wehrmeister
 */
public class MappingRulesScripts {
	// XML nodes identification
	// General
	public final static String PLATFORMS = "Platforms";
	public final static String APPLICATION = "Application";
	public final static String PLATFORM_CONFIGURATION = "PlatformConfiguration";
	public final static String SOFTWARE = "Software";
	public final static String HARDWARE = "Hardware";
	public final static String DECLARATION = "Declaration";
	public final static String IMPLEMENTATION = "Implementation";
	// Source Options
	public final static String SOURCE_OPTIONS = "SourceOptions";
	public final static String FILE_NAME_CONVENTION = "FileNameConvention";
	public final static String PACKAGE = "Package";
	public final static String SOURCE_REFERENCE = "SourceReference";
	public final static String SOURCE_ORGANIZATION = "SourceOrganization";
	public final static String DECLARATION_FILE = "DeclarationFile";
	public final static String IMPLEMENTATION_FILE = "ImplementationFile";
	// Primary Elements
	public final static String PRIMARY_ELEMENTS = "PrimaryElementsMapping";
	// Data types
	public final static String DATA_TYPES = "DataTypes";
	public final static String DATA_TYPE_DEFAULT_VALUES = "DataTypeDefaultValues";
	public final static String ARRAY = "Array";
	public final static String BOOLEAN = "Boolean";
	public final static String BYTE = "Byte";
	public final static String CHAR = "Char";
	public final static String CLASS = "Class";
	public final static String DATETIME = "DateTime";
	public final static String ENUMERATION = "Enumeration";
	public final static String ENUMERATION_DEFINITION = "EnumerationDefinition";
	public final static String INTEGER = "Integer";
	public final static String LONG = "Long";
	public final static String SHORT = "Short";
	public final static String STRING = "String";
	public final static String VOID = "Void";
	public final static String DOUBLE = "Double";
	public final static String FLOAT = "Float";
	// Visibility
	public final static String VISIBILITIES = "Visibilities";
	public final static String PRIVATE = "Private";
	public final static String PROTECTED = "Protected";
	public final static String PUBLIC = "Public";
	// Parameter Kind
	public final static String PARAMETERKINDS = "ParameterKinds";
	public final static String IN = "In";
	public final static String OUT = "Out";
	public final static String INOUT = "InOut";
	// Classes
	public final static String CLASSES = "Classes";
	public final static String ATTRIBUTES = "Attributes";
	public final static String MESSAGES = "Messages";
	// Behavior
	public final static String BEHAVIOR = "Behavior";
	public final static String VARIABLE_DECLARATION = "VariableDeclaration";
	public final static String BRANCH = "Branch";
	public final static String LOOP = "Loop";
	public final static String ASSIGNMENT = "Assignment";
	public final static String OBJECT = "Object";
	public final static String CREATION = "Creation";
	public final static String DESTRUCTION = "Destruction";
	public final static String EXPRESSION = "Expression";
	public final static String RETURN = "Return";
	public final static String STATE_CHANGE = "StateChange";
	public final static String SEND_MESSAGE = "SendMessage";
	public final static String TO_LOCAL = "ToLocal";
	public final static String TO_REMOTE = "ToRemote";
	public final static String ARRAY_INSERT = "InsertArrayElement";
	public final static String ARRAY_REMOVE = "RemoveArrayElement";
	public final static String ARRAY_GET = "GetArrayElement";
	public final static String ARRAY_SET = "SetArrayElement";
	public final static String ARRAY_LENGTH = "ArrayLength";
	// Interrupt handling
	public final static String INTERRUPT_HANDLING = "InterruptHandling";
	// Aspects
	public final static String ASPECTS = "Aspects";
	public final static String ADAPTATIONS = "Adaptations";
	public final static String STRUCTURAL = "Structural";
	public final static String BEHAVIORAL = "Behavioral";
	public final static String NAME = "Name";
	public final static String ORDER = "Order";
	public final static String MODEL_LEVEL = "ModelLevel";
	// Platform configuration
	public final static String FILES = "Files";
	public final static String FILE = "File";
	public final static String FRAGMENT = "Fragment";
	public final static String XI_INCLUDE = "xi:include";
	
	// XML attributes identification
	// Source code attributes
	public final static String IS_ASPECT_LANGUAGE = "isAspectLanguage";
	public final static String CLASSES_PER_FILE = "ClassesPerFile"; 
	public final static String HAS_CLASSES_DECLARATION = "hasClassesDeclaration";
	public final static String HAS_PACKAGES_DECLARATION = "hasPackagesDeclaration";
	public final static String IS_PACKAGES_PER_CLASS = "isPackagePerClass";
	public final static String IDENTATION = "Identation";
	public final static String FILE_EXTENSION = "FileExtension";
	public final static String PACKAGE_NAME = "PackageName";
	// Behavior attributes
	public final static String BLOCK_START = "BlockStart";
	public final static String BLOCK_END = "BlockEnd";
	// Platform configuration
	public final static String OUTPUT_DIRECTORY = "OutputDirectory";
	
	
	// Name of the target platform to which the code will be generated
	private String Name = "";
	protected OutputLog m_Log;
	
	// scripts of parts of the XML mapping rules files
	private ApplicationMappingRules ApplicationSW;
	private ApplicationMappingRules ApplicationHW;
	private PlatformMappingRules PlatformSW;
	private PlatformMappingRules PlatformHW;
	private boolean ScriptsLoaded = false;
	private String m_MappingRulesFileDir;
	
	public MappingRulesScripts(String mappingRulesFileDir, String name, Node XMLPlatformMappingRules, OutputLog log) {
		Name = name;
		m_Log = log;
		m_MappingRulesFileDir = mappingRulesFileDir;
		if (!m_MappingRulesFileDir.endsWith(File.separator))
			m_MappingRulesFileDir += File.separator;
		
		NodeList childElements = XMLPlatformMappingRules.getChildNodes();
		for(int i=0; i < childElements.getLength(); i++) {
			if (childElements.item(i).getNodeName().compareTo(APPLICATION) == 0) {
				NodeList tmpLst = childElements.item(i).getChildNodes();
				for(int j=0; j < tmpLst.getLength(); j++) {
					if (tmpLst.item(j).getNodeName().compareTo(SOFTWARE) == 0) {
						ApplicationSW = new ApplicationMappingRules(m_MappingRulesFileDir, tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(HARDWARE) == 0) {
						ApplicationHW = new ApplicationMappingRules(m_MappingRulesFileDir, tmpLst.item(j));
					}
				}
			}
			else if (childElements.item(i).getNodeName().compareTo(PLATFORM_CONFIGURATION) == 0) {
				NodeList tmpLst = childElements.item(i).getChildNodes();
				for(int j=0; j < tmpLst.getLength(); j++) {
					if (tmpLst.item(j).getNodeName().compareTo(SOFTWARE) == 0) {
						PlatformSW = new PlatformMappingRules(m_MappingRulesFileDir, tmpLst.item(j), m_Log);
					}
					else if (tmpLst.item(j).getNodeName().compareTo(HARDWARE) == 0) {
						PlatformHW = new PlatformMappingRules(m_MappingRulesFileDir, tmpLst.item(j), m_Log);
					}
				}
			}
			else {
				// TODO CG-100 Colocar o tratamento de erro
			}
		}
		try {
			ScriptsLoaded = (ApplicationSW.scriptsSuccessfullLoaded() ||
			                 ApplicationHW.scriptsSuccessfullLoaded());
		} catch (Exception e) {
			ScriptsLoaded = false;
		}
	}

	/**
	 * Get the scripts related the hardware part of the application.
	 * @return ApplicationMappingRules object containing the hardware related
	 * scripts.
	 */
	public ApplicationMappingRules getApplicationHW() {
		return ApplicationHW;
	}

	/**
	 * Get the scripts related the software part of the application.
	 * @return ApplicationMappingRules object containing the software related
	 * scripts.
	 */
	public ApplicationMappingRules getApplicationSW() {
		return ApplicationSW;
	}

	/**
	 * Get the scripts related the hardware part of the chose platform.
	 * @return PlatformMappingRules object containing the hardware related
	 * scripts.
	 */
	public PlatformMappingRules getPlatformHW() {
		return PlatformHW;
	}

	/**
	 * Get the scripts related the software part of the chose platform.
	 * @return PlatformMappingRules object containing the software related
	 * scripts.
	 */
	public PlatformMappingRules getPlatformSW() {
		return PlatformSW;
	}
	
	/**
	 * Indicate if the XML file could be parsed and if the scripts were loaded
	 * into the attributes of this class.
	 * @return TRUE if the script were sucessfull loaded, FALSE otherwise.
	 */
	public boolean scriptsSuccessfullLoaded() {
		return ScriptsLoaded;
	}

	/**
	 * Get the platform name.
	 * @return The name.
	 */
	public String getName() {
		return Name;
	}
}
