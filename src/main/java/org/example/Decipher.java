package org.example;

import java.util.HashMap;
import java.util.Map;

public class Decipher {

    private Integer[] permutation;
    private Map sBox;
    private Map sInv;
    private KeyMgmt key;

    public Decipher(Integer[] permutation, Map<Integer,Integer> sBox, KeyMgmt key) {
        this.permutation = permutation;
        this.sBox = sBox;
        this.key=key;
        this.sInv=inverse(sBox);
    }

    private Map inverse(Map sBox) {
        Map<Integer,Integer> map = new HashMap<>();
        for (Object o : sBox.keySet()) {
            Integer key = (Integer) o;
            Integer value = (Integer) sBox.get(key);
            map.put(value,key);
        }
        return map;
    }

    public int doDecipher(int text){

        //TODO: decipher
        return 0;
    }


}
