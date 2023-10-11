package utils;

public class StringUtils {

	public static boolean isEmpty(String string) {
		return string == null || string.isEmpty();
	}

	public static boolean isBlank(String string) {
		return string == null || string.isBlank();
	}

	public static String toUpperCamelCase(String... strings) {
		StringBuilder sb = new StringBuilder();
		for (String s : strings) {
			if (!isBlank(s)) {
				sb.append(s.substring(0, 1).toUpperCase());
				sb.append(s.substring(1).toLowerCase());
			}
		}
		return sb.toString();
	}

}
