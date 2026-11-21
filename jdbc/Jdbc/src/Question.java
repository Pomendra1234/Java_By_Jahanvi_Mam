import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

/*
 Today's task : Revise Today's Class and Solve below problem :
Problem Statement:
You are given a sentence containing multiple words. Your task is to find the first word that appears exactly once in the sentence.
The comparison must be case-insensitive, and punctuation marks should not affect word matching.
If every word is repeated, print No non-repeating word found.
Example: "Apple banana apple mango banana orange" = "mango"
 Do not use nested loops for frequency calculation.
Do not use Java 8 Stream API.
Use Collections Framework.
Handle uppercase and lowercase words.
Ignore punctuation marks.
Maintain the original order of words.
Target time complexity: O(n), assuming average-case HashMap operations.
 Why would you choose HashMap instead of HashSet?
How would you solve this using LinkedHashMap?
What is the time complexity of your solution?
How would you handle an empty sentence?
Can you solve it without using an additional collection?
 */

public class Question {
    public static void main(String[] args) {
        String sentence = "Apple banana apple mango !banana orange";
    //    here if sentence contains whitespace like " ".so here trim() remove the spaces from left and right in a string. 
    // And also if the string is empty then it simply print it and return.
        sentence = sentence.trim();
        if (sentence.isEmpty()) {
            System.out.println("Sentence is empty");
            return;
        }
        // here if sentence contains string and in that string there are some similar string but they are differ according to uppercase
        // and lowercase then we make the sentence in lowercase and also make sure that if any punctuation mark their then we replace them 
        // with empty string.
        sentence = sentence.toLowerCase().replaceAll("[^a-z0-9\\s]", "");
        // here we use two collection which is linkedhashmap and arraylist.Linkedhashmap is used here because it maintains the insertion 
        // order.
        LinkedHashMap<String, Integer> hm = new LinkedHashMap<>();
        ArrayList<String> arr = new ArrayList<>(Arrays.asList(sentence.toLowerCase().split("\\s+")));
        // System.out.println(arr);
        int i = 0;
        while (i < arr.size()) {

            if (hm.containsKey(arr.get(i))) {
                hm.put(arr.get(i), hm.get(arr.get(i)) + 1);
            } else {
                hm.put(arr.get(i), 1);

            }

            i++;
        }
        Object[] keys = hm.keySet().toArray();
        for (Object o : keys) {
            if (hm.get(o) == 1) {
                System.out.println(o);
                return;
            }
        }
        System.out.println("No non-repeating word found");
    }
}
