package com.imagetyperzapi;

/**
 * Created by icebox on 22/05/17.
 */
public class Recaptcha {
    private String _captcha_id = "";
    private String _response = "";
    public Recaptcha(String captcha_id)
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
    public String response()
    {
        return this._response;
    }
}
