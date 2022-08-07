package genertica.Exceptions;

public class CodeGenerationException extends Exception {
	
	// Code generation exceptions
	public static String ERROR_001 = "Error-001: An unexpected error occured in the script";
	public static String ERROR_002 = "Error-002: An error within the code generation for the attribute ";
	public static String ERROR_003 = "Error-003: An error within the code generation for the declaration of the message ";
	public static String ERROR_004 = "Error-004: An error within the code generation for the implementation of the message ";
	public static String ERROR_005 = "Error-005: Magic Draw project cannot be found.";
	public static String ERROR_006 = "Error-006: Mapping rules XML file cannot be found.";
	public static String ERROR_007 = "Error-007: Directory does not exist.";
	
	protected String Script;

	/**
	 * Creates an instance of CodeGenerationException class.
	 * @param msg Message of exception.
	 * @param generic Unused
	 */
	public CodeGenerationException(String msg, boolean generic) {
		super(msg);
	}
	
	/**
	 * Creates an instance of CodeGenerationException class.
	 * @param script Script wrote using the Velocity Template Language.
	 */
	public CodeGenerationException(String script) {
		super(ERROR_001 + "\n\n" + script);
		this.Script = script;
	}
	
	/**
	 * Creates an instance of CodeGenerationException class.
	 * @param msg Message to be appended before the script.
	 * @param script Script wrote using the Velocity Template Language.
	 */
	public CodeGenerationException(String msg, String script) {
		super(msg + "\n\n" + script );
		this.Script = script;
	}
}
