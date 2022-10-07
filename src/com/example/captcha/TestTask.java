package com.example.captcha;

import com.example.Config;
import com.imagetyperzapi.ImageTyperzAPI;
import com.imagetyperzapi.Utils;

import java.util.HashMap;


public class TestTask {
    // test_api API method
    public static void run() throws Exception {
        // get access token from: http://www.imagetyperz.com/Forms/ClientHome.aspx
        ImageTyperzAPI i = new ImageTyperzAPI(Config.TOKEN);      // init API with your access token

        // get balance
        String balance = i.account_balance();
        System.out.println(String.format("Balance: %s", balance));

        // solve captcha
        HashMap<String, String> d = new HashMap<String, String>();
        d.put("template_name", "Login test page");
        d.put("page_url", "https://imagetyperz.net/automation/login");
        d.put("variables", "{\"username\": \"abc\", \"password\": \"paZZW0rd\"}");
        // d.put("proxy", "126.45.34.53:123"); // or with auth 126.45.34.53:123:user:pass - optional
        // d.put("user_agent", "Your user agent"); // optional
        String captcha_id = i.submit_task(d);
        System.out.println("Waiting for captcha to be solved ...");

        // send pushVariable - update of variable while task is running (e.g 2FA code)
        // String code = "24323";
        // i.task_push_variables(captcha_id, "{\"twofactor_code\": \"" + code + "\"}");

        HashMap<String, String> response = null;
        while (response == null) {
            Thread.sleep(10000);
            response = i.retrieve_response(captcha_id);
        }
        Utils.print_response(response);
    }
}