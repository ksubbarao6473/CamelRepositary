package info.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MyStream {

	public static void main(String[] args) {
		List<String> namesList = Arrays.asList("subbu","sudha","medha","subbu","medha","subbu");
		// streamFilter();
		//StreamMap();
		//StreamIteratorMap();
		//parallelListSorting();
		//myCollectorGroupingCount(namesList);
		myStringJoiner();
	}
	
	public static void myStringJoiner() {
		StringJoiner myString = new StringJoiner(",","(",")");
		myString.add("subbu");
		myString.add("sudha");
		myString.add("medha");
		System.out.println(myString);
		
		System.out.println("---------------------------------------------------");
		StringJoiner myString1 = new StringJoiner(",");
		//myString1.setEmptyValue("This is default string");
		System.out.println(myString1);
	}
	
	public static void myCollectorGroupingCount(List<String> namesList) {
		Map<String, Long> countMap = namesList.stream().collect
				(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		System.out.println("result is:"+countMap);
	}
	
	public static void parallelListSorting() {
		int[] number = {56, 45, 3, 6, 45};
		
		Arrays.parallelSort(number);
		Arrays.stream(number).forEach(n -> System.out.print(n+"  "));
		
	}
	public static void StreamIteratorMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(10, "mango");
		map.put(20, "guvva");
		map.put(30, "orange");
		map.put(40, "kiwi");
		map.put(50, "strawberry");
		
		Map<Integer, String> costmap = map.entrySet().stream()
										.filter(hmap -> hmap.getKey().intValue() > 19)
										.collect(Collectors.toMap(hmap -> hmap.getKey(), hmap -> hmap.getValue()));
		System.out.println("result is:"+costmap);
	}
	
	public static void StreamMap() {
		List<Integer> numberList = Arrays.asList(1,2,3,4,5);
		List<Integer> squareList = numberList.stream().map(n -> n * n).collect(Collectors.toList());
		//squareList.forEach(System.out::println);
		System.out.println(squareList);
	}
	
	public static void streamFilter() {
		List<String> namesList = new ArrayList<String>();
		namesList.add("subbu");
		namesList.add("sudha");
		namesList.add("medhansha");
		
		long count = namesList.stream().filter(str -> str.length()<6).count();
		System.out.println("names count:"+count);
		
		List<String> name = namesList.stream().filter(str -> str.contains("su")).collect(Collectors.toList());
				name.forEach(System.out::println);
	
	}

}
