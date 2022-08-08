package genertica;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents the generated source file. A source code file was 
 * divided in four parts:<br>
 * 1. Package declaration: indicates which package encompases the declared 
 * classes;<br>
 * 2. References declaration: specify the references for elements uses within 
 * the source code;<br>
 * 3. Classes declaration: declare classes members without considering their 
 * implementation. Used by target language which requires classes' declarations 
 * separated from classes' implementations, e.g. C++ header/source files);<br>
 * 4. Classes implementation: declare classes' members implementation (commonly
 * method implementations).
 * @author Marco Aurelio Wehrmeister
 *
 */
public class SourceCode {
	
	private String FileName;
	private ArrayList<String> PackageDeclaration = new ArrayList<String>();
	private ArrayList<String> ReferencesDeclaration = new ArrayList<String>();
	private ArrayList<String> ClassesDeclaration = new ArrayList<String>();
	private ArrayList<String> ClassesImplementation = new ArrayList<String>();
	
	public SourceCode(String fileName) {
		FileName = fileName;
	}

	/**
	 * Get the string representing the declarations of all classes which are
	 * contained into a source file.
	 * @return string representing all declarations of all classes
	 */
	public String getClassesDeclaration() {
		String result = "";
		for(Iterator<String> it=ClassesDeclaration.iterator(); it.hasNext();)
			result += it.next();
		return result;
	}

	/**
	 * Add a declaration of a class.
	 * @param classDeclaration New class declaration to be added.
	 */
	public void addClassDeclaration(String classDeclaration) {
		ClassesDeclaration.add(classDeclaration);
	}
	
	/**
	 * Get the list of class declarations.
	 * @return classes declarations list
	 */
	public ArrayList<String> getClassDeclarationList() {
		return ClassesDeclaration;
	}

	/**
	 * Get the string representing the implementation of all classes which are
	 * contained into a source file.
	 * @return string representing all implementations of all classes
	 */
	public String getClassesImplementation() {
		String result = "";
		for(Iterator<String> it=ClassesImplementation.iterator(); it.hasNext();)
			result += it.next();
		return result;
	}

	/**
	 * Add a declaration of a class.
	 * @param classImplementation New class declaration to be added.
	 */
	public void addClassImplementation(String classImplementation) {
		ClassesImplementation.add(classImplementation);
	}
	
	/**
	 * Get the list of class implementations
	 * @return classes implementations list
	 */
	public ArrayList<String> getClassImplementationList() {
		return ClassesImplementation;
	}

	/**
	 * Get the name of the source code file.
	 * @return the file name
	 */
	public String getFileName() {
		return FileName;
	}

	/**
	 * Set the name of the source code file.
	 * @param fileName File name of the source code
	 */
	public void setFileName(String fileName) {
		FileName = fileName;
	}

	/**
	 * Get the string representing the package declaration into a source file.
	 * @return the package declaration
	 */
	public String getPackagesDeclaration() {
		String result = "";
		for(Iterator<String> it=PackageDeclaration.iterator(); it.hasNext();)
			result += it.next();
		return result;
	}

	/**
	 * Add a package declaration.
	 * @param packageDeclaration New package declaration.
	 */
	public void addPackageDeclaration(String packageDeclaration) {
		PackageDeclaration.add(packageDeclaration);
	}
	
	/**
	 * Get the list of package declarations.
	 * @return package declarations list
	 */
	public ArrayList<String> getPackageDeclarationList() {
		return PackageDeclaration;
	}

	/**
	 * Get the string representing the references declaration into a source file.
	 * @return the references declaration
	 */
	public String getReferencesDeclaration() {
		String result = "";
		for(Iterator<String> it=ReferencesDeclaration.iterator(); it.hasNext();)
			result += it.next();
		return result;
	}

	/**
	 * Add a reference declaration.
	 * @param referenceDeclaration New reference declaration
	 */
	public void addReferenceDeclaration(String referenceDeclaration) {
		ReferencesDeclaration.add(referenceDeclaration);
	}
	
	/**
	 * Get the list of references declarations.
	 * @return references declarations list
	 */
	public ArrayList<String> getReferenceDeclarationList() {
		return PackageDeclaration;
	}
	
	/**
	 * Save the source code file into the specified directory.
	 * @param dirName Directory name into which the file will be saved.
	 */
	public void saveTo(String dirName) throws IOException {
		
	}
}
