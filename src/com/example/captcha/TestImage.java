package com.example.captcha;

import com.example.Config;
import com.imagetyperzapi.ImageTyperzAPI;
import com.imagetyperzapi.Utils;
import org.json.JSONObject;

import java.util.HashMap;


public class TestImage {
    // test_api API method
    public static void run() throws Exception {
        // get access token from: http://www.imagetyperz.com/Forms/ClientHome.aspx
        ImageTyperzAPI i = new ImageTyperzAPI(Config.TOKEN);      // init API with your access token

        // get balance
        String balance = i.account_balance();
        System.out.println(String.format("Balance: %s", balance));

        // solve captcha
        HashMap<String, String> image_params = new HashMap<String, String>();       // optional image params
//        image_params.put("iscase", "true");         // case sensitive captcha
//        image_params.put("isphrase", "true");       // text contains at least one space (phrase)
//        image_params.put("ismath", "true");         // instructs worker that a math captcha has to be solved
//        image_params.put("alphanumeric", "2");      // 1 - digits only, 2 - letters only
//        image_params.put("minlength", "1");         // captcha text length (minimum)
//        image_params.put("maxlength", "8");         // captcha text length (maximum)

        System.out.println("Waiting for captcha to be solved ...");
        String captcha_id = i.submit_image("/tmp/captcha.jpg", image_params);
        HashMap<String, String> response = i.retrieve_response(captcha_id);
        Utils.print_response(response);
    }
}
