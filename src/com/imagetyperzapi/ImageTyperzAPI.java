package com.imagetyperzapi;

import java.io.File;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.*;

/**
 * Created by icebox on 22/05/17.
 */
public class ImageTyperzAPI {
    // consts
    private static String CAPTCHA_ENDPOINT = "http://captchatypers.com/Forms/UploadFileAndGetTextNEW.ashx";
    private static String RECAPTCHA_SUBMIT_ENDPOINT = "http://captchatypers.com/captchaapi/UploadRecaptchaV1.ashx";
    private static String RECAPTCHA_RETRIEVE_ENDPOINT = "http://captchatypers.com/captchaapi/GetRecaptchaText.ashx";
    private static String BALANCE_ENDPOINT = "http://captchatypers.com/Forms/RequestBalance.ashx";
    private static String BAD_IMAGE_ENDPOINT = "http://captchatypers.com/Forms/SetBadImage.ashx";
    private static String PROXY_CHECK_ENDPOINT = "http://captchatypers.com/captchaAPI/GetReCaptchaTextJSON.ashx";
    private static String GEETEST_SUBMIT_ENDPOINT = "http://captchatypers.com/captchaapi/UploadGeeTest.ashx";
    private static String GEETEST_RETRIEVE_ENDPOINT = "http://captchatypers.com/captchaapi/getrecaptchatext.ashx";

    private static String CAPTCHA_ENDPOINT_CONTENT_TOKEN = "http://captchatypers.com/Forms/UploadFileAndGetTextNEWToken.ashx";
    private static String CAPTCHA_ENDPOINT_URL_TOKEN = "http://captchatypers.com/Forms/FileUploadAndGetTextCaptchaURLToken.ashx";
    private static String RECAPTCHA_SUBMIT_ENDPOINT_TOKEN = "http://captchatypers.com/captchaapi/UploadRecaptchaToken.ashx";
    private static String RECAPTCHA_RETRIEVE_ENDPOINT_TOKEN = "http://captchatypers.com/captchaapi/GetRecaptchaTextToken.ashx";
    private static String BALANCE_ENDPOINT_TOKEN = "http://captchatypers.com/Forms/RequestBalanceToken.ashx";
    private static String BAD_IMAGE_ENDPOINT_TOKEN = "http://captchatypers.com/Forms/SetBadImageToken.ashx";
    private static String PROXY_CHECK_ENDPOINT_TOKEN = "http://captchatypers.com/captchaAPI/GetReCaptchaTextTokenJSON.ashx";
    private static String GEETEST_SUBMIT_ENDPOINT_TOKEN = "http://captchatypers.com/captchaapi/UploadGeeTestToken.ashx";

    private static String USER_AGENT = "JavaAPI1.0";      // user agent used in requests

    private String _access_token;
    private String _username;
    private String _password;
    private String _affiliate_id = "0";

    private Captcha _captcha;
    private Recaptcha _recaptcha;
    private Geetest _geetest;

    private String _error = "";

    public ImageTyperzAPI(String access_token)
    {
        this._access_token = access_token;
    }
    // Constructor with affiliateid
    public ImageTyperzAPI(String access_token, String affiliateid)
    {
        this._access_token = access_token;
        this._affiliate_id = affiliateid;
    }
    // Set username and password - old authentication method, token should be used though
    public void set_user_and_password(String user, String password)
    {
        this._username = user;
        this._password = password;
    }
    // solve normal captcha
    public String solve_captcha(String captcha_path, boolean case_sensitive) throws Exception {
        // file exists, create params of request
        String url = "";
        Map<String,Object> params = new LinkedHashMap<>();

        String image_data = "";

        if(this._username != null && !this._username.isEmpty())
        {
            params.put("username", this._username);
            params.put("password", this._password);
            url = CAPTCHA_ENDPOINT;
            File f = new File(captcha_path);
            // check if file exists
            if(!f.exists())
            {
                throw new Exception(String.format("File does not exist: %s", captcha_path));
            }
            image_data = Utils.read_file_b64(captcha_path);     // read file as b64
        }
        else
        {
            if(captcha_path.toLowerCase().startsWith("http"))
            {
                url = CAPTCHA_ENDPOINT_URL_TOKEN;
                image_data = captcha_path;      // set URL
            }
            else
            {
                url = CAPTCHA_ENDPOINT_CONTENT_TOKEN;
                File f = new File(captcha_path);
                // check if file exists
                if(!f.exists())
                {
                    throw new Exception(String.format("File does not exist: %s", captcha_path));
                }
                image_data = Utils.read_file_b64(captcha_path);     // read as b64
            }
            params.put("token", this._access_token);
        }

        params.put("action", "UPLOADCAPTCHA");
        params.put("chkCase", (case_sensitive) ? 1 : 0);
        params.put("file", image_data);

        // affiliate
        if(!this._affiliate_id.equals("0")) {
            params.put("affiliateid", this._affiliate_id);
        }

        // do request
        String response = Utils.post(url, params, USER_AGENT);

        // check if error
        int i = response.indexOf("ERROR:");
        if(i != -1)     // it's an error
        {
            String resp_err = response.substring(6, response.length()).trim();
            this._error = resp_err;         // save last error
            throw new Exception(resp_err);
        }

        // we have a good response here, create captcha obj
        if(response.contains("Uploading file..."))
        {
            response = response.replace("Uploading file...","");
        }
        Captcha c = new Captcha(response);
        this._captcha = c;      // save captcha to obj

        return this._captcha.text();
    }

    // Submit recaptcha with params
    public String submit_recaptcha(HashMap<String, String> d) throws Exception {
        String page_url = d.get("page_url");
        String sitekey = d.get("sitekey");

        // check vars first
        if(page_url.isEmpty())
        {
            throw new Exception("page_url variable is null or empty");
        }
        if(sitekey.isEmpty())
        {
            throw new Exception("sitekey variable is null or empty");
        }

        // create params with request
        String url = "";
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("action", "UPLOADCAPTCHA");
        params.put("pageurl", page_url);
        params.put("googlekey", sitekey);

        if(this._username != null && !this._username.isEmpty())
        {
            params.put("username", this._username);
            params.put("password", this._password);
            url = RECAPTCHA_SUBMIT_ENDPOINT;
        }
        else
        {
            params.put("token", this._access_token);
            url = RECAPTCHA_SUBMIT_ENDPOINT_TOKEN;
        }

        // add proxy
        if(d.containsKey("proxy")){
            params.put("proxy", d.get("proxy"));                 // with proxy
            params.put("proxytype", "HTTP");
        }

        // affiliate
        if(!this._affiliate_id.equals("0")) {
            params.put("affiliateid", this._affiliate_id);
        }

        // user agent
        if (d.containsKey("user_agent")) params.put("useragent", d.get("user_agent"));

        // v3
        if (d.containsKey("type")) params.put("recaptchatype", d.get("type"));
        if (d.containsKey("v3_action")) params.put("captchaaction", d.get("v3_action"));
        if (d.containsKey("v3_min_score")) params.put("score", d.get("v3_min_score"));

        // do request
        String response = Utils.post(url, params, USER_AGENT);

        // check if error
        int i = response.indexOf("ERROR:");
        if(i != -1)     // it's an error
        {
            String resp_err = response.substring(6, response.length()).trim();
            this._error = resp_err;         // save last error
            throw new Exception(resp_err);
        }

        // create recaptcha object with received captcha ID
        Recaptcha rc = new Recaptcha(response);
        this._recaptcha = rc;       // save to obj

        return response;        // return response
    }

    // Retrieve captcha response using captcha ID
    public String retrieve_captcha(String captcha_id) throws Exception {
        // file exists, create params of request
        String url = "";
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("action", "GETTEXT");
        params.put("captchaid", captcha_id);

        if(this._username != null && !this._username.isEmpty())
        {
            params.put("username", this._username);
            params.put("password", this._password);
            url = RECAPTCHA_RETRIEVE_ENDPOINT;
        }
        else
        {
            params.put("token", this._access_token);
            url = RECAPTCHA_RETRIEVE_ENDPOINT_TOKEN;
        }

        // do request
        String response = Utils.post(url, params, USER_AGENT);

        // check if error
        int i = response.indexOf("ERROR:");
        if(i != -1)     // it's an error
        {
            String resp_err = response.substring(6, response.length()).trim();
            if(response.indexOf("NOT_DECODED") == -1) {     // save it as obj error, only if it's not, NOT_DECODED
                this._error = resp_err;         // save last error
            }
            throw new Exception(resp_err);
        }

        // we got a good response at this point, save it to recapthca obj and return it
        this._recaptcha = new Recaptcha(captcha_id);
        this._recaptcha.set_response(response);
        return response;
    }

    // Submit geetest with params
    public String submit_geetest(HashMap<String, String> d) throws Exception {
        // check vars first
        if(!d.containsKey("domain")) throw new Exception("domain variable is missing");
        if(!d.containsKey("challenge")) throw new Exception("challenge variable is missing");
        if(!d.containsKey("gt")) throw new Exception("gt variable is missing");

        // create params with request
        String url = "";
        d.put("action", "UPLOADCAPTCHA");

        if(this._username != null && !this._username.isEmpty())
        {
            d.put("username", this._username);
            d.put("password", this._password);
            url = GEETEST_SUBMIT_ENDPOINT;
        }
        else
        {
            d.put("token", this._access_token);
            url = GEETEST_SUBMIT_ENDPOINT_TOKEN;
        }


        // affiliate
        if(!this._affiliate_id.equals("0")) {
            d.put("affiliateid", this._affiliate_id);
        }

        String params = Utils.map_to_url(d);
        params = params.substring(1, params.length());
        url = url + "?" + params;

        // do request
        String response = Utils.get(url, USER_AGENT);

        // check if error
        int i = response.indexOf("ERROR:");
        if(i != -1)     // it's an error
        {
            String resp_err = response.substring(6, response.length()).trim();
            this._error = resp_err;         // save last error
            throw new Exception(resp_err);
        }

        // create recaptcha object with received captcha ID
        this._geetest = new Geetest(response);

        return response;        // return response
    }

    // Retrieve geetest response using captcha ID
    public HashMap<String, String> retrieve_geetest(String captcha_id) throws Exception {
        // file exists, create params of request
        String url = "";
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("action", "GETTEXT");
        params.put("captchaid", captcha_id);

        if(this._username != null && !this._username.isEmpty())
        {
            params.put("username", this._username);
            params.put("password", this._password);
            url = GEETEST_RETRIEVE_ENDPOINT;
        }
        else
        {
            params.put("token", this._access_token);
            url = GEETEST_RETRIEVE_ENDPOINT;
        }

        // do request
        String response = Utils.post(url, params, USER_AGENT);

        // check if error
        int i = response.indexOf("ERROR:");
        if(i != -1)     // it's an error
        {
            String resp_err = response.substring(6, response.length()).trim();
            if(response.indexOf("NOT_DECODED") == -1) {     // save it as obj error, only if it's not, NOT_DECODED
                this._error = resp_err;         // save last error
            }
            throw new Exception(resp_err);
        }

        // we got a good response at this point, save it to recapthca obj and return it
        this._geetest.set_response(response);
        return this._geetest.response();
    }

    // Check if recaptcha still in progress of solving
    public boolean in_progress(String captcha_id) throws Exception {
        try
        {
            if(this._geetest != null) this.retrieve_geetest(captcha_id);
            else this.retrieve_captcha(captcha_id);
            return false;       // no error, we're good
        }
        catch(Exception ex)
        {
            if(ex.getMessage().contains("NOT_DECODED"))
            {
                return true;
            }
            // otherwise throw exception (if different error)
            throw ex;
        }
    }

    // Get account balance
    public String account_balance() throws Exception {
        // create params of request
        String url = "";
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("action", "REQUESTBALANCE");
        params.put("submit", "Submit");

        if(this._username != null && !this._username.isEmpty())
        {
            params.put("username", this._username);
            params.put("password", this._password);
            url = BALANCE_ENDPOINT;
        }
        else
        {
            params.put("token", this._access_token);
            url = BALANCE_ENDPOINT_TOKEN;
        }

        // do request
        String response = Utils.post(url, params, USER_AGENT);

        // check if error
        int i = response.indexOf("ERROR:");
        if(i != -1)     // it's an error
        {
            String resp_err = response.substring(6, response.length()).trim();
            this._error = resp_err;         // save last error
            throw new Exception(resp_err);
        }

        return String.format("$%s", response);      // return balance
    }

    // Set captcha bad
    public String set_captcha_bad(String captcha_id) throws Exception {
        // create params of request
        String url = "";
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("action", "SETBADIMAGE");
        params.put("imageid", captcha_id);
        params.put("submit", "Submissssst");

        if(this._username != null && !this._username.isEmpty())
        {
            params.put("username", this._username);
            params.put("password", this._password);
            url = BAD_IMAGE_ENDPOINT;
        }
        else
        {
            params.put("token", this._access_token);
            url = BAD_IMAGE_ENDPOINT_TOKEN;
        }

        // do request
        String response = Utils.post(url, params, USER_AGENT);

        // check if error
        int i = response.indexOf("ERROR:");
        if(i != -1)     // it's an error
        {
            String resp_err = response.substring(6, response.length()).trim();
            this._error = resp_err;         // save last error
            throw new Exception(resp_err);
        }

        return response;        // return response
    }

    // Tells if proxy was used with captcha, if not, why
    public String was_proxy_used(String captcha_id) throws Exception {
        // create params of request
        String url = "";
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("action", "GETTEXT");
        params.put("captchaid", captcha_id);

        if(this._username != null && !this._username.isEmpty())
        {
            params.put("username", this._username);
            params.put("password", this._password);
            url = PROXY_CHECK_ENDPOINT;
        }
        else
        {
            params.put("token", this._access_token);
            url = PROXY_CHECK_ENDPOINT_TOKEN;
        }

        // do request
        String response = Utils.post(url, params, USER_AGENT);
        response = response.substring(1, response.length() - 1);

        // parse as JSON
        JSONObject jsobj = new JSONObject(response);

        // check for error
        if(jsobj.has("Error")){
            String err = jsobj.getString("Error");
            this._error = err;
            throw new Exception(err);
        }

        // get all variables
        String result = jsobj.getString("Result");
        String p_worker = jsobj.getString("Proxy_worker");
        String p_client = jsobj.getString("Proxy_client");
        String p_reason = jsobj.getString("Proxy_reason");

        // check if we have a result or not
        if(result.trim().equals("")){
            String err = "captcha not completed yet";
            this._error = err;
            throw new Exception(err);
        }
        // check if client proxy was submitted
        if(p_client.trim().equals("")){
            return "no, reason: proxy was no sent with recaptcha submission request";
        }
        // check for reason
        if(!p_reason.trim().equals("")){
            return "no, reason: " + p_reason;
        }
        // check if proxy was used
        if(p_client.split(":").length >= 2 && p_client.equals(p_worker)){
            return "yes, used proxy: " + p_worker;
        }

        return "no, reason: unknown";
    }

    // Get last solved captcha text
    public String captcha_text()
    {
        if(this._captcha != null)
        {
            return this._captcha.text();
        }
        return "";      // return nothing if not initialized yet
    }
    // Get last solved captcha id
    public String captcha_id()
    {
        if(this._captcha != null)
        {
            return this._captcha.captcha_id();
        }
        return "";      // return nothing if not initialized yet
    }

    // Get last solved recaptcha response
    public String recaptcha_response()
    {
        if(this._recaptcha != null)
        {
            return this._recaptcha.response();
        }
        return "";      // return nothing if not initialized yet
    }
    // Get last solved recaptcha id
    public String recaptcha_id()
    {
        if(this._recaptcha != null)
        {
            return this._recaptcha.captcha_id();
        }
        return "";      // return nothing if not initialized yet
    }

    // Return last error
    public String error()
    {
        return this._error;
    }
}
