package genertica;

import com.nomagic.actions.AMConfigurator;
import com.nomagic.actions.ActionsCategory;
import com.nomagic.actions.NMAction;
import com.nomagic.actions.ActionsManager;
import com.nomagic.magicdraw.actions.ActionsConfiguratorsManager;
import com.nomagic.magicdraw.actions.ActionsID;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.core.project.ProjectEventListener;
import dercs.MDOA_DERCSLoader;
import dercs.Model;
import dercs.util.ModelHelpers;
import dercs.util.OutputLog;
import graphutil.GraphUtil;
import graphutil.Main;

import javax.swing.*;
import java.io.File;
import java.util.*;

/**
 * This is a Magic Draw Plug-in to generate metrics for UML models that uses 
 * Aspect Oriented Paradigm (AOP) concepts to deal with Non-Functiona 
 * Requirements (NFR). 
 * @see AOMetricsCalculator AOMetricsCalculator
 * @author Marco Aurelio Wehrmeister
 * @version 0.1
 */
public class GenERTiCA_MDPlugIn extends com.nomagic.magicdraw.plugins.Plugin {

	/**
	 * Overrides init() method from Plugin class of Magic Draw OpenAPI. This 
	 * code will be executed when the plug-in is loaded by Magic Draw's Plug-in 
	 * Manager at tool initialization.
	 * @see com.nomagic.magicdraw.plugins.Plugin#init()
	 */
	@Override
	public void init() {
		registerAndConfigureMainMenuAction();
		//javax.swing.JOptionPane.showMessageDialog(null, "GenERTiCA plug-in loaded!");
	}

	/**
	 * Overrides close() method from Plugin class of Magic Draw OpenAPI. This
	 * code will be executed when the plug-in is unloaded by Magic Draw's Plug-in
	 * Manager at tool shutdown.
	 * @see com.nomagic.magicdraw.plugins.Plugin#close()
	 */
	@Override
	public boolean close() {
		//javax.swing.JOptionPane.showMessageDialog(null, "Good bye World!");
		return true;
	}

	/**
	 * Overrides isSupported() method from Plugin class of Magic Draw OpenAPI.
	 * This method is used to check some specific conditions before loading the
	 * plug-in. 
	 * @see com.nomagic.magicdraw.plugins.Plugin#isSupported()
	 */
	@Override
	public boolean isSupported() {
		return true;
	}
	
	private void registerAndConfigureMainMenuAction() {
		ProjectEventListener listener = new ProjectEventListener() {
			@Override
			public void projectOpened(Project project) {
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
								//ModelHelpers.saveModel(dercsModel, saveTo + "dercs_model.bin");
								GraphUtil.createGraphFileFromModel(dercsModel, new File(saveTo + "dercs_model_graph.dot").toPath(), msg -> GenERTiCA.Log.println(msg, true));
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

			@Override
			public void projectClosed(Project project) {

			}

			@Override
			public void projectSaved(Project project, boolean b) {

			}

			@Override
			public void projectActivated(Project project) {

			}

			@Override
			public void projectDeActivated(Project project) {

			}

			@Override
			public void projectReplaced(Project project, Project project1) {

			}

			@Override
			public void projectCreated(Project project) {

			}

			@Override
			public void projectPreClosed(Project project) {

			}

			@Override
			public void projectPreClosedFinal(Project project) {

			}

			@Override
			public void projectPreSaved(Project project, boolean b) {

			}

			@Override
			public void projectPreActivated(Project project) {

			}

			@Override
			public void projectPreDeActivated(Project project) {

			}

			@Override
			public void projectPreReplaced(Project project, Project project1) {

			}

			@Override
			public void projectOpenedFromGUI(Project project) {

			}

			@Override
			public void projectActivatedFromGUI(Project project) {

			}
		};
		Application.getInstance().addProjectEventListener(listener);
		AMConfigurator conf = new AMConfigurator() {
			// instantiating main menu action
			private MainMenuAction m_MainMenuAction = new MainMenuAction("GenERTiCA", "GenERTiCA - Code Generation");
			public void configure(ActionsManager manager) {
				String s = ActionsID.GOTO;
				// searching for action after which insert should be done. 
				NMAction found = manager.getActionFor(s);
				// action found, inserting
				if (found != null) {
					ActionsCategory category = (ActionsCategory)manager.getActionParent(found);
					// get all actions from this category (menu)
					List actionsInCategory = category.getActions();
					// add action after "Metrics" action
					int i = actionsInCategory.indexOf(found);
					actionsInCategory.add(0, m_MainMenuAction);
					// set all actions
					category.setActions(actionsInCategory);
				}
			}
			public int getPriority() {
				return AMConfigurator.MEDIUM_PRIORITY;
			}
		};
		// registering the configurator
		ActionsConfiguratorsManager.getInstance().addMainMenuConfigurator(conf);
	}
	
}
