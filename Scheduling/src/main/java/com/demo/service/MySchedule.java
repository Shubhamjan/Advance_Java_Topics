package com.demo.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MySchedule {

//    @Scheduled(fixedRate = 5000)
//    public void runTask() {
//        System.out.println("The task run at "+System.currentTimeMillis());
//    }

//    @Scheduled(fixedDelay = 5000)
//    public void fixedDelay(){
//        System.out.println("The task run at "+System.currentTimeMillis());
//    }

    @Scheduled(cron = "0 50 15 * * ?")
    public void fixedDelay(){
        System.out.println("The task run at "+System.currentTimeMillis());
    }


}
