package genertica;

import java.io.File;
import java.io.IOException;

import dercs.Model;
import org.slf4j.LoggerFactory;


public class CMD_GenERTiCA /*extends CommandLine*/ {

	private static final String PROJECT = "project_file";
	private static final String MAPPING_RULES = "mapping";
	private static final String DESTINATION = "destination_dir";
	private static String mProjectFile;
	private static String mMappingRules;
	private static File mDestinationDir;
	
	
	/**
	 * Check the given argument and set the system property.
	 *
	 * @param arg          argument.
	 * @param propertyName property name.
	 * @return true if given argument is describes given property, otherwise - false.
	 */
	private static boolean checkArgument(String arg, String propertyName)
	{
		final String start = propertyName + "=";
		if (arg.startsWith(start))
		{
			System.setProperty(propertyName, arg.substring(start.length()));
			return true;
		}
		return false;
	}
	
	protected void parseArgs(String[] args) throws Exception
	{
		int x = 0, i = 0;
		String s = "";
		for (final String argument : args)
		{
			if (i == 0)
				s += argument;
			else
				s += ", " + argument;
			i++;
			
			if (checkArgument(argument, PROJECT))
				x++;
			if (checkArgument(argument, MAPPING_RULES))
				x++;
			if (checkArgument(argument, DESTINATION))
				x++;
		}
		
		if (x < 3) {
			System.out.println("Invalid parameters [" + s + "]");
			throw new IllegalArgumentException("Invalid parameters [" + s + "]");
		}

		/*
		 * Check project file.
		 */
		mProjectFile = System.getProperty(PROJECT, "");
		if (mProjectFile.length() == 0)
		{
			System.out.println("Project file not defined! " + mProjectFile + "]");
			throw new IllegalArgumentException("Project file not defined! " + mProjectFile + "]");
		}
		else
			System.out.println("Project: " + mProjectFile);
		/*
		 * Check mapping rules file.
		 */
		mMappingRules = System.getProperty(MAPPING_RULES, "");
		if (mMappingRules.length() == 0)
		{
			System.out.println("Mapping rules not defined! [" + mMappingRules +"]");
			throw new IllegalArgumentException("Mapping rules not defined! [" + mMappingRules +"]");
		}
		else
			System.out.println("Mapping Rules: " + mMappingRules);
		File tmp = new File(mMappingRules);
		if (!tmp.exists())
		{
			System.out.println("Mapping rules file does not exist! [" + mMappingRules + "]");
			throw new IllegalArgumentException("Mapping rules file does not exist! [" + mMappingRules + "]");
		}
		/*
		 * Check destination dir name.
		 */
		final String destinationDirName = System.getProperty(DESTINATION, "");
		if (destinationDirName.length() == 0)
		{
			System.out.println("Destination directory not defined! [" + destinationDirName +"]");
			throw new IllegalArgumentException("Destination directory not defined! [" + destinationDirName +"]");
		}
		else
			System.out.println("Destination: " + destinationDirName);
		mDestinationDir = new File(destinationDirName);
		if (!mDestinationDir.exists() && !mDestinationDir.mkdirs())
		{
			System.out.println("Cannot create or open destination directory! [" + destinationDirName +"]");
			throw new IllegalArgumentException("Cannot create or open destination directory! [" + destinationDirName +"]");
		}
	}
	
	protected byte execute()
	{
		System.out.println("***********************************************");
		System.out.println("* Project: " + mProjectFile);
		System.out.println("* Mapping Rules: " + mMappingRules);
		System.out.println("* Destination: " + mDestinationDir.getAbsolutePath());
		System.out.println("***********************************************");
		
		final File file = new File(mProjectFile);
//		final ProjectDescriptor projectDescriptor = ProjectDescriptorsFactory.createProjectDescriptor(file.toURI());
//		if (projectDescriptor == null)
//		{
//			System.out.println("Project descriptor was not created for " + file.getAbsolutePath());
//			return -1;
//		}
//		final ProjectsManager projectsManager = Application.getInstance().getProjectsManager();
//		projectsManager.loadProject(projectDescriptor, true);
//		final Project project = projectsManager.getActiveProject();
//		if (project == null)
//		{
//			System.out.println("Project " + file.getAbsolutePath() + " was not loaded.");
//			return -1;
//		}
		
		try {
			//SaveAllImagesSVG(project);
			//performCodeGeneration(project);

			System.out.println("***********************************************");
			System.out.println("*   Code generation completed successfully!   *");
			System.out.println("***********************************************");
			System.out.println("Check: " + mDestinationDir.getAbsolutePath());
		} catch (Exception e) {
			//nothing to do here.
			// exception is handled inside called methods
		}
		return 0;
	}
	
	/*
	 * This method allows the automatic generation of SVG images from all diagrams. 
	 */
//	protected void SaveAllImagesSVG(final Project project) {
//		for (DiagramPresentationElement diagram : project.getDiagrams())
//		{
//			final File diagramFile = new File(mDestinationDir, diagram.getHumanName() + diagram.getID() + ".svg");
//			try
//			{
//				ImageExporter.export(diagram, ImageExporter.SVG, diagramFile);
//			}
//			catch (IOException e)
//			{
//				e.printStackTrace();
//			}
//		}
//	}
	
	protected void performCodeGeneration(/*final Project activeProject*/) throws Exception {
		// save complete file path to the mapping rules XML file
		String mappingRulesFileName = mMappingRules;
		try {
 			String saveTo = mDestinationDir.getAbsolutePath();
			if (!saveTo.endsWith("/")) 
				saveTo += "/";
			
			if (GenERTiCA.LOGGER == null)
				GenERTiCA.LOGGER = LoggerFactory.getLogger("GenERTiCA");
//			GenERTiCA.LOGGER.createLogFile(saveTo + "CodeGeneration.LOG");
			
			// load DERCS model from Project
			GenERTiCA.LOGGER.info("Loading DERCS ... ");
			//MDOA_DERCSLoader dercsLoader = new MDOA_DERCSLoader(activeProject, GenERTiCA.LOGGER);
			Model dercsModel = null;//dercsLoader.loadDERCSModel();
			GenERTiCA.LOGGER.info("DERCS loaded successfull.");
			
			// allowing model interchagen among tools
			// this has been made for the umlVM project
			String binFileName = (new File(mProjectFile)).getName().replace("mdzip", "bin");
			GenERTiCA.LOGGER.info("Saving loaded model as \"" + saveTo + "binFileName\"...");
			//dercsModel.saveTo(saveTo + binFileName);
			GenERTiCA.LOGGER.info("Model saved.");
						
			// TODO CG-000 complete code generation
			GenERTiCA.LOGGER.info("DERCS starting code generation ... ");
			CodeGenerationEngine cge = new CodeGenerationEngine(dercsModel);
			cge.execute(mappingRulesFileName, saveTo);
//			GenERTiCA.Log.println("OK", false);

			// code generation process was performed successfully
			GenERTiCA.LOGGER.info("Code generated at " + saveTo);
			// force the release of memory after the code generation
//			dercsModel = null;
//			dercsLoader = null;
			cge = null;
		} catch (Exception e) {
			StackTraceElement[] stack = e.getStackTrace(); 
			String s = "";
			for(int i=0; (i < stack.length) && (stack[i].toString().indexOf("actionPerformed") == -1); i++) {
				s += "\n"+stack[i].toString();
			}
			GenERTiCA.LOGGER.info("\n---------\n" + e.getClass().getName() +
					": \n" + e.getMessage() + "\n\nStack trace:" + s);
			throw e;
		}		
	}

	protected void run() {
		// load project
		System.out.print("++++++++++++++++++ Nothing to do here +++++++++++++++++");
	}

}
