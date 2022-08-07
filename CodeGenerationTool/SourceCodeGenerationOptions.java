package CodeGenerationTool;

/**
 * This class represent the option used by the code generation tool in order
 * to produce the source code files.
 * @author Marco Aurelio Wehrmeister
 *
 */
public class SourceCodeGenerationOptions {
	// source code files
	private int ClassesPerFile = 1;
	private String DeclarationFileExt = "";
	private String ImplementionFileExt = "*.java";
	private int Identation = 5;
	
	// target language
	private boolean PackagesDeclaration = false;
	private boolean PackagePerClass = false;
	private boolean ClassesDeclaration = false;
	private String BlockStart = "{";
	private String BlockEnd = "}";
	private String PackageName = "packages";
	private boolean AspectLanguage = false;
	
	/**
	 * Indicate if the target implementation languange is an Aspect-Oriented
	 * Programming Language.
	 * @return TRUE if the target language is Aspect-Oriented, FALSE otherwise.
	 */
	public boolean isAspectLanguage() {
		return AspectLanguage;
	}
	/**
	 * Set the option indicating that the target language is an Aspect-Oriented
	 * Programming Language.
	 * @param aspectLanguage TRUE if the target language is Aspect-Oriented,
	 * FALSE otherwise
	 */
	public void isAspectLanguage(boolean aspectLanguage) {
		AspectLanguage = aspectLanguage;
	}
	/**
	 * Get the file extation for declaration files (e.g. header files from C++)
	 * @return the file extension
	 */
	public String getDeclarationFileExt() {
		return DeclarationFileExt;
	}
	/**
	 * Set the file extation for declaration files (e.g. header files from C++)
	 * @param declarationFileExt File extension
	 */
	public void setDeclarationFileExt(String declarationFileExt) {
		DeclarationFileExt = declarationFileExt;
	}
	/**
	 * Get the file extation for implementation files (e.g. implementation 
	 * (*.c++) files from C++).
	 * @return the file extension
	 */
	public String getImplementionFileExt() {
		return ImplementionFileExt;
	}
	/**
	 * Set the file extation for implementation files (e.g. implementation 
	 * (*.c++) files from C++).
	 * @param implementionFileExt File extension
	 */
	public void setImplementionFileExt(String implementionFileExt) {
		ImplementionFileExt = implementionFileExt;
	}
	/**
	 * Get the amount of spaces, which will be used as one level of identation.
	 * @return amount of spaces represent the identation.
	 */
	public int getIdentation() {
		return Identation;
	}
	/**
	 * Set the amount of spaces, which will be used as one level of identation.
	 * @param identation Number of spaces used by one level of identation.
	 */
	public void setIdentation(int identation) {
		Identation = identation;
	}
	
	public String getIdentationStr(int level) {
		String result = "";
		for(int i=0; i < (level*Identation); i++)
			result += " ";
		return result;
	}
	/**
	 * Get the string indicating the end of an action sequence block.
	 * @return the "end of block" string
	 */
	public String getBlockEnd() {
		return BlockEnd;
	}
	/**
	 * Set the string indicating the end of an action sequence block. An action
	 * sequence block can have no string representing the end of a block.
	 * @param blockEnd Indication of end of block
	 */
	public void setBlockEnd(String blockEnd) {
		BlockEnd = blockEnd;
	}
	/**
	 * Get the string indicating the start of an action sequence block. 
	 * @return the "start of block" string
	 */
	public String getBlockStart() {
		return BlockStart;
	}
	/**
	 * Set the string indicating the start of an action sequence block. An action
	 * sequence block can have no string representing the start of a block.
	 * @param blockStart the blockStart to set
	 */
	public void setBlockStart(String blockStart) {
		BlockStart = blockStart;
	}
	
	/**
	 * Get the string indicating the package name of project when it is not by class declarad. 
	 * @return the "package name" string
	 */
	public String getPackageName() {
		return PackageName;
	}
	/**
	 * Set the string indicating the package name of project when it is not by class declared.
	 * @param packageName the PackageName to set
	 */
	public void setPackageName(String packageName) {
		PackageName = packageName;
	}


	/**
	 * Indicates if the target language requires packages declaration.
	 * @return TRUE if the target language requires packages declaration, FALSE
	 * otherwise.
	 */
	public boolean hasPackagesDeclaration() {
		return PackagesDeclaration;
	}
	/**
	 * Set the option indication that the target language requires the declaration
	 * of packages.
	 * @param packagesDeclaration Indicates if the target language requires or not
	 * packages declaration.
	 */
	public void hasPackagesDeclaration(boolean packagesDeclaration) {
		PackagesDeclaration = packagesDeclaration;
	}	
	
	/**
	 * Indicates if the target language requires packages declaration per class.
	 * @return TRUE if the target language requires packages declaration per class, FALSE
	 * otherwise.
	 */
	public boolean isPackagePerClass() {
		return PackagePerClass;
	}
	/**
	 * Set the option indication that the target language requires the declaration
	 * of packages per class.
	 * @param packagePerClass Indicates if the target language requires or not
	 * packages declaration per class.
	 */
	public void isPackagePerClass(boolean packagePerClass) {
		PackagePerClass = packagePerClass;
	}
	
	
	/**
	 * Indicates if the target language requires classes declaration.
	 * @return TRUE if the target language requires classes declaration, FALSE
	 * otherwise.
	 */
	public boolean hasClassesDeclaration() {
		return ClassesDeclaration;
	}
	/**
	 * Set the option indication that the target language requires the declaration
	 * of classes.
	 * @param classesDeclaration Indicates if the target language requires or not
	 * classes declaration.
	 */
	public void hasClassesDeclaration(boolean classesDeclaration) {
		ClassesDeclaration = classesDeclaration;
	}
	/**
	 * Get the number of classes that should be inserted within a single source
	 * code file.
	 * @return Number of classes per file.
	 */
	public int getClassesPerFile() {
		return ClassesPerFile;
	}
	/**
	 * Set the number of classes that should be inserted within a single source
	 * code file.
	 * @param classesPerFile Number of classes per file.
	 */
	public void setClassesPerFile(int classesPerFile) {
		ClassesPerFile = classesPerFile;
	}
}
