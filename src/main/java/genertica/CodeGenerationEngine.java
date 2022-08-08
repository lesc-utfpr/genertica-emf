package genertica;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import genertica.Exceptions.AttributeCGException;
import genertica.Exceptions.MessageDeclarationCGException;
import genertica.Exceptions.MessageImplementationCGException;
import genertica.util.BinarySearch;
import genertica.util.QuickSort;

import javax.xml.parsers.*;

import dercs.DERCSFactory;
import genertica.util.DERCSHelper;
import dercs.Model;
import dercs.AO.Aspect;
import dercs.AO.AspectAdaptation;
import dercs.AO.JoinPoint;
import dercs.AO.Pointcut;
import dercs.behavior.BehavioralElement;
import dercs.behavior.actions.AssignmentAction;
import dercs.structure.Attribute;
import dercs.structure.BaseElement;
import dercs.structure.Class;
import dercs.structure.Method;
import dercs.structure.Visibility;

/**
 * <h1>GenEReTiCA - Generation of Embedded Real-Time Code base on Aspects</h1>
 * <h1>GenERTiCA - Generation of Embedded Real-Time Code base on Aspects</h1>
 * This class represents the engine for code generation, which uses the Velocity 
 * Template Language (VTL) and the Velocity Template Engine (http://velocity.apache.org/) 
 * to describe how to generate source code for a DERCS model used as input.
 * Mapping rules are stored into a XML file which is organized as follows:
 * <br><img src=../XML_Struct.jpg> 
 * @author Marco Aurelio Wehrmeister
 *
 */
public class CodeGenerationEngine {
	
	// Variable names within the context of Velocity Template Engine
	public static final String DERCS_MODEL = "DERCSModel";
	public static final String CLASS = "Class";
	public static final String REFERENCED_CLASS = "ReferencedClass";
	public static final String PACKAGE = "Package";
	public static final String ATTRIBUTE = "Attribute";
	public static final String DATA_TYPE = "DataType";
	public static final String DATA_TYPE_STR = "DataTypeStr";
	public static final String VISIBILITY_ENUM_STR = "VisibilityEnum";
	public static final String VISIBILITY_STR = "VisibilityStr";
	public static final String MESSAGE = "Message";
	public static final String CONSTRUCTOR = "Constructor";
	public static final String DESTRUCTOR = "Destructor";
	public static final String RETURN_TYPE_STR = "ReturnTypeStr";
	public static final String BEHAVIOR = "Behavior";
	public static final String VARIABLE = "Variable";
	public static final String ACTION = "Action";
	public static final String BRANCH = "Branch";
	public static final String LOOP = "Loop";
	public static final String OBJECT = "Object";
	public static final String INDEX_VARIABLE_NAME = "IndexVariableName";
	public static final String SOURCE_CODE = "SourceCode";
	public static final String OPTIONS = "Options";
	public static final String CODE_GENERATOR = "CodeGenerator";
	public static final String IDENTATION_LEVEL = "IdentationLevel";
	public static final String GENERATED_CODE_FRAGMENT = "GeneratedCodeFragment";
	public static final String DERCS_FACTORY = "DERCSFactory";
	public static final String DERCS_HELPER = "DERCSHelper";
	public static final String CROSSCUTTING = "Crosscutting";
	
	// Represent the DERCS model extracted from the UML model
	private Model DERCSModel;
	// Indicate the class which are been analysed
	private Class ClassBeenAnalysed;
	// Indicate a referenced class from the references list
	private Class ReferencedClass;
	// Logging system
	protected Logger LOGGER;
	
	// Indicate the source code files for the class which are been analysed.
	// It can be a declaration (e.g. *.h) or an implementation file (e.g. *.c, *.java)
	private SourceCode SourceCodeBeenGenerated;
	// indicate the option for the code generation
	private SourceCodeGenerationOptions Options;
	// represent the context for the Velocity Template Engine
	private VelocityContext velocityContext;
	
	// List of mapping rules inside a single XML file
	private ArrayList<MappingRulesScripts> PlatformMappingRules = new ArrayList<MappingRulesScripts>();
	
	// Aspects weaving
	// These are helper list that will help to speed up the weaving process
	// AffectedElements is an ordered list (orderder by the Objectc's ID) of all 
	// objects of DERCS model. If the object is affected by more than one aspect,
	// it will have repeated entries in AffectedElements and at the same position
	// in RelatedAspects it will have a different aspect. For example, the object
	// o1 is affected by the aspects a1, a2 and a3. So, it has positions x, x+1 
	// and x+2 in AffectedElements. The element at postion x of RelatedAspects is
	// a1, at x+1 is a2 and at x+2 is a3. 
	private ArrayList<BaseElement> AffectedElements = new ArrayList<BaseElement>();
	private ArrayList<Pointcut> RelatedPointcuts = new ArrayList<Pointcut>();
	
	/**
	 * Creates an instance of the code generation engine.
	 * @param dercsModel An instance of the DERCS model, previously loaded from 
	 * a UML model
	 * @throws Exception Velocity related exceptions
	 */
	public CodeGenerationEngine(Model dercsModel) throws Exception {
		DERCSModel = dercsModel;
		LOGGER = LoggerFactory.getLogger("GenERTiCA");
		Velocity.init();
		createContext();
	}
	
	// Creates the context that objects that can be used by the code generation
	// script in order to access informations on DERCS model, source code files,
	// and options for the code generation tool
	private void createContext() {
		velocityContext = new VelocityContext();
		
		// DERCS related objects
		velocityContext.put(DERCS_MODEL, DERCSModel);
		velocityContext.put(DERCS_FACTORY, new DERCSFactory());
		velocityContext.put(DERCS_HELPER, new DERCSHelper());
		velocityContext.put(CLASS, null);
		velocityContext.put(REFERENCED_CLASS, null);
		velocityContext.put(PACKAGE, null);
		velocityContext.put(DATA_TYPE_STR, null);
		velocityContext.put(VISIBILITY_ENUM_STR, Visibility.class);
		velocityContext.put(VISIBILITY_STR, null);
		velocityContext.put(RETURN_TYPE_STR, null);
		velocityContext.put(INDEX_VARIABLE_NAME, null);
		// other
		velocityContext.put(SOURCE_CODE, null);
		velocityContext.put(OPTIONS, null);
	}
	
	// Loads the XML file containing the mapping rules as well as the code
	// generation options.
	// TODO validate XML file through a schemas 
	//      (see http://java.sun.com/webservices/jaxp/dist/1.1/docs/tutorial/overview/2_specs.html#schema)
	private void loadXMLMappingRules(String fileName) {
		Document mappingRulesDoc = null;
		
		// parse XML file
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// enabling the mapping rules XML file be broken 
		// (see <xi:include> statement in <PlatformConfiguration> node)
		try {
			factory.setNamespaceAware(true);
			factory.setXIncludeAware(true);
		}
		catch (Exception e) {
			// the JVM does not support inserting nodes from an external XML file.
			// the parsing of XML tree should be aware to this kind of node.
			LOGGER.info("\tWARNING: JVM is not aware of <xi:include>. " +
					  "The inclusion of such files are done by GenERTiCA code.");
		}
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			mappingRulesDoc = builder.parse(fileName);
		} 
		catch (Exception e) {
			LOGGER.info(CodeGenerator.CODE_GENERATION_ERROR_1013 + " [" + fileName + "]");
			LOGGER.info("\t"+e.getMessage());
		}
		
		if (mappingRulesDoc != null) {
			// <Platforms> node
			NodeList platforms = mappingRulesDoc.getFirstChild().getChildNodes();
			for(int i=0; i < platforms.getLength(); i++) {
				// ignores all nodes that are not "elements"
				if (platforms.item(i).getNodeType() == Node.ELEMENT_NODE) {
					File tmp = new File(fileName);
					PlatformMappingRules.add(new MappingRulesScripts(tmp.getParent(), 
							platforms.item(i).getNodeName(), platforms.item(i)));
				}
			}
		}
	}
	
	/**
	 * Performs the code generation for the DERCS model representing the UML model.
	 * The following activities diagram shows how the code generation processes
	 * was implemented. As can be seen, code fragments are generated for each 
	 * element of DERCS model. At the end of all source code fragments are combined
	 * into a single file, representing the generated source code for the class.
	 * Additionally, it can be also seen that after the generation of the code
	 * fragments if some aspect affects the selected element, the aspect 
	 * adaptations are performed.<br>
	 * <img src=../CodeGenerationProcess.jpg>
	 * @param targetDirName Name of the directory in which the source code files will be
	 * created.
	 * @param XMLMappingRules File name of the XML file containing the mapping rules.
	 * @throws IOException
	 */
	public void execute(String XMLMappingRules, String targetDirName) throws Exception {
		// initialize the internal structures of the code generation engine
		// loads the XML file containing the mapping rules and code generation options
		loadXMLMappingRules(XMLMappingRules);
		
		FillWeavingHelperLists();
		
		// pass through all platforms found into the mapping rules XML file
		for(Iterator<MappingRulesScripts> itmr=PlatformMappingRules.iterator(); itmr.hasNext();) {
			MappingRulesScripts mrs = itmr.next();
			
			if (mrs.scriptsSuccessfullLoaded()) {
				LOGGER.info("Generating code for the platform: " + mrs.getName());
				//****************************************************************
				//****          Generating    APPLICATION    code              ***
				//****************************************************************
				LOGGER.info("Generating code for the APPLICATION ... ");
				
				// iterate the classes list
				for(Iterator<Class> itc=DERCSModel.getClasses().iterator(); itc.hasNext(); ) {
					ClassBeenAnalysed = itc.next();
					LOGGER.info("\t" + ClassBeenAnalysed.getName());
					
					CodeGenerator appCodeGenSW = new CodeGenerator(mrs.getApplicationSW(), velocityContext);
					CodeGenerator appCodeGenHW = new CodeGenerator(mrs.getApplicationHW(), velocityContext);
					// setup context
					velocityContext.put(CLASS, ClassBeenAnalysed);
					
					//list with all pointcuts which will be used to weave the adaptations
					// in the code generated for Classes, Attributes, Methods, Behaviors and Actions
					ArrayList<Pointcut> pointcuts = null;
					
					// list with all pointcuts which select the class analyzed
					ArrayList<Pointcut> classPointcuts = getPointcutsSelecting(ClassBeenAnalysed);
					
					// model level aspects for classes must be woven before code generation
					if (mrs.getApplicationSW().scriptsSuccessfullLoaded())
						appCodeGenSW.weaveAspectAdaptationsAtModelLevel(ClassBeenAnalysed, classPointcuts, true);
					// REMARK: it important to note that hardware model level aspects adaptations are ignored
					//         due to the way the code generation was implemented. If hardware and software
					//         code generation have been implemented as two call for the same method 
					//         instead of one DERCS model pass and two code fragments generation.
					
					LOGGER.info("\t\t Attributes:");
					// pass through attributes list
					for(Iterator<Attribute> ita=ClassBeenAnalysed.getAttributes().iterator(); ita.hasNext();) {
						Attribute attr = ita.next();
						LOGGER.info("\t\t\t" + attr.getName());
						// list with all pointcuts which select the attribute analyzed
						pointcuts = getPointcutsSelecting(attr);
						if (mrs.getApplicationSW().scriptsSuccessfullLoaded()) {
							// reorder adaptations according the order describe in the XML
							orderAspectAdaptations(mrs.getApplicationSW(), pointcuts);
							// generate code for the attribute
							if (!appCodeGenSW.generateCodeForAttribute(attr, pointcuts))
								throw new AttributeCGException(attr, mrs.getApplicationSW().getAttributeDeclaration());
						}
						if (mrs.getApplicationHW().scriptsSuccessfullLoaded()) {
							// reorder adaptations according the order describe in the XML
							orderAspectAdaptations(mrs.getApplicationHW(), pointcuts);
							// generate code for the attribute
							if (!appCodeGenHW.generateCodeForAttribute(attr, pointcuts))
								throw new AttributeCGException(attr, mrs.getApplicationHW().getAttributeDeclaration());
						}
					}
					
					LOGGER.info("\t\t Methods:");
					pointcuts = null;
					// pass through methods list
					for(Iterator<Method> itm=ClassBeenAnalysed.getMethods().iterator(); itm.hasNext();) {
						Method mth = itm.next();
						LOGGER.info("\t\t\t" + mth.toString());
						// list with all pointcuts wich select the method analyzed
						pointcuts = getPointcutsSelecting(mth);
						
						// the pointcuts that affect the actions inside a behavior
						// should be also added in pointcuts list
						if (mth.getTriggeredBehavior() != null) {
							// look for pointcust that select the behavior triggered by the method
							ArrayList<Pointcut> tmp = getPointcutsSelecting(mth.getTriggeredBehavior());
							if (tmp != null) {
								if (pointcuts == null)
									pointcuts = tmp;
								else
									exclusiveAddAll(pointcuts, tmp);
							}
							
							for(Iterator<BehavioralElement> ita=mth.getTriggeredBehavior().getBehavioralElements().iterator(); ita.hasNext();) {
								BehavioralElement be = ita.next();
								
								tmp = getPointcutsSelecting(be);
								if (tmp != null) {
									if (pointcuts == null)
										// there is no pointcut selecting the method,
										// but some of the actions within it are select.
										// Thus is is necessary to instantiate the list.
										pointcuts = new ArrayList<Pointcut>(tmp);
									else
										exclusiveAddAll(pointcuts, tmp);
								}
								
								// in assignment actions, the value can also come from another 
								// action, thus it is necessary to check if this action is 
								// selected by any of the pointcuts
								if ((be instanceof AssignmentAction) 
									&& (((AssignmentAction)be)).isAssignmentOfActionResult()) {
									tmp = getPointcutsSelecting(((AssignmentAction)be).getResultOfAction());
									if (tmp != null) {
										exclusiveAddAll(pointcuts, tmp);
									}									
								}
							}
						}
						
						if (mrs.getApplicationSW().scriptsSuccessfullLoaded()) {
							// reorder adaptations according the order describe in the XML
							orderAspectAdaptations(mrs.getApplicationSW(), pointcuts);
							// generate code for the method
							if (!appCodeGenSW.generateCodeForMessageDeclaration(mth, pointcuts))
								throw new MessageDeclarationCGException(mth, mrs.getApplicationSW().getAttributeDeclaration());
							if (!appCodeGenSW.generateCodeForMessageImplementation(mth, pointcuts))
								throw new MessageImplementationCGException(mth, mrs.getApplicationSW().getAttributeDeclaration());
						}
						if (mrs.getApplicationHW().scriptsSuccessfullLoaded()) {
							// reorder adaptations according the order describe in the XML
							orderAspectAdaptations(mrs.getApplicationHW(), pointcuts);
							// generate code for the method
							if (!appCodeGenHW.generateCodeForMessageDeclaration(mth, pointcuts))
								throw new MessageDeclarationCGException(mth, mrs.getApplicationSW().getAttributeDeclaration());
							if (!appCodeGenHW.generateCodeForMessageImplementation(mth, pointcuts))
								throw new MessageImplementationCGException(mth, mrs.getApplicationSW().getAttributeDeclaration());
						}
					}
					
					// list with all pointcuts which select the class analyzed
					//pointcuts = getPointcutsSelecting(ClassBeenAnalysed);
					
					// model level aspects adaptations have already been performed,
					// now just code level aspect adaptations must be executed
					pointcuts = classPointcuts;  
					
					// merge all generated fragments
					if (mrs.getApplicationSW().scriptsSuccessfullLoaded()) {
						// reorder adaptations according the order describe in the XML
						orderAspectAdaptations(mrs.getApplicationSW(), pointcuts);
						// generate code for packages declaration						
						appCodeGenSW.generateCodeForPackageDeclaratation();
						// generate code for the class
						
						appCodeGenSW.generateCodeForClassDeclaratation(ClassBeenAnalysed, pointcuts);
						appCodeGenSW.generateCodeForClassImplementation(ClassBeenAnalysed, pointcuts);
						mergeCodeFragments(appCodeGenSW, mrs.getApplicationSW(), targetDirName);
					}
					if (mrs.getApplicationHW().scriptsSuccessfullLoaded()) {
						// reorder adaptations according the order describe in the XML
						orderAspectAdaptations(mrs.getApplicationSW(), pointcuts);
						// generate code for the class
						appCodeGenHW.generateCodeForClassDeclaratation(ClassBeenAnalysed, pointcuts);
						appCodeGenHW.generateCodeForClassImplementation(ClassBeenAnalysed, pointcuts);
						mergeCodeFragments(appCodeGenHW, mrs.getApplicationHW(), targetDirName);
					}
					LOGGER.info("\tDone!\n");
				}
				LOGGER.info("The application code was generated. ");
				//****************************************************************
				//****      Generating PLATFORM CONFIGURATION code             ***
				//****************************************************************
				LOGGER.info("Generating code for the PLATFORM CONFIGURATION ... ");
				
				if (mrs.getPlatformSW().scriptsSuccessfullLoaded())
					generatePlatformConfiguration(mrs.getName(), mrs.getPlatformSW(), targetDirName);
				if (mrs.getPlatformHW().scriptsSuccessfullLoaded())
					generatePlatformConfiguration(mrs.getName(), mrs.getPlatformHW(), targetDirName);
				
				LOGGER.info("The platform configuration was generated. ");
			}
		}
	}
	
	// Generates the platform configuration according the specification in the
	// <PlatformConfiguration> node of XML mapping rules file 
	private void generatePlatformConfiguration(String platformName, PlatformMappingRules pmr, String dirName) {
		if (!dirName.endsWith(File.separator))
			dirName += File.separator;
		dirName += pmr.getOutputDirectory();
		try {
			File tmp = new File(dirName);
			// checking if the output directory exists
			if (!tmp.exists())
				// if not, it should be created
				if (!tmp.mkdirs())
					throw new IOException(CodeGenerator.CODE_GENERATION_ERROR_1011 + "[" + dirName + "]");
			for(Iterator<PlatformConfigurationFileSpecification> itf = pmr.getFilesIterator(); itf.hasNext();) {
				PlatformConfigurationFileSpecification pcfs = itf.next();
				String filePath = dirName + pcfs.getOutputDirectory();
				tmp = new File(filePath);
				// checking if the output directory exists
				if (!tmp.exists())
					// if not, it should be created
					if (!tmp.mkdirs())
						throw new IOException(CodeGenerator.CODE_GENERATION_ERROR_1011 + "[" + filePath + "]");
				
				ArrayList<String> fileAspects = pcfs.getAspects();
				ArrayList<String> fragmentAspects = pcfs.getAspectsOfFragments();
				ArrayList<String> fragments = pcfs.getFragments();
				
				// checking if the file has any aspect associated with it
				boolean canContinue = (fileAspects.size() > 0);
				if (canContinue) {
					// the list of aspects should be checked
					canContinue = false;
					for(Iterator<String> itfa = fileAspects.iterator(); (!canContinue) && itfa.hasNext();)
						canContinue = (DERCSModel.getAspect(itfa.next()) != null);
				}
				if (canContinue) {
					// it has at least one aspect associated with it, 
					// now the fragments must be checked
					// Creating the file
					LOGGER.info("\t" + filePath + pcfs.getCompleteFileName() + " ... ");
					FileWriter fw = new FileWriter(dirName + pcfs.getCompleteFileName());
					for(int i=0; i < fragments.size(); i++) {
						String aspects = fragmentAspects.get(i).trim();
						canContinue = !aspects.equals("");
						if (canContinue) {
							canContinue = false;
							// checking the list of aspect that affect the fragment
							// at least one must match
							while ((!canContinue) && (aspects.indexOf(",") >= 0)) {
								canContinue = (DERCSModel.getAspect(aspects.substring(0, aspects.indexOf(",")).trim()) != null);
								aspects = aspects.substring(aspects.indexOf(",")+1).trim();
							}
							if (!canContinue)
								canContinue = (DERCSModel.getAspect(aspects.trim()) != null);
						}
						if (canContinue
							|| (aspects.equals("") && !canContinue)) {
							// the fragment is allowed to be written in the 
							// platform configuration file
							fw.write(fragments.get(i));
							if (!fragments.get(i).endsWith("\n"))
								fw.write("\n");
						}
					}
					fw.flush();
					LOGGER.info("Done!");
				}
				
			}
		} catch (Exception e) {
			LOGGER.info(CodeGenerator.CODE_GENERATION_ERROR_1010 + "[" + platformName + "]");
			LOGGER.info("\t"+e.getMessage());
		}
	}
	
	/*
	 * Mege all generated code fragments into one single source code file.
	 */
	private void mergeCodeFragments(CodeGenerator codeGen, ApplicationMappingRules amr, String dirName) throws IOException {
		String fileName = runScript(amr.getFileName());
		
		SourceCode srcImplSW = new SourceCode(fileName + amr.getOptions().getImplementionFileExt());
		// TODO CG-500 adicionar package declaration
		// TODO CG-500 adicionar references declaration
		srcImplSW.addPackageDeclaration(codeGen.getPackageDeclaration());
		srcImplSW.addClassDeclaration(codeGen.getClassDeclaration());
		srcImplSW.addClassImplementation(codeGen.getClassImplementation());
		
		velocityContext.put(SOURCE_CODE, srcImplSW);
		String s = runScript(amr.getImplementationFileOrganization());
		
		// TODO CG-501 handle more than one class in the same source file
		// save source code file
		if (dirName.charAt(dirName.length()-1) != File.separatorChar)
			dirName += File.separatorChar;
		FileWriter fw = new FileWriter(dirName + srcImplSW.getFileName());
		fw.write(s);
		fw.flush();
		
		// if the target language uses declaration files (e.g. *.h of C/C++),
		// these files should also be generated
		if (amr.getOptions().hasClassesDeclaration()) {
			SourceCode srcHeadSW = null;
			srcHeadSW = new SourceCode(fileName + amr.getOptions().getDeclarationFileExt());
            // TODO CG-500 adicionar package declaration
			// TODO CG-500 adicionar references declaration
			srcHeadSW.addPackageDeclaration(codeGen.getPackageDeclaration());
			srcHeadSW.addClassDeclaration(codeGen.getClassDeclaration());
			srcHeadSW.addClassImplementation(codeGen.getClassImplementation());
			
			velocityContext.put(SOURCE_CODE, srcHeadSW);
			s = runScript(amr.getDeclarationFileOrganization());
			
            // save source code file
			fw = new FileWriter(dirName + srcHeadSW.getFileName());
			fw.write(s);
			fw.flush();
		}
		if (amr.getOptions().hasPackagesDeclaration()) {
			if (amr.getOptions().isPackagePerClass()){
				SourceCode srcPCKSW = null;
				srcPCKSW = new SourceCode(fileName + amr.getOptions().getDeclarationFileExt());
				// TODO CG-500 adicionar references declaration
				srcPCKSW.addPackageDeclaration(codeGen.getPackageDeclaration());
		
				velocityContext.put(SOURCE_CODE, srcPCKSW);
				s = runScript(amr.getDeclarationFileOrganization());
				
	            // save source code file
				fw = new FileWriter(dirName + srcPCKSW.getFileName());
				fw.write(s);
				fw.flush();
			}else{
				SourceCode srcPCKSW = null;
				srcPCKSW = new SourceCode( amr.getOptions().getPackageName() + amr.getOptions().getDeclarationFileExt());
				srcPCKSW.addPackageDeclaration(codeGen.getPackageDeclaration());
		
				velocityContext.put(SOURCE_CODE, srcPCKSW);
				s = runScript(amr.getDeclarationFileOrganization());
				
	            // save source code file
				fw = new FileWriter(dirName + srcPCKSW.getFileName());

				fw.write(s);
				fw.flush();
			}
		}
	}
	
	/*
	 * Executes a Velocity Template Language script with the global context of
	 * the code generation. As result, this method returns the generated text.
	 */
	private String runScript(String script) {
		StringWriter result = new StringWriter();
		try {
			Velocity.evaluate(velocityContext, result, "General Script", script);
		} catch (Exception e) {
			// TODO tratamento de erro caso o VTE gere alguma excecao
			LOGGER.info("\n\n\tError-9999: General script error.");
			LOGGER.info("\t" + e.getMessage());
			LOGGER.info("\t---------- Script Begin ----------");
			LOGGER.info(script);
			LOGGER.info("\t----------  Script Begin ----------");
			return "";
		}
		return result.toString();
	}
	
	/*
	 * Get all pointcuts that affect an specific element.
	 */
	private ArrayList<Pointcut> getPointcutsSelecting(BaseElement element) {
		int i = BinarySearch.binarySearch(AffectedElements, element);
		if (i >= 0) {
			// A single element can have more entries in the pointcuts list,
			// this means that more than an aspect adaptation affect the element.
			// The returned position can be at some point between the first and
			// last entry for the element. Thus the entries near to the returned 
			// position should be checked.
			ArrayList<Pointcut> result = new ArrayList<Pointcut>();
			// first, all entries before the returned position
			int start = i;
			for(; (start >= 0) && (AffectedElements.get(start) == element); start--) {
				result.add(RelatedPointcuts.get(start));
			}
			// then, all entries after the returned position
			int end = i+1;
			for(; (end < AffectedElements.size()) && (AffectedElements.get(end) == element); end++) {
				result.add(RelatedPointcuts.get(end));
			}
			return result;
		}
		else
			return null;
	}
	
	/*
	 * Add elements from the src array to dst array. If the element been inserted
	 * is already in dst array, it is not inserted.
	 */
	protected static void exclusiveAddAll(ArrayList dst, ArrayList src) {
		for(int i=0; i < src.size(); i++) {
			if (dst.indexOf(src.get(i)) < 0)
				dst.add(src.get(i));
		}
	}
	
	// TODO CG-100 consider aspects order!
	protected static void orderAspectAdaptations(ApplicationMappingRules amr, ArrayList<Pointcut> pointcuts) {
		if (pointcuts != null) {
			// bubble sort :-(
			for(int i=pointcuts.size()-1; i > 1; i--) {
				for(int j=0; j < i; j++) {
					AspectAdaptation aai = pointcuts.get(j).getAspectAdaptation();
					AspectAdaptation aaj = pointcuts.get(j+1).getAspectAdaptation();
					if (amr.getAspectAdaptationOrder(aai) > amr.getAspectAdaptationOrder(aaj))
						Collections.swap(pointcuts, j, j+1);
				}
			}
		}
	}
	
	private void debugVelocityTest() throws IOException {
		String script = "Numero de classes $DERCSModel.getClasses().size()\n";
		script += "#foreach ($class in $DERCSModel.getClasses())";
		script += "$class.Name\n";
		script += "#end";
		StringWriter sw = new StringWriter();
		
		Velocity.evaluate(velocityContext, sw, "", script);
//		Log.info(""+sw);
	}
	
	/*
	 * Pass through all join points used in pointcuts of aspects of the DERCS model
	 * in order to fill the AffectedElements array with the elements selected by 
	 * those join points.
	 * Additionally, the RelatedAspects array is filled with the corresponding
	 * aspect that affects the element at the same position of AffectedElements
	 * array.
	 */
	private void FillWeavingHelperLists() {
		for(Iterator<Aspect> ita=DERCSModel.getAspects().iterator(); ita.hasNext();) {
			Aspect aspect = ita.next();
			for(Iterator<Pointcut> itpc=aspect.getPointcuts().iterator(); itpc.hasNext();) {
				Pointcut pc = itpc.next();
				for(Iterator<JoinPoint> itjp=pc.getJoinPoints().iterator(); itjp.hasNext();) {
					JoinPoint jp = itjp.next();
					for(Iterator<BaseElement> itbe=jp.getSelectedElements().iterator(); itbe.hasNext();) {
						BaseElement be = itbe.next();
						AffectedElements.add(be);
						RelatedPointcuts.add(pc);
					}
				}
			}
		}
		ArrayList a[] = {AffectedElements, RelatedPointcuts};
		QuickSort.quickSort(a);
		
		// debug only
//		int i=0;
//		for(Iterator<BaseElement> it=AffectedElements.iterator(); it.hasNext(); i++) {
//			BaseElement be = it.next();
//			String s = be.getName() + " [" + be.hashCode() + "]";
//			while (s.length() < 50)
//				s += " ";
//			Log.info(i + "\t" + s + "\t" + 
//					RelatedPointcuts.get(i).getAdaptation().getAspect().getName() + "." +
//					RelatedPointcuts.get(i).getAdaptation().getName(), false);
//		}
//		Log.print("", false);
	}
	
}
