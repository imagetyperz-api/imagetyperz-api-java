package com.example;

import com.imagetyperzapi.ImageTyperzAPI;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class Example {
    // test_api API method
    private static void test_api() throws Exception {
        // get access token from: http://www.imagetyperz.com/Forms/ClientHome.aspx
        String access_token = "access_token_here";
        ImageTyperzAPI i = new ImageTyperzAPI(access_token);      // init API with your access token

        // legacy way, will get deprecated at some point
        //i.set_user_and_password("your_username", "your_password");

        // get balance
        // -------------------------
        String balance = i.account_balance();
        System.out.println(String.format("Balance: %s", balance));

        // solve normal captcha
        // --------------------
        HashMap<String, String> image_params = new HashMap<String, String>();       // optional image params
//        image_params.put("iscase", "true");         // case sensitive captcha
//        image_params.put("isphrase", "true");       // text contains at least one space (phrase)
//        image_params.put("ismath", "true");         // instructs worker that a math captcha has to be solved
//        image_params.put("alphanumeric", "2");      // 1 - digits only, 2 - letters only
//        image_params.put("minlength", "1");         // captcha text length (minimum)
//        image_params.put("maxlength", "8");         // captcha text length (maximum)

        System.out.println("Waiting for captcha to be solved ...");
        String captcha_text = i.solve_captcha("/home/user/captcha.jpg", image_params);
        System.out.println(String.format("Captcha text: %s", captcha_text));

        // solve recaptcha
        // -------------------------
        // check: https://www.github.com/imagetyperz-api/imagetyperz-api-java on how to get page_url and googlekey
        // submit captcha first to get ID
        HashMap<String, String> d = new HashMap<String, String>();
        d.put("page_url", "your_page_url");     // add --capy to the end of it to make it a capy captcha
        d.put("sitekey", "your_sitekey");
//        d.put("type", "3");                 // optional
//        d.put("v3_min_score", "0.1");       // optional
//        d.put("v3_action", "homepage");     // optional
//        d.put("proxy", "126.45.34.53:123"); // or with auth 126.45.34.53:123:user:pass - optional
//        d.put("user_agent", "Your user agent"); // optional
        String captcha_id = i.submit_recaptcha(d);
        System.out.println("Waiting for recaptcha to be solved ...");
        while(i.in_progress(captcha_id))
        {
            TimeUnit.SECONDS.sleep(10);     // sleep for 10 seconds
        }
        // completed at this point
        String recaptcha_response = i.retrieve_captcha(captcha_id);     // get recaptcha response
        System.out.println(String.format("Recaptcha response: %s", recaptcha_response));

        // Geetest
        // ------------------------------------------
//        HashMap<String, String> gp = new HashMap<String, String>();
//        gp.put("domain", "example.com");
//        gp.put("challenge", "challenge here");
//        gp.put("gt", "gt here");
//        //gp.put("proxy", "126.45.34.53:123"); // or with auth 126.45.34.53:123:user:pass - optional
//        //gp.put("user_agent", "Your user agent"); // optional
//
//        String geetest_id = i.submit_geetest(gp);
//        System.out.println(String.format("Geetest captcha id: %s", geetest_id));
//        System.out.println("Waiting for geetest captcha to be solved ...");
//
//        // retrieve
//        // ---------
//        while (i.in_progress(geetest_id)) Thread.sleep(10000);      // sleep for 10 seconds and retry
//
//        // we got a response at this point
//        // ---------------------------------
//        HashMap<String, String> gr = i.retrieve_geetest(geetest_id);     // get the response
//        System.out.println(String.format("Geetest response: %s - %s - %s", gr.get("challenge"),
//            gr.get("validate"), gr.get("seccode")));
//

        // Other examples
        // ----------------------
        // com.imagetyperzapi.ImageTyperzAPI i = new com.imagetyperzapi.ImageTyperzAPI("your_token", "123");      // example with affiliate id
        // System.out.println(i.was_proxy_used(captcha_id));                          // check if proxy submitted was used in solving
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
