/*
Copyright © 2010 to Present, Local Matters, Inc.
All rights reserved.
*/
package org.localmatters.lesscss4j.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hex {
    /**
     * Used building output as Hex
     */
    private static final char[] DIGITS = {
        '0', '1', '2', '3', '4', '5', '6', '7',
           '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * Converts an array of bytes into an array of characters representing the hexidecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data
     *                  a byte[] to convert to Hex characters
     * @return A char[] containing hexidecimal characters
     */
    public static char[] encodeHex(byte[] data) {

        int l = data.length;

           char[] out = new char[l << 1];

           // two characters form the hex value.
           for (int i = 0, j = 0; i < l; i++) {
               out[j++] = DIGITS[(0xF0 & data[i]) >>> 4 ];
               out[j++] = DIGITS[ 0x0F & data[i] ];
           }

           return out;
    }

    public static String md5(byte[] value) {
        try {
            return new String(Hex.encodeHex(MessageDigest.getInstance("MD5").digest(value)));
        }
        catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Unable to find MD5 MessageDigest instance", ex);
        }

    }
}
