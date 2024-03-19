package org.example;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        //demo values are for the example from the task, non demo is for decryption of the given chiffretext
        int r = 4;
        int s = 32;
        int n = 4;
        int m = 4;
        int[] beta = {0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15};

        Map<String, String> sBox = initSbox();


        String key = "00111010100101001101011000111111";
        KeyMgmt keys = new KeyMgmt(r, s, n, m, key, beta);//Key von Aufgabenstellung


        String demoKey = "00010001001010001000110000000000";
        KeyMgmt demoKeys = new KeyMgmt(r, s, n, m, demoKey, beta); //Key für test der Ver/Entschlüsselung

        String klartext = "0001001010001111"; //Demo Klartext von Aufgabenstellung


        Cipher cipher = new Cipher(r, n, m, beta, sBox, keys);
        Cipher demoCipher = new Cipher(r, n, m, beta, sBox, demoKeys);

        String chiffretext = demoCipher.chiffreText(klartext);
        System.out.println("klartext davor: " + klartext);
        System.out.println("Chiffretext: " + chiffretext);
        System.out.println("Klartext danach: " + demoCipher.dechiffreText(chiffretext));
        System.out.println(
            "Klartext davor und danach sind gleich: " + klartext.equals(demoCipher.dechiffreText(chiffretext)));

        String solution = cipher.dechiffreTextCTR(
            "00000100110100100000101110111000000000101000111110001110011111110110000001010001010000111010000000010011011001110010101110110000");

        System.out.printf("Solution to given chiffretext: %s\n", solution);

    }

    private static Map<String, String> initSbox() {
        Map<String, String> sBox = new HashMap<>();
        sBox.put("0000", "1110");//0 E
        sBox.put("0001", "0100");
        sBox.put("0010", "1101");//2 D
        sBox.put("0011", "0001");
        sBox.put("0100", "0010");//4 2
        sBox.put("0101", "1111");
        sBox.put("0110", "1011");//6 B
        sBox.put("0111", "1000");
        sBox.put("1000", "0011");//8 3
        sBox.put("1001", "1010");
        sBox.put("1010", "0110");//10 6
        sBox.put("1011", "1100");
        sBox.put("1100", "0101");//12 5
        sBox.put("1101", "1001");
        sBox.put("1110", "0000");//14 0
        sBox.put("1111", "0111");
        return sBox;
    }
}