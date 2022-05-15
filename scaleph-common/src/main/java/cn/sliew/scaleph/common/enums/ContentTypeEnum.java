package cn.sliew.scaleph.common.enums;

import cn.hutool.core.util.StrUtil;

public enum ContentTypeEnum {
    DEFAULT("default", "application/octet-stream"),
    JPG("jpg", "image/jpeg"),
    TIFF("tiff", "image/tiff"),
    GIF("gif", "image/gif"),
    JFIF("jfif", "image/jpeg"),
    PNG("png", "image/png"),
    TIF("tif", "image/tiff"),
    ICO("ico", "image/x-icon"),
    JPEG("jpeg", "image/jpeg"),
    WBMP("wbmp", "image/vnd.wap.wbmp"),
    FAX("fax", "image/fax"),
    NET("net", "image/pnetvue"),
    JPE("jpe", "image/jpeg"),
    RP("rp", "image/vnd.rn-realpix");

    private String prefix;

    private String type;

    public static String getContentType(String prefix) {
        if (StrUtil.isEmpty(prefix)) {
            return DEFAULT.getType();
        }
        prefix = prefix.substring(prefix.lastIndexOf(".") + 1);
        for (ContentTypeEnum value : ContentTypeEnum.values()) {
            if (prefix.equalsIgnoreCase(value.getPrefix())) {
                return value.getType();
            }
        }
        return DEFAULT.getType();
    }

    ContentTypeEnum(String prefix, String type) {
        this.prefix = prefix;
        this.type = type;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getType() {
        return type;
    }
}
