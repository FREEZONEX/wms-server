package com.supos.app.service.cron;

import com.supos.app.service.SuposService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SuposCron {

    @Autowired
    SuposService suposService;

    @Scheduled(cron = "0 0 * * * ?") //execute with per hour
    public void syncUsers() {
        suposService.syncUserData();
    }
}
