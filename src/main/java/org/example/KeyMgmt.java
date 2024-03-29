package org.example;

import java.util.Arrays;

public class KeyMgmt {

    private final int r;
    private final int s;
    private final int n;
    private final int m;
    private final int[] beta;
    private int[] key;

    /**
     * r: How many rounds for encryption & decryption
     * s: Length of key
     * n * m: Length of roundkey i
     * k: The full key who will get split into roundkeys
     * beta: Bit at index i has the index beta[i] after the permutation
     */
    public KeyMgmt(int r, int s, int n, int m, String k, int[] beta) {
        this.r = r;
        this.s = s;
        this.n = n;
        this.m = m;
        this.beta = beta;
        keyIntoArray(k);
    }

    private void keyIntoArray(String k) throws IllegalArgumentException {
        if (s != k.length()) {
            throw new IllegalArgumentException("Keylenght is not s");
        } else {
            key = new int[s];
            for (int i = 0; i < s; i++) {
                if (k.charAt(i) == '1') {
                    key[i] = 1;
                } else if (k.charAt(i) == '0') {
                    key[i] = 0;
                } else {
                    throw new IllegalArgumentException("Chars other than 0 or 1 in key");
                }
            }
        }
    }


    /**
     * Roundkey i is n*m bits from key beginning position 4i
     * Example
     * -> r:3, s:24, m:3, n:4, k:0001 1010 1111 1100 0000 0111
     * -> parameter i:3 returns k3: 1100 0000 0111
     */
    public int[] getKeyForEncription(int i) throws IllegalArgumentException {
        int[] roundkey = new int[n * m];
        if (i < 0 || i > r) {
            throw new IllegalArgumentException("There is no roundkey i for " + r + " rounds");
        } else if (4 * i + n * m > s) {
            throw new IllegalArgumentException("Roundkey i out of bounds");
        } else {
            if (4 * i + n * m - 4 * i >= 0) {
                System.arraycopy(key, 4 * i, roundkey, 0, 4 * i + n * m - 4 * i);
            }
        }
        return roundkey;
    }

    /**
     * i = 0 or i = r then return roundkey r-i
     * else return roundkey r-i with beta permutated
     */
    public int[] getKeyForDecription(int i) throws IllegalArgumentException {
        int[] tmpRoundkey = getKeyForEncription(r - i);
        if (i == 0 || i == r) {
            return tmpRoundkey;
        } else {
            if (beta.length != n * m) {
                throw new IllegalArgumentException("Beta has not the right length");
            } else {
                int[] roundkey = new int[n * m];
                for (int j = 0; j < n * m; j++) {
                    if (beta[j] > n * m - 1) {
                        throw new IllegalArgumentException("Beta has illegal values");
                    }
                    roundkey[j] = tmpRoundkey[beta[j]];
                }
                return roundkey;
            }
        }
    }
}
