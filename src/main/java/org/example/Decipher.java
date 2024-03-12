package org.example;

public class Decipher {

    private Integer[] permutation;
    private var sBox;
    private var sInv;
    private keyManagement key;

    public Decipher(Integer[] permutation, var sBox, keyManagement key) {
        this.permutation = permutation;
        this.sBox = sBox;
        this.key=key;
        this.sInv=inverse(sBox);
    }

    private var inverse(var sBox) {
        // TODO: inverse sBox
        return null;
    }




}
