package com.tip.b18.electronicsales.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum Status {
    PENDING("chờ xử lý"),
    SHIPPING("vận chuyển"),
    COMPLETED("hoàn thành"),
    CANCELED("hủy");

    private final String displayName;
}
