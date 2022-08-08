package genertica.util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Helper class that implements the Quick Sort algorithm.
 * Source code copied from http://www.mycsresource.net/articles/programming/sorting_algos/quicksort
 * @author Marco Aurelio Wehrmeister
 */
public class QuickSort {

    /**
     * Sorts an array using the quick sort algorithm.
     * @param a A
     */
    public static void quickSort(ArrayList[] a)
    // pre: array is full, all elements are non-null integers
    // post: the array is sorted in ascending order
    {
        quickSort(a, 0, a[0].size()-1); // quicksort all the elements in the array
    }

    /**
     * Sorts part of an array using the quick sort algorithm.
     * @param a Array to be sorted.
     * @param start Start position
     * @param end End position
     */
    public static void quickSort(ArrayList[] a, int start, int end)
    {
        int i = start; // index of left-to-right scan
        int k = end;   // index of right-to-left scan

        // check that there are at least two elements to sort
        if (end - start >= 1) {
            // set the pivot as the first element in the partition
            Object pivot = a[0].get(start);
            while (k > i) {                                         // while the scan indices from left and right have not met,
                while ((a[0].get(i).hashCode() <= pivot.hashCode()) // from the left, look for the first
                        && (i <= end) && (k > i))                    // element greater than the pivot
                    i++;
                while ((a[0].get(k).hashCode() > pivot.hashCode())  // from the right, look for the first
                        && (k >= start) && (k >= i))                 // element not greater than the pivot
                    k--;
                if (k > i) {                                        // if the left seekindex is still smaller than
                    Collections.swap(a[0], i, k);                   // the right index, swap the corresponding elements
                    for(int x=1; x < a.length; x++)
                        Collections.swap(a[x], i, k);
                }
            }
            Collections.swap(a[0], start, k);          // after the indices have crossed, swap the last element in
            for(int x=1; x < a.length; x++)         // the left partition with the pivot
                Collections.swap(a[x], start, k);

            quickSort(a, start, k - 1); // quicksort the left partition
            quickSort(a, k + 1, end);   // quicksort the right partition
        }
        else {
            // if there is only one element in the partition, do not do any sorting
            return; // the array is sorted, so exit
        }
    }

    /**
     * Sorts an array of strings using the quick sort algorithm.
     * @param a A
     */
    public static void quickSortStringList(ArrayList[] a)
    // pre: array is full, all elements are non-null integers
    // post: the array is sorted in ascending order
    {
        quickSortStringList(a, 0, a[0].size()-1); // quicksort all the elements in the array
    }

    /**
     * Sorts part of an array of strings using the quick sort algorithm.
     * @param a Array to be sorted.
     * @param start Start position
     * @param end End position
     */
    public static void quickSortStringList(ArrayList[] a, int start, int end)
    {
        int i = start; // index of left-to-right scan
        int k = end;   // index of right-to-left scan

        // check that there are at least two elements to sort
        if (end - start >= 1) {
            // set the pivot as the first element in the partition
            String pivot = (String)a[0].get(start);
            while (k > i) {                                         // while the scan indices from left and right have not met,
                while ((((String)a[0].get(i)).compareTo(pivot) <= 0)// from the left, look for the first
                        && (i <= end) && (k > i))                    // element greater than the pivot
                    i++;
                while ((((String)a[0].get(k)).compareTo(pivot) > 0)// from the right, look for the first
                        && (k >= start) && (k >= i))                 // element not greater than the pivot
                    k--;
                if (k > i) {                                        // if the left seekindex is still smaller than
                    Collections.swap(a[0], i, k);                   // the right index, swap the corresponding elements
                    for(int x=1; x < a.length; x++)
                        Collections.swap(a[x], i, k);
                }
            }
            Collections.swap(a[0], start, k);          // after the indices have crossed, swap the last element in
            for(int x=1; x < a.length; x++)         // the left partition with the pivot
                Collections.swap(a[x], start, k);

            quickSortStringList(a, start, k - 1); // quicksort the left partition
            quickSortStringList(a, k + 1, end);   // quicksort the right partition
        }
        else {
            // if there is only one element in the partition, do not do any sorting
            return; // the array is sorted, so exit
        }
    }

//    /**
//     * Sorts an array of UML meta-model's NamedElement using the quick sort
//     * algorithm.
//     * @param a Array of Arrays. The first element is an array which is the
//     * index key. Other elements are other arrays and move according the index
//     * key.
//     */
//    public static void quickSortNamedElementList(ArrayList[] a)
//    // pre: array is full, all elements are non-null integers
//    // post: the array is sorted in ascending order
//    {
//        quickSortNamedElementList(a, 0, a[0].size()-1); // quicksort all the elements in the array
//    }
//
//    /**
//     * Sorts part of an array of UML meta-model's NamedElement using the quick
//     * sort algorithm.
//     * @param a Array of Arrays. The first element is an array which is the
//     * index key. Other elements are other arrays and move according the index
//     * key.
//     * @param start Start position
//     * @param end End position
//     */
//    public static void quickSortNamedElementList(ArrayList[] a, int start, int end)
//    {
//        int i = start; // index of left-to-right scan
//        int k = end;   // index of right-to-left scan
//
//        // check that there are at least two elements to sort
//        if (end - start >= 1) {
//            // set the pivot as the first element in the partition
//            String pivot = ((NamedElement)(a[0].get(start))).getName();
//            while (k > i) {                                         // while the scan indices from left and right have not met,
//                while ((((NamedElement)(a[0].get(i))).getName().compareTo(pivot) <= 0) // from the left, look for the first
//                        && (i <= end) && (k > i))                    // element greater than the pivot
//                    i++;
//                while ((((NamedElement)(a[0].get(k))).getName().compareTo(pivot) > 0)// from the right, look for the first
//                        && (k >= start) && (k >= i))                 // element not greater than the pivot
//                    k--;
//                if (k > i) {                                        // if the left seekindex is still smaller than
//                    Collections.swap(a[0], i, k);                   // the right index, swap the corresponding elements
//                    for(int x=1; x < a.length; x++)
//                        Collections.swap(a[x], i, k);
//                }
//            }
//            Collections.swap(a[0], start, k);          // after the indices have crossed, swap the last element in
//            for(int x=1; x < a.length; x++)         // the left partition with the pivot
//                Collections.swap(a[x], start, k);
//
//            quickSortNamedElementList(a, start, k - 1); // quicksort the left partition
//            quickSortNamedElementList(a, k + 1, end);   // quicksort the right partition
//        }
//        else {
//            // if there is only one element in the partition, do not do any sorting
//            return; // the array is sorted, so exit
//        }
//    }
//
//    public static int insertedOrdered(ArrayList a, NamedElement el, boolean unique) {
//        boolean exit = false;
//        int i = 0;
//        int index = -1;
//        //Marcela 09/09/2013
//        // adding a new variable to keep the correct position in the array to add the new enumeration
//        int pos = 0;
//        for(i=0; !exit && (i < a.size()); i++) {
//            index = el.getName().compareTo(((NamedElement)a.get(i)).getName());
//            if ((index < 0)
//                    || ((index == 0) && !unique)) { // allows duplicated
//                a.add(i, el);
//                exit = true;
//                pos = i;
//            }
//            else if (index == 0){
//                exit = true;
//                pos = i;
//            }
//        }
//        if (exit)
//            return pos;
//        else {
//            // element's name is greater than the name of all other elements
//            // it should be inserted at the end of the list.
//            a.add(el);
//            return i;
//        }
//    }
}
