package genertica.util;

import java.util.ArrayList;

import dercs.structure.NamedElement;

/**
 * Helper class that implements the Binary Search algorithm.
 * Source code copied from http://www.java-tips.org/java-se-tips/java.lang/binary-search-implementation-in-java.html
 * @author Marco Aurelio Wehrmeister
 */
public class BinarySearch {

    /**
     * Searches for an element into a sorted array.
     * @param a Array to be looked.
     * @param element Element to be found.
     * @return postion of the element within the array. If the element cannot be
     * founded the result is -1.
     */
    public static int binarySearch(ArrayList a, Object element)
    {
        int low = 0;
        int high = a.size() - 1;
        int mid;

        while( low <= high ) {
            mid = ( low + high ) / 2;
            if ((a.get(mid).hashCode() - element.hashCode()) < 0)
                low = mid + 1;
            else if ((a.get(mid).hashCode() - element.hashCode()) > 0)
                high = mid - 1;
            else
                return mid;
        }
        return -1;
    }

    /**
     * Searches for an string into a sorted array of strings.
     * @param a Array to be looked.
     * @param str String to be found.
     * @return position of the string within the array. If the string cannot be
     * founded the result is -1.
     */
    public static int binarySearchString(ArrayList<String> a, String str)
    {
        int low = 0;
        int high = a.size() - 1;
        int mid;

        while( low <= high ) {
            mid = ( low + high ) / 2;
            if (a.get(mid).compareTo(str) < 0)
                low = mid + 1;
            else if (a.get(mid).compareTo(str) > 0)
                high = mid - 1;
            else
                return mid;
        }
        return -1;
    }

    /**
     * Searches for a specific object into a sorted array of objects using its name.
     * @param a Array to be looked.
     * @param name Name of the object to be found.
     * @return postion of the object within the array. If the string cannot be
     * found the result is -1.
     */
    public static int binarySearchByName(ArrayList a, String name)
    {
        int low = 0;
        int high = a.size() - 1;
        int mid;

        while( low <= high ) {
            mid = ( low + high ) / 2;
            String objName = "";
            if (a.get(mid) instanceof NamedElement)
                objName = ((NamedElement)a.get(mid)).getName();
//            else if (a.get(mid) instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement)
//                objName = ((com.nomagic.uml2.ext.magicdraw.classes.mdkernel.NamedElement)a.get(mid)).getName();
            if (objName.compareTo(name) < 0)
                low = mid + 1;
            else if (objName.compareTo(name) > 0)
                high = mid - 1;
            else
                return mid;
        }
        return -1;
    }
}