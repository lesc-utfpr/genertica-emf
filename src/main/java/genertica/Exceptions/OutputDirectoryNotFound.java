package genertica.Exceptions;

public class OutputDirectoryNotFound extends CodeGenerationException {
	
	public OutputDirectoryNotFound(String dirName) {
		super(ERROR_007 + " [" + dirName + "]", false);
	}
}
