package com.example;

import com.example.captcha.*;

public class Example {
    // main/run method
    public static void main(String[] args) {
        try
        {
            TestRecaptcha.run();
            // TestImage.run();
            // TestGeetest.run();
            // TestCapy.run();
            // TestHcaptcha.run();
            // TestTiktok.run();
            // TestFuncaptcha.run();
            // TestTurnstile.run();
            // TestTask.run();
        }
        catch (Exception ex)
        {
            System.out.printf("Error occurred: %s%n", ex.getMessage());     // print exception message
        }
    }
}
