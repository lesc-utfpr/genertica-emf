package CodeGenerationTool;

import java.awt.event.ActionEvent;
import java.util.Iterator;

import dercs.util.OutputLog;

import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;

import dercs.MDOA_DERCSLoader;
import dercs.Model;
import dercs.structure.Class;
import dercs.structure.Method;
import dercs.structure.runtime.Node;
import dercs.structure.runtime.Object;

import javax.swing.*;

/**
 * This class implements an option into Magic Draw's main menu, in order to
 * execute the Aspect Oriented Metrics Suit. The menu option is inserted under
 * the "Analysis" menu under the name "Aspect Oriented (AO) Metrics".
 * @author Marco Aurelio Wehrmeister
 *
 */
public class MainMenuAction extends MDAction {
	
	/**
	 * Overrides the default constructor just to compile the class.
	 * @param id   - Unique identification for the action
	 * @param name - Name for the action  
	 * @see com.nomagic.magicdraw.actions.MDAction#actionPerformed(java.awt.event.ActionEvent)
	 */
	public MainMenuAction(String id, String name) {
		super(id, name, null, null);
	}

	/**
	 * This method calculates the AOP metrics, saving them into a file.
	 * @see com.nomagic.magicdraw.actions.MDAction#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// the metrics can be calculated only wheter a project is active
		Project activeProject = Application.getInstance().getProjectsManager().getActiveProject();
		if (activeProject != null) {
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
						
						if (GenERTiCA.Log == null)
							GenERTiCA.Log = new OutputLog(null);
						GenERTiCA.Log.createLogFile(saveTo + "CodeGeneration.LOG");
						
						// load DERCS model from Project
						GenERTiCA.Log.println("Loading DERCS ... ", true);
						MDOA_DERCSLoader dercsLoader = new MDOA_DERCSLoader(activeProject, GenERTiCA.Log);
						Model dercsModel = dercsLoader.loadDERCSModel();
						GenERTiCA.Log.println("DERCS loaded successfull.", true);
						
						// allowing model interchagen among tools
						// this has been made for the umlVM project 
						GenERTiCA.Log.println("Saving loaded model as \"" + saveTo + "dercs_model.bin\"...", true);
						dercsModel.saveTo(saveTo + "dercs_model.bin");
						GenERTiCA.Log.println("Model saved.", true);
						
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
						GenERTiCA.Log.println("DERCS starting code generation ... ", true);
						CodeGenerationEngine cge = new CodeGenerationEngine(dercsModel, GenERTiCA.Log);
						cge.execute(mappingRulesFileName, saveTo);
//						GenERTiCA.Log.println("OK", false);

						// code generation process was performed successfully
						GenERTiCA.Log.println("Code generated at " + saveTo, true);
						// force the release of memory after the code generation
						dercsModel = null;
						dercsLoader = null;
						cge = null;
						JOptionPane.showMessageDialog(null, "Done... code generated at " + saveTo);
					} catch (Exception e) {
						StackTraceElement[] stack = e.getStackTrace(); 
						String s = "";
						for(int i=0; (i < stack.length) && (stack[i].toString().indexOf("actionPerformed") == -1); i++) {
							s += "\n"+stack[i].toString();
						}
						GenERTiCA.Log.println("\n---------\n" + e.getClass().getName() + 
								": \n" + e.getMessage() + "\n\nStack trace:" + s, false);
						JOptionPane.showMessageDialog(null, "ERROR (" + e.getClass().getName() + ") :\n" + e.getMessage() + "\n--------" + s);
					}
				}
			}
		}
		else
			JOptionPane.showMessageDialog(null, "There is none active project.");
	}
	
	/**
	 * This method controls whether the menu option is enabled or not. The AO
	 * Metrics option should be enable only if a project is loaded into Magic
	 * Draw environment.
	 * */
	@Override
	public void updateState() {
		setEnabled(Application.getInstance().getProjectsManager().getProjects().size() > 0);
	}

}
