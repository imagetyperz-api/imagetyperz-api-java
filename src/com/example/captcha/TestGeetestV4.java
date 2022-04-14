package com.example.captcha;

import com.example.Config;
import com.imagetyperzapi.ImageTyperzAPI;
import com.imagetyperzapi.Utils;

import java.util.HashMap;


public class TestGeetestV4 {
    // test_api API method
    public static void run() throws Exception {
        // get access token from: http://www.imagetyperz.com/Forms/ClientHome.aspx
        ImageTyperzAPI i = new ImageTyperzAPI(Config.TOKEN);      // init API with your access token

        // get balance
        String balance = i.account_balance();
        System.out.println(String.format("Balance: %s", balance));

        // solve captcha
        HashMap<String, String> d = new HashMap<String, String>();
        d.put("domain", "https://example.com");
        d.put("geetestid", "647f5ed2ed8acb4be36784e01556bb71");
//        d.put("proxy", "126.45.34.53:123"); // or with auth 126.45.34.53:123:user:pass - optional
//        d.put("user_agent", "Your user agent"); // optional
        String captcha_id = i.submit_geetest_v4(d);
        System.out.println("Waiting for captcha to be solved ...");
        HashMap<String, String> response = null;
        while (response == null) {
            Thread.sleep(10000);
            response = i.retrieve_response(captcha_id);
        }
        Utils.print_response(response);
    }
}
