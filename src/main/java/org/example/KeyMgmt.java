package org.example;

public class KeyMgmt {

    int r;
    int s;
    int n;
    int m;
    String k;
    Integer[] beta;

    /**
     * r: How many rounds for encryption & decryption
     * s: Length of key
     * n: Length one box of s-box
     * n * m: Length of roundkey i
     * k: The full key who will get split into roundkeys
     * */
    public KeyMgmt(int r, int s, int n, int m, String k, Integer[] beta){
        this.r = r;
        this.s = s;
        this.n = n;
        this.m = m;
        this.k = k;
        this.beta = beta;
    }

    /**
     * Roundkey i is 16 bits from key beginning position 4i
     * Example
     * -> r:4, s:32, m:4, n:4, k:0011 1010 1001 0100 1101 0110 0011 1111
     * -> parameter i:4 returns k4: 1101 0110 0011 1111 equals 54847
     * */
    public Integer getKeyForEncription(int i){
        return 0;
    }

    /**
     * i = 0 or i = r then k(i,r-i)
     * else k(i,r-i) with beta permutated
     * */
    public Integer getKeyForDecription(int i, int k){
        return 0;
    }
}
