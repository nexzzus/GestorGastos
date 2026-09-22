package com.nexzus.gestiongastos.controller;

import com.nexzus.gestiongastos.dto.response.DashboardSummaryDto;
import com.nexzus.gestiongastos.security.jwt.JwtUtils;
import com.nexzus.gestiongastos.service.abstraction.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final IDashboardService dashboardService;
    private final JwtUtils jwtUtils;

    @GetMapping()
    public ResponseEntity<DashboardSummaryDto> getDashboard(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtUtils.extractUserId(token);
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getDashboardSummary(userId));
    }
}
