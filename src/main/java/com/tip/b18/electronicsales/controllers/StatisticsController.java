package com.tip.b18.electronicsales.controllers;

import com.tip.b18.electronicsales.dto.DailySummaryDTO;
import com.tip.b18.electronicsales.dto.ResponseDTO;
import com.tip.b18.electronicsales.services.StatisticService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class StatisticsController {
    private final StatisticService statisticService;

    @GetMapping
    public ResponseDTO<DailySummaryDTO> getDailySummary(){
        ResponseDTO<DailySummaryDTO> responseDTO = new ResponseDTO<>();

        responseDTO.setStatus("success");
        responseDTO.setData((statisticService.getDailySummary()));

        return responseDTO;
    }
}
