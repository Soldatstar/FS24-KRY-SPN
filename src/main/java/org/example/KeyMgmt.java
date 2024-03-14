package org.example;

import java.util.Arrays;

public class KeyMgmt {

    int r,s,n,m;
    int[] key;
    int[] beta;

    /**
     * r: How many rounds for encryption & decryption
     * s: Length of key
     * n: Length one box of s-box
     * n * m: Length of roundkey i
     * k: The full key who will get split into roundkeys
     * */
    public KeyMgmt(int r, int s, int n, int m, String k, int[] beta) {
        this.r = r;
        this.s = s;
        this.n = n;
        this.m = m;
        this.beta = beta;
        keyIntoArray(k);
    }

    public static void main(String[] args){
        KeyMgmt mgmt = new KeyMgmt(3,24,4,3,"000110101111110000000111",null);
        for(int i = 0; i < 3+1; i++){
            System.out.println(Arrays.toString(mgmt.getKeyForEncription(i)));
        }
    }

    private void keyIntoArray(String k) throws IllegalArgumentException {
        if(s != k.length()){
            throw new IllegalArgumentException("keylenght is not s");
        }
        else {
            key = new int[s];
            for(int i = 0; i < s; i++){
                if(k.charAt(i) == '1'){
                    key[i] = 1;
                }
                else if(k.charAt(i) == '0'){
                    key[i] = 0;
                }
                else{
                    throw new IllegalArgumentException("chars other than 0 or 1 in key");
                }
            }
        }
    }


    /**
     * Roundkey i is n*m bits from key beginning position 4i
     * Example
     * -> r:3, s:24, m:3, n:4, k:0001 1010 1111 1100 0000 0111
     * -> parameter i:3 returns k3: 1100 0000 0111
     * */
    public int[] getKeyForEncription(int i) throws IllegalArgumentException {
        int[] roundkey = new int[n*m];
        if(i < 0 || i > r){
            throw new IllegalArgumentException("There is no roundkey i for " + r + " rounds");
        }
        else if(4*i + n*m > s){
            throw new IllegalArgumentException("Roundkey i out of bounds");
        }
        else {
            for(int j = 4*i; j < 4*i + n*m; j++){
                roundkey[j-4*i] = key[j];
            }
        }
        return roundkey;
    }

    /**
     * i = 0 or i = r then k(i,r-i)
     * else k(i,r-i) with beta permutated
     * */
    public int[] getKeyForDecription(int i, int k){
        return null;
    }
}
