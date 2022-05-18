package cn.sliew.scaleph.common.codec;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public enum CodecUtil {
    ;

    private static Base64.Encoder encoder = Base64.getEncoder();
    private static Base64.Decoder decoder = Base64.getDecoder();

    public static String encodeToBase64(String text) {
        return encoder.encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeFromBase64(String text) {
        return new String(decoder.decode(text), StandardCharsets.UTF_8);
    }
}
