package com.exercise.highscore;

import java.util.Arrays;
import java.util.List;

public class Test {


    public int solution(int number){

        String binary = Integer.toBinaryString(number);
        int maxlength = 0;
        int maxlengthDef = 0;
        for (Character character: binary.toCharArray()) {
            if(character.equals('0')){
                maxlength++;
            }else{
                maxlengthDef = maxlength;
                maxlength = 0;
            }
        }
        System.out.println("rta "+maxlengthDef);
        return maxlengthDef;
    }


    public static void main(String[] args) {
        new Test().solution(20  );
    }
}
