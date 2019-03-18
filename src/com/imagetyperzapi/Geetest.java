package com.imagetyperzapi;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by icebox on 22/05/17.
 */
public class Geetest {
    private String _captcha_id = "";
    private String _response = "";
    public Geetest(String captcha_id)
    {
        this._captcha_id = captcha_id;
    }
    // Save captcha response
    public void set_response(String response)
    {
        this._response = response;
    }
    // Getter for ID
    public String captcha_id()
    {
        return this._captcha_id;
    }
    // Getter for response
    public HashMap<String, String> response() throws Exception {
        String[] s = this._response.split(";;;");
        // check length of response we've got
        if(s.length != 3) throw new Exception("invalid geetest response: " + this._response);
        HashMap<String, String> j = new HashMap<>();
        j.put("challenge", s[0]);
        j.put("validate", s[1]);
        j.put("seccode", s[2]);
        return j;
    }
}
