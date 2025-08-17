package com.example.minierp.metrics;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardViewController {

    /**
     * 대시보드 메인 화면
     *
     * @return material/dashboard.html
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        return "material/dashboard";
    }

}