package genertica;

import java.awt.event.ActionEvent;

import dercs.Model;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * This class implements an option into Magic Draw's main menu, in order to
 * execute the Aspect Oriented Metrics Suit. The menu option is inserted under
 * the "Analysis" menu under the name "Aspect Oriented (AO) Metrics".
 * @author Marco Aurelio Wehrmeister
 *
 */
public class MainMenuAction /*extends MDAction*/ {
	
	/**
	 * Overrides the default constructor just to compile the class.
	 * @param id   - Unique identification for the action
	 * @param name - Name for the action
	 */
	public MainMenuAction(String id, String name) {
		/*super(id, name, null, null);*/
	}

	/**
	 * This method calculates the AOP metrics, saving them into a file.
	 */
	public void actionPerformed(ActionEvent arg0) {
		// the metrics can be calculated only wheter a project is active
//		Project activeProject = Application.getInstance().getProjectsManager().getActiveProject();
//		if (activeProject != null) {
			// open a dialog to ask the mapping rules XML file
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Choose the mapping rules XML file");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				// save complete file path to the mapping rules XML file
				String mappingRulesFileName = chooser.getSelectedFile().getAbsolutePath();
						
				// open a dialog to ask the directory where the source code files will be stored
				chooser = new JFileChooser();
				chooser.setDialogTitle("Choose the directory to store the source code files");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						String saveTo = chooser.getSelectedFile().getAbsolutePath();
						if (!saveTo.endsWith("/")) 
							saveTo += "/";
						
						if (GenERTiCA.LOGGER == null)
							GenERTiCA.LOGGER = LoggerFactory.getLogger("GenERTiCA");
//						GenERTiCA.LOGGER.createLogFile(saveTo + "CodeGeneration.LOG");
						
						// load DERCS model from Project
						GenERTiCA.LOGGER.info("Loading DERCS ... ");
						//MDOA_DERCSLoader dercsLoader = new MDOA_DERCSLoader(activeProject, GenERTiCA.LOGGER);
						Model dercsModel = null;//dercsLoader.loadDERCSModel();
						GenERTiCA.LOGGER.info("DERCS loaded successfull.");
						
						// allowing model interchagen among tools
						// this has been made for the umlVM project 
						GenERTiCA.LOGGER.info("Saving loaded model as \"" + saveTo + "dercs_model.bin\"...");
						//dercsModel.saveTo(saveTo + "dercs_model.bin");
						GenERTiCA.LOGGER.info("Model saved.");
						
						// ******** DEBUG ************
//						GenERTiCA.Log.println("DEBUG: Objects found within de UML model", false);
//						for(Iterator<Object> ito = dercsModel.getObjects().iterator(); ito.hasNext();) {
//							Object obj = ito.next();
//							GenERTiCA.Log.println("\t"+ obj + " - " + obj.hashCode(), false);
//						}
//						GenERTiCA.Log.println("", false);
						
//						GenERTiCA.Log.println("DEBUG: Objects deployment", false);
//						for(Iterator<Node> itn = dercsModel.getNodes().iterator(); itn.hasNext();) {
//							Node n = itn.next();
//							GenERTiCA.Log.println("\tNode: "+ n.getName() + " [" + n.getPlatformName() + "]", false);
//							for(Iterator<Object> ito = n.getDeployedObjectsIterator(); ito.hasNext();) {
//								GenERTiCA.Log.println("\t\t"+ ito.next(), false);
//							}
//						}
//						GenERTiCA.Log.println("", false);
						
//						GenERTiCA.Log.println("DEBUG: Behaviors ******", false);
//						for(Iterator<Class> itc = dercsModel.getClasses().iterator(); itc.hasNext();) {
//							Class cl = itc.next();
//							GenERTiCA.Log.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++", false);
//							GenERTiCA.Log.println(cl.getName(), false);
//							for(Iterator<Method> itm=cl.getMethodsIterator(); itm.hasNext();) {
//								Method mth = itm.next();
//								GenERTiCA.Log.println("\n"+mth.toString(), false);
//								if (mth.getTriggeredBehavior() != null) {
//									GenERTiCA.Log.println(mth.getTriggeredBehavior().toString(), false);
//								}
//								else
//									GenERTiCA.Log.println("***", false);
//							}
//							GenERTiCA.Log.println("\n\n\n", false);
//						}
//						GenERTiCA.Log.println("", false);
						// ******** DEBUG ************
						
						// TODO CG-000 complete code generation
						GenERTiCA.LOGGER.info("DERCS starting code generation ... ");
						CodeGenerationEngine cge = new CodeGenerationEngine(dercsModel);
						cge.execute(mappingRulesFileName, saveTo);
//						GenERTiCA.Log.println("OK", false);

						// code generation process was performed successfully
						GenERTiCA.LOGGER.info("Code generated at " + saveTo);
						// force the release of memory after the code generation
						dercsModel = null;
//						dercsLoader = null;
						cge = null;
						JOptionPane.showMessageDialog(null, "Done... code generated at " + saveTo);
					} catch (Exception e) {
						StackTraceElement[] stack = e.getStackTrace(); 
						String s = "";
						for(int i=0; (i < stack.length) && (stack[i].toString().indexOf("actionPerformed") == -1); i++) {
							s += "\n"+stack[i].toString();
						}
						GenERTiCA.LOGGER.info("\n---------\n" + e.getClass().getName() +
								": \n" + e.getMessage() + "\n\nStack trace:" + s);
						JOptionPane.showMessageDialog(null, "ERROR (" + e.getClass().getName() + ") :\n" + e.getMessage() + "\n--------" + s);
					}
				}
			}
//		}
//		else
//			JOptionPane.showMessageDialog(null, "There is none active project.");
	}
	
	/**
	 * This method controls whether the menu option is enabled or not. The AO
	 * Metrics option should be enable only if a project is loaded into Magic
	 * Draw environment.
	 * */
	public void updateState() {
//		setEnabled(Application.getInstance().getProjectsManager().getProjects().size() > 0);
	}

}
