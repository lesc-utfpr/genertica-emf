package genertica;

import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import genertica.util.BinarySearch;
import genertica.util.QuickSort;

import dercs.AO.Aspect;
import dercs.AO.AspectAdaptation;

public class ApplicationMappingRules {	
	// scripts related to Source Code Files
	private String FileName = "";
	private String PackageDeclaration = "";
	private String SourceReference = "";
	private String DeclarationFileOrganization = "";
	private String ImplementationFileOrganization = "";
	// scripts related to primary elements
	//      data types
	private String DataTypeArray = "";
	private String DataTypeBoolean = "";
	private String DataTypeByte = "";
	private String DataTypeChar = "";
	private String DataTypeClass = "";
	private String DataTypeDateTime = "";
	private String DataTypeEnumeration = "";
	private String DataTypeEnumerationDefinition = "";
	private String DataTypeInteger = "";
	private String DataTypeLong = "";
	private String DataTypeShort = "";
	private String DataTypeString = "";
	private String DataTypeVoid = "";
	private String DataTypeDouble = "";
	private String DataTypeFloat = "";
	//       data types default values
	private String DataTypeDefaultValueArray = "";
	private String DataTypeDefaultValueBoolean = "";
	private String DataTypeDefaultValueByte = "";
	private String DataTypeDefaultValueChar = "";
	private String DataTypeDefaultValueClass = "";
	private String DataTypeDefaultValueDateTime = "";
	private String DataTypeDefaultValueEnumeration = "";
	private String DataTypeDefaultValueInteger = "";
	private String DataTypeDefaultValueLong = "";
	private String DataTypeDefaultValueShort = "";
	private String DataTypeDefaultValueString = "";
	private String DataTypeDefaultValueVoid = "";
	private String DataTypeDefaultValueDouble = "";
	private String DataTypeDefaultValueFloat = "";
	//      visibilities
	private String VisibilityPrivate = "";
	private String VisibilityProtected = "";
	private String VisibilityPublic = "";
	//      parameter kinds
	private String ParameterIn = "";
	private String ParameterOut = "";
	private String ParameterInOut = "";
	// scripts related to Classes
	private String ClassDeclaration = "";
	private String ClassImplementation = "";
	private String AttributeDeclaration = "";
	private String MessageDeclaration = "";
	private String MessageImplementation = "";
	// scripts related to Behaviors
	private String VariableDeclaration = "";
	private String BehaviorBranch = "";
	private String BehaviorLoop = "";
	private String BehaviorAssignment = "";
	private String BehaviorObjectCreation = "";
	private String BehaviorObjectDestruction = "";
	private String BehaviorExpression = "";
	private String BehaviorReturn = "";
	private String BehaviorStateChange = "";
	private String BehaviorSendMessageLocalToSW = "";
	private String BehaviorSendMessageLocalToHW = "";
	private String BehaviorSendMessageRemoteToSW = "";
	private String BehaviorSendMessageRemoteToHW = "";
	private String BehaviorInsertArrayElement = "";
	private String BehaviorRemoveArrayElement = "";
	private String BehaviorGetArrayElement = "";
	private String BehaviorSetArrayElement = "";
	private String BehaviorArrayLength = "";
	// scripts related to Interruptions Handling
	private String InterruptionHandling = "";
	// scripts related to Aspects
	private String AspectDeclaration = "";
	private String BehavioralAdaptation = "";
	private String StructuralAdaptation = "";
    // options of code generation for the target platform 
	private SourceCodeGenerationOptions Options = new SourceCodeGenerationOptions();
	private boolean ScriptsLoaded = false;
	// aspects weaving helper lists, 1-to-1 lists
	private ArrayList<String> AspectAdaptations = new ArrayList<String>();
	private ArrayList<String> AdaptationsScripts = new ArrayList<String>();
	private ArrayList<Integer> AdaptationsOrder = new ArrayList<Integer>();
	private ArrayList<Boolean> AdaptationsMetaLevel = new ArrayList<Boolean>();
	private String m_MappingRulesFileDir;
	
	/**
	 * Creates an instance of ApplicationMappingRules class.
	 * @param platformMappingRules Node that represents the mapping rules for a given
	 * platform, e.g. node Platforms/RT-FemtoJava/Application/(Software or Hardware)
	 */
	public ApplicationMappingRules(String mappingRulesFileDir, Node platformMappingRules) {
		m_MappingRulesFileDir = mappingRulesFileDir;
		ScriptsLoaded = loadScripts(platformMappingRules); 
	}

	/**
	 * Returns the script that will generate the code for the declaration
	 * of an aspect. This script will be executed if the target language is an
	 * Aspect-Oriented Programming Language.
	 * @return the script
	 */
	public String getAspectDeclaration() {
		return AspectDeclaration;
	}

	/**
	 * Returns the script that will generate the code for the declaration
	 * of an attribute.
	 * @return the script
	 */
	public String getAttributeDeclaration() {
		return AttributeDeclaration;
	}

	/**
	 * Returns the script that will perform modifications on the previously
	 * generated source code fragment. The adaptation follows the semantics
	 * of the adaptation of the aspect, respecting DERAF high-level semantics.
	 * @return the script
	 */
	public String getBehavioralAdaptation() {
		return BehavioralAdaptation;
	}

	/**
	 * Returns the script that will generate the code for a assignment action
	 * in the target language.
	 * @return the script
	 */
	public String getBehaviorAssignment() {
		return BehaviorAssignment;
	}

	/**
	 * Returns the script that will generate the code for a branch
	 * in the target language.
	 * @return the script
	 */
	public String getBehaviorBranch() {
		return BehaviorBranch;
	}

	/**
	 * Returns the script that will generate the code for a expression
	 * in the target language.
	 * @return the script
	 */
	public String getBehaviorExpression() {
		return BehaviorExpression;
	}

	/**
	 * Returns the script that will generate the code for a loop mechanism
	 * in the target language.
	 * @return the script
	 */
	public String getBehaviorLoop() {
		return BehaviorLoop;
	}

	/**
	 * Returns the script that will generate the code for an object creation 
	 * action in the target language.
	 * @return the script
	 */
	public String getBehaviorObjectCreation() {
		return BehaviorObjectCreation;
	}

	/**
	 * Returns the script that will generate the code for an object destruction
	 * action in the target language.
	 * @return the script
	 */
	public String getBehaviorObjectDestruction() {
		return BehaviorObjectDestruction;
	}

	/**
	 * Returns the script that will generate the code for a return action
	 * from a method or function call, in the target language.
	 * @return the script
	 */
	public String getBehaviorReturn() {
		return BehaviorReturn;
	}

	/**
	 * Returns the script that will generate the code for a send message action
	 * in the target language. This script will be execute when a message is
	 * sent from a local object to a local object implemented as hardware.
	 * @return the script
	 */
	public String getBehaviorSendMessageLocalToHW() {
		return BehaviorSendMessageLocalToHW;
	}

	/**
	 * Returns the script that will generate the code for a send message action
	 * in the target language. This script will be execute when a message is
	 * sent from a local object to a local object implemented as software.
	 * @return the script
	 */
	public String getBehaviorSendMessageLocalToSW() {
		return BehaviorSendMessageLocalToSW;
	}

	/**
	 * Returns the script that will generate the code for a send message action
	 * in the target language. This script will be execute when a message is
	 * sent from a local object to a remote object implemented as hardware.
	 * @return the script
	 */
	public String getBehaviorSendMessageRemoteToHW() {
		return BehaviorSendMessageRemoteToHW;
	}

	/**
	 * Returns the script that will generate the code for a send message action
	 * in the target language. This script will be execute when a message is
	 * sent from a local object to a remote object implemented as software.
	 * @return the script
	 */
	public String getBehaviorSendMessageRemoteToSW() {
		return BehaviorSendMessageRemoteToSW;
	}

	/**
	 * Returns the script that will generate the code for a state change action
	 * in the target language. Generally, the class has an attribute representing
	 * its state, and a state change is simply the assignment of a new value to
	 * this attribute.
	 * @return the script
	 */
	public String getBehaviorStateChange() {
		return BehaviorStateChange;
	}

	/**
	 * Returns the script that will generate the code for the declaration of 
	 * classes in the target language.
	 * @return the script
	 */
	public String getClassDeclaration() {
		return ClassDeclaration;
	}

	/**
	 * Returns the script that will organize the sections of a source file (i.e.
	 * package, references, and class declaration, and class implementation)
	 * into a single file representing the declaration of classes.
	 * @return the script
	 */
	public String getDeclarationFileOrganization() {
		return DeclarationFileOrganization;
	}

	/**
	 * Returns the script used to generate the name for the source code file.
	 * @return the script
	 */
	public String getFileName() {
		return FileName;
	}

	/**
	 * Returns the script that will organize the sections of a source file (i.e.
	 * package, references, and class declaration, and class implementation)
	 * into a single file representing the implementation of classes.
	 * @return the script
	 */
	public String getImplementationFileOrganization() {
		return ImplementationFileOrganization;
	}

	/**
	 * Returns the script that will generate the code for the handling of 
	 * interruptions in the target language.
	 * @return the script
	 */
	public String getInterruptionHandling() {
		return InterruptionHandling;
	}

	/**
	 * Returns the script that will generate the code for the declaration of 
	 * messages in the target language.
	 * @return the script
	 */
	public String getMessageDeclaration() {
		return MessageDeclaration;
	}

	/**
	 * Returns the script that will generate the code for the declaration of 
	 * the package in the target language.
	 * @return the script
	 */
	public String getPackageDeclaration() {
		return PackageDeclaration;
	}

	/**
	 * Returns the script that will generate the code for the declaration of 
	 * references to other source code files in the target language.
	 * @return the script
	 */
	public String getSourceReference() {
		return SourceReference;
	}

	/**
	 * Returns the script that will perform modifications on the previously
	 * generated source code fragment. The adaptation follows the semantics
	 * of the adaptation of the aspect, respecting DERAF high-level semantics.
	 * @return the script
	 */
	public String getStructuralAdaptation() {
		return StructuralAdaptation;
	}
	
	public String trimLeft(String s) {
		int len = s.length();
		int i = 0;
		for(; (i < len) && (s.charAt(i) <= '\u0020'); i++);
		return s.substring(i);
	}
	
	public boolean isIdentifierChar(char c) {
		return ((c >= 'a') && (c <= 'z'))
		       || ((c >= 'A') && (c <= 'Z'))
		       || ((c >= '0') && (c <= '9'))
		       || (c == '_')
		       || (c == '.');
	}
	
	/**
	 * Helper method to return the text value within a node.
	 * @param n The node to be evaluated
	 * @return string containing the text value
	 */
	public String getStringFromNode(Node n) {
		if (n.getChildNodes().getLength() > 0) {
			String s = n.getFirstChild().getNodeValue().trim();
			if (s.indexOf('\n') >= 0) {
				// get the first line
				String s2 = trimLeft(s.substring(0, s.indexOf('\n')));
				// ignore comments inside scripts
				if (s2.startsWith("##"))
					s2 = "";
				else
					// identifies that a new line should me present
					if (s2.startsWith("\\n"))
						s2 = "\n" + s2.substring(2); // remove the "\n" string
				s = s.substring(s.indexOf('\n')+1);
				while (s.indexOf('\n') != -1) {
					String x = trimLeft(s.substring(0, s.indexOf('\n')));
					// ignore comments inside scripts
					if (x.startsWith("##"))
						x = "";
					else
						if (x.startsWith("\\n"))
							s2 += "\n" + x.substring(2); // remove the "\n" string
						else {
							if ((s2.length() > 0) && isIdentifierChar(s2.charAt(s2.length()-1))
								&& ((x.trim().length() > 0) && isIdentifierChar(x.charAt(0))))
								s2 += " ";
							s2 += x;
						}
					s = s.substring(s.indexOf('\n')+1);
				}
				String x = trimLeft(s);
				// ignore comments inside scripts
				if (x.startsWith("##"))
					x = "";
				else
					if (x.startsWith("\\n"))
						s2 += "\n" + x.substring(2); // remove the "\n" string
					else {
						if ((s2.length() > 0) && isIdentifierChar(s2.charAt(s2.length()-1))
								&& isIdentifierChar(x.charAt(0)))
							s2 += " ";
						s2 += x;
					}
				return s2;
			}
			else
				return s;
		}
		else
			return "";
	}
	
	// TODO incluir uma figura mostrando a estrutura do arquivo XML de mapeamento
	// Loads the scripts stored into the XML file representing the mapping rules.
	// It returns TRUE if the mappingRules has at least one child node.
	private boolean loadScripts(Node mappingRules) {
		// Source options
		NodeList childElements = mappingRules.getChildNodes();
		for(int i=0; i < childElements.getLength(); i++) {
			if (childElements.item(i).getNodeName().compareTo(MappingRulesScripts.SOURCE_OPTIONS) == 0) {
				NodeList tmpLst = childElements.item(i).getChildNodes();
				for(int j=0; j < tmpLst.getLength(); j++) {
					if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.FILE_NAME_CONVENTION) == 0) {
						FileName = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.PACKAGE) == 0) {
						PackageDeclaration = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.SOURCE_REFERENCE) == 0) {
						SourceReference = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.SOURCE_ORGANIZATION) == 0) {
						NodeList tmpLst2 = tmpLst.item(j).getChildNodes();
						for(int x=0; x < tmpLst2.getLength(); x++) {
							if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.DECLARATION_FILE) == 0) {
								DeclarationFileOrganization = getStringFromNode(tmpLst2.item(x));
								// read configuration options
								Element el = (Element)tmpLst2.item(x);
								Options.setDeclarationFileExt(el.getAttribute(MappingRulesScripts.FILE_EXTENSION));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.IMPLEMENTATION_FILE) == 0) {
								ImplementationFileOrganization = getStringFromNode(tmpLst2.item(x));
								// read configuration options
								Element el = (Element)tmpLst2.item(x);
								Options.setImplementionFileExt(el.getAttribute(MappingRulesScripts.FILE_EXTENSION));
							}
						}
					}
				}
				// read configuration of options to generate code for the target platform
				Element sourceOptions = (Element)childElements.item(i);
				Options.isAspectLanguage(sourceOptions.getAttribute(MappingRulesScripts.IS_ASPECT_LANGUAGE).compareTo("yes") == 0);
				Options.setClassesPerFile(Integer.valueOf(sourceOptions.getAttribute(MappingRulesScripts.CLASSES_PER_FILE)).intValue());
				Options.hasPackagesDeclaration(sourceOptions.getAttribute(MappingRulesScripts.HAS_PACKAGES_DECLARATION).compareTo("yes") == 0);
				Options.isPackagePerClass(sourceOptions.getAttribute(MappingRulesScripts.IS_PACKAGES_PER_CLASS).compareTo("yes") == 0);
				Options.hasClassesDeclaration(sourceOptions.getAttribute(MappingRulesScripts.HAS_CLASSES_DECLARATION).compareTo("yes") == 0);
				Options.setIdentation(Integer.valueOf(sourceOptions.getAttribute(MappingRulesScripts.IDENTATION)).intValue());
				Options.setBlockStart(sourceOptions.getAttribute(MappingRulesScripts.BLOCK_START));
				Options.setBlockEnd(sourceOptions.getAttribute(MappingRulesScripts.BLOCK_END));
				Options.setPackageName(sourceOptions.getAttribute(MappingRulesScripts.PACKAGE_NAME));
			}
			//**************************************************************************************************
			else if (childElements.item(i).getNodeName().compareTo(MappingRulesScripts.PRIMARY_ELEMENTS) == 0) {
				NodeList tmpLst = childElements.item(i).getChildNodes();
				for(int j=0; j < tmpLst.getLength(); j++) {
					if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.DATA_TYPES) == 0) {
						NodeList tmpLst2 = tmpLst.item(j).getChildNodes();
						for(int x=0; x < tmpLst2.getLength(); x++) {
							if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.ARRAY) == 0) {
								DataTypeArray = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.BOOLEAN) == 0) {
								DataTypeBoolean = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.BYTE) == 0) {
								DataTypeByte = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.CHAR) == 0) {
								DataTypeChar = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.CLASS) == 0) {
								DataTypeClass = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.DATETIME) == 0) {
								DataTypeDateTime = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.ENUMERATION) == 0) {
								DataTypeEnumeration = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.ENUMERATION_DEFINITION) == 0) {
								DataTypeEnumerationDefinition = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.INTEGER) == 0) {
								DataTypeInteger = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.LONG) == 0) {
								DataTypeLong = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.SHORT) == 0) {
								DataTypeShort = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.STRING) == 0) {
								DataTypeString = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.VOID) == 0) {
								DataTypeVoid = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.DOUBLE) == 0) {
								DataTypeDouble = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.FLOAT) == 0) {
								DataTypeFloat = getStringFromNode(tmpLst2.item(x));
							}
						}
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.DATA_TYPE_DEFAULT_VALUES) == 0) {
						NodeList tmpLst2 = tmpLst.item(j).getChildNodes();
						for(int x=0; x < tmpLst2.getLength(); x++) {
							if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.ARRAY) == 0) {
								DataTypeDefaultValueArray = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.BOOLEAN) == 0) {
								DataTypeDefaultValueBoolean = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.BYTE) == 0) {
								DataTypeDefaultValueByte = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.CHAR) == 0) {
								DataTypeDefaultValueChar = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.CLASS) == 0) {
								DataTypeDefaultValueClass = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.DATETIME) == 0) {
								DataTypeDefaultValueDateTime = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.ENUMERATION) == 0) {
								DataTypeDefaultValueEnumeration = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.INTEGER) == 0) {
								DataTypeDefaultValueInteger = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.LONG) == 0) {
								DataTypeDefaultValueLong = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.SHORT) == 0) {
								DataTypeDefaultValueShort = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.STRING) == 0) {
								DataTypeDefaultValueString = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.VOID) == 0) {
								DataTypeDefaultValueVoid = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.DOUBLE) == 0) {
								DataTypeDefaultValueDouble = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.FLOAT) == 0) {
								DataTypeDefaultValueFloat = getStringFromNode(tmpLst2.item(x));
							}
						}
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.VISIBILITIES) == 0) {
						NodeList tmpLst2 = tmpLst.item(j).getChildNodes();
						for(int x=0; x < tmpLst2.getLength(); x++) {
							if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.PRIVATE) == 0) {
								VisibilityPrivate = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.PROTECTED) == 0) {
								VisibilityProtected = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.PUBLIC) == 0) {
								VisibilityPublic = getStringFromNode(tmpLst2.item(x));
							}
						}
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.PARAMETERKINDS) == 0) {
						NodeList tmpLst2 = tmpLst.item(j).getChildNodes();
						for(int x=0; x < tmpLst2.getLength(); x++) {
							if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.IN) == 0) {
								ParameterIn = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.OUT) == 0) {
								ParameterOut = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.INOUT) == 0) {
								ParameterInOut = getStringFromNode(tmpLst2.item(x));
							}
						}
					}
				}
			}
			//**************************************************************************************************
			else if (childElements.item(i).getNodeName().compareTo(MappingRulesScripts.CLASSES) == 0) {
				NodeList tmpLst = childElements.item(i).getChildNodes();
				for(int j=0; j < tmpLst.getLength(); j++) {
					if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.DECLARATION) == 0) {
						ClassDeclaration = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.IMPLEMENTATION) == 0) {
						ClassImplementation = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.ATTRIBUTES) == 0) {
						AttributeDeclaration = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.MESSAGES) == 0) {
						NodeList tmpLst2 = tmpLst.item(j).getChildNodes();
						for(int x=0; x < tmpLst2.getLength(); x++) {
							if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.DECLARATION) == 0) {
								MessageDeclaration = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.IMPLEMENTATION) == 0) {
								MessageImplementation = getStringFromNode(tmpLst2.item(x));
							}
						}
					}
				}
			}
			//**************************************************************************************************
			else if (childElements.item(i).getNodeName().compareTo(MappingRulesScripts.BEHAVIOR) == 0) {
				NodeList tmpLst = childElements.item(i).getChildNodes();
				for(int j=0; j < tmpLst.getLength(); j++) {
					if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.VARIABLE_DECLARATION) == 0) {
						VariableDeclaration = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.BRANCH) == 0) {
						BehaviorBranch = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.LOOP) == 0) {
						BehaviorLoop = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.ASSIGNMENT) == 0) {
						BehaviorAssignment = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.OBJECT) == 0) {
						NodeList tmpLst2 = tmpLst.item(j).getChildNodes();
						for(int x=0; x < tmpLst2.getLength(); x++) {
							if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.CREATION) == 0) {
								BehaviorObjectCreation = getStringFromNode(tmpLst2.item(x));
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.DESTRUCTION) == 0) {
								BehaviorObjectDestruction = getStringFromNode(tmpLst2.item(x));
							}
						}
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.EXPRESSION) == 0) {
						BehaviorExpression = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.RETURN) == 0) {
						BehaviorReturn = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.STATE_CHANGE) == 0) {
						BehaviorStateChange = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.SEND_MESSAGE) == 0) {
						NodeList tmpLst2 = tmpLst.item(j).getChildNodes();
						for(int x=0; x < tmpLst2.getLength(); x++) {
							if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.TO_LOCAL) == 0) {
								NodeList tmpLst3 = tmpLst2.item(x).getChildNodes();
								for(int y=0; y < tmpLst3.getLength(); y++) {
									if (tmpLst3.item(y).getNodeName().compareTo(MappingRulesScripts.SOFTWARE) == 0) {
										BehaviorSendMessageLocalToSW = getStringFromNode(tmpLst3.item(y));
									}
									else if (tmpLst3.item(y).getNodeName().compareTo(MappingRulesScripts.HARDWARE) == 0) {
										BehaviorSendMessageLocalToHW = getStringFromNode(tmpLst3.item(y));
									}
								}
							}
							else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.TO_REMOTE) == 0) {
								NodeList tmpLst3 = tmpLst2.item(x).getChildNodes();
								for(int y=0; y < tmpLst3.getLength(); y++) {
									if (tmpLst3.item(y).getNodeName().compareTo(MappingRulesScripts.SOFTWARE) == 0) {
										BehaviorSendMessageRemoteToSW = getStringFromNode(tmpLst3.item(y));
									}
									else if (tmpLst3.item(y).getNodeName().compareTo(MappingRulesScripts.HARDWARE) == 0) {
										BehaviorSendMessageRemoteToHW = getStringFromNode(tmpLst3.item(y));
									}
								}
							}
						}
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.ARRAY_INSERT) == 0) {
						BehaviorInsertArrayElement = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.ARRAY_REMOVE) == 0) {
						BehaviorRemoveArrayElement = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.ARRAY_GET) == 0) {
						BehaviorGetArrayElement = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.ARRAY_SET) == 0) {
						BehaviorSetArrayElement = getStringFromNode(tmpLst.item(j));
					}
					else if (tmpLst.item(j).getNodeName().compareTo(MappingRulesScripts.ARRAY_LENGTH) == 0) {
						BehaviorArrayLength = getStringFromNode(tmpLst.item(j));
					}
				}
			}
			//**************************************************************************************************
			else if (childElements.item(i).getNodeName().compareTo(MappingRulesScripts.INTERRUPT_HANDLING) == 0) {
				
			}
			// **************************************************************************************************
			else if (childElements.item(i).getNodeName().compareTo(MappingRulesScripts.ASPECTS) == 0) {
				NodeList tmpLst = childElements.item(i).getChildNodes();
				for(int j=0; j < tmpLst.getLength(); j++) {
					NodeList tmpLst2 = tmpLst.item(j).getChildNodes();
					for(int x=0; x < tmpLst2.getLength(); x++) {
						if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.DECLARATION) == 0) {
							
						}
						else if (tmpLst2.item(x).getNodeName().compareTo(MappingRulesScripts.ADAPTATIONS) == 0) {
							NodeList tmpLst3 = tmpLst2.item(x).getChildNodes();
							for(int y=0; y < tmpLst3.getLength(); y++) {
								if (tmpLst3.item(y).getNodeName().compareTo(MappingRulesScripts.BEHAVIORAL) == 0) {
									Element aspectAdaptation = (Element)tmpLst3.item(y);
									AspectAdaptations.add(tmpLst.item(j).getNodeName() + "." + 
											              aspectAdaptation.getAttribute(MappingRulesScripts.NAME));
									AdaptationsScripts.add(getStringFromNode(aspectAdaptation));
									// The priority order of adaptations is obtained by:
									//      AspectOrderInXML*100 + OrderParameter
									// This means that an aspect can have up to 100 adaptations
									// but this is just a magical number... it culd be increased! :-)
									AdaptationsOrder.add(j*100+Integer.valueOf(aspectAdaptation.getAttribute(MappingRulesScripts.ORDER)));
									AdaptationsMetaLevel.add(Boolean.valueOf(aspectAdaptation.getAttribute(MappingRulesScripts.MODEL_LEVEL).compareTo("yes") == 0));
								}
								else if (tmpLst3.item(y).getNodeName().compareTo(MappingRulesScripts.STRUCTURAL) == 0) {
									Element aspectAdaptation = (Element)tmpLst3.item(y);
									AspectAdaptations.add(tmpLst.item(j).getNodeName() + "." + 
											              aspectAdaptation.getAttribute(MappingRulesScripts.NAME));
									AdaptationsScripts.add(getStringFromNode(aspectAdaptation));
									// The priority order of adaptations is obtained by:
									//      AspectOrderInXML*100 + OrderParameter
									// This means that an aspect can have up to 100 adaptations
									// but this is just a magical number... it culd be increased! :-)
									AdaptationsOrder.add(j*100+Integer.valueOf(aspectAdaptation.getAttribute(MappingRulesScripts.ORDER)));
									AdaptationsMetaLevel.add(Boolean.valueOf(aspectAdaptation.getAttribute(MappingRulesScripts.MODEL_LEVEL).compareTo("yes") == 0));
								}
							}
						}
					}
				}
				ArrayList a[] = {AspectAdaptations, AdaptationsScripts, AdaptationsOrder, AdaptationsMetaLevel};
				QuickSort.quickSortStringList(a);

				// debug only!
				for(Iterator<String> it=AspectAdaptations.iterator(); it.hasNext();) 
					System.out.println(it.next());
				System.out.print(" ");
			}
		}
		return childElements.getLength() > 0;
	}
	
	/**
	 * Get the script correspondent to an aspect adaptation.
	 * @param adaptation Adaptation whose script is wanted.
	 * @return The script of the aspect adaptation. If the adaptation could not be found, 
	 * a blank string is returned. 
	 */
	public String getAspectAdaptationScript(AspectAdaptation adaptation) {
		int i = BinarySearch.binarySearchString(AspectAdaptations, 
				adaptation.getOwner().getName() + "." + adaptation.getName());
		if (i > 0)
			return AdaptationsScripts.get(i);
		else
			return "";
	}
	
	/**
	 * Get the order in which the aspect adaptation should be applied.
	 * @param adaptation Adaptation whose script is wanted.
	 * @return The order of the aspect adaptation. If the adaptation could not 
	 * be found, -1 is returned. 
	 */
	public Integer getAspectAdaptationOrder(AspectAdaptation adaptation) {
		int i = BinarySearch.binarySearchString(AspectAdaptations, 
				adaptation.getOwner().getName() + "." + adaptation.getName());
		if (i > 0)
			return AdaptationsOrder.get(i);
		else
			return Integer.valueOf(-1);
	}
	
	/**
	 * Get the order in which the aspect adaptation should be applied.
	 * @param adaptation Adaptation whose script is wanted.
	 * @return The order of the aspect adaptation. If the adaptation could not 
	 * be found, -1 is returned. 
	 */
	public boolean isAdaptationAtModelLevel(AspectAdaptation adaptation) {
		int i = BinarySearch.binarySearchString(AspectAdaptations, 
				adaptation.getOwner().getName() + "." + adaptation.getName());
		if (i > 0)
			return AdaptationsMetaLevel.get(i);
		else
			return false;
	}

	/**
	 * Get the options that will drive the code generation process for the target
	 * implmentation language/platform.
	 * @return the options
	 */
	public SourceCodeGenerationOptions getOptions() {
		return Options;
	}

	/**
	 * Returns the script that will generate the code for the implementation
	 * of the behavior of a message in the target language.
	 * @return the script
	 */
	public String getMessageImplementation() {
		return MessageImplementation;
	}

	/**
	 * Returns the script that will generate the code representing the array 
	 * datatype in the target language
	 * @return the script
	 */
	public String getDataTypeArray() {
		return DataTypeArray;
	}

	/**
	 * Returns the script that will generate the code representing the boolean 
	 * datatype in the target language.
	 * @return the script
	 */
	public String getDataTypeBoolean() {
		return DataTypeBoolean;
	}

	/**
	 * Returns the script that will generate the code representing the byte 
	 * datatype in the target language.
	 * @return the script
	 */
	public String getDataTypeByte() {
		return DataTypeByte;
	}

	/**
	 * Returns the script that will generate the code representing the char 
	 * datatype in the target language.
	 * @return the script
	 */
	public String getDataTypeChar() {
		return DataTypeChar;
	}

	/**
	 * Returns the script that will generate the code representing the class 
	 * datatype in the target language.
	 * @return the script
	 */
	public String getDataTypeClass() {
		return DataTypeClass;
	}

	/**
	 * Returns the script that will generate the code representing the date/time 
	 * datatype in the target language.
	 * @return the script
	 */
	public String getDataTypeDateTime() {
		return DataTypeDateTime;
	}

	/**
	 * Returns the script that will generate the code representing the enumeration 
	 * datatype in the target language
	 * @return the script
	 */
	public String getDataTypeEnumeration() {
		return DataTypeEnumeration;
	}
	
	/**
	 * Returns the script that will generate the code representing the definition of the 
	 * enumeration datatype in the target language
	 * @return the script
	 */
	public String getDataTypeEnumerationDefinition() {
		return DataTypeEnumerationDefinition;
	}

	/**
	 * Returns the script that will generate the code representing the integer 
	 * datatype in the target language
	 * @return the script
	 */
	public String getDataTypeInteger() {
		return DataTypeInteger;
	}

	/**
	 * Returns the script that will generate the code representing the long 
	 * datatype in the target language
	 * @return the script
	 */
	public String getDataTypeLong() {
		return DataTypeLong;
	}

	/**
	 * Returns the script that will generate the code representing the short 
	 * datatype in the target language
	 * @return the script
	 */
	public String getDataTypeShort() {
		return DataTypeShort;
	}

	/**
	 * Returns the script that will generate the code representing the string 
	 * datatype in the target language
	 * @return the script
	 */
	public String getDataTypeString() {
		return DataTypeString;
	}

	/**
	 * Returns the script that will generate the code representing the void 
	 * datatype in the target language
	 * @return the script
	 */
	public String getDataTypeVoid() {
		return DataTypeVoid;
	}

	/**
	 * Returns the script that will generate the code representing the input
	 * parameter of a method in the target language.
	 * @return the script
	 */
	public String getParameterIn() {
		return ParameterIn;
	}

	/**
	 * Returns the script that will generate the code representing the input/output
	 * parameter of a method in the target language.
	 * @return the script
	 */
	public String getParameterInOut() {
		return ParameterInOut;
	}

	/**
	 * Returns the script that will generate the code representing the output
	 * parameter of a method in the target language.
	 * @return the script
	 */
	public String getParameterOut() {
		return ParameterOut;
	}

	/**
	 * Returns the script that will generate the code representing the private
	 * visibility in the target language.
	 * @return the script
	 */
	public String getVisibilityPrivate() {
		return VisibilityPrivate;
	}

	/**
	 * Returns the script that will generate the code representing the protected
	 * visibility in the target language.
	 * @return the script
	 */
	public String getVisibilityProtected() {
		return VisibilityProtected;
	}

	/**
	 * Returns the script that will generate the code representing the public
	 * visibility in the target language.
	 * @return the script
	 */
	public String getVisibilityPublic() {
		return VisibilityPublic;
	}

	/**
	 * Returns the script that will generate the code representing the double
	 * data type in the target language.
	 * @return the script
	 */
	public String getDataTypeDouble() {
		return DataTypeDouble;
	}

	/**
	 * Returns the script that will generate the code representing the float
	 * data type in the target language.
	 * @return the script
	 */
	public String getDataTypeFloat() {
		return DataTypeFloat;
	}

	/**
	 * Returns the script that will generate the code for the implementation of 
	 * classes in the target language.
	 * @return the script
	 */
	public String getClassImplementation() {
		return ClassImplementation;
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
	 * Returns the script that will generate the code for the declaration of a
	 * variable.
	 * @return the script
	 */
	public String getVariableDeclaration() {
		return VariableDeclaration;
	}
	
	/**
	 * Returns the script that will generate the code representing the array 
	 * datatype in the target language
	 * @return the script
	 */
	public String getDataTypeDefaultValueArray() {
		return DataTypeDefaultValueArray;
	}

	/**
	 * Returns the script that will generate the code representing the boolean 
	 * data type in the target language.
	 * @return the script
	 */
	public String getDataTypeDefaultValueBoolean() {
		return DataTypeDefaultValueBoolean;
	}

	/**
	 * Returns the script that will generate the code representing the byte 
	 * data type in the target language.
	 * @return the script
	 */
	public String getDataTypeDefaultValueByte() {
		return DataTypeDefaultValueByte;
	}

	/**
	 * Returns the script that will generate the code representing the char 
	 * data type in the target language.
	 * @return the script
	 */
	public String getDataTypeDefaultValueChar() {
		return DataTypeDefaultValueChar;
	}

	/**
	 * Returns the script that will generate the code representing the class 
	 * data type in the target language.
	 * @return the script
	 */
	public String getDataTypeDefaultValueClass() {
		return DataTypeDefaultValueClass;
	}

	/**
	 * Returns the script that will generate the code representing the date/time 
	 * data type in the target language.
	 * @return the script
	 */
	public String getDataTypeDefaultValueDateTime() {
		return DataTypeDefaultValueDateTime;
	}

	/**
	 * Returns the script that will generate the code representing the enumeration 
	 * data type in the target language
	 * @return the script
	 */
	public String getDataTypeDefaultValueEnumeration() {
		return DataTypeDefaultValueEnumeration;
	}

	/**
	 * Returns the script that will generate the code representing the integer 
	 * data type in the target language
	 * @return the script
	 */
	public String getDataTypeDefaultValueInteger() {
		return DataTypeDefaultValueInteger;
	}

	/**
	 * Returns the script that will generate the code representing the long 
	 * data type in the target language
	 * @return the script
	 */
	public String getDataTypeDefaultValueLong() {
		return DataTypeDefaultValueLong;
	}

	/**
	 * Returns the script that will generate the code representing the short 
	 * data type in the target language
	 * @return the script
	 */
	public String getDataTypeDefaultValueShort() {
		return DataTypeDefaultValueShort;
	}

	/**
	 * Returns the script that will generate the code representing the string 
	 * data type in the target language
	 * @return the script
	 */
	public String getDataTypeDefaultValueString() {
		return DataTypeDefaultValueString;
	}

	/**
	 * Returns the script that will generate the code representing the void 
	 * data type in the target language
	 * @return the script
	 */
	public String getDataTypeDefaultValueVoid() {
		return DataTypeDefaultValueVoid;
	}
	
	/**
	 * Returns the script that will generate the code representing the double
	 * data type in the target language.
	 * @return the script
	 */
	public String getDataTypeDefaultValueDouble() {
		return DataTypeDefaultValueDouble;
	}

	/**
	 * Returns the script that will generate the code representing the float
	 * data type in the target language.
	 * @return the script
	 */
	public String getDataTypeDefaultValueFloat() {
		return DataTypeDefaultValueFloat;
	}

	/**
	 * Returns the script that will generate the code representing the array 
	 * element insertion action
	 * @return the script
	 */
	public String getBehaviorInsertArrayElement() {
		return BehaviorInsertArrayElement;
	}

	/**
	 * Returns the script that will generate the code representing the array 
	 * element remove action
	 * @return the script
	 */
	public String getBehaviorRemoveArrayElement() {
		return BehaviorRemoveArrayElement;
	}

	/**
	 * Returns the script that will generate the code representing the array 
	 * element get action
	 * @return the script
	 */
	public String getBehaviorGetArrayElement() {
		return BehaviorGetArrayElement;
	}

	/**
	 * Returns the script that will generate the code representing the array 
	 * element set action
	 * @return the script
	 */
	public String getBehaviorSetArrayElement() {
		return BehaviorSetArrayElement;
	}

	/**
	 * Returns the script that will generate the code representing the array 
	 * length retrieval action
	 * @return the script
	 */
	public String getBehaviorArrayLength() {
		return BehaviorArrayLength;
	}
}
