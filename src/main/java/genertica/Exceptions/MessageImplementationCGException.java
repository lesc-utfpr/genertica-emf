package genertica.Exceptions;

import dercs.structure.Method;

public class MessageImplementationCGException extends CodeGenerationException {
	private Method Meth;

	/**
	 * @param script
	 * @param meth
	 */
	public MessageImplementationCGException(Method meth, String script) {
		super(ERROR_004 + meth.getClass().getName() + "." + meth.getName(), script);
		Meth = meth;
	}
}
