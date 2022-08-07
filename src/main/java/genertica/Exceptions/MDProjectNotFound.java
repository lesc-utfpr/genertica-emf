package genertica.Exceptions;

public class MDProjectNotFound extends CodeGenerationException {
 
	public MDProjectNotFound(String fileName) {
		super(ERROR_005 + " [" + fileName + "]", false);
	}
}
