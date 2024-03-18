package org.example;

import java.util.HashMap;
import java.util.Map;

public class cipher {

    private int r,n,m;
    private int[] beta;
    private Map<String,String> sBox;
    private Map<String,String> sInv;
    private KeyMgmt keys;
    private int[] inputText;

    /**
     * r: How many rounds for encryption
     * n: Length one box of s-box
     * m: How many s-boxes for round i
     * beta: Bit at index i has the index beta[i] after the permutation
     * sbox: Has n*n many mappings from bitstring with lenght n to another bitstring lenght n
     * keys: Generates the roundkeys
     * */
    public cipher(int r, int n, int m, int[] beta, Map<String,String> sBox, KeyMgmt keys) {
        this.r = r;
        this.n = n;
        this.m = m;
        this.beta = beta;
        this.sBox = sBox;
        this.sInv = inverse(sBox);
        this.keys = keys;
    }

    /**
     * creates Inverse of sBox
     * */
    private Map<String,String> inverse(Map<String, String> sBox) {
        Map<String,String> map = new HashMap<>();
        for (String o : sBox.keySet()) {
            String value = sBox.get(o);
            map.put(value, o);
        }
        return map;
    }


    private void inputTextIntoArray(String k) throws IllegalArgumentException {
    if(n*m != k.length()){
        throw new IllegalArgumentException("Input text length is not " + n*m);
    }
    else {
        inputText = new int[n*m];
        for(int i = 0; i < n*m; i++){
            if(k.charAt(i) == '1'){
                inputText[i] = 1;
            }
            else if(k.charAt(i) == '0'){
                inputText[i] = 0;
            }
            else{
                throw new IllegalArgumentException("Chars other than 0 or 1 in input text");
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
        return getInts(text, sBox);
    }



    private int[] sboxInverseOperation(int[] text){
        return getInts(text, sInv);
    }

    private int[] getInts(int[] text, Map<String, String> sBox) {
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
    public String chiffreText(String plainText){
        int[] result = new int[n*m];
        inputTextIntoArray(plainText);
        for(int i = 0; i < r+1; i++){

            if(i == 0){
                result = xor(inputText,keys.getKeyForEncription(0));
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

    public String dechiffreText(String chiffretext){
        int[] result = new int[n*m];
        inputTextIntoArray(chiffretext);

        for(int i = 0; i < r+1; i++){

            if(i == 0){
                result = xor(inputText,keys.getKeyForDecription(0));
            }
            else if(i == r){
                int[] tmp = sboxInverseOperation(result);
                result = xor(tmp,keys.getKeyForDecription(r));
            }
            else{
                int[] tmp = sboxInverseOperation(result);
                tmp = permutation(tmp);
                result = xor(tmp,keys.getKeyForDecription(i));
            }

        }

        String plainText = "";
        for(int j = 0; j < n*m; j++){
            plainText += result[j];
        }
        return plainText;
    }

}
