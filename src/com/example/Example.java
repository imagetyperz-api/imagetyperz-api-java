package com.example;

import com.example.captcha.*;
import com.imagetyperzapi.ImageTyperzAPI;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


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
        }
        catch (Exception ex)
        {
            System.out.println(String.format("Error occurred: %s", ex.getMessage()));     // print exception message
        }
    }
}
