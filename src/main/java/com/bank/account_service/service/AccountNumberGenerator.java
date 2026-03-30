package com.bank.account_service.service;

import org.springframework.stereotype.Component;
import java.util.Random;

@Component public class AccountNumberGenerator {
    public String generateAccountNumber() {
        Random random = new Random();
        int number = 10000000 + random.nextInt(90000000);
        return "ACC" + number;
    }
}
