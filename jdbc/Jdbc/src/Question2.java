/*
 Today's task : jdbc Complete revision & below problem:
Problem Statement:
You are given a string s and an integer k.
Your task is to find the length of the longest contiguous substring that contains exactly k distinct characters.
If no such substring exists, return -1.
Important: The substring must be contiguous, and character comparison is case-sensitive.
String contains lowercase English letters.
-
Solve using Java.
Try solving without using nested loops.
Target time complexity: (O(n)).
Explain why the Sliding Window technique works.
Handle all edge cases.
 Can you modify the solution to print the actual longest substring instead of just its length
*/

import java.util.ArrayList;
import java.util.HashMap;

public class Question2 {
    public static void main(String[] args) {
       String s = "aabacbebebe";
       int k=3,l=0,r=0;
       HashMap<String,Integer> hm=new HashMap<>();
       ArrayList<Character> ch=new ArrayList<>();
       while(r<s.length()){
        
        if(!ch.contains(s.charAt(r))){
            if(ch.size()==k){
                hm.put(s.substring(l, r),s.substring(l, r).length() );
                ch.clear();
                 l=r;
                 ch.add(s.charAt(r));
            }else{
                ch.add(s.charAt(r));
            }
            
        }
        System.out.println(r);
        r++;
       }
       
       if(ch.size()==k){
                hm.put(s.substring(l, r),s.substring(l, r).length() );
                ch.clear();
                 l=r;
                 ch.add(s.charAt(r-1));
            }
            System.out.println(l);
            System.out.println(r);
       System.out.println(ch);
       System.out.println(hm);
    }
    
}
