package com.tip.b18.electronicsales.services;

import com.tip.b18.electronicsales.dto.CustomList;
import com.tip.b18.electronicsales.dto.DailySummaryDTO;
import com.tip.b18.electronicsales.dto.ProductDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface StatisticService {
    DailySummaryDTO getDailySummary();
    CustomList<ProductDTO> getTopProducts(int limit, String startDay, String endDay);
}
