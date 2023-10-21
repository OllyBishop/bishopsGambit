package utils;

import java.util.List;

public class ListUtils {

	public static boolean hasIndex(List<?> list, int index) {
		return 0 <= index && index < list.size();
	}

	/**
	 * Finds the element in <b>list1</b> that has the same index as the given
	 * <b>object</b> in <b>list2</b>.
	 * 
	 * @param <T>    the type of the element to be found
	 * @param <U>    the type of the given <b>object</b>
	 * @param list1  the list containing the element to be found
	 * @param list2  the list containing the given <b>object</b>
	 * @param object the object whose index we are interested in
	 * @return the element in <b>list1</b> that has the same index as the given
	 *         <b>object</b> in <b>list2</b>
	 */
	public static <T, U> T get(List<T> list1, List<U> list2, U object) {
		T element = null;
		int index;
		if (object != null && hasIndex(list1, index = list2.indexOf(object)))
			element = list1.get(index);
		return element;
	}

}
