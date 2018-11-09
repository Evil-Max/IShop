package com.test.ishop.domain;

public enum DeliveryStatus {
    MAIL("Почтой"),
    COURIER("Курьером"),
    SELF("Самовывоз");

    private String name;

    DeliveryStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Integer getStatusByName(String name) {
        if ((name==null)||(name.isEmpty())) return null;
        for(DeliveryStatus d:DeliveryStatus.values())
            if (d.name.equals(name)) return d.ordinal();
        return null;
    }
}
