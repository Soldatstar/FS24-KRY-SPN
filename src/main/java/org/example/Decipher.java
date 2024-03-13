package org.example;

import java.util.HashMap;
import java.util.Map;

public class Decipher {

    private final Integer[] permutation;
    private final Map<Integer,Integer> sBox;
    private final Map<Integer,Integer> sInv;
    private final KeyMgmt key;

    public Decipher(Integer[] permutation, Map<Integer,Integer> sBox, KeyMgmt key) {
        this.permutation = permutation;
        this.sBox = sBox;
        this.key=key;
        this.sInv=inverse(sBox);
    }

    private Map<Integer,Integer> inverse(Map<Integer,Integer> sBox) {
        Map<Integer,Integer> map = new HashMap<>();
        for (Integer o : sBox.keySet()) {
            Integer value = sBox.get(o);
            map.put(value, o);
        }
        return map;
    }

    public int doDecipher(int text){

        //TODO: do decipher rounds 0 to r-1




        //TODO: do last round r
        return 0;
    }


}
