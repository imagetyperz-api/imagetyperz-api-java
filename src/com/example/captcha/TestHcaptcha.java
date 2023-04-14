package com.example.captcha;

import com.example.Config;
import com.imagetyperzapi.ImageTyperzAPI;
import com.imagetyperzapi.Utils;

import java.util.HashMap;


public class TestHcaptcha {
    // test_api API method
    public static void run() throws Exception {
        // get access token from: http://www.imagetyperz.com/Forms/ClientHome.aspx
        ImageTyperzAPI i = new ImageTyperzAPI(Config.TOKEN);      // init API with your access token

        // get balance
        String balance = i.account_balance();
        System.out.printf("Balance: %s%n", balance);

        // solve captcha
        HashMap<String, String> d = new HashMap<String, String>();
        d.put("page_url", "https://your-site.com");
        d.put("sitekey", "8c7062c7-cae6-4e12-96fb-303fbec7fe4f");
        // if invisible hcaptcha - optional
        // d.put("invisible", "1");

        // domain used in loading of hcaptcha interface, default: hcaptcha.com - optional
        // d.put("domain", "hcaptcha.com");

        // extra parameters, useful for enterprise
        // submit userAgent from requests too, when this is used
        // d.put("HcaptchaEnterprise", "{\"rqdata\": \"value taken from web requests\"}");

        // d.put("proxy", "126.45.34.53:123"); // or with auth 126.45.34.53:123:user:pass - optional
        // d.put("user_agent", "Your user agent"); // optional
        String captcha_id = i.submit_hcaptcha(d);
        System.out.println("Waiting for captcha to be solved ...");
        HashMap<String, String> response = null;
        while (response == null) {
            Thread.sleep(10000);
            response = i.retrieve_response(captcha_id);
        }
        Utils.print_response(response);
    }
}
