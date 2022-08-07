package genertica.Exceptions;

import dercs.structure.Attribute;

public class AttributeCGException extends CodeGenerationException {
	private Attribute Attr;
	
	/**
	 * @param attr
	 * @param script
	 */
	public AttributeCGException(Attribute attr, String script) {
		super(ERROR_002 + attr.getClass().getName() + "." + attr.getName(), script);
		Attr = attr;
	}
}
