package com.example;

import com.imagetyperzapi.ImageTyperzAPI;
import java.util.concurrent.TimeUnit;

public class Example {
    // test_api API method
    private static void test_api() throws Exception {
        // get access token from: http://www.imagetyperz.com/Forms/ClientHome.aspx
        String access_token = "your_access_token";
        ImageTyperzAPI i = new ImageTyperzAPI(access_token);      // init API with your access token

        // legacy way, will get deprecated at some point
        //i.set_user_and_password("your_username", "your_password");

        // get balance
        // -------------------------
        String balance = i.account_balance();
        System.out.println(String.format("Balance: %s", balance));

        // solve normal captcha
        // --------------------
        System.out.println("Waiting for captcha to be solved ...");
        String captcha_text = i.solve_captcha("http://scurt.pro/captcha.jpg", false);
        System.out.println(String.format("Captcha text: %s", captcha_text));

        // solve recaptcha
        // -------------------------
        // check: http://www.imagetyperz.com/Forms/recaptchaapi.aspx on how to get page_url and googlekey
        String page_url = "your_pageurl_here";
        String sitekey = "your_sitekey_here";
        // submit captcha first to get ID
        String captcha_id = i.submit_recaptcha(page_url, sitekey);
        System.out.println("Waiting for recaptcha to be solved ...");
        while(i.in_progress(captcha_id))
        {
            TimeUnit.SECONDS.sleep(10);     // sleep for 10 seconds
        }
        // completed at this point
        String recaptcha_response = i.retrieve_captcha(captcha_id);     // get recaptcha response
        System.out.println(String.format("Recaptcha response: %s", recaptcha_response));

        // Other examples
        // ----------------------
        // com.imagetyperzapi.ImageTyperzAPI i = new com.imagetyperzapi.ImageTyperzAPI("your_token", "123");      // example with affiliate id
        // i.submit_recaptcha(page_url, sitekey, "127.0.0.1:1234");             // solve recaptcha with proxy
        // i.submit_recaptcha(page_url, sitekey, "127.0.0.1:1234:user:pass");             // solve recaptcha with proxy auth
        // System.out.println(i.set_captcha_bad(i.captcha_id()));                       // set captcha bad
        // System.out.println(i.captcha_id());                                        	// last captcha solved id
        // System.out.println(i.captcha_text());                                  	// last captcha solved text
        // System.out.println(i.recaptcha_id());                                        // last recaptcha solved id
        // System.out.println(i.recaptcha_response());                                  // last recaptcha solved response
        // System.out.println(i.error());                                               // last error
    }

    // main/run method
    public static void main(String[] args) {
        try
        {
            test_api();        // test_api API
        }
        catch(Exception ex)
        {
            System.out.println(String.format("Error occured: %s", ex.getMessage()));     // print exception message
        }
    }
}
