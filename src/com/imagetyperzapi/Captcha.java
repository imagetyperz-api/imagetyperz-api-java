package com.imagetyperzapi;

/**
 * Created by icebox on 22/05/17.
 */
public class Captcha {
    // we'll use this variables, to store last captcha processed/solved
    private String _captcha_id = "";
    private String _text = "";

    // in case it cannot parse the response, throws exception
    public Captcha(String response) throws Exception {
        this.parse_response(response);
    }
    // parse response (split it into id and text)
    private void parse_response(String response) throws Exception {
        int i = response.indexOf("|");
        if (i == -1) {
            throw new Exception(String.format("cannot parse response from server: %s", response));
        }

        // save captcha id to obj
        this._captcha_id = response.substring(0, i);                    // get id/substr from response
        this._text = response.substring(i + 1, response.length());      // get text/substr from response
    }
    // Getter last captcha_id
    public String captcha_id()
    {
        return this._captcha_id;
    }
    // Getter last captcha text
    public String text()
    {
        return this._text;
    }
}
