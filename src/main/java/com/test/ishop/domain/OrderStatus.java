package com.test.ishop.domain;

public enum OrderStatus {
    NEW("Новый"), // (зарезервировано)
    CANCELED("Отменен"),
    CONFIRMED("Сформирован"),
    SENDED("Отправлен"),
    DELIVERED("Доставлен"),
    PAYED("Оплачен");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
