package com.tip.b18.electronicsales.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum Status {
    PENDING("Pending"),
    SHIPPING("Shipping"),
    COMPLETED("Completed"),
    CANCELED("Canceled");

    private final String displayName;
}
