/**
 * 
 */
package info.practice.java8;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Subbu
 *
 */
public class MyMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Integer> number1 = Arrays.asList(2,6,3,7,9);
		List<Integer> number2 = Arrays.asList(1,4,0,5,8);
		List<Integer> number3 = Arrays.asList(10,11,12,13,14);
		List<List<Integer>> numberList = Arrays.asList(number1, number2, number3);
		flatMapExample(numberList);
	}
	
	public static void flatMapExample(List<List<Integer>> numberList) {
		List<Integer> combineList = numberList.stream().flatMap(list -> list.stream()).collect(Collectors.toList());
		System.out.println("combine list:"+combineList);
	}

}
