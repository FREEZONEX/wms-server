package com.supos.app.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.UUID;

public class CommonUtils {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    public static long generateUnsignedLongUUID() {
        UUID uuid = UUID.randomUUID();
        byte[] bytes = new byte[16];
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();
        // Convert the most significant bits and least significant bits to a byte array
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (mostSigBits >>> (56 - i * 8));
            bytes[i + 8] = (byte) (leastSigBits >>> (56 - i * 8));
        }
        // Convert the byte array to a BigInteger
        BigInteger bigInteger = new BigInteger(1, bytes);
        // Get the long value of the BigInteger
        long result = bigInteger.longValue();
        // Ensure the value is positive
        return result >= 0 ? result : -result;
    }
}
