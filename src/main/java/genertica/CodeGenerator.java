package genertica;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import genertica.Exceptions.CodeGenerationException;

import genertica.util.DERCSHelper;
import dercs.AO.Aspect;
import dercs.AO.BehavioralAdaptation;
import dercs.AO.Crosscutting;
import dercs.AO.Pointcut;
import dercs.AO.RelativePosition;
import dercs.AO.StructuralAdaptation;
import dercs.behavior.Behavior;
import dercs.behavior.BehavioralElement;
import dercs.behavior.LocalVariable;
import dercs.behavior.actions.Action;
import dercs.behavior.actions.AssignmentAction;
import dercs.behavior.actions.CreateObjectAction;
import dercs.behavior.actions.DestroyObjectAction;
import dercs.behavior.actions.ExpressionAction;
import dercs.behavior.actions.GetArrayElementAction;
import dercs.behavior.actions.GetArrayLengthAction;
import dercs.behavior.actions.InsertArrayElementAction;
import dercs.behavior.actions.ModifyStateAction;
import dercs.behavior.actions.ObjectAction;
import dercs.behavior.actions.RemoveArrayElementAction;
import dercs.behavior.actions.ReturnAction;
import dercs.behavior.actions.SendMessageAction;
import dercs.behavior.actions.SetArrayElementAction;
import dercs.datatypes.DataType;
import dercs.datatypes.Enumeration;
import dercs.structure.Attribute;
import dercs.structure.BaseElement;
import dercs.structure.Method;
import dercs.structure.ParameterKind;
import dercs.structure.Visibility;
import dercs.structure.Class;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the source code fragment of a class. A class can contain:
 * <OL>
 * <LI>Class declaration;</LI>
 * <LI>Attributes declaration;</LI>
 * <LI>Methods declaration;</LI>
 * <LI>Methods implementation.</LI>
 * </OL>
 * <br>Additionally, this class has also helper methods to execute scripts in 
 * order to return the primary elements of the target language, such as data
 * types, visibility and parameter types.
 * @author marcow
 *
 */
public class CodeGenerator {

	private ApplicationMappingRules AppMappingRules;
	private VelocityContext VelocityCtx;
	private String PackageDeclaration;
	private String ClassDeclaration;
	private String ClassImplementation;
	private ArrayList<String> AttributesDeclaration = new ArrayList<String>();
	private ArrayList<String> MessagesDeclaration = new ArrayList<String>();
	private ArrayList<String> MessagesImplementation = new ArrayList<String>();
	
	// helper attribute to "remember" the code generator that the element been
	// used is affected by aspects
	private ArrayList<Pointcut> PointcutList = null;
	private ArrayList<Pointcut> PointcutListForAction = null;
	private String GeneratedCodeFragment = null;
	
	// log support
	protected Logger LOGGER;
	public static String CODE_GENERATION_ERROR_1000 = "Error-1000: Error in script execution.";
	public static String CODE_GENERATION_ERROR_1001 = "Error-1001: Error in script execution for DATA TYPE";
	public static String CODE_GENERATION_ERROR_1002 = "Error-1002: Error in script execution for ACTION";
	public static String CODE_GENERATION_ERROR_1003 = "Error-1003: Error in script execution for VARABLES DECLARATION";
	public static String CODE_GENERATION_ERROR_1004 = "Error-1004: Error in script execution for ATTRIBUTES DECLARATION";
	public static String CODE_GENERATION_ERROR_1005 = "Error-1005: Error in script execution for MESSAGES DECLARATION";
	public static String CODE_GENERATION_ERROR_1006 = "Error-1006: Error in script execution for MESSAGES IMPLEMENTATION";
	public static String CODE_GENERATION_ERROR_1007 = "Error-1007: Error in script execution for CLASSES DECLARATION";
	public static String CODE_GENERATION_ERROR_1008 = "Error-1008: Error in script execution for CLASSES IMPLEMENTATION";
	public static String CODE_GENERATION_ERROR_1009 = "Error-1009: Error in script execution for ASPECT ADAPTATION";
	public static String CODE_GENERATION_ERROR_1010 = "Error-1010: Error in the generation of the platform configuration";
	public static String CODE_GENERATION_ERROR_1011 = "Error-1011: Cannot create output directory.";
	public static String CODE_GENERATION_ERROR_1012 = "Error-1012: Error while importing external XML file.";
	public static String CODE_GENERATION_ERROR_1013 = "Error-1013: Error while importing XML file of mapping rules.";
	public static String CODE_GENERATION_ERROR_1014 = "Error-1014: Error in script execution for ENUMERATION DECLARATION";
	
	public CodeGenerator(ApplicationMappingRules amr, VelocityContext ctx) {
		AppMappingRules = amr;
		VelocityCtx = ctx;
		LOGGER = LoggerFactory.getLogger("GenERTiCA");
	}
	
	/*
	 * Prints an error message.
	 */
	private void printError(String error, Exception expt, String script) {
		LOGGER.error("\n\t   " + error, false);
		LOGGER.error("\t      " + expt.getMessage(), false);
		LOGGER.error("\t      -----", false);
		LOGGER.error(applyIdentation(script, "\t      "), false);
		LOGGER.error("\t      -----", false);
	}
	
	/**
	 * Return the source code for the declaration of the packages
	 * @return the declaration of the packages
	 */
	public String getPackageDeclaration() {
		return PackageDeclaration;
	}
	
	/**
	 * Return the source code for the declaration of the class
	 * @return the declaration of the class
	 */
	public String getClassDeclaration() {
		return ClassDeclaration;
	}

	/**
	 * Return the source code for the implementation of the class
	 * @return the implementation of the class
	 */
	public String getClassImplementation() {
		return ClassImplementation;
	}
	
	/**
	 * Get the string which represents the data types in the target language. 
	 * @param dt Source data type
	 * @return string representing the data type in the target language.
	 */
	public String getDataTypeStr(DataType dt) {
		VelocityCtx.put(CodeGenerationEngine.DATA_TYPE, dt);
		
		String script= "";
		StringWriter result = new StringWriter();
		
		if (dt instanceof dercs.datatypes.Array)
			script = AppMappingRules.getDataTypeArray();
		else if (dt instanceof dercs.datatypes.Boolean)
			script = AppMappingRules.getDataTypeBoolean();
		else if (dt instanceof dercs.datatypes.Byte)
			script = AppMappingRules.getDataTypeByte();
		else if (dt instanceof dercs.datatypes.Char)
			script = AppMappingRules.getDataTypeChar();
		else if (dt instanceof dercs.datatypes.ClassDataType)
			script = AppMappingRules.getDataTypeClass();
		else if (dt instanceof dercs.datatypes.DateTime)
			script = AppMappingRules.getDataTypeDateTime();
		else if (dt instanceof dercs.datatypes.Double)
			script = AppMappingRules.getDataTypeDouble();
		else if (dt instanceof dercs.datatypes.Enumeration)
			script = AppMappingRules.getDataTypeEnumeration();
		else if (dt instanceof dercs.datatypes.Float)
			script = AppMappingRules.getDataTypeFloat();
		else if (dt instanceof dercs.datatypes.Integer)
			script = AppMappingRules.getDataTypeInteger();
		else if (dt instanceof dercs.datatypes.Long)
			script = AppMappingRules.getDataTypeLong();
		else if (dt instanceof dercs.datatypes.Short)
			script = AppMappingRules.getDataTypeShort();
		else if (dt instanceof dercs.datatypes.String)
			script = AppMappingRules.getDataTypeString();
		else if (dt instanceof dercs.datatypes.Void)
			script = AppMappingRules.getDataTypeVoid();
		
		try {
			Velocity.evaluate(VelocityCtx, result, "DataType Script", script);
		} catch (Exception e) {
			printError(CODE_GENERATION_ERROR_1001, e, script);
			return "";
		}
		VelocityCtx.put(CodeGenerationEngine.DATA_TYPE, null);
		return result.toString();
	}
	
	/**
	 * Get the string which represents the parameters kind in the target language. 
	 * @param pk Source parameter kind
	 * @return string representing the parameter kind in the target language.
	 */
	public String getParameterKindStr(ParameterKind pk) {
		if (pk == dercs.structure.ParameterKind.IN)
			return AppMappingRules.getParameterIn();
		else if (pk == dercs.structure.ParameterKind.OUT)
			return AppMappingRules.getParameterInOut();
		else if (pk == dercs.structure.ParameterKind.INOUT)
			return AppMappingRules.getParameterOut();
		else
			return "";
	}
	
	/**
	 * Get the string which represents the visibility in the target language. 
	 * @param v Source visibility
	 * @return string representing the visibility in the target language.
	 */
	public String getVisibilityStr(Visibility v) {
		if (v == dercs.structure.Visibility.PRIVATE)
			return AppMappingRules.getVisibilityPrivate();
		else if (v == dercs.structure.Visibility.PROTECTED)
			return AppMappingRules.getVisibilityProtected();
		else if (v == dercs.structure.Visibility.PUBLIC)
			return AppMappingRules.getVisibilityPublic();
		else
			return "";
	}
	
	private String applyIdentation(String s, String ident) {
		String result = "\n" + ident;
		String x = new String(s);
		
		if (x.indexOf('\n') >= 0) {
			int i = x.indexOf('\n');
			result += x.substring(0, i);
			x = x.substring(i+1, x.length());
			i = x.indexOf('\n');
			while (i >= 0) {
				result += "\n" + ident + x.substring(0, i);
				x = x.substring(i+1, x.length());
				i = x.indexOf('\n');
			}
			x = "\n" + ident + x.substring(i+1);
			result += ident + x;
		}
		else
			result += x;
		
		return result;
	}
	
	public String getAttributesDeclaration(int identationLevel) {
		VelocityCtx.put(CodeGenerationEngine.IDENTATION_LEVEL, identationLevel);
		String ident = AppMappingRules.getOptions().getIdentationStr(identationLevel);
		String result = "";
		for(Iterator<String> it=AttributesDeclaration.iterator(); it.hasNext();) {
			if (result.compareTo("") != 0)
				result += "\n";
			result += applyIdentation(it.next(), ident);
		}
		return result;
	}
	
	public String getAttributeDeclaration(int identationLevel, int index) {
		if ((index >= 0) && (index < AttributesDeclaration.size()))
			return AttributesDeclaration.get(index);
		else
			return "";
	}
	
	public String getMessagesDeclaration(int identationLevel) {
		VelocityCtx.put(CodeGenerationEngine.IDENTATION_LEVEL, identationLevel);
		String ident = AppMappingRules.getOptions().getIdentationStr(identationLevel);
		String result = "";
		for(Iterator<String> it=MessagesDeclaration.iterator(); it.hasNext();) {
			String s = it.next();
			//if (result.compareTo("") != 0)
				//result += "\n";
			result += applyIdentation(s, ident);
		}
		return result;
	}
	
	public String getMessagesImplementation(int identationLevel) {
		VelocityCtx.put(CodeGenerationEngine.IDENTATION_LEVEL, identationLevel);
		String ident = AppMappingRules.getOptions().getIdentationStr(identationLevel);
		String result = "";
		for(Iterator<String> it=MessagesImplementation.iterator(); it.hasNext();) {
			String s = it.next();
			if (result.compareTo("") != 0)
				result += "\n";
			result += applyIdentation(s, ident);
		}
		return result;
	}
	
	public String getBehaviorCode(Behavior behavior) {
		return "";
	}
	
	public String getActionCode(BehavioralElement be) throws CodeGenerationException {
		// saving some variables, which could be changed by any script calling getActionCode() method
		Behavior behavior = (Behavior)VelocityCtx.get(CodeGenerationEngine.BEHAVIOR);
		Behavior loop = (Behavior)VelocityCtx.get(CodeGenerationEngine.LOOP);
		Behavior branch = (Behavior)VelocityCtx.get(CodeGenerationEngine.BRANCH);
		
		// separate pointcuts related to the action form the other ones
		ArrayList<Pointcut> pointcuts = null;
		if (PointcutListForAction != null) {
			int i = 0;
			// try to find the first pointcut related to the action
			for(; (i < PointcutListForAction.size()) && (!PointcutListForAction.get(i).checkElement(be)); i++);
			// add the pointcuts related to the action
			if (i < PointcutListForAction.size()) {
				pointcuts = new ArrayList<Pointcut>();
				for(; (i < PointcutListForAction.size()) && (PointcutListForAction.get(i).checkElement(be)); i++)
					pointcuts.add(PointcutListForAction.get(i));
			}
		}
		
		String script = "";
		if ((be instanceof AssignmentAction) && !(be instanceof ReturnAction)) {
			script = AppMappingRules.getBehaviorAssignment();
			AssignmentAction a = (AssignmentAction)be;
			VelocityCtx.put(CodeGenerationEngine.ACTION, a);
		}
		else if (be instanceof CreateObjectAction) {
			script = AppMappingRules.getBehaviorObjectCreation();
			CreateObjectAction coa = (CreateObjectAction)be;
			VelocityCtx.put(CodeGenerationEngine.ACTION, coa);
			//VelocityCtx.put(CodeGenerationEngine.OBJECT, a.getObject());
		}
		else if (be instanceof DestroyObjectAction) {
			script = AppMappingRules.getBehaviorObjectDestruction();
		}
		else if (be instanceof ExpressionAction) {
			script = AppMappingRules.getBehaviorExpression();
			ExpressionAction ea = (ExpressionAction)be;
			VelocityCtx.put(CodeGenerationEngine.ACTION, ea);
		}
		else if (be instanceof InsertArrayElementAction) {
			script = AppMappingRules.getBehaviorInsertArrayElement();
			InsertArrayElementAction iaa = (InsertArrayElementAction)be;
			VelocityCtx.put(CodeGenerationEngine.ACTION, iaa);
		}
		else if (be instanceof RemoveArrayElementAction) {
			script = AppMappingRules.getBehaviorRemoveArrayElement();
			RemoveArrayElementAction rae = (RemoveArrayElementAction)be;
			VelocityCtx.put(CodeGenerationEngine.ACTION, rae);
		}
		else if (be instanceof GetArrayElementAction) {
			script = AppMappingRules.getBehaviorGetArrayElement();
			GetArrayElementAction gaea = (GetArrayElementAction)be;
			VelocityCtx.put(CodeGenerationEngine.ACTION, gaea);
		}
		else if (be instanceof SetArrayElementAction) {
			script = AppMappingRules.getBehaviorSetArrayElement();
			SetArrayElementAction saea = (SetArrayElementAction)be;
			VelocityCtx.put(CodeGenerationEngine.ACTION, saea);
		}
		else if (be instanceof GetArrayLengthAction) {
			script = AppMappingRules.getBehaviorArrayLength();
			GetArrayLengthAction gal = (GetArrayLengthAction)be;
			VelocityCtx.put(CodeGenerationEngine.ACTION, gal);
		}
		else if (be instanceof ModifyStateAction) {
			script = AppMappingRules.getBehaviorStateChange();
		}
		else if (be instanceof ReturnAction) {
			script = AppMappingRules.getBehaviorReturn();
			ReturnAction ra = (ReturnAction)be;
			VelocityCtx.put(CodeGenerationEngine.ACTION, ra);
			// the string __DEFAULT_VALUE__ must be replaced by the default
			// value specified in the mapping rules file.
			if (ra.isAssignmentOfValue() 
				&& ra.getValue().equals(DataType.DEFAULT_VALUE)) {
				if ((ra.getMethod() == null) || (ra.getMethod().getReturnType() == null))
					throw new CodeGenerationException(script);
				ra.setValue(getDataTypeDefaultValueStr(ra.getMethod().getReturnType()));
			}
		}
		else if (be instanceof SendMessageAction) {
			SendMessageAction sma = (SendMessageAction)be;
			script = AppMappingRules.getBehaviorSendMessageLocalToSW();
			
			if (DERCSHelper.isRemoteSendMessageAction(sma))
				script = AppMappingRules.getBehaviorSendMessageRemoteToSW();
				
			VelocityCtx.put(CodeGenerationEngine.ACTION, sma);
		}
		else if (be instanceof Behavior) {
			Behavior b = (Behavior)be;
			
			// the behavior owning the branch/loop must be substituted
			VelocityCtx.internalRemove(CodeGenerationEngine.BEHAVIOR); 
			VelocityCtx.put(CodeGenerationEngine.BEHAVIOR, b);
			
			if (b.isBranch()) {
				// the behavior represents a branch in the actions flow
				script = AppMappingRules.getBehaviorBranch();
				VelocityCtx.remove(CodeGenerationEngine.BRANCH);
				VelocityCtx.put(CodeGenerationEngine.BRANCH, be);
			}
			else if ((b.isUnboundLoop()) || (b.isBoundLoop())) {
				// the behavior represents a repetition of actions
				script = AppMappingRules.getBehaviorLoop();
				VelocityCtx.remove(CodeGenerationEngine.LOOP);
				VelocityCtx.put(CodeGenerationEngine.LOOP, be);
			}
			else {
				throw new CodeGenerationException(script);
			}
		}
		
		// weave aspects that affect the model representation of the action
		weaveAspectAdaptationsAtModelLevel(be, pointcuts, true);

		StringWriter s = new StringWriter();
		try {
			Velocity.evaluate(VelocityCtx, s, "Action Script", script);
		} catch (Exception e) {
			printError(CODE_GENERATION_ERROR_1002, e, script);
		}
		
		// rollback the modification performed in the case of branch/loop behavioral elements
//		if (be instanceof Behavior) {
//			VelocityCtx.internalRemove(CodeGenerationEngine.BEHAVIOR); 
//			VelocityCtx.put(CodeGenerationEngine.BEHAVIOR, owner);
//			VelocityCtx.remove(CodeGenerationEngine.BRANCH);
//			VelocityCtx.remove(CodeGenerationEngine.LOOP);
//		}
		
		// restoring the context
		if (behavior != null) {
			VelocityCtx.internalRemove(CodeGenerationEngine.BEHAVIOR);
			VelocityCtx.put(CodeGenerationEngine.BEHAVIOR, behavior);
		}
		if (loop != null) {
			VelocityCtx.internalRemove(CodeGenerationEngine.LOOP);
			VelocityCtx.put(CodeGenerationEngine.LOOP, behavior);
		}
		if (branch != null) {
			VelocityCtx.internalRemove(CodeGenerationEngine.BRANCH);
			VelocityCtx.put(CodeGenerationEngine.BRANCH, branch);
		}
		
		// Assignment actions can change the $Action variable in the velocity
		// context. This happens when the assigned value is a result from 
		// other action. Thus, the $Action variable should be restored
		if (be instanceof AssignmentAction) {
			AssignmentAction a = (AssignmentAction)be;
			VelocityCtx.put(CodeGenerationEngine.ACTION, a);
		}
		
		String tmp = s.toString();
		
		// only behavioral adaptation can be wove in action code
		pointcuts = selectPointcuts(pointcuts, true, false, null, false, false);
		String result = weaveAspectAdaptations(be, tmp, "", pointcuts, false);
		
		return result;
	}
	
	private String getDataTypeDefaultValueStr(DataType dt) {
		if (dt instanceof dercs.datatypes.Array)
			return AppMappingRules.getDataTypeDefaultValueArray();
		else if (dt instanceof dercs.datatypes.Boolean)
			return AppMappingRules.getDataTypeDefaultValueBoolean();
		else if (dt instanceof dercs.datatypes.Byte)
			return AppMappingRules.getDataTypeDefaultValueByte();
		else if (dt instanceof dercs.datatypes.Char)
			return AppMappingRules.getDataTypeDefaultValueChar();
		else if (dt instanceof dercs.datatypes.ClassDataType)
			return AppMappingRules.getDataTypeDefaultValueClass();
		else if (dt instanceof dercs.datatypes.DateTime)
			return AppMappingRules.getDataTypeDefaultValueDateTime();
		else if (dt instanceof dercs.datatypes.Double)
			return AppMappingRules.getDataTypeDefaultValueDouble();
		else if (dt instanceof dercs.datatypes.Enumeration)
			return AppMappingRules.getDataTypeDefaultValueEnumeration();
		else if (dt instanceof dercs.datatypes.Float)
			return AppMappingRules.getDataTypeDefaultValueFloat();
		else if (dt instanceof dercs.datatypes.Integer)
			return AppMappingRules.getDataTypeDefaultValueInteger();
		else if (dt instanceof dercs.datatypes.Long)
			return AppMappingRules.getDataTypeDefaultValueLong();
		else if (dt instanceof dercs.datatypes.Short)
			return AppMappingRules.getDataTypeDefaultValueShort();
		else if (dt instanceof dercs.datatypes.String)
			return AppMappingRules.getDataTypeDefaultValueString();
		else if (dt instanceof dercs.datatypes.Void)
			return AppMappingRules.getDataTypeDefaultValueVoid();
		else
			return "";
	}
	
	public String getActionsCode(int identationLevel) throws CodeGenerationException {
		String ident = AppMappingRules.getOptions().getIdentationStr(identationLevel);
		String result = "";
		
//		weaveAspectAdaptationsAtModelLevel(PointcutList, true);
		
		Behavior tmpBehavior = (Behavior)VelocityCtx.get(CodeGenerationEngine.BEHAVIOR);
		if (tmpBehavior != null) {
			weaveAspectAdaptationsAtModelLevel(tmpBehavior, PointcutList, true);
			for(Iterator<BehavioralElement> ita=tmpBehavior.getBehavioralElements().iterator(); ita.hasNext();) {
				BehavioralElement be = ita.next();
				VelocityCtx.put(CodeGenerationEngine.IDENTATION_LEVEL, identationLevel);
				
				String s = getActionCode(be).trim();
				if (s.compareTo("") != 0) {
					if (result != "")
						result += "\n"; //+ ident;
					
					result += applyIdentation(s.toString(), ident);//ident + s.toString();
				}
			}
		}
		
		String tmp = result;
		// only behavioral adaptation can be wove into behavior code
		ArrayList<Pointcut> pointcuts = selectPointcuts(PointcutList, true, false, null, false, false);
		result = weaveAspectAdaptations(tmpBehavior, result, ident, pointcuts, false);
		// check if some modification was done in the generated code fragment
		if (tmp.trim().compareTo(result.trim()) != 0)
			result = applyIdentation(result, ident);
		
		return result;
	}
	
	public String getActionsCode(Behavior b, int identationLevel) throws CodeGenerationException {
		String ident = AppMappingRules.getOptions().getIdentationStr(identationLevel);
		String result = "";
		
		weaveAspectAdaptationsAtModelLevel(b, PointcutList, true);
		
		Behavior tmpBehavior = b;
		if (tmpBehavior != null) {
			// the previous version of this method considers only flat behaviors.
			// However, if "b" is a branch or a loop, the right script should be chosen.			
			if ((tmpBehavior.isBranch() 
					&& !tmpBehavior.getEnterCondition().trim().toUpperCase().equals("ELSE")) 
				|| tmpBehavior.isUnboundLoop() 
				|| tmpBehavior.isBoundLoop()) {
				VelocityCtx.put(CodeGenerationEngine.IDENTATION_LEVEL, identationLevel);

				String s = getActionCode(tmpBehavior).trim();
				if (s.compareTo("") != 0) {
					if (result != "")
						result += "\n"; //+ ident;

					result += applyIdentation(s.toString(), ident);//ident + s.toString();
				}
			}
			else {
				for(Iterator<BehavioralElement> ita=tmpBehavior.getBehavioralElements().iterator(); ita.hasNext();) {
					BehavioralElement be = ita.next();
					VelocityCtx.put(CodeGenerationEngine.IDENTATION_LEVEL, identationLevel);

					String s = getActionCode(be).trim();
					if (s.compareTo("") != 0) {
						if (result != "")
							result += "\n"; //+ ident;

						result += applyIdentation(s.toString(), ident);//ident + s.toString();
					}
				}
			}
		}
		
		String tmp = result;
		// only behavioral adaptation can be wove into behavior code
		ArrayList<Pointcut> pointcuts = selectPointcuts(PointcutList, true, false, null, false, false);
		result = weaveAspectAdaptations(b, result, ident, pointcuts, false);
		// check if some modification was done in the generated code fragment
		if (tmp.trim().compareTo(result.trim()) != 0)
			result = applyIdentation(result, ident);
		
		return result;
	}
	
	public String getVariableDeclaration(LocalVariable var) {
		VelocityCtx.put(CodeGenerationEngine.VARIABLE, var);
		VelocityCtx.put(CodeGenerationEngine.DATA_TYPE_STR, getDataTypeStr(var.getDataType()));
		
		weaveAspectAdaptationsAtModelLevel(var, PointcutList, true);
		
		StringWriter s = new StringWriter();
		try {
			Velocity.evaluate(VelocityCtx, s, "Variable Declaration Script", AppMappingRules.getVariableDeclaration());
		} catch (Exception e) {
			printError(CODE_GENERATION_ERROR_1003, e, AppMappingRules.getVariableDeclaration());
		}
		return s.toString();
	}
	
	public String getVariablesDeclaration(int identationLevel) {
		String ident = AppMappingRules.getOptions().getIdentationStr(identationLevel);
		String result = "";
		
//		weaveAspectAdaptationsAtModelLevel(PointcutList, true);
		
		Behavior tmpBehavior = (Behavior)VelocityCtx.get(CodeGenerationEngine.BEHAVIOR);
		if (tmpBehavior != null) {
			for(Iterator<LocalVariable> itv=tmpBehavior.getLocalVariables().iterator(); itv.hasNext();) {
				LocalVariable v = itv.next();
				VelocityCtx.put(CodeGenerationEngine.IDENTATION_LEVEL, identationLevel);
				
				String s = getVariableDeclaration(v);
				if (s.compareTo("") != 0) {
					if (result != "")
						result += "\n";
					result += /*ident +*/ s.toString();
				}
			}
		}
		// check if some modification was done in the generated code fragment
		if (result.trim().compareTo("") != 0)
			result = applyIdentation(result, ident);
		return result;
	}
	
	public String getVariablesDeclaration(Behavior b, int identationLevel) {
		String ident = AppMappingRules.getOptions().getIdentationStr(identationLevel);
		String result = "";
		
//		weaveAspectAdaptationsAtModelLevel(PointcutList, true);
		
		Behavior tmpBehavior = b;
		if (tmpBehavior != null) {
			for(Iterator<LocalVariable> itv=tmpBehavior.getLocalVariables().iterator(); itv.hasNext();) {
				LocalVariable v = itv.next();
				VelocityCtx.put(CodeGenerationEngine.IDENTATION_LEVEL, identationLevel);
				
				String s = getVariableDeclaration(v);
				if (s.compareTo("") != 0) {
					if (result != "")
						result += "\n";
					result += /*ident +*/ s.toString();
				}
			}
		}
		// check if some modification was done in the generated code fragment
		if (result.trim().compareTo("") != 0)
			result = applyIdentation(result, ident);
		return result;
	}
	
	public String getEnumerationDeclaration(Enumeration _enum, int identationLevel) {
		String ident = AppMappingRules.getOptions().getIdentationStr(identationLevel);
		String result = "";
		
		if (_enum != null) {
			VelocityCtx.put(CodeGenerationEngine.DATA_TYPE, _enum);
			StringWriter s = new StringWriter();
			try {
				Velocity.evaluate(VelocityCtx, s, "Enumeration Definition Script", AppMappingRules.getDataTypeEnumerationDefinition());
			} catch (Exception e) {
				printError(CODE_GENERATION_ERROR_1014, e, AppMappingRules.getDataTypeEnumerationDefinition());
			}
			VelocityCtx.put(CodeGenerationEngine.DATA_TYPE, null);
			result = s.toString();
		}
		// check if some modification was done in the generated code fragment
		if (result.trim().compareTo("") != 0)
			result = applyIdentation(result, ident);
		return result;
	}
	
	public String getEnumerationDeclaration(int identationLevel) {
		String ident = AppMappingRules.getOptions().getIdentationStr(identationLevel);
		String result = "";
		
		StringWriter s = new StringWriter();
		try {
			Velocity.evaluate(VelocityCtx, s, "Enumeration Definition Script", AppMappingRules.getDataTypeEnumerationDefinition());
		} catch (Exception e) {
			printError(CODE_GENERATION_ERROR_1014, e, AppMappingRules.getDataTypeEnumerationDefinition());
		}

		result = s.toString();

		// check if some modification was done in the generated code fragment
		if (result.trim().compareTo("") != 0)
			result = applyIdentation(result, ident);
		return result;
	}
	

	/**
	 * Return the last code fragment which was generated to an element.
	 * @param identationLevel Level of indentation to be applied in the code fragment.
	 * @return the code fragment.
	 */
	public String getGeneratedCodeFragment(int identationLevel) {
		return AppMappingRules.getOptions().getIdentationStr(identationLevel) + 
		       GeneratedCodeFragment;
	}
	
	/**
	 * Returns a string containing the amount of spaces for a given indentation level. 
	 * @param identationLevel Level of indentation to be considered.
	 * @return the string containing the amount of spaces according to the indentation level. 
	 */
	public String getIdentationSpace(int identationLevel) {
		return AppMappingRules.getOptions().getIdentationStr(identationLevel);
	}
	
	/* ************************************************************************
	 *           Methods used internally by the code generation tool          *
	 **************************************************************************/
	
	/*
	 * Generate code for an attribute using the correspondent script within
	 * the mapping rules in XML for the target platform. 
	 */
	protected boolean generateCodeForAttribute(Attribute attr, ArrayList<Pointcut> pointcuts) {
		VelocityCtx.put(CodeGenerationEngine.OPTIONS, AppMappingRules.getOptions());
		VelocityCtx.put(CodeGenerationEngine.ATTRIBUTE, attr);
		VelocityCtx.put(CodeGenerationEngine.DATA_TYPE_STR, getDataTypeStr(attr.getDataType()));
		VelocityCtx.put(CodeGenerationEngine.VISIBILITY_STR, getVisibilityStr(attr.getVisibility()));
		VelocityCtx.put(CodeGenerationEngine.CODE_GENERATOR, this);
		
		weaveAspectAdaptationsAtModelLevel(attr, pointcuts, true);
		
		StringWriter result = new StringWriter();
		try {
			Velocity.evaluate(VelocityCtx, result, "Attributes Declaration Script", AppMappingRules.getAttributeDeclaration());
		} catch (Exception e) {
			printError(CODE_GENERATION_ERROR_1004, e, AppMappingRules.getAttributeDeclaration());
			return false;
		}
		
		// weaving aspects
		GeneratedCodeFragment = result.toString();
		// weaving aspects
		if (pointcuts != null) {
			// allow the pointcuts list be accessed by method called inside
			// code generation scripts in the XML file, i.e. allow the aspect
			// weaving even in methods called inside scripts (e.g. getActionsCode())
			pointcuts = selectPointcuts(pointcuts, false, true, null, false, false);
			PointcutList = pointcuts;
			GeneratedCodeFragment = weaveAspectAdaptations(attr, GeneratedCodeFragment, "", pointcuts, false);
		}
		
		if (GeneratedCodeFragment.trim().compareTo("") != 0)
			AttributesDeclaration.add(GeneratedCodeFragment);
		return true;
	}
	
	/*
	 * Generate code for the declaration of a message using the correspondent 
	 * script within the mapping rules in XML for the target platform. 
	 */
	protected boolean generateCodeForMessageDeclaration(Method mth, ArrayList<Pointcut> pointcuts) {
		VelocityCtx.put(CodeGenerationEngine.OPTIONS, AppMappingRules.getOptions());
		VelocityCtx.put(CodeGenerationEngine.CODE_GENERATOR, this);
		VelocityCtx.put(CodeGenerationEngine.MESSAGE, mth);
		VelocityCtx.put(CodeGenerationEngine.RETURN_TYPE_STR, getDataTypeStr(mth.getReturnType()));
		VelocityCtx.put(CodeGenerationEngine.VISIBILITY_STR, getVisibilityStr(mth.getVisibility()));
		VelocityCtx.put(CodeGenerationEngine.BEHAVIOR, mth.getTriggeredBehavior());
		
		// save original list of pointcuts to be used by other methods
		ArrayList<Pointcut> originalPointcuts = null;
		if (pointcuts != null) {
			originalPointcuts = new ArrayList<Pointcut>();
			originalPointcuts.addAll(pointcuts);
		}
		
		GeneratedCodeFragment = "";
		PointcutList = null;
		PointcutListForAction = null;
		
		// separate pointcuts related to the method from those related to its actions
		if (pointcuts != null) {
			// try to find the pointcut related to any action
			for(int i=0; (i < pointcuts.size()); i++) {
				if (!pointcuts.get(i).checkElement(mth)) {
					if (PointcutListForAction == null)
						PointcutListForAction = new ArrayList<Pointcut>();
					PointcutListForAction.add(pointcuts.get(i));
				}
			}
			//remove the action related pointcust from the original list
			if (PointcutListForAction != null)
				pointcuts.removeAll(PointcutListForAction);
		}
		
		weaveAspectAdaptationsAtModelLevel(mth, pointcuts, true);
		
		StringWriter result = new StringWriter();
		try {
			Velocity.evaluate(VelocityCtx, result, "Messages Declaration Script", AppMappingRules.getMessageDeclaration());
		} catch (Exception e) {
			// merge actions related pointcuts 
			if (PointcutListForAction != null) {
				pointcuts.addAll(PointcutListForAction);
			}
			printError(CODE_GENERATION_ERROR_1005, e, AppMappingRules.getMessageDeclaration());
			return false;
		}
		
		// weaving aspects
		if (pointcuts != null) {
			// TODO implement aspect weaving for attributes
		}
		
		if (result.toString().trim().compareTo("") != 0)
			MessagesDeclaration.add(result.toString());
		
		// merge actions related pointcuts 
		if (PointcutListForAction != null) {
			pointcuts.addAll(PointcutListForAction);
		}
		return true;
	}
	
	/*
	 * Generate code for the implementation of a message using the correspondent 
	 * script within the mapping rules in XML for the target platform. 
	 */
	protected boolean generateCodeForMessageImplementation(Method mth, ArrayList<Pointcut> pointcuts) {
		VelocityCtx.put(CodeGenerationEngine.OPTIONS, AppMappingRules.getOptions());
		VelocityCtx.put(CodeGenerationEngine.CODE_GENERATOR, this);
		VelocityCtx.put(CodeGenerationEngine.MESSAGE, mth);
		VelocityCtx.put(CodeGenerationEngine.RETURN_TYPE_STR, getDataTypeStr(mth.getReturnType()));
		VelocityCtx.put(CodeGenerationEngine.VISIBILITY_STR, getVisibilityStr(mth.getVisibility()));
        // remove previously assigned behavior
		VelocityCtx.internalRemove(CodeGenerationEngine.BEHAVIOR); 
		VelocityCtx.put(CodeGenerationEngine.BEHAVIOR, mth.getTriggeredBehavior());
		
		//save original list of pointcuts to be used by other methods
		ArrayList<Pointcut> originalPointcuts = null;
		if (pointcuts != null) {
			originalPointcuts = new ArrayList<Pointcut>();
			originalPointcuts.addAll(pointcuts);
		}
		
		GeneratedCodeFragment = "";
		PointcutList = null;
		PointcutListForAction = null;
		
		// separate pointcuts related to the method from those related to its actions
		if (pointcuts != null) {
			// try to find the pointcut related to any action
			for(int i=0; (i < pointcuts.size()); i++) {
				// check the the method object or its behavior is selected by any join point
				if (!(pointcuts.get(i).checkElement(mth) 
					 || ((mth.getTriggeredBehavior() != null) 
						 && pointcuts.get(i).checkElement(mth.getTriggeredBehavior())))) {
					// if none of them is selected, the pointcut selects any action 
					// inside the behavior 
					if (PointcutListForAction == null)
						PointcutListForAction = new ArrayList<Pointcut>();
					PointcutListForAction.add(pointcuts.get(i));
				}
			}
			//remove the action related pointcust from the original list
			if (PointcutListForAction != null)
				pointcuts.removeAll(PointcutListForAction);
		}
		
		weaveAspectAdaptationsAtModelLevel(mth, pointcuts, true);
		
		PointcutList = pointcuts;
		StringWriter result = new StringWriter();
		try {
			Velocity.evaluate(VelocityCtx, result, "Messages Implementation Script", AppMappingRules.getMessageImplementation());			
		} catch (Exception e) {
			// merge actions related pointcuts 
			if (PointcutListForAction != null) {
				pointcuts.addAll(PointcutListForAction);
			}
			printError(CODE_GENERATION_ERROR_1006, e, AppMappingRules.getMessageImplementation());
			return false;
		}
		
		if (pointcuts != null) {
			// allow the pointcuts list be accessed by method called inside
			// code generation scripts in the XML file, i.e. allow the aspect
			// weaving even in methods called inside scripts (e.g. getActionsCode())
			PointcutList = pointcuts; 
		}

		if (result.toString().trim().compareTo("") != 0)
			MessagesImplementation.add(result.toString());
		
		// merge actions related pointcuts 
		if (PointcutListForAction != null) {
			pointcuts.addAll(PointcutListForAction);
		}
		return true;
	}
	
	/*
	 * Generate code for the declaration of the class
	 */
	protected boolean generateCodeForClassDeclaratation(Class _class, ArrayList<Pointcut> pointcuts) {
		VelocityCtx.put(CodeGenerationEngine.OPTIONS, AppMappingRules.getOptions());
		VelocityCtx.put(CodeGenerationEngine.CODE_GENERATOR, this);
		
		weaveAspectAdaptationsAtModelLevel(_class, pointcuts, true);
		
		StringWriter result = new StringWriter();
		try {
			Velocity.evaluate(VelocityCtx, result, "Class Declaration Script", AppMappingRules.getClassDeclaration());
		} catch (Exception e) {
			printError(CODE_GENERATION_ERROR_1007, e, AppMappingRules.getClassDeclaration()); 
			return false;
		}
		
		// weaving aspects
		if (pointcuts != null) {
			// TODO implement aspect weaving for attributes
		}
		
		if (result.toString().trim().compareTo("") != 0)
			ClassDeclaration = result.toString();
		return true;
	}
	
	protected boolean generateCodeForPackageDeclaratation() {
		VelocityCtx.put(CodeGenerationEngine.OPTIONS, AppMappingRules.getOptions());
		VelocityCtx.put(CodeGenerationEngine.CODE_GENERATOR, this);
		
		//weaveAspectAdaptationsAtModelLevel(_class, pointcuts, true);
		
		StringWriter result = new StringWriter();
		try {
			Velocity.evaluate(VelocityCtx, result, "Package Declaration Script", AppMappingRules.getPackageDeclaration());
		} catch (Exception e) {
			printError(CODE_GENERATION_ERROR_1007, e, AppMappingRules.getClassDeclaration()); 
			return false;
		}
		
		// weaving aspects
		//if (pointcuts != null) {
			// TODO implement aspect weaving for attributes
		//}
		
		if (result.toString().trim().compareTo("") != 0)
			PackageDeclaration = result.toString();
		return true;
	}
		
	
	/*
	 * Generate code for the implementation of the class
	 */
	protected boolean generateCodeForClassImplementation(Class _class, ArrayList<Pointcut> pointcuts) {
		VelocityCtx.put(CodeGenerationEngine.OPTIONS, AppMappingRules.getOptions());
		VelocityCtx.put(CodeGenerationEngine.CODE_GENERATOR, this);
		
		ArrayList<dercs.structure.runtime.Object> ol = DERCSHelper.getAllObjectsFromClass(_class.getOwner(), _class);
		if (ol.size() == 0) {
			ol.add(DERCSHelper.createDummyObject(_class));
		}
		VelocityCtx.put(CodeGenerationEngine.OBJECT, ol.get(0));
		
		weaveAspectAdaptationsAtModelLevel(_class, pointcuts, true);
		
		if (pointcuts != null) {
			// Separate "add features" structural adaptations from the other ones
			RelativePosition[] rp = {RelativePosition.ADD_NEW_FEATURE};
			ArrayList<Pointcut> tmpPointcuts = selectPointcuts(pointcuts, false, true, rp, false, true);
			if (tmpPointcuts.size() > 0) {
				GeneratedCodeFragment = "";
				GeneratedCodeFragment = weaveAspectAdaptations(_class, GeneratedCodeFragment, "", tmpPointcuts, false);
				AttributesDeclaration.add(GeneratedCodeFragment);
			}
		}
		
		StringWriter result = new StringWriter();
		try {
			Velocity.evaluate(VelocityCtx, result, "Class Implementation Script", AppMappingRules.getClassImplementation());
		} catch (Exception e) {
			printError(CODE_GENERATION_ERROR_1008, e, AppMappingRules.getClassImplementation()); 
			return false;
		}
		
		GeneratedCodeFragment = result.toString();
		// weaving aspects
		if (pointcuts != null) {
			// allow the pointcuts list be accessed by method called inside
			// code generation scripts in the XML file, i.e. allow the aspect
			// weaving even in methods called inside scripts (e.g. getActionsCode())
			pointcuts = selectPointcuts(pointcuts, false, true, null, false, false);
			PointcutList = pointcuts;
			GeneratedCodeFragment = weaveAspectAdaptations(_class, GeneratedCodeFragment, "", pointcuts, false);
		}
		
		if (GeneratedCodeFragment.trim().compareTo("") != 0)
			ClassImplementation = GeneratedCodeFragment;
		return true;
	}

	/*
	 * Weaves the aspect adaptation in the join points described in the pointcuts list.
	 * Parameters:
	 *      generatedCode - Code fragment that was just generated.
	 *      ident - Indentation string.
	 *      pointcuts - List of pointcuts to be evaluated.
	 */
	protected String weaveAspectAdaptations(BaseElement affectedElement, String generatedCode, 
			String ident, ArrayList<Pointcut> pointcuts, boolean modelWeaving) {
		String result = new String(generatedCode);
		// aspects weaving
		if (pointcuts != null) {
			ArrayList codeGenerationContext = new ArrayList();
			// try to apply the adaptations
			for(Iterator<Pointcut> itpc=pointcuts.iterator(); itpc.hasNext();) {
				Pointcut pc = itpc.next();
				
				if (pc.checkElement(affectedElement)) {
					BaseElement crossOwner = affectedElement;
					
					// saving variables registered in the Velocity engine
					codeGenerationContext.add(VelocityCtx.get(CodeGenerationEngine.REFERENCED_CLASS));
					codeGenerationContext.add(VelocityCtx.get(CodeGenerationEngine.CROSSCUTTING));
					codeGenerationContext.add(VelocityCtx.get(CodeGenerationEngine.ACTION));

					if (modelWeaving) {
						// look for the owner of the crosscuting information
						VelocityCtx.remove(CodeGenerationEngine.REFERENCED_CLASS);
					
						if (crossOwner instanceof AssignmentAction) {
							// assignment actions cannot have its model element modified. 
							// However, the right side could be one of the following action.
							// They can be modified. Thus this action is in fact the
							// affected element.
							AssignmentAction aa = (AssignmentAction)crossOwner;
							if (aa.isAssignmentOfActionResult()
								&& ((aa.getResultOfAction() instanceof SendMessageAction)
									|| (aa.getResultOfAction() instanceof ObjectAction))) {
								crossOwner = aa.getResultOfAction();
								VelocityCtx.remove(CodeGenerationEngine.ACTION);
								VelocityCtx.put(CodeGenerationEngine.ACTION, crossOwner);
							}
						}
						if (crossOwner instanceof SendMessageAction) {
							SendMessageAction sma = (SendMessageAction)crossOwner;
							// this line could generate an exception!
							crossOwner = ((dercs.structure.runtime.Object)sma.getToObject()).getInstanceOfClass();
							VelocityCtx.put(CodeGenerationEngine.REFERENCED_CLASS, crossOwner);
						}
						else if (crossOwner instanceof ObjectAction) {
							ObjectAction coa = (ObjectAction)crossOwner;
							// this line could generate an exception!
							crossOwner = coa.getRelatedObject().getInstanceOfClass();
							VelocityCtx.put(CodeGenerationEngine.REFERENCED_CLASS, crossOwner);
						}
						else if (crossOwner instanceof Attribute) {
							crossOwner = ((Attribute)crossOwner).getOwnerClass();
							VelocityCtx.put(CodeGenerationEngine.REFERENCED_CLASS, crossOwner);
						}
						else if (crossOwner instanceof Method) {
							crossOwner = ((Method)crossOwner).getOwnerClass();
							VelocityCtx.put(CodeGenerationEngine.REFERENCED_CLASS, crossOwner);
						}
					}
					
					// get the crosscuting information that the aspect 
					// inserts in the affected element
					Crosscutting crosscutting = null;
					Aspect aspect = pc.getAspectAdaptation().getOwner();
					for(int i = aspect.getCrosscutting().size()-1; (crosscutting == null) && (i >= 0); i--) {
						if (aspect.getCrosscutting().get(i).getAffectedElement() == crossOwner)
							crosscutting = aspect.getCrosscutting().get(i);
						else if (crossOwner instanceof Behavior) {
							Class cls = (Class)VelocityCtx.get(CodeGenerationEngine.REFERENCED_CLASS);
							if (cls ==  aspect.getCrosscutting().get(i).getAffectedElement())
								crosscutting = aspect.getCrosscutting().get(i);
						}
						else if (crossOwner instanceof AssignmentAction) { 
							AssignmentAction aa = (AssignmentAction)crossOwner;
							if (aa.isAssignmentOfActionResult()
								&& (aa.getResultOfAction() instanceof CreateObjectAction)) {
								CreateObjectAction coa = (CreateObjectAction)aa.getResultOfAction();
								if (aspect.getCrosscutting().get(i).getAffectedElement() == coa.getRelatedObject().getInstanceOfClass())
									crosscutting = aspect.getCrosscutting().get(i);
							}
						}
					}
					VelocityCtx.remove(CodeGenerationEngine.CROSSCUTTING);
					if (crosscutting != null)
						VelocityCtx.put(CodeGenerationEngine.CROSSCUTTING, crosscutting);
					
					GeneratedCodeFragment = result;
					String script = AppMappingRules.getAspectAdaptationScript(pc.getAspectAdaptation());

					if (script.trim().compareTo("") != 0) {
						// execute the adaptation script
						StringWriter s = new StringWriter();
						try {
							Velocity.evaluate(VelocityCtx, s, "Class Implementation Script", script);
						} catch (Exception e) {
							// it should not stop here!
							printError(CODE_GENERATION_ERROR_1008, e, script);
						}

						String ss = s.toString();
						if (ss.trim().compareTo("") != 0) {
							// verify the position to insert the adaptations
							if (pc.getRelativePosition() == RelativePosition.BEFORE) {
								result = ident + ss + "\n" + result;
							}
							else if (pc.getRelativePosition() == RelativePosition.AFTER) {
								if (result != "")
									result += "\n" + ident;
								result += ss;
							}
							else if (pc.getRelativePosition() == RelativePosition.AROUND) {
								// TODO CG-300 AROUND: talvez mudar a estrutura no XML com dois nos: antes e depois
								result = ss;
							}
							else if (pc.getRelativePosition() == RelativePosition.REPLACE) {
								result = ss;
							}
							else if (pc.getRelativePosition() == RelativePosition.ADD_NEW_FEATURE) {
								// pensar na forma de poder adicionar parametros a um metodo
								// Talvez isso nao seja possivel, porque implicaria em modificar
								// as acoes que chamam esse metodo.
								// **** PROBLEMA RESOLVIDO! Em principio... :-)
								if (result != "")
									result += "\n" + ident;
								result += ss;
							}
							else if (pc.getRelativePosition() == RelativePosition.MODIFY_STRUCTURE) {
								// TODO improve the way to perform the weaving of structural
								//      adaptations, e.g. to change the class inheritance of
								//      the type of an attribute.
								result = ss;
							}
						}
					}
					
					// restoring the context
					for(int i = codeGenerationContext.size()-1; i >= 0; i--) {
						String varName = "";
						switch (i) {
						case 0:
							varName = CodeGenerationEngine.REFERENCED_CLASS;
							break;
						case 1:
							varName = CodeGenerationEngine.CROSSCUTTING;
							break;
						case 2:
							varName = CodeGenerationEngine.ACTION;
							break;
						}
						if ((!varName.equals(""))
							&& (codeGenerationContext.get(i) != null)) {
							VelocityCtx.remove(varName);
							VelocityCtx.put(varName, codeGenerationContext.get(i));
						}
					}					
				}
			}
		}
//		if ((result.compareTo("") != 0) && (ident.compareTo("") != 0))
//			result = applyIdentation(result, ident);
		return result;
	}
	
	protected void weaveAspectAdaptationsAtModelLevel(BaseElement affectedElement, 
			ArrayList<Pointcut> pointcuts, boolean removeFromList) {
		ArrayList<Pointcut> tmp = selectPointcuts(pointcuts, true, true, null, true, removeFromList); 
		weaveAspectAdaptations(affectedElement, "", "", tmp, true);
	}
	
	/*
	 * Filter the a pointcut list according the options passed as parameters. A
	 * new list is returned with the selected pointcuts.
	 * Parameters:
	 *      pointcuts - Original list of pointcuts
	 *      behavioral - Indicate if beharioral adaptations should be selected or not
	 *      structural - Indicate if structutal adaptations should be selected or not
	 *      affectedPositions - Indicate the selection of a pointcut according the
	 *                          relative position where the adaptation is applied.
	 *                          Empty list indicates that all position should be considered
	 *      metaLevel - Indicate if only adatations that affect the meta-level 
	 *                  (i.e. DERCS model) should be selected.
	 *      removeFromOriginal - indicate if the selected pointcuts should be
	 *                           removed from the original list.
	 */
	protected ArrayList<Pointcut> selectPointcuts(ArrayList<Pointcut> pointcuts, boolean behavioral, 
			boolean structural, RelativePosition[] affectedPositions, boolean metaLevel, boolean removeFromOriginal) {
		if (pointcuts != null) {
			ArrayList<Pointcut> result = new ArrayList<Pointcut>();
			for(int i=0; i < pointcuts.size(); i++) {
				Pointcut pc = pointcuts.get(i);
				if (((behavioral && (pc.getAspectAdaptation() instanceof BehavioralAdaptation))
					|| (structural && (pc.getAspectAdaptation() instanceof StructuralAdaptation)))
					// select the pointcut if metaLevel is false or if metaLevel 
					// is true and the pointcut applies the adaptation at model level  
					&& (!metaLevel || (metaLevel && AppMappingRules.isAdaptationAtModelLevel(pc.getAspectAdaptation())))) {
					if (affectedPositions != null) {
						int j = 0;
						for(; (j < affectedPositions.length) && (pc.getRelativePosition() != affectedPositions[j]); j++);

						if ((affectedPositions.length == 0) || (j < affectedPositions.length)) {
							result.add(pc);
							if (removeFromOriginal) {
								pointcuts.remove(i);
								i--;
							}
						}
					}
					else {
						result.add(pc);
						if (removeFromOriginal) {
							pointcuts.remove(i);
							i--;
						}
					}
				}
			}
			return result;
		}
		else
			return null;
	}
	
	/**
	 * Display a message dialog for during the execution of a Velocity Script
	 * @param msg
	 */
	public static void showMessageDialog(Object msg) {
		JOptionPane.showMessageDialog(null, msg);
	}
	
	public static void showMessageDialog() {
		JOptionPane.showMessageDialog(null, "");
	}

}
