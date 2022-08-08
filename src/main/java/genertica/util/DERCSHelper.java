package genertica.util;

import java.util.*;

import dercs.AO.RelativePosition;
import dercs.Model;
import dercs.behavior.Behavior;
import dercs.behavior.BehavioralElement;
import dercs.behavior.LocalVariable;
import dercs.behavior.MessageSort;
import dercs.behavior.actions.AssignmentAction;
import dercs.behavior.actions.CreateObjectAction;
import dercs.behavior.actions.ExpressionAction;
import dercs.behavior.actions.GetArrayElementAction;
import dercs.behavior.actions.ReturnAction;
import dercs.behavior.actions.SendMessageAction;
import dercs.behavior.actions.SetArrayElementAction;
import dercs.datatypes.Array;
import dercs.datatypes.ClassDataType;
import dercs.datatypes.DataType;
import dercs.datatypes.Enumeration;
//import dercs.exceptions.DERCSLoaderException;
//import dercs.exceptions.InvalidRelativePosition;
import dercs.loader.util.DercsCreationHelper;
import dercs.structure.Attribute;
import dercs.structure.Class;
import dercs.structure.Constructor;
import dercs.structure.Destructor;
import dercs.structure.Method;
import dercs.structure.Parameter;
import dercs.structure.ParameterKind;
import dercs.structure.Visibility;
import dercs.structure.runtime.ActiveObject;
import dercs.structure.runtime.Object;
import dercs.structure.runtime.PassiveObject;
//import dercs.util.UMLMetaModelHelper;


/**
 * This class was intended to be a class with static methods, which can be used
 * in the VTL script in order to provide functionality that is not provided by
 * the VTL itself (e.g. cast checking)
 */
/**
 * @author marcow
 *
 */
public class DERCSHelper {

    /**
     * Test if the object is an instace of ActiveObject class.
     * @param o Object to be checked
     * @return TRUE if the object is an instance of ActiveObject class, FALSE otherwise.
     */
    public static boolean isActiveObject(Object o) {
        return o instanceof ActiveObject;
    }

    /**
     * Test if the object is an action representing the construction of an
     * active object.
     * @param o Object to be checked
     * @return TRUE if the object is an action representing the construction of
     * an active object, FALSE otherwise.
     */
    public static boolean isInstantiationOfActiveObject(Object o) {
        return (o instanceof CreateObjectAction)
                && (((CreateObjectAction)o).getRelatedObject() != null)
                && (((CreateObjectAction)o).getRelatedObject() instanceof ActiveObject);
    }

    /**
     * Test if the object is an action which assigns an instance of an active
     * object to a variable or attribute.
     * @param o Object to be checked
     * @return TRUE if the object is an action which assigns an instance of an
     * active object to a variable or attribute, FALSE otherwise.
     */
    public static boolean isAssignmentOfActiveObject(Object o) {
        return (o instanceof AssignmentAction)
                && ((AssignmentAction)o).isAssignmentOfActionResult()
                && isInstantiationOfActiveObject((Object) ((AssignmentAction)o).getResultOfAction());
    }

    /**
     * Test if the object is an instance of PassiveObject class.
     * @param o Object to be checked
     * @return TRUE if the object is an instance of PassiveObject class, FALSE otherwise.
     */
    public static boolean isPassiveObject(Object o) {
        return o instanceof PassiveObject;
    }

    /**
     * Test if the object is an instance of Constructor class.
     * @param o Object to be checked
     * @return TRUE if the object is an instance of Contructor class, FALSE otherwise.
     */
    public static boolean isConstructor(Object o) {
        return o instanceof Constructor;
    }

    /**
     * Test if the object is an instance of Destructor class.
     * @param o Object to be checked
     * @return TRUE if the object is an instance of Destructor class, FALSE otherwise.
     */
    public static boolean isDestructor(Object o) {
        return o instanceof Destructor;
    }

    /**
     * Test if the object is an instance of Destructor class.
     * @param o Object to be checked
     * @return TRUE if the object is an instance of Destructor class, FALSE otherwise.
     */
    public static boolean isNormalMethod(Object o) {
        return !((o instanceof Constructor) || (o instanceof Destructor));
    }

    /**
     * Test if the object is an instance of ClassDataType class.
     * @param o Object to be checked
     * @return TRUE if the object is an instance of ClassDataType class, FALSE otherwise.
     */
    public static boolean isClassDataType(Object o) {
        return o instanceof ClassDataType;
    }

    /**
     * Check if the class already has a constructor.
     * @param cl Class to be checked
     * @param createNew Indicate that a constructor must be created in the class cl.
     * @return The constructor of the class. If createNew is TRUE and a constructor
     * could not be found in the class cl, a new constructor is created is added
     * in the class.
     */
    public static Constructor getClassConstructor(Class cl, boolean createNew) {
        Constructor result = null;
        for(Iterator it=cl.getMethods().iterator(); (result == null) && it.hasNext();) {
            Method m = (Method)it.next();
            if (m instanceof Constructor)
                result = (Constructor)m;
        }
//        if (createNew && (result == null)) {
//            // creating a new constructor in the class
//            Behavior behav = new Behavior(null, null, null, 0);
//            result = cl.addConstructor(cl.getName(), false, behav);
//        }
        return result;
    }

    /**
     * Check if the class already has a destructor.
     * @param cl Class to be checked
     * @param createNew Indicate that a destructor must be created in the class cl.
     * @return The destructor of the class. If createNew is TRUE and a destructor
     * could not be found in the class cl, a new destructor is created is added
     * in the class.
     */
    public static Destructor getClassDestructor(Class cl, boolean createNew) {
        Destructor result = null;
        for(Iterator it=cl.getMethods().iterator(); (result == null) && it.hasNext();) {
            Method m = (Method)it.next();
            if (m instanceof Destructor)
                result = (Destructor)m;
        }
//        if (createNew && (result == null)) {
//            // creating a new constructor in the class
//            Behavior behav = new Behavior(null, null, null, 0);
//            result = cl.addDestructor(cl.getName(), false, behav);
//        }
        return result;
    }

//    /**
//     * Create a get method for one attribute in the class.
//     * @param cl Class where the new method should be created
//     * @param attr Attribute that is returned by the method.
//     * @return The created method.
//     */
//    public static Method addGetMethod(Class cl, Attribute attr) {
//        Method result = null;
//
//        // create the method and its behavior
//        Behavior b = new Behavior(null, null, null, 0);
//        result = cl.addMethod("get" + attr.getName(), attr.getDataType(),
//                Visibility.PUBLIC, false, false, b);
//        result.setAssociatedAttribute(attr);
//
//        ExpressionAction ea = new ExpressionAction(attr.getName());
//        // return the new attribute
//        ReturnAction ra = new ReturnAction(result, ea);
//        b.addBehavioralElement(ra);
//
//        return result;
//    }
//
//    /**
//     * Create a get method for one attribute in the class.
//     * @param cl Class where the new method should be created
//     * @param attr Attribute that is returned by the method.
//     * @param isArray Indicates if the get method should have an index parameter.
//     * @return The created method.
//     */
//    public static Method addGetMethod(Class cl, Attribute attr, boolean isArray) {
//        Method result = null;
//        // create the method and its behavior
//        Behavior b = new Behavior(null, null, null, 0);
//        result = cl.addMethod("get" + attr.getName(),
//                ((Array)attr.getDataType()).getDataType(),
//                Visibility.PUBLIC, false, false, b);
//        result.setAssociatedAttribute(attr);
//        // check if the attribute is really an array
//        if (isArray && (attr.getDataType() instanceof Array)) {
//            // adding the parameter representing the index of the array element
//            Parameter p = result.addParameter("index", DERCSFactory.newInteger(true),
//                    ParameterKind.IN);
//            // adding index to the array element
//            GetArrayElementAction gaea = new GetArrayElementAction(null, attr, p.getName());
//            // return the new attribute
//            ReturnAction ra = new ReturnAction(result, gaea);
//            b.addBehavioralElement(ra);
//        }
//        return result;
//    }
//
//    /**
//     * Create a set method for one attribute in the class.
//     * @param cl Class where the new method should be created
//     * @param attr Attribute that is set.
//     * @return The created method.
//     */
//    public static Method addSetMethod(Class cl, Attribute attr) {
//        Method result = null;
//
//        // create the method and its behavior
//        Behavior b = new Behavior(null, null, null, 0);
//        result = cl.addMethod("set" + attr.getName(), DERCSFactory.newVoid(), Visibility.PUBLIC, false, false, b);
//        Parameter p = result.addParameter("_" + attr.getName(), attr.getDataType(), ParameterKind.IN);
//        result.setAssociatedAttribute(attr);
//
//        ExpressionAction ea = new ExpressionAction(p.getName());
//        // adding assignment action
//        AssignmentAction aa = new AssignmentAction(null, attr, ea);
//        b.addBehavioralElement(aa);
//
//        return result;
//    }
//
//    /**
//     * Create a set method for one attribute in the class.
//     * @param cl Class where the new method should be created
//     * @param attr Attribute that is set.
//     * @param isArray Indicates if the get method should have an index parameter.
//     * @return The created method.
//     */
//    public static Method addSetMethod(Class cl, Attribute attr, boolean isArray) {
//        Method result = null;
//
//        // create the method and its behavior
//        Behavior b = new Behavior(null, null, null, 0);
//        result = cl.addMethod("set" + attr.getName(), DERCSFactory.newVoid(), Visibility.PUBLIC, false, false, b);
//        Parameter pIndex = null;
//        result.setAssociatedAttribute(attr);
//        // adding parameters
//        if (isArray && (attr.getDataType() instanceof Array)) {
//            // adding the parameter representing the index of the array element
//            pIndex = result.addParameter("index", DERCSFactory.newInteger(true), ParameterKind.IN);
//        }
//        Parameter pValue = result.addParameter("_" + attr.getName(), ((Array)attr.getDataType()).getDataType(), ParameterKind.IN);
//        if (isArray && (attr.getDataType() instanceof Array)) {
//            // adding index to the array element
//            SetArrayElementAction saea = new SetArrayElementAction(null, attr, pIndex.getName(), pValue.getName());
//            //  adding assignment action
//            AssignmentAction aa = new AssignmentAction(null, attr, saea);
//            b.addBehavioralElement(aa);
//        }
//        return result;
//    }
//
//    /**
//     * Add a parameter in a method that represents an attribute of the class.
//     * It also adds an assigment action that assigns the parameter with the
//     * attribute.
//     * @param cl Class the owns the attribute and the method.
//     * @param attr Attribute that is affected.
//     * @param meth Method that receives the new attribute.
//     * @param initPos The position where the action is inserted. TRUE inserts at
//     * the action at the beginning of the behavior, FALSE inserts at the end.
//     */
//    public static Parameter addParameterFromAttribute(Class cl, Attribute attr,
//                                                      Method meth, boolean initPos) {
//        // the attribute is not an array, this is the simplest situation
//        Parameter p = meth.addParameter("_" + attr.getName(), attr.getDataType(), ParameterKind.IN);
//        AssignmentAction aa = new AssignmentAction(null, attr, p);
//        if (initPos)
//            meth.getTriggeredBehavior().getBeharioralElements().add(0, aa);
//        else
//            meth.getTriggeredBehavior().addBehavioralElement(aa);
//        return p;
//    }
//
//    /**
//     * Create an instance of Object class that is related to an attribute or variable.
//     * @param attr Attribute that represents the object being created.
//     * @param createAssociation Indicate whether the attribute should be associated with the object and vice-versa.
//     * @return The created object instance.
//     */
//    public static dercs.structure.runtime.Object createObjectRelatedTo(Attribute attr,
//                                                                       boolean createAssociation) {
//        dercs.structure.runtime.Object result = null;
//        String objName = attr.getOwnerClass().getName() + "~" + attr.getName();
//        Class cl = ((ClassDataType)attr.getDataType()).getRepresent();
//        if (cl.isActiveClass())
//            result = new ActiveObject(objName, cl, null);
//        else if (cl.isPassiveClass())
//            result = new PassiveObject(objName, cl);
//        else
//            result = new dercs.structure.runtime.Object(objName, cl);
//
//        if (createAssociation) {
//            attr.setAssociatedElement(result);
//            result.setAssociatedElement(attr);
//        }
//
//        return result;
//    }
//
//    /**
//     * Create an instance of Object class that is related to an attribute or variable.
//     * @param var Variable that represents the object being created.
//     * @param createAssociation Indicate whether the variable should be associated with the object and vice-versa.
//     * @return The created object instance.
//     */
//    public static dercs.structure.runtime.Object createObjectRelatedTo(LocalVariable var,
//                                                                       boolean createAssociation) {
//        dercs.structure.runtime.Object result = null;
//        String objName = "VAR~" + var.getName();
//        Class cl = ((ClassDataType)var.getDataType()).getRepresent();
//        if (cl.isActiveClass())
//            result = new ActiveObject(objName, cl, null);
//        else if (cl.isPassiveClass())
//            result = new PassiveObject(objName, cl);
//        else
//            result = new dercs.structure.runtime.Object(objName, cl);
//
//        if (createAssociation) {
//            var.setAssociatedElement(result);
//            result.setAssociatedElement(var);
//        }
//
//        return result;
//    }
//
//    /**
//     * Create an instance of Object class that is related to an array (attribute
//     * or variable) element
//     * @param attr Attribute that represents the object being created.
//     * @param index Index of the array element representing the object
//     * @param createAssociation Indicate whether the attribute should be associated with the object and vice-versa.
//     * @return The created object instance.
//     */
//    public static dercs.structure.runtime.Object createObjectRelatedTo(Attribute attr,
//                                                                       String index, boolean createAssociation) {
//        dercs.structure.runtime.Object result = null;
//        String objName = attr.getOwnerClass().getName() + "~" + attr.getName() + "[" + index + "]";
//        Class cl = ((ClassDataType)(((Array)attr.getDataType()).getDataType())).getRepresent();
//        if (cl.isActiveClass())
//            result = new ActiveObject(objName, cl, null);
//        else if (cl.isPassiveClass())
//            result = new PassiveObject(objName, cl);
//        else
//            result = new dercs.structure.runtime.Object(objName, cl);
//
//        if (createAssociation) {
//            attr.setAssociatedElement(result);
//            result.setAssociatedElement(attr);
//        }
//
//        return result;
//    }
//
//    /**
//     * Create an instance of Object class that is related to an array (attribute
//     * or variable) element
//     * @param var Variable that represents the object being created.
//     * @param index Index of the array element representing the object
//     * @param createAssociation Indicate whether the variable should be associated with the object and vice-versa.
//     * @return The created object instance.
//     */
//    public static dercs.structure.runtime.Object createObjectRelatedTo(LocalVariable var,
//                                                                       String index, boolean createAssociation) {
//        dercs.structure.runtime.Object result = null;
//        String objName = "VAR~" + var.getName() + "[" + index + "]";
//        Class cl = ((ClassDataType)(((Array)var.getDataType()).getDataType())).getRepresent();
//        if (cl.isActiveClass())
//            result = new ActiveObject(objName, cl, null);
//        else if (cl.isPassiveClass())
//            result = new PassiveObject(objName, cl);
//        else
//            result = new dercs.structure.runtime.Object(objName, cl);
//
//        if (createAssociation) {
//            var.setAssociatedElement(result);
//            result.setAssociatedElement(var);
//        }
//
//        return result;
//    }

    /**
     * Create a dummy object from a specific class.
     * @param cl Class from which the object is created
     * @return The object.
     */
    public static dercs.structure.runtime.Object createDummyObject(Class cl) {
        return DercsCreationHelper.createDummyObject(cl);
    }

    /**
     * Check if the object is a dummy object or not.
     * @param obj Object to be tested
     * @return TRUE if the object is dummy, FALSE otherwise.
     */
    public static boolean isDummyObject(dercs.structure.runtime.Object obj) {
        return obj.getName().compareTo("dummy") == 0;
    }

    /**
     * Get the object that is associated with a given attribute.
     * @param attr Attribute which is associated with a object
     * @param model DERCS model that owns the all objects
     * @return The object found, or NULL when there is no object associated with the attribute
     */
    public static dercs.structure.runtime.Object getObjectRelatedTo(Attribute attr, Model model) {
        return searchForObject(attr.getOwnerClass().getName() + "~" + attr.getName(), model.getObjects(), false);
    }

    /**
     * Get the object that is associated with a given local variable.
     * @param var Local variable which is associated with a object
     * @param model DERCS model that owns the all objects
     * @return The object found, or NULL when there is no object associated with the variable.
     */
    public static dercs.structure.runtime.Object getObjectRelatedTo(LocalVariable var, Model model) {
        return searchForObject("VAR~" + var.getName(), model.getObjects(), false);
    }

    /**
     * Search for an object using its name.
     * @param objName Name of the object
     * @param model DERCS model that owns the all objects
     * @return The object found, or NULL when there is no object with the given name.
     */
    public static dercs.structure.runtime.Object searchForObject(String objName,
                                                                 List<Object> objList, boolean partialMatch) {
        int i=0;
        for(; (i < objList.size()) &&
                (((partialMatch == false) && (objList.get(i).getName().compareTo(objName) != 0))
                        || ((partialMatch == true) && (objList.get(i).getName().indexOf(objName) < 0))); i++);
        if (i < objList.size())
            return objList.get(i);
        else
            return null;
    }

//    /**
//     * Get the relative position value whose name is equal to the string passed as paramenter.
//     * @param relativePostion Relative postion name.
//     * @return The relative position.
//     */
//    public static RelativePosition getRelativePosition(String relativePostion) throws DERCSLoaderException {
//        if (relativePostion.compareTo(RelativePosition.ADD_NEW_FEATURE.toString()) == 0)
//            return RelativePosition.ADD_NEW_FEATURE;
//        else if (relativePostion.compareTo(RelativePosition.AFTER.toString()) == 0)
//            return RelativePosition.AFTER;
//        else if (relativePostion.compareTo(RelativePosition.AROUND.toString()) == 0)
//            return RelativePosition.AROUND;
//        else if (relativePostion.compareTo(RelativePosition.BEFORE.toString()) == 0)
//            return RelativePosition.BEFORE;
//        else if (relativePostion.compareTo(RelativePosition.MODIFY_STRUCTURE.toString()) == 0)
//            return RelativePosition.MODIFY_STRUCTURE;
//        else if (relativePostion.compareTo(RelativePosition.REPLACE.toString()) == 0)
//            return RelativePosition.REPLACE;
//        else
//            throw new InvalidRelativePosition(relativePostion);
//    }

    /**
     * Test if the object is an instance of AssignmentAction class.
     * @param o Object to be checked
     * @return TRUE if the object is an instance of AssignmentAction class, FALSE otherwise.
     */
    public static boolean isAssignmentAction(Object o) {
        return (o instanceof AssignmentAction);
    }

    /**
     * Changes the super class of a class. Besides changing the SuperClass
     * attribute this method also resolves the inheritance problem of missing
     * methods and attributes, i.e. all attributes and methods from all classes
     * in inheritance tree are copied to this class.
     * @param cl Class whose super class is to be changed
     * @param newSuper New super class for <i>cl</i>
     * @param copyMethods Indicate whether the inherited features must be copied.
     */
    public static void changeSuperClass(Class cl, Class newSuper, boolean copyMethods) {
        if (copyMethods)
            performInheritanceFeaturesCopy(cl);
        cl.setSuperClass(newSuper);
    }

    /**
     * Performs a copy of all missing features of the class that come from other
     * classes in the inheritance tree.
     * @param cl Class that copies inherited features
     */
    public static void performInheritanceFeaturesCopy(Class cl) {
        Class tmp = cl.getSuperClass();
        while (tmp != null) {
            // copying attributes
            int i = 0;
            for(i = tmp.getAttributes().size()-1; i >= 0; i--) {
                Attribute attr = tmp.getAttributes().get(i);
                if (cl.getAttribute(attr.getName()) == null)
                    cl.getAttributes().add(0, attr);
            }
            // copying methods
            for(i = tmp.getMethods().size()-1; i >= 0; i--) {
                Method mth = tmp.getMethods().get(i);
                if (cl.getMethod(mth) == null) {
                    if ((mth instanceof Constructor)
                            || (mth instanceof Destructor)){
                        Behavior b = null;
                        if (mth instanceof Constructor)
                            b = getClassConstructor(cl, true).getTriggeredBehavior();
                        else
                            b = getClassDestructor(cl, true).getTriggeredBehavior();
                        // copying actions
                        for(Iterator<BehavioralElement> it = mth.getTriggeredBehavior().getBehavioralElements().iterator(); it.hasNext();)
                            b.getBehavioralElements().add(it.next());
                    }
                    else
                        cl.getMethods().add(0, mth);
                }
                // TODO resolve inheritance problem in methods behavior

                // REMARK: instead of copying the Method object, it
                // should be cloned, and the clone method be added in the class
            }
            tmp = tmp.getSuperClass();
        }
    }

    /**
     * Converts a string representing a time value in a integer number.
     * @param time String to be converted, whose format is "timeValue unit", where
     * <b>timeValue</b> is a number representing the time value; and
     * <b>unit</b> indicates the time unit (optional), which can be <i>h</i>, <i>m</i>,
     * <i>s</i>, <i>ms</i>, <i>us</i>, <i>ns</i>, representing, respectively,
     * hour, minute, second, millisecond, microsecond, and nanosecond.
     * @param toTimeUnit Time unit to which the time must be converted.
     * @return The integer representing the time value.
     */
    public static String strTimeToInteger(String time, String toTimeUnit) {
        long result = -1;
        if ((time != null) && !time.trim().equals("")) {
            String timeValue = "";
            String timeUnit = "";

            time = time.trim();
            if (time.indexOf(" ") >= 0) {
                timeValue = time.substring(0, time.indexOf(" ")).trim();
                timeUnit = time.substring(time.indexOf(" ")+1).trim().toUpperCase();
            }
            else {
                boolean exit = false;
                int i;
                for(i = time.length()-1; (i >= 0) && !exit;) {
                    try {
                        int x = Integer.parseInt(time.substring(i, i+1));
                        exit = true;
                    } catch (NumberFormatException e) {
                        exit = false;
                        i--;
                    }
                }
                if ((i >= 0) && exit) {
                    timeValue = time.substring(0, i+1).trim();
                    timeUnit = time.substring(i+1).trim().toUpperCase();
                }
                else {
                    timeValue = time;
                    timeUnit = "";
                }
            }

            result = Integer.parseInt(timeValue) * getTimeConvertionFactor(timeUnit, toTimeUnit);
        }
        return String.valueOf(result);
    }

    private static long getTimeConvertionFactor(String from, String to) {
        from = from.toUpperCase();
        to = to.toUpperCase();
        long result = -1;
        if (from.equals("H")) {
            if (to.equals("H")) {
                result =  1;
            }
            else if (to.equals("M")) {
                result =  60;
            }
            else if (to.equals("S")) {
                result =  3600;
            }
            else if (to.equals("MS")) {
                result =  3600000;
            }
            else if (to.equals("US")) {
                result =  3600000*1000;
            }
            else if (to.equals("NS")) {
                result =  3600000*1000000;
            }
            else
                result =  -1;
        }
        else if (from.equals("M")) {
            if (to.equals("H")) {
                result =  60;
            }
            else if (to.equals("M")) {
                result =  1;
            }
            else if (to.equals("S")) {
                result =  60;
            }
            else if (to.equals("MS")) {
                result =  60000;
            }
            else if (to.equals("US")) {
                result =  60000000;
            }
            else if (to.equals("NS")) {
                result =  60000000*1000;
            }
            else
                result =  -1;
        }
        else if (from.equals("S")) {
            if (to.equals("H")) {
                result =  3600;
            }
            else if (to.equals("M")) {
                result =  60;
            }
            else if (to.equals("S")) {
                result =  1;
            }
            else if (to.equals("MS")) {
                result =  1000;
            }
            else if (to.equals("US")) {
                result =  1000000;
            }
            else if (to.equals("NS")) {
                result =  1000000000;
            }
            else
                result =  -1;
        }
        else if (from.equals("MS")) {
            if (to.equals("H")) {
                result =  3600000;
            }
            else if (to.equals("M")) {
                result =  60000;
            }
            else if (to.equals("S")) {
                result =  1000;
            }
            else if (to.equals("MS")) {
                result =  1;
            }
            else if (to.equals("US")) {
                result =  1000;
            }
            else if (to.equals("NS")) {
                result =  1000000;
            }
            else
                result =  -1;
        }
        else if (from.equals("US")) {
            if (to.equals("H")) {
                result =  3600000*1000;
            }
            else if (to.equals("M")) {
                result =  60000000;
            }
            else if (to.equals("S")) {
                result =  1000000;
            }
            else if (to.equals("MS")) {
                result =  1000;
            }
            else if (to.equals("US")) {
                result =  1;
            }
            else if (to.equals("NS")) {
                result =  1000;
            }
            else
                result =  -1;
        }
        else if (from.equals("NS")) {
            if (to.equals("H")) {
                result =  3600000*1000000;
            }
            else if (to.equals("M")) {
                result =  60000000*1000;
            }
            else if (to.equals("S")) {
                result =  1000000000;
            }
            else if (to.equals("MS")) {
                result =  1000000;
            }
            else if (to.equals("US")) {
                result =  1000;
            }
            else if (to.equals("NS")) {
                result =  1;
            }
            else
                result =  -1;
        }
        return result;
    }

//    public static HashSet<Enumeration> getListOfEnumerationsUsedInClass(Class cl) {
//        HashSet<Enumeration> result = new HashSet<Enumeration>();
//
//        for(Iterator<Attribute> ita = cl.getAttributesIterator(); ita.hasNext();) {
//            Attribute attr = ita.next();
//            if (attr.getDataType() instanceof Enumeration)
//                result.add((Enumeration)attr.getDataType());
//        }
//        for(Iterator<Method> itm = cl.getMethodsIterator(); itm.hasNext();) {
//            Method mth = itm.next();
//            for(Iterator<Parameter> itp = mth.getParametersIterator(); itp.hasNext();) {
//                Parameter par = itp.next();
//                if (par.getDataType() instanceof Enumeration)
//                    result.add((Enumeration)par.getDataType());
//            }
//        }
//
//        return result;
//    }

    /**
     * Check if a send message action involves objects deployed in different
     * nodes.
     * @param sma SendMessageAction object to be checked.
     * @return TRUE if this action is a remote message sending action, FALSE otherwise.
     */
    public static boolean isRemoteSendMessageAction(SendMessageAction sma) {
        dercs.structure.runtime.Object fromObj = (dercs.structure.runtime.Object)sma.getFromObject();
        dercs.structure.runtime.Object toObj = (dercs.structure.runtime.Object)sma.getToObject();
        if ((fromObj != null) && (toObj != null) && !fromObj.getName().equals(toObj.getName())) {
            if (!(DERCSHelper.isDummyObject(fromObj) || DERCSHelper.isDummyObject(toObj))) {
                if (
                        (
                                (
                                        (fromObj.getNode() != null)
                                                && (toObj.getNode() != null)
                                                && !fromObj.getNode().getName().equals(toObj.getNode().getName()
                                        )
                                )
//					   ||
//					   (
//					     DERCSHelper.isDummyObject(fromObj)
//					     && (toObj.getNode() != null)
//					     // to be considered a remote message sending
//					     // one should also check if the class
//					     // of the dummy object is a class deployed
//					     // in the node of 'toObj'
//					   )
                        )
                ) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if an attribute is public.
     * @param attr Attribute to be checked.
     * @return TRUE if the attribute is public, FALSE otherwise.
     */
    public static boolean isPublic(Attribute attr) {
        return attr.getVisibility() == Visibility.PUBLIC;
    }

    /**
     * Check if an attribute is protected.
     * @param attr Attribute to be checked.
     * @return TRUE if the attribute is protected, FALSE otherwise.
     */
    public static boolean isProtected(Attribute attr) {
        return attr.getVisibility() == Visibility.PROTECTED;
    }

    /**
     * Check if an attribute is private.
     * @param attr Attribute to be checked.
     * @return TRUE if the attribute is private, FALSE otherwise.
     */
    public static boolean isPrivate(Attribute attr) {
        return attr.getVisibility() == Visibility.PRIVATE;
    }

    /**
     * Check if a method is public.
     * @param mth Method to be checked.
     * @return TRUE if the method is public, FALSE otherwise.
     */
    public static boolean isPublic(Method mth) {
        return mth.getVisibility() == Visibility.PUBLIC;
    }

    /**
     * Check if a method is protected.
     * @param mth Method to be checked.
     * @return TRUE if the method is protected, FALSE otherwise.
     */
    public static boolean isProtected(Method mth) {
        return mth.getVisibility() == Visibility.PROTECTED;
    }

    /**
     * Check if a method is private.
     * @param mth Method to be checked.
     * @return TRUE if the method is private, FALSE otherwise.
     */
    public static boolean isPrivate(Method mth) {
        return mth.getVisibility() == Visibility.PRIVATE;
    }

//    /**
//     * Get the Attribute list used in a method behavior.
//     * @param Class the class of the method analyzed
//     * @param msgName the name of method analyzed
//     * @param type inform if the function must collect the attributes read(0) or write(1) in the method
//     * @return HashSet of Attributes used in the method.
//     */
//    public static HashSet<Attribute> getListOfAttributesUsed(Class cls, String msgName, int type){
//        HashSet<String> list = new HashSet<String>();
//
//        HashSet<Attribute> attrList = new HashSet<Attribute>();
//        Method mth = cls.getMethod(msgName);
//        if (mth != null){
//            Behavior be = mth.getTriggeredBehavior();
//
//            if (be != null){
//                if (type == 0) { // read values
//                    for(Iterator<BehavioralElement> BeEl =  be.getBeharioralElements().iterator(); BeEl.hasNext();){
//                        BehavioralElement element = BeEl.next();
//                        list.addAll(getListValuesRead(element));
//                    }
//                }else if (type == 1){ // write values
//                    for(Iterator<BehavioralElement> BeEl =  be.getBeharioralElements().iterator(); BeEl.hasNext();){
//                        BehavioralElement element = BeEl.next();
//                        list.addAll(getListValuesWrite(element));
//                    }
//                }
//            }
//        }
//
//        attrList.addAll(getAttributeListFromName(cls,list));
//        return attrList;
//
//    }

//    public static HashSet<String> getListValuesRead(BehavioralElement be){
//        HashSet<String> list = new HashSet<String>();
//
//        if (be instanceof AssignmentAction)  {
//            AssignmentAction a = (AssignmentAction)be;
//            if (!a.isAssignmentOfValue()){
//                if (a.isAssignmentOfActionResult()){
//                    if (a.getAction() instanceof ExpressionAction)
//                        list.addAll(UMLMetaModelHelper.getElementOfExpression(a.getAction().toString(),false));
//                    else if (a.getAction() instanceof SendMessageAction){
//                        SendMessageAction aa = (SendMessageAction)a.getAction();
//                        if (aa.getRelatedMethod().isGetSetMethod())
//                            list.add(aa.getRelatedMethod().getAssociatedAttribute().getName());
//                    }
//                }else
//                    list.add(a.getAction().toString());
//            }
//        }else if (be instanceof ExpressionAction){
//            ExpressionAction ea = (ExpressionAction)be;
//            list.addAll(UMLMetaModelHelper.getElementOfExpression(ea.getExpression(),false));
//        }else if (be instanceof SendMessageAction){
//            SendMessageAction sma = (SendMessageAction)be;
//            int i = 0;
//            for (i = 0; i <= sma.getParametersValuesCount()-1;i++){
//                if (sma.isParameterAttribute(i))
//                    list.add(sma.getParameterAttribute(i).getName());
//                else if (sma.isParameterVariable(i))
//                    list.add(sma.getParameterVariable(i).getName());
//                else if (sma.isParameterValue(i))
//                    list.add(sma.getParameterValue(i));
//            }
//        }else if (be instanceof Behavior){
//            Behavior bh = (Behavior)be;
//
//            list.addAll(UMLMetaModelHelper.getElementOfExpression(bh.getEnterCondition(), false));
//            if (bh.hasAlternativeBehavior()){
//                list.addAll(UMLMetaModelHelper.getElementOfExpression(bh.getAlternativeBehavior().getEnterCondition(), false));
//                list.addAll(getListValuesRead(bh.getAlternativeBehavior()));
//            }
//            Iterator<BehavioralElement> bei =  bh.getBeharioralElements().iterator();
//            while (bei.hasNext()){
//                list.addAll(getListValuesRead(bei.next()));
//            }
//        }
//        return list;
//    }

    public static HashSet<String> getListValuesWrite(BehavioralElement be){
        HashSet<String> list = new HashSet<String>();

        if (be instanceof AssignmentAction)  {
            AssignmentAction a = (AssignmentAction)be;
            if (a.isAttributeAssignment())
                list.add(a.getDestinationAttribute().getName());
        }else if (be instanceof SendMessageAction){
            SendMessageAction sma = (SendMessageAction)be;
            if (sma.getOutputReceiverAction() != null)
                list.add(sma.getOutputReceiverAction().toString());
        }else if (be instanceof Behavior){
            Behavior bh = (Behavior)be;
            if (bh.hasAlternativeBehavior()){
                list.addAll(getListValuesWrite(bh.getAlternateBehavior()));
            }
            Iterator<BehavioralElement> bei =  bh.getBehavioralElements().iterator();
            while (bei.hasNext()){
                list.addAll(getListValuesWrite(bei.next()));
            }
        }
        return list;
    }


    public static HashSet<Attribute> getAttributeListFromName(Class cl, HashSet<String> attrName) {
        HashSet<Attribute> attrList = new HashSet<Attribute>();

        Iterator<String> i = attrName.iterator();
        while(i.hasNext()){
            Attribute att = cl.getAttribute(i.next());
            if (att != null)
                attrList.add(att);
        }
        return attrList;
    }

    /**
     * Check if a message type is synchronous communication
     * @param msg Message to be checked.
     * @return TRUE if the message is synchronous, FALSE otherwise.
     */
    public static boolean isSynchronous(SendMessageAction msg) {
        return  msg.getMessageSort() == MessageSort.SYNCHCALL;
    }

    /**
     * Check if a message type is asynchronous communication
     * @param msg Message to be checked.
     * @return TRUE if the message is asynchronous, FALSE otherwise.
     */
    public static boolean isAsynchronous(SendMessageAction msg) {
        return  msg.getMessageSort() == MessageSort.ASYNCHCALL;
    }

//    public static boolean hasAsynchrnousBehavior(Class cls, String mthMain, Method msg){
//        boolean result = false;
//        Method mth = cls.getMethod(mthMain);
//        if (mth != null){
//            Behavior be = mth.getTriggeredBehavior();
//
//            if (be != null){
//                for(Iterator<BehavioralElement> BeEl =  be.getBeharioralElements().iterator(); BeEl.hasNext() && !result;){
//                    BehavioralElement element = BeEl.next();
//                    result = checkMessageSortBehavior(element,1,msg);
//                }
//            }
//        }
//
//        return result;
//    }
//
//    public static boolean hasSynchrnousBehavior(Class cls, String mthMain, Method msg){
//        boolean result = false;
//        Method mth = cls.getMethod(mthMain);
//        if (mth != null){
//            Behavior be = mth.getTriggeredBehavior();
//
//            if (be != null){
//                for(Iterator<BehavioralElement> BeEl =  be.getBeharioralElements().iterator(); BeEl.hasNext() && !result;){
//                    BehavioralElement element = BeEl.next();
//                    result = checkMessageSortBehavior(element,0,msg);
//                }
//            }
//        }
//
//        return result;
//    }

    public static boolean checkMessageSortBehavior(BehavioralElement be, int type, Method msg){
        boolean result = false;
        if (be instanceof SendMessageAction){
            SendMessageAction sma = (SendMessageAction)be;
            if (sma.getRelatedMethod().signatureMatches(msg)){
                if (type == 0)
                    result = isSynchronous(sma);
                else
                    result = isAsynchronous(sma);
            }
        }else if (be instanceof Behavior){
            Behavior bh = (Behavior)be;
            if (bh.hasAlternativeBehavior()){
                result = checkMessageSortBehavior(bh.getAlternateBehavior(),type, msg);
            }
            Iterator<BehavioralElement> bei =  bh.getBehavioralElements().iterator();
            while (bei.hasNext() && !result){
                result = checkMessageSortBehavior(bei.next(),type, msg);
            }
        }
        return result;
    }

    public static boolean checkIfContainAttribute(HashSet<Attribute> list, Attribute element){
        return list.contains(element);
    }

    /**
     * Get the Method list used in a method behavior.
     * @param Class the class of the method analyzed
     * @param msgName the name of method analyzed
     * @return HashSet of Methods used in the method.
     */
    public static HashSet<SendMessageAction> getListOfMethodsUsed(Class cls, String msgName){
        HashSet<SendMessageAction> mthList = new HashSet<SendMessageAction>();
        Method mth = cls.getMethod(msgName);
        if (mth != null){
            Behavior be = mth.getTriggeredBehavior();

            for(Iterator<BehavioralElement> BeEl =  be.getBehavioralElements().iterator(); BeEl.hasNext();){
                BehavioralElement element = BeEl.next();
                mthList.addAll(getListMethods(element));
            }
        }
        return mthList;
    }

    public static HashSet<SendMessageAction> getListMethods(BehavioralElement be){
        HashSet<SendMessageAction> list = new HashSet<SendMessageAction>();
        if (be instanceof SendMessageAction){
            SendMessageAction sma = (SendMessageAction)be;
            list.add(sma);
        }else if (be instanceof Behavior){
            Behavior bh = (Behavior)be;
            if (bh.hasAlternativeBehavior()){
                list.addAll(getListMethods(bh.getAlternateBehavior()));
            }
            Iterator<BehavioralElement> bei =  bh.getBehavioralElements().iterator();
            while (bei.hasNext()){
                list.addAll(getListMethods(bei.next()));
            }
        }
        return list;
    }

    public static ArrayList<HashMap> getListofRelatedMethods(Class cls){

        ArrayList<HashMap> mth = new ArrayList<HashMap>();
        if (cls != null){
            Iterator<Method> mthList = cls.getMethods().iterator();
            while (mthList.hasNext()){
                Method mthAttr = mthList.next();
                if (mthAttr.isGetSetMethod()){
                    Attribute attr = mthAttr.getAssociatedAttribute();
                    if (!attr.getName().isEmpty()){
                        if (attr.getDataType() instanceof ClassDataType){
                            Class newCls = ((ClassDataType) attr.getDataType()).getRepresents();
                            HashMap mMap = new HashMap();
                            mMap.put("idx",attr);
                            mMap.put("mth",newCls.getMethods());
                            mth.add(mMap);
                            mth.addAll(getListofRelatedMethods(newCls));
                        }else if (attr.getDataType() instanceof Array){
                            dercs.datatypes.DataType arr = ((Array)attr.getDataType()).getDataType();
                            if (arr instanceof ClassDataType){
                                Class newCls = ((ClassDataType) arr).getRepresents();
                                HashMap mMap = new HashMap();
                                mMap.put("idx",attr);
                                mMap.put("mth",newCls.getMethods());
                                mth.add(mMap);
                                mth.addAll(getListofRelatedMethods(newCls));
                            }
                        }
                    }
                }
            }
        }
        return mth;
    }

//    public static HashSet<Attribute> getListAtributesinExpression(String expression, Class cls){
//        HashSet<Attribute> attr = new HashSet<Attribute>();
//        HashSet<String> list = new HashSet<String>();
//
//        list.addAll(UMLMetaModelHelper.getElementOfExpression(expression, false));
//        attr.addAll(getAttributeListFromName(cls,list));
//
//        return attr;
//    }

    public static void addAttributeToClass(Class cls, String attrName, DataType dtType, String defaultValue, Boolean isReadOnly){
        cls.addAttribute(attrName,dtType,Visibility.PRIVATE,false,defaultValue,isReadOnly);
    }

    public static ArrayList<Attribute> getAllAttributesFromClass(Class cls) {
        ArrayList<Attribute> al = new ArrayList<Attribute>();

        if (cls.getSuperClass() != null)
            al.addAll(getAllAttributesFromClass(cls.getSuperClass()));
        al.addAll(cls.getAttributes());

        return al;
    }

    public static ArrayList<Method> getAllMethodsFromClass(Class cls) {
        ArrayList<Method> ml = new ArrayList<Method>();

        if (cls.getSuperClass() != null)
            ml.addAll(getAllMethodsFromClass(cls.getSuperClass()));
        ml.addAll(cls.getMethods());

        return ml;
    }

    public static Method getMainBehaviorFromActiveClass(Class cls) {
        Iterator<Method> itm = getAllMethodsFromClass(cls).iterator();
        while (itm.hasNext()) {
            Method method = (Method) itm.next();
            if (method.getName().startsWith("run"))
                return method;
        }
        return null;
    }

    public static ArrayList<dercs.structure.runtime.Object> getAllObjectsFromClass(Model model, Class cls) {
        ArrayList<dercs.structure.runtime.Object> ol = new ArrayList<dercs.structure.runtime.Object>();

        Iterator<dercs.structure.runtime.Object> ito = model.getObjects().iterator();
        while (ito.hasNext()) {
            dercs.structure.runtime.Object obj = ito.next();
            if ( obj.getInstanceOfClass().equals(cls) )
                ol.add(obj);
        }
        return ol;
    }

    public String toSeconds(String val) {
        String result = "";
        int lastnumber = val.lastIndexOf('0');
        for(char n = '1'; (lastnumber < 0) && (n <= '9'); n++)
            lastnumber = val.lastIndexOf(n);
        if (lastnumber >= 0) {
            String unit = val.substring(lastnumber+1).toLowerCase();
            if (unit.compareTo("s") == 0)
                result = val.substring(0, lastnumber+1);
            else if (unit.compareTo("ms") == 0)
                result = "" + (Integer.parseInt(val.substring(0, lastnumber+1))/1000);
        }
        return result;
    }

    public String toMilliseconds(String val) {
        String result = "";
        int lastnumber = val.lastIndexOf('0');
        for(char n = '1'; (lastnumber < 0) && (n <= '9'); n++)
            lastnumber = val.lastIndexOf(n);
        if (lastnumber >= 0) {
            String unit = val.substring(lastnumber+1).toLowerCase();
            if (unit.compareTo("ms") == 0)
                result = val.substring(0, lastnumber+1);
            else if (unit.compareTo("s") == 0)
                result = "" + (Integer.parseInt(val.substring(0, lastnumber+1))*1000);
        }
        return result;
    }

    public String toHertz(String period) {
        String result = "";
        int lastnumber = period.lastIndexOf('0');
        for(char n = '1'; (lastnumber < 0) && (n <= '9'); n++)
            lastnumber = period.lastIndexOf(n);
        if (lastnumber >= 0) {
            int number = Integer.parseInt(period.substring(0, lastnumber+1));
            String unit = period.substring(lastnumber+1).toLowerCase();
            if (unit.compareTo("s") == 0)
                result = "" + (1.0/number);
            else if (unit.compareTo("ms") == 0)
                result = "" + (1.0/(number/1000));
        }
        return result;
    }

}
