/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.common.codec;

import cn.sliew.scaleph.common.constant.Constants;

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

    public static String encrypt(String str) {
        return Constants.CODEC_STR_PREFIX + encodeToBase64(str);
    }

    public static String decrypt(String str) {
        return decodeFromBase64(str.substring(Constants.CODEC_STR_PREFIX.length()));
    }

    public static boolean isEncryptedStr(String str) {
        return str.startsWith(Constants.CODEC_STR_PREFIX);
    }
}
