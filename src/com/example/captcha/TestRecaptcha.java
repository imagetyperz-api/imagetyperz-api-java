package com.example.captcha;

import com.example.Config;
import com.imagetyperzapi.ImageTyperzAPI;
import com.imagetyperzapi.Utils;

import java.util.HashMap;


public class TestRecaptcha {
    // test_api API method
    public static void run() throws Exception {
        // get access token from: http://www.imagetyperz.com/Forms/ClientHome.aspx
        ImageTyperzAPI i = new ImageTyperzAPI(Config.TOKEN);      // init API with your access token

        // get balance
        String balance = i.account_balance();
        System.out.println(String.format("Balance: %s", balance));

        // solve captcha
        HashMap<String, String> d = new HashMap<String, String>();
        d.put("page_url", "https://your-site.com");
        d.put("sitekey", "7LrGJmcUABBAALFtIb_FxC0LXm_GwOLyJAfbbUCL");
        // reCAPTCHA type(s) - optional, defaults to 1
        // ---------------------------------------------
        // 1 - v2
        // 2 - invisible
        // 3 - v3
        // 4 - enterprise v2
        // 5 - enterprise v3
        //
        // d.put("type", "1");                 // optional
        //
        // d.put("v3_min_score", "0.1");       // optional
        // d.put("v3_action", "homepage");     // optional
        // d.put("proxy", "126.45.34.53:123"); // or with auth 126.45.34.53:123:user:pass - optional
        // d.put("user_agent", "Your user agent"); // optional
        // d.put("data-s", "recaptcha data-s value"); // optional
        // d.put("cookie_input", "a=b;c=d");   // optional
        String captcha_id = i.submit_recaptcha(d);
        System.out.println("Waiting for captcha to be solved ...");
        HashMap<String, String> response = null;
        while (response == null) {
            Thread.sleep(10000);
            response = i.retrieve_response(captcha_id);
        }
        Utils.print_response(response);

        // other examples
        // ImageTyperzAPI i = new ImageTyperzAPI("your_access_token", "123");  // initialize library with affiliate ID
        // i.set_captcha_bad(captcha_id);      // set captcha bad
    }
}
