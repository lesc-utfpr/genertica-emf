package CodeGenerationTool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import CodeGenerationTool.Exceptions.MDProjectNotFound;
import CodeGenerationTool.Exceptions.MappingRulesFileNotFound;
import CodeGenerationTool.Exceptions.OutputDirectoryNotFound;
import dercs.util.OutputLog;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.core.project.ProjectDescriptorsFactory;
import com.nomagic.runtime.ApplicationExitedException;
import com.nomagic.magicdraw.commandline.CommandLine;

import dercs.MDOA_DERCSLoader;
import dercs.Model;

/**
 * This is the main class og GenERTiCA. Basically it contains just a method
 * to start Magic Draw tool in batch mode.
 * @author marcow
 *
 */
public class GenERTiCA extends CommandLine {
	
	public static OutputLog Log;
	
//	/**
//	 * It just starts Magic Draw tool in batch mode.
//	 * @param args[0] is the complete path to the Magic Draw project from which
//	 *                the source code must be generated;
//	 * @param args[1] is the complete path to the mapping rules XML file;
//	 * @param args[2] is the output directory where the generated source code
//	 *                files are stored; 
//	 */
//	public static void main(String[] args) throws MDProjectNotFound, 
//	                                              MappingRulesFileNotFound, 
//	                                              OutputDirectoryNotFound {
//		if ((args.length == 0) || (args[0].compareTo("-h") == 0) 
//		    || (args[0].compareTo("-help") == 0))
//			printHelp();
//		else {
//			// check all parameters
//			if (!(new File(args[0])).exists()) {
//				throw new MDProjectNotFound(args[0]);
//			}
//			if (!(new File(args[1])).exists()) {
//				throw new MappingRulesFileNotFound(args[2]);
//			}
//			if (!(new File(args[2])).exists()) {
//				throw new OutputDirectoryNotFound(args[2]);
//			}
//			
//			// creates the log file for the code generation process
//			if (!args[2].endsWith("/"))
//				args[2] += "/";
//			Log = new OutputLog(args[2] + "CodeGeneration.LOG");
//
//			Application app = Application.getInstance();
//			try
//			{
//				// loads Magic Draw
//				Log.print("Loading Magic Draw ... ", true);
//				app.start(false, true, false, args, null);
//				Log.println("OK", false);
//
//				// loads the project
//				Log.print("Loading Project " + args[0] + " ... ", true);
//				app.getProjectsManager().loadProject(
//						ProjectDescriptorsFactory.createProjectDescriptor(
//								new File(args[0]).toURI()), true);
//				Log.println("OK", false);
//				
//				// load DERCS model from Project
//				Log.println("Loading DERCS ... ", true);
//				MDOA_DERCSLoader dercsLoader = new MDOA_DERCSLoader(app.getProjectsManager().getActiveProject(), Log);
//				Model dercsModel = dercsLoader.loadDERCSModel();
//				Log.println("DERCS loaded successfull.", true);
//
//				Log.println("Code generated at " + args[2], true);
//				
//				// shutdown Magic Draw
//				Log.print("Shutdown Magic Draw ... ", true);
//				app.shutdown();
//				Log.println("OK", false);
//			}
//			catch (Exception e)
//			{
//				Log.printStack(e);
//			}
//		}
//	}
	
	public static void printHelp() {
		System.out.println("----------------------------------------------------------------------------");
		System.out.println("     GenERTiCA - Generation of Embedded Real-time Code based on Aspects");
		System.out.println("                                  version 0.15");
		System.out.println("----------------------------------------------------------------------------");
		System.out.println("Usage:");
		System.out.println("   java CodeGenerationTool.GenERTiCA [source_model mapping_rules output_dir]");
		System.out.println("");
		System.out.println("where");
		System.out.println("   source_model   complete path to the file for the Magic Draw project");
		System.out.println("                  from which the source code will be generated.");
		System.out.println("   mapping_rules  complete path to the XML file that contains the");
		System.out.println("                  mapping rules.");
		System.out.println("   output_dir     directory where the source code files will be stored.");
		System.out.println("");
		System.out.println("Important: Magic Draw tool with a valid license is required.");
		System.out.println("");
	}
	
	public static void loadMagicDraw(String[] args) throws Exception {
		Log.print("Loading Magic Draw ... ", true);
		try{
			com.nomagic.magicdraw.core.Application.getInstance().start(true, false, false, args, null);
			Log.println("OK", false);
		}
		catch (ApplicationExitedException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void executeCodeGeneration(String srcModel, String mappingRules, String outputDir) {
		try 
		{
			if (GenERTiCA.Log == null)
				GenERTiCA.Log = new OutputLog(null);
			GenERTiCA.Log.createLogFile(outputDir + "CodeGeneration.LOG");
			
			// validating parameters
			File tmp = new File(srcModel);
			if (!tmp.exists())
				throw new FileNotFoundException("Model not found. [" + srcModel + "]");
			tmp = new File(mappingRules);
			if (!tmp.exists())
				throw new FileNotFoundException("Mapping rules file not found. [" + mappingRules + "]");
			tmp = new File(outputDir);
			if (!tmp.exists())
				throw new FileNotFoundException("Output directory not found. [" + outputDir + "]");
			
			if (!outputDir.endsWith("/")) 
				outputDir += "/";

			// load DERCS model from file
			GenERTiCA.Log.println("Loading DERCS ... ", true);
			Model dercsModel = Model.loadFrom(srcModel);
			GenERTiCA.Log.println("DERCS loaded successfull.", true);

			// generating code
			GenERTiCA.Log.println("DERCS starting code generation ... ", true);
			CodeGenerationEngine cge = new CodeGenerationEngine(dercsModel, GenERTiCA.Log);
			cge.execute(mappingRules, outputDir);

			// code generation process was performed successfully
			GenERTiCA.Log.println("Code generated at " + outputDir, true);

		} catch (Exception e) {
			StackTraceElement[] stack = e.getStackTrace(); 
			String s = "";
			for(int i=0; (i < stack.length) && (stack[i].toString().indexOf("actionPerformed") == -1); i++)
				s += "\n"+stack[i].toString();
			GenERTiCA.Log.println("\n---------\n" + e.getClass().getName() + 
					": \n" + e.getMessage() + "\n\nStack trace:" + s, false);
		}
	}

	public static void main(String[] args) {
		Log = new OutputLog(null);
		try
		{
			if ((args.length == 1) && (args[0].equals("md"))) {
				args[0] = "";
				//loadMagicDraw(args);
				new GenERTiCA().launch(args);
			}
			else if (args.length == 3) {
				executeCodeGeneration(args[0], args[1], args[2]);
			}
			else {
				printHelp();
			}
		}
		catch (Exception e)
		{
			Log.printStack(e);
		}
	}
	
	protected void run()
    {
		Log = new OutputLog(null);
		try {
			loadMagicDraw(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }
	
}
