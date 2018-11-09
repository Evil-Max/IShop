package com.test.ishop.domain;

public enum ProdStatus {
    AVAILABLE,
    ON_ORDER,
    NO_AVAILABLE;

    public static Integer getStatusByName(String name) {
        if ((name==null)||(name.isEmpty())) return null;
        if (name.equals("В наличии")) return ProdStatus.AVAILABLE.ordinal();
        else if (name.equals("Под заказ")) return ProdStatus.ON_ORDER.ordinal();
        else return ProdStatus.NO_AVAILABLE.ordinal();
    }
}
