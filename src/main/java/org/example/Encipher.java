package org.example;

import java.util.HashMap;
import java.util.Map;

public class Encipher {

    private int r,n,m;
    private int[] beta;
    private Map<String,String> sBox;
    private KeyMgmt keys;
    private int[] klartext;

    /**
     * r: How many rounds for encryption
     * n: Length one box of s-box
     * m: How many s-boxes for round i
     * beta: Bit at index i has the index beta[i] after the permutation
     * sbox: Has n*n many mappings from bitstring with lenght n to another bitstring lenght n
     * keys: Generates the roundkeys
     * klartext: Text to encript, as bitstring
     * */
    public Encipher(int r, int n, int m, int[] beta, Map<String,String> sBox, KeyMgmt keys, String klartext) {
        this.r = r;
        this.n = n;
        this.m = m;
        this.beta = beta;
        this.sBox = sBox;
        this.keys = keys;
        klartextIntoArray(klartext);
    }

    //TODO Delete this main function befor submitting the code to vogt
    public static void main(String[] args){
        int r = 3;
        int s = 24;
        int n = 4;
        int m = 3;
        String key = "000110101111110000000111";
        int[] beta = {4,5,8,9,0,1,10,11,2,3,6,7};
        KeyMgmt keys = new KeyMgmt(r,s,n,m,key,beta);
        String klartext = "111101010110";
        Map<String,String> sBox = new HashMap<>();
        sBox.put("0000","0101");
        sBox.put("0001","0100");
        sBox.put("0010","1101");
        sBox.put("0011","0001");
        sBox.put("0100","0011");
        sBox.put("0101","1100");
        sBox.put("0110","1011");
        sBox.put("0111","1000");
        sBox.put("1000","1010");
        sBox.put("1001","0010");
        sBox.put("1010","0110");
        sBox.put("1011","1111");
        sBox.put("1100","1001");
        sBox.put("1101","1110");
        sBox.put("1110","0000");
        sBox.put("1111","0111");

        Encipher encipher = new Encipher(r,n,m,beta,sBox,keys,klartext);
        System.out.println("Chiffretext: " + encipher.chiffretext());
    }

    private void klartextIntoArray(String k) throws IllegalArgumentException {
        if(n*m != k.length()){
            throw new IllegalArgumentException("Klartext length is not " + n*m);
        }
        else {
            klartext = new int[n*m];
            for(int i = 0; i < n*m; i++){
                if(k.charAt(i) == '1'){
                    klartext[i] = 1;
                }
                else if(k.charAt(i) == '0'){
                    klartext[i] = 0;
                }
                else{
                    throw new IllegalArgumentException("Chars other than 0 or 1 in klartext");
                }
            }
        }
    }

    /**
     * Text xor key
     * Example
     * -> 1111 0101 0110 xor 0001 1010 1111 = 1110 1111 1001
     */
    private int[] xor(int[] text, int[] key){
        if(text.length != key.length){
            throw new IllegalArgumentException("Wrong keylength");
        }
        else {
            int[] result = new int[n*m];
            for(int i = 0; i < n*m; i++){
                result[i] = text[i] ^ key[i];
            }
            return result;
        }
    }

    /**
     * Text will be split into m parts
     * For each part there is a mapping in one of the n*n sboxes
     * The result is the composition of these m mappings
     * Example
     * -> text: 1110 1111 1001
     * -> used sboxs: {1110:0000},{1111:0111},{1001:0010}
     * -> result: 0000 0111 0010
     */
    private int[] sboxOperation(int[] text){
        String toMap = "";
        String mapping = "";

        for(int x = 0; x < m; x++){
            for(int y = x*n; y < x*n + n; y++){
                toMap += text[y];
            }

            if(!sBox.containsKey(toMap)){
                throw new IllegalArgumentException("No mapping in sbox for " + toMap);
            }
            else{
                mapping += sBox.get(toMap);
            }

            toMap = "";
        }

        if(mapping.length() != n*m){
            throw new IllegalArgumentException("sBox has values with wrong length");
        }
        else{
            int[] result = new int[n*m];
            for(int z = 0; z < n*m; z++){
                if(mapping.charAt(z) == '1'){
                    result[z] = 1;
                }
                else if(mapping.charAt(z) == '0'){
                    result[z] = 0;
                }
                else{
                    throw new IllegalArgumentException("sBox has values not equal 1 or 0");
                }
            }
            return result;
        }
    }

    /**
     * Text will be permutated according beta
     * Bit at index i has the index beta[i] after the permutation
     * Example
     * -> text: 0101
     * -> beta: {3,2,1,0}
     * -> result: 1010
     */
    private int[] permutation(int[] text){
        if(beta.length != n*m){
            throw new IllegalArgumentException("Beta has not the right length");
        }
        else{
            int[] result = new int[n*m];
            for(int j = 0; j < n*m; j++){
                if (beta[j] > n*m-1) {
                    throw new IllegalArgumentException("Beta has illegal values");
                }
                result[j] = text[beta[j]];
            }
            return result;
        }
    }

    /**
     * There is a first round, then normal rounds except for the last one, which is shortened
     * Initial: xor
     * Normal: sbox-mapping, permutation, xor
     * Last: sbox-mapping, xor
     */
    public String chiffretext(){
        int[] result = new int[n*m];

        for(int i = 0; i < r+1; i++){

            if(i == 0){
                result = xor(klartext,keys.getKeyForEncription(0));
            }
            else if(i == r){
                int[] tmp = sboxOperation(result);
                result = xor(tmp,keys.getKeyForEncription(r));
            }
            else{
                int[] tmp = sboxOperation(result);
                tmp = permutation(tmp);
                result = xor(tmp,keys.getKeyForEncription(i));
            }

        }

        String chiffre = "";
        for(int j = 0; j < n*m; j++){
            chiffre += result[j];
        }
        return chiffre;
    }

}
