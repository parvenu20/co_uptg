package com.pay.tg;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObtainNum {
   public static void main(String[] args) {
       Pattern pattern = Pattern.compile("(\\d{5})");
       Matcher matcher = pattern.matcher("aaa12135411an1aannna12351");
       while(matcher.find()){
           System.out.println(matcher.group(1));
       }
   }
}