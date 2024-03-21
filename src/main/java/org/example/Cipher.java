package org.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Cipher {

    private final int r;
    private final int n;
    private final int m;
    private final int[] beta;
    private final Map<String, String> sBox;
    private final Map<String, String> sInv;
    private final KeyMgmt keys;
    private int[] inputText;

    /**
     * r: How many rounds for encryption
     * n: Length one box of s-box
     * m: How many s-boxes for round i
     * beta: Bit at index i has the index beta[i] after the permutation
     * sbox: Has n*n many mappings from bitstring with lenght n to another bitstring lenght n
     * keys: Generates the roundkeys
     */
    public Cipher(int r, int n, int m, int[] beta, Map<String, String> sBox, KeyMgmt keys) {
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
     */
    private Map<String, String> inverse(Map<String, String> sBox) {
        Map<String, String> map = new HashMap<>();
        for (String o : sBox.keySet()) {
            String value = sBox.get(o);
            map.put(value, o);
        }
        return map;
    }


    private void inputTextIntoArray(String k) throws IllegalArgumentException {
        if (n * m != k.length()) {
            throw new IllegalArgumentException("Input text length is not " + n * m);
        } else {
            inputText = new int[n * m];
            for (int i = 0; i < n * m; i++) {
                if (k.charAt(i) == '1') {
                    inputText[i] = 1;
                } else if (k.charAt(i) == '0') {
                    inputText[i] = 0;
                } else {
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
    private int[] xor(int[] text, int[] key) {
        if (text.length != key.length) {
            throw new IllegalArgumentException("Wrong keylength");
        } else {
            int[] result = new int[n * m];
            for (int i = 0; i < n * m; i++) {
                result[i] = text[i] ^ key[i];
            }
            return result;
        }
    }


    private int[] sboxOperation(int[] text) {
        return getInts(text, sBox);
    }

    private int[] sboxInverseOperation(int[] text) {
        return getInts(text, sInv);
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
    private int[] getInts(int[] text, Map<String, String> sBox) {
        String toMap = "";
        String mapping = "";

        for (int x = 0; x < m; x++) {
            for (int y = x * n; y < x * n + n; y++) {
                toMap += text[y];
            }

            if (!sBox.containsKey(toMap)) {
                throw new IllegalArgumentException("No mapping in sbox for " + toMap);
            } else {
                mapping += sBox.get(toMap);
            }

            toMap = "";
        }

        if (mapping.length() != n * m) {
            throw new IllegalArgumentException("sBox has values with wrong length");
        } else {
            int[] result = new int[n * m];
            for (int z = 0; z < n * m; z++) {
                if (mapping.charAt(z) == '1') {
                    result[z] = 1;
                } else if (mapping.charAt(z) == '0') {
                    result[z] = 0;
                } else {
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
    private int[] permutation(int[] text) {
        if (beta.length != n * m) {
            throw new IllegalArgumentException("Beta has not the right length");
        } else {
            int[] result = new int[n * m];
            for (int j = 0; j < n * m; j++) {
                if (beta[j] > n * m - 1) {
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
    public String chiffreText(String plainText) {
        int[] result = new int[n * m];
        inputTextIntoArray(plainText);
        for (int i = 0; i < r + 1; i++) {

            if (i == 0) {
                result = xor(inputText, keys.getKeyForEncription(0));
            } else if (i == r) {
                int[] tmp = sboxOperation(result);
                result = xor(tmp, keys.getKeyForEncription(r));
            } else {
                int[] tmp = sboxOperation(result);
                tmp = permutation(tmp);
                result = xor(tmp, keys.getKeyForEncription(i));
            }

        }

        String chiffre = "";
        for (int j = 0; j < n * m; j++) {
            chiffre += result[j];
        }
        return chiffre;
    }

    public String dechiffreText(String chiffretext) {
        int[] result = new int[n * m];
        inputTextIntoArray(chiffretext);

        for (int i = 0; i < r + 1; i++) {

            if (i == 0) {
                result = xor(inputText, keys.getKeyForDecription(0));
            } else if (i == r) {
                int[] tmp = sboxInverseOperation(result);
                result = xor(tmp, keys.getKeyForDecription(r));
            } else {
                int[] tmp = sboxInverseOperation(result);
                tmp = permutation(tmp);
                result = xor(tmp, keys.getKeyForDecription(i));
            }

        }

        String plainText = "";
        for (int j = 0; j < n * m; j++) {
            plainText += result[j];
        }
        return plainText;
    }

    //CTR MODE STUFF AFTER THIS LINE

    public String dechiffreTextCTR(String chiffreText) {
        StringBuilder plainText = new StringBuilder();

        String block0 = chiffreText.substring(0, 16); // Erster Block (Zähler) extrahieren

        chiffreText = chiffreText.substring(16); // entfernt den Zähler aus dem Chiffretext

        for (int i = 0; i < chiffreText.length(); i += 16) {
            String block = chiffreText.substring(i, i + 16);
            String counter = String.format("%16s", Integer.toBinaryString(Integer.parseInt(block0, 2) + i / 16))
                .replaceAll(" ", "0"); //erhöht den Zähler um 1 und konvertiert ihn in einen 16-Bit-String

            String encryptedCounter = chiffreText(counter); // verschlüsselt den Zähler

            // führt ein XOR zwischen dem verschlüsselten Zähler und dem Chiffretext-Block durch
            int[] xorResult = xor(convertStringToIntArray(block), convertStringToIntArray(encryptedCounter));

            for (int bit : xorResult) {
                plainText.append(bit);
            }
        }

        // Remove padding
        int lastOneIndex = plainText.lastIndexOf("1");
        if (lastOneIndex != -1) {
            plainText.delete(lastOneIndex, plainText.length());
        }

        return convertToASCII(plainText.toString());
    }

    /**
     * Die Methode chiffreTextCTR verschlüsselt einen ASCII-Text im CTR-Modus auf folgende Weise:
     * Zunächst wird der ASCII-Text in eine binäre Zeichenfolge umgewandelt.
     * Anschliessend wird ein Zufallswert rand generiert und die binäre Zeichenfolge so angepasst,
     * dass sie durch 16 teilbar ist. Ein weiterer Zufallswert y wird erzeugt und die binäre Zeichenfolge in
     * 16-Bit-Blöcke unterteilt. Für jeden Block wird ein Zählerwert n berechnet, der dann verschlüsselt wird.
     * Schliesslich werden die verschlüsselten Blöcke zu einer Ergebniszeichenfolge kombiniert,
     * die als verschlüsselte Zeichenfolge zurückgegeben wird.
     * */
    public String chiffreTextCTR(String ascitext){
        String text = convertToBinary(ascitext);
        StringBuilder result = new StringBuilder();
        int rand = new Random().nextInt((int)Math.pow(2, 16));

        if(text.length() % 16 != 0){
            text += "1";
            while(text.length() % 16 != 0){
                text += "0";
            }
        }

        String y = Integer.toBinaryString(rand);
        while(y.length() < 16){
            y = "0" + y;
        }
        result.append(y);

        for (int i = 0; i < text.length(); i += 16) {
            int n = (rand + (i/16)) % (int)Math.pow(2, 16);
            y = Integer.toBinaryString(n);
            while(y.length() < 16){
                y = "0" + y;
            }

            String z = chiffreText(y);
            String x = text.substring(i, i + 16);
            int[] xorResult = xor(convertStringToIntArray(z),convertStringToIntArray(x));
            for (int bit : xorResult) {
                result.append(bit);
            }
        }
        return result.toString();
    }


    // Hilfsmethode zum Konvertieren eines Bitstrings in ein int-Array
    private int[] convertStringToIntArray(String s) {
        int[] result = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            result[i] = Character.getNumericValue(s.charAt(i));
        }
        return result;
    }

    public String convertToASCII(String binary) {
        StringBuilder ascii = new StringBuilder();

        for (int i = 0; i < binary.length(); i += 8) {

            int endIndex = Math.min(i + 8, binary.length());
            String block = binary.substring(i, endIndex);
            int asciiValue = Integer.parseInt(block, 2);
            ascii.append((char) asciiValue);
        }

        return ascii.toString();
    }

    public String convertToBinary(String asciiText) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < asciiText.length(); i++) {
            int asciiValue = asciiText.charAt(i);
            String binaryString = Integer.toBinaryString(asciiValue);

            // Ensure each binary representation is 8 bits long by adding leading zeros if necessary
            while (binaryString.length() < 8) {
                binaryString = "0" + binaryString;
            }

            result.append(binaryString);
        }

        return result.toString();
    }
}

