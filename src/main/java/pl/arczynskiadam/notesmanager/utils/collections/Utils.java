package pl.arczynskiadam.notesmanager.utils.collections;

import java.util.Set;
import java.util.stream.Collectors;

public class Utils {
	
	public static Set<Integer> mapStringSetToIntSet(Set<String> source) {
		return source.stream().map(Integer::parseInt).collect(Collectors.toSet());
	}
	
	public static Set<String> mapIntSetToStringSet(Set<Integer> source) {
		return source.stream(). map(Object::toString).collect(Collectors.toSet());
	}
}
