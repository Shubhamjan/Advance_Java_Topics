package com.demo.service;

import com.demo.entity.BlackListToken;
import com.demo.repository.BlackListRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BlackListService{

    @Autowired
    private BlackListRepo blackListRepo;

    public BlackListToken blackListToken(String token, LocalDateTime expiry) {

        BlackListToken blackListToken = new BlackListToken();
        blackListToken.setToken(token);
        blackListToken.setExpiryDate(expiry);

       BlackListToken b = blackListRepo.save(blackListToken);
       return b;
    }

    public boolean isTokenBlackListed(String token) {
        return blackListRepo.existsByToken(token);
    }


    @Transactional
    @Scheduled(fixedRate = 3600000)
    public void removeExiredToken(){
        blackListRepo.deleteExpiredToken(LocalDateTime.now());
    }

}
