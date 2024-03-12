package org.example;

import java.security.Key;

public class Decipher {

    private Integer[] permutation;
    private var sBox;
    private var sInv;
    private KeyMgmt key;

    public Decipher(Integer[] permutation, var sBox, KeyMgmt key) {
        this.permutation = permutation;
        this.sBox = sBox;
        this.key=key;
        this.sInv=inverse(sBox);
    }

    private var inverse(var sBox) {
        // TODO: inverse sBox
        return null;
    }

    public int doDecipher(int text){

        //TODO: decipher
        return 0;
    }


}
