package genertica.Exceptions;

import dercs.structure.Method;

public class MessageDeclarationCGException extends CodeGenerationException {
	private Method Meth;

	/**
	 * @param script
	 * @param meth
	 */
	public MessageDeclarationCGException(Method meth, String script) {
		super(ERROR_003 + meth.getClass().getName() + "." + meth.getName(), script);
		Meth = meth;
	}
	
	
}
