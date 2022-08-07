package genertica;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import dercs.datatypes.DataType;
import dercs.structure.ParameterKind;
import dercs.structure.Visibility;

/**
 * Represents the source code fragment of a class. A class can contain:
 * \n1.Class declaration;
 * \n2.Attributes declaration;
 * \n3. Methods declaration;
 * \n4. Methods implementation.
 * @author marcow
 *
 */
public class ClassCodeGenerationHelper {

	private ApplicationMappingRules AppMappingRules;
	private String ClassDeclaration;
	private ArrayList<String> AttributesDeclaration = new ArrayList<String>();
	private ArrayList<String> MessagesDeclaration = new ArrayList<String>();
	private ArrayList<String> MessagesImplementation = new ArrayList<String>();
	
	public ClassCodeGenerationHelper(ApplicationMappingRules amr) {
		AppMappingRules = amr;
	}
	
	public String getDataTypeStr(DataType dt) {
		VelocityContext ctx = new VelocityContext();
		ctx.put("DataType", dt);
		
		String script= "";
		StringWriter result = new StringWriter();
		StringReader sr = new StringReader(script);
		
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
			Velocity.evaluate(ctx, result, null, sr);
		} catch (Exception e) {
			return "";
		}
		return result.toString();
	}
	
	public String getParameterKindStr(ParameterKind pk) {
		return "";
	}
	
	public String getVisibilityStr(Visibility v) {
		return "";
	}
	

}
