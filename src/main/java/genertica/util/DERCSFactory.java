package genertica.util;

import dercs.datatypes.Array;
import dercs.datatypes.Boolean;
import dercs.datatypes.Byte;
import dercs.datatypes.Char;
import dercs.datatypes.ClassDataType;
import dercs.datatypes.DataType;
import dercs.datatypes.DateTime;
import dercs.datatypes.Double;
import dercs.datatypes.Enumeration;
import dercs.datatypes.Float;
import dercs.datatypes.Integer;
import dercs.datatypes.Long;
import dercs.datatypes.Short;
import dercs.datatypes.String;
import dercs.datatypes.Void;
import dercs.structure.Class;
import dercs.structure.ParameterKind;
import dercs.structure.Visibility;
import dercs.util.DercsConstructors;

/**
 * Class used as factory to create instances of data type classes
 * @author Marco Aurelio Wehrmeister
 *
 */
public class DERCSFactory {

    /*
     * Data types
     */
    public static Array newArray(DataType dataType, java.lang.Integer size, java.lang.Integer lower, java.lang.Integer upper) {
        return DercsConstructors.newArray(dataType, size, lower, upper);
    }

    public static Boolean newBoolean() {
        return DercsConstructors.newBoolean();
    }

    public static ClassDataType newClass(dercs.structure.Class represent) {
        return DercsConstructors.newClassDataType(represent);
    }

    public static DateTime newDateTime() {
        return DercsConstructors.newDateTime();
    }

    public static Enumeration newEnumeration(java.lang.String name) {
        return DercsConstructors.newEnumeration(name);
    }

    public static Void newVoid() {
        return DercsConstructors.newVoid();
    }

    // Integer data types
    public static Byte newByte(boolean signal) {
        return DercsConstructors.newByte(signal);
    }

    public static Integer newInteger(boolean signal) {
        return DercsConstructors.newInteger(signal);
    }

    public static Long newLong(boolean signal) {
        return DercsConstructors.newLong(signal);
    }

    public static Short newShort(boolean signal) {
        return DercsConstructors.newShort(signal);
    }

    // Char data types
    public static Char newChar() {
        return DercsConstructors.newChar();
    }

    public static String newString(java.lang.Integer size) {
        return DercsConstructors.newString(size);
    }

    // Floating point data types
    public static Float newFloat() {
        return DercsConstructors.newFloat();
    }

    public static Double newDouble() {
        return DercsConstructors.newDouble();
    }

    /*
     * Visibility
     */
    public static Visibility getPrivate() {
        return Visibility.PRIVATE;
    }

    public static Visibility getProtected() {
        return Visibility.PROTECTED;
    }

    public static Visibility getPublic() {
        return Visibility.PUBLIC;
    }

    /*
     * Parameter kinds
     */
    public static ParameterKind getParameterIn() {
        return ParameterKind.IN;
    }

    public static ParameterKind getParameterOut() {
        return ParameterKind.OUT;
    }

    public static ParameterKind getParameterInOut() {
        return ParameterKind.INOUT;
    }

    /*
     * Class related elements
     */
    public static Class newClass(java.lang.String name, Class superClass, boolean isAbstract) {
        return DercsConstructors.newClass(name, superClass, isAbstract, null);
    }
}
