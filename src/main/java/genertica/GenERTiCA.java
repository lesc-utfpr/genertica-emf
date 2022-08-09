package genertica;

import java.io.File;
import java.io.FileNotFoundException;

import dercs.Model;
import dercs.loader.IDercsLoader;
import dercs.loader.UmlDercsLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the main class og GenERTiCA. Basically it contains just a method
 * to start Magic Draw tool in batch mode.
 * @author marcow
 *
 */
public class GenERTiCA {
	
	public static Logger LOGGER = LoggerFactory.getLogger("GenERTiCA");
	
	public static void printHelp() {
		System.out.println("----------------------------------------------------------------------------");
		System.out.println("     GenERTiCA - Generation of Embedded Real-time Code based on Aspects");
		System.out.println("                                  version 0.15");
		System.out.println("----------------------------------------------------------------------------");
		System.out.println("Usage:");
		System.out.println("   java genertica.GenERTiCA [source_model mapping_rules output_dir]");
		System.out.println();
		System.out.println("where");
		System.out.println("   source_model   complete path to the file for the UML project");
		System.out.println("                  from which the source code will be generated.");
		System.out.println("   mapping_rules  complete path to the XML file that contains the");
		System.out.println("                  mapping rules.");
		System.out.println("   output_dir     directory where the source code files will be stored.");
		System.out.println();
	}
	
	public static void executeCodeGeneration(String srcModel, String mappingRules, String outputDir) {
		try 
		{
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
			LOGGER.info("Loading DERCS ... ");
			IDercsLoader loader = new UmlDercsLoader(new File(srcModel).toURI());
			Model dercsModel = loader.loadDercsModel();
			LOGGER.info("DERCS loaded successfull.");

			// generating code
			LOGGER.info("DERCS starting code generation ... ");
			CodeGenerationEngine cge = new CodeGenerationEngine(dercsModel);
			cge.execute(mappingRules, outputDir);

			// code generation process was performed successfully
			LOGGER.info("Code generated at " + outputDir);

		} catch (Exception e) {
			StackTraceElement[] stack = e.getStackTrace(); 
			String s = "";
			for(int i=0; (i < stack.length) && (stack[i].toString().indexOf("actionPerformed") == -1); i++)
				s += "\n"+stack[i].toString();
			LOGGER.info("\n---------\n" + e.getClass().getName() +
					": \n" + e.getMessage() + "\n\nStack trace:" + s);
		}
	}

	public static void main(String[] args) {
		try
		{
			if (args.length == 3) {
				executeCodeGeneration(args[0], args[1], args[2]);
			} else {
				printHelp();
			}
		}
		catch (Exception e)
		{
			LOGGER.error("", e);
		}
	}
}
