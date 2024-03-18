package org.example;

import java.util.*;

public class Main {

    public static void main(String[] args){
        int r = 4;
        int s = 32;
        int n = 4;
        int m = 4;
        String key = "00010001001010001000110000000000";
        int[] beta = {0,4,8,12,1,5,9,13,2,6,10,14,3,7,11,15};
        KeyMgmt keys = new KeyMgmt(r,s,n,m,key,beta);
        String klartext = "0001001010001111";
        Map<String,String> sBox = new HashMap<>();
        sBox.put("0000","1110");// 0 E
        sBox.put("0001","0100");
        sBox.put("0010","1101");// 2 D
        sBox.put("0011","0001");
        sBox.put("0100","0010");//4 2
        sBox.put("0101","1111");
        sBox.put("0110","1011");//6 B
        sBox.put("0111","1000");
        sBox.put("1000","0011");//8 3
        sBox.put("1001","1010");
        sBox.put("1010","0110");//10 6
        sBox.put("1011","1100");
        sBox.put("1100","0101");//12 5
        sBox.put("1101","1001");
        sBox.put("1110","0000");//14 0
        sBox.put("1111","0111");

        cipher encipher = new cipher(r,n,m,beta,sBox,keys,klartext);
        String chiffretext = encipher.chiffretext();
        System.out.println("klartext: " + klartext);
        System.out.println("Chiffretext: " + chiffretext);
        System.out.println("Klartext: " + encipher.dechiffretext(chiffretext));
    }
}