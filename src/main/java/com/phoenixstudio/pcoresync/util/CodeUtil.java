package com.phoenixstudio.pcoresync.util;

import java.util.*;

public class CodeUtil {

    private static Map<UUID, String> codes = new HashMap<>();

    public static String generateUniqueCode(UUID uuid) {
        if (codes.containsKey(uuid)) return null;
        char[] numbers = "0123456789".toCharArray();
        char[] alphabet = "abcdefghjklmnopqrstuvwxyz".toUpperCase().toCharArray();
        StringBuilder sb = new StringBuilder();
        int x = 2;
        Random rand = new Random();

        for (int i = 0; i < 6; i++) {
            if (x == 0) {
                int randomIndex = rand.nextInt(numbers.length);
                sb.append(numbers[randomIndex]);
                char[] newNumbers = new char[numbers.length - 1];
                System.arraycopy(numbers, 0, newNumbers, 0, randomIndex);
                System.arraycopy(numbers, randomIndex + 1, newNumbers, randomIndex, numbers.length - randomIndex - 1);
                numbers = newNumbers;

                x = 2;
            } else {
                int randomIndex2 = rand.nextInt(alphabet.length);
                sb.append(alphabet[randomIndex2]);
                char[] newAlphabet = new char[alphabet.length - 1];
                System.arraycopy(alphabet, 0, newAlphabet, 0, randomIndex2);
                System.arraycopy(alphabet, randomIndex2 + 1, newAlphabet, randomIndex2, alphabet.length - randomIndex2 - 1);
                alphabet = newAlphabet;

                x--;
            }
        }

        while (codes.containsValue(sb.toString())) {
            sb = new StringBuilder(generateUniqueCode(uuid));
        }

        codes.put(uuid,sb.toString());

        return sb.toString();
    }

}
