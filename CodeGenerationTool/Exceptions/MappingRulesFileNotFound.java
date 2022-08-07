package CodeGenerationTool.Exceptions;

public class MappingRulesFileNotFound extends CodeGenerationException {
	
	public MappingRulesFileNotFound(String fileName) {
		super(ERROR_006 + " [" + fileName + "]", false);
	}
}
