package io.github.ollybishop.bishopsgambit.util;

import java.util.List;
import java.util.stream.Stream;

public class ListUtils
{
    public static <T> List<T> combine( List<? extends T> list1, List<? extends T> list2 )
    {
        return Stream.concat( list1.stream(), list2.stream() ).toList();
    }

    public static boolean hasIndex( List<?> list, int index )
    {
        return 0 <= index && index < list.size();
    }

    /**
     * Returns the element in {@code list1} at the same index as the first occurrence of {@code element} in {@code list2}.
     * 
     * @param <T>     the type of elements in {@code list1}
     * @param <U>     the type of elements in {@code list2}
     * @param list1   the list containing the element to return
     * @param list2   the list to search for {@code element}
     * @param element the element to search for in {@code list2}
     * @return the element in {@code list1} at the same index as the first occurrence of {@code element} in {@code list2}, if
     *         such an element exists; {@code null} otherwise
     */
    public static <T, U> T getCorrespondingElement( List<T> list1, List<U> list2, U element )
    {
        int index = list2.indexOf( element );

        if ( hasIndex( list1, index ) )
            return list1.get( index );

        return null;
    }
}
