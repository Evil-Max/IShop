package com.test.ishop.domain;

public enum ProdStatus {
    AVAILABLE,
    ON_ORDER,
    NO_AVAILABLE;

    public static Integer getStatusByName(String name) {
        if ((name==null)||(name.isEmpty())) return null;
        switch (name) {
            case "В наличии":
                return ProdStatus.AVAILABLE.ordinal();
            case "Под заказ":
                return ProdStatus.ON_ORDER.ordinal();
            default:
                return ProdStatus.NO_AVAILABLE.ordinal();
        }
    }
}
