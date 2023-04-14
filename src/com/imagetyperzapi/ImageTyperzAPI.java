package com.imagetyperzapi;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

import org.json.*;

/**
 * Created by icebox on 22/05/17.
 */
public class ImageTyperzAPI {
    private final static String CAPTCHA_ENDPOINT = "http://captchatypers.com/Forms/UploadFileAndGetTextNEW.ashx";
    private final static String RECAPTCHA_SUBMIT_ENDPOINT = "http://captchatypers.com/captchaapi/UploadRecaptchaV1.ashx";
    private final static String RECAPTCHA_ENTERPRISE_SUBMIT_ENDPOINT = "http://captchatypers.com/captchaapi/UploadRecaptchaEnt.ashx";
    private final static String BALANCE_ENDPOINT = "http://captchatypers.com/Forms/RequestBalance.ashx";
    private final static String BAD_IMAGE_ENDPOINT = "http://captchatypers.com/Forms/SetBadImage.ashx";
    private final static String PROXY_CHECK_ENDPOINT = "http://captchatypers.com/captchaAPI/GetReCaptchaTextJSON.ashx";
    private final static String GEETEST_SUBMIT_ENDPOINT = "http://captchatypers.com/captchaapi/UploadGeeTest.ashx";
    private final static String TASK_ENDPOINT = "http://captchatypers.com/captchaapi/UploadCaptchaTask.ashx";
    private final static String TASK_PUSH_ENDPOINT = "http://captchatypers.com/CaptchaAPI/SaveCaptchaPush.ashx";

    private final static String HCAPTCHA_ENDPOINT = "http://captchatypers.com/captchaapi/UploadHCaptchaUser.ashx";
    private final static String CAPY_ENDPOINT = "http://captchatypers.com/captchaapi/UploadCapyCaptchaUser.ashx";
    private final static String TIKTOK_ENDPOINT = "http://captchatypers.com/captchaapi/UploadTikTokCaptchaUser.ashx";
    private final static String FUNCAPTCHA_ENDPOINT = "http://captchatypers.com/captchaapi/UploadFunCaptcha.ashx";
    private final static String TURNSTILE_ENDPOINT = "http://captchatypers.com/captchaapi/Uploadturnstile.ashx";
    private final static String RETRIEVE_JSON_ENDPOINT = "http://captchatypers.com/captchaapi/GetCaptchaResponseJson.ashx";

    private final static String CAPTCHA_ENDPOINT_CONTENT_TOKEN = "http://captchatypers.com/Forms/UploadFileAndGetTextNEWToken.ashx";
    private final static String CAPTCHA_ENDPOINT_URL_TOKEN = "http://captchatypers.com/Forms/FileUploadAndGetTextCaptchaURLToken.ashx";
    private final static String RECAPTCHA_SUBMIT_ENDPOINT_TOKEN = "http://captchatypers.com/captchaapi/UploadRecaptchaToken.ashx";
    private final static String BALANCE_ENDPOINT_TOKEN = "http://captchatypers.com/Forms/RequestBalanceToken.ashx";
    private final static String BAD_IMAGE_ENDPOINT_TOKEN = "http://captchatypers.com/Forms/SetBadImageToken.ashx";
    private final static String PROXY_CHECK_ENDPOINT_TOKEN = "http://captchatypers.com/captchaAPI/GetReCaptchaTextTokenJSON.ashx";
    private final static String GEETEST_SUBMIT_ENDPOINT_TOKEN = "http://captchatypers.com/captchaapi/UploadGeeTestToken.ashx";
    private final static String GEETEST_V4_SUBMIT_ENDPOINT = "http://www.captchatypers.com/captchaapi/UploadGeeTestV4.ashx";

    private final static String USER_AGENT = "JavaAPI1.0";      // user agent used in requests

    private final String _access_token;
    private String _username;
    private String _password;
    private String _affiliate_id = "0";

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

    public String submit_image(String captcha_path, HashMap<String, String> optional_parameters) throws Exception {
        // file exists, create params of request
        String url = "";
        Map<String,Object> params = new LinkedHashMap<>();

        // add optional parameters
        if(optional_parameters.size() > 0){
            for (String key : optional_parameters.keySet()) {
                params.put(key, optional_parameters.get(key));
            }
        }

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
            throw new Exception(resp_err);
        }

        // we have a good response here, create captcha obj
        if(response.contains("Uploading file..."))
        {
            response = response.replace("Uploading file...","");
        }
        return response.split(Pattern.quote("|"))[0];
    }

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

        // type / enterprise
        if (d.containsKey("type")) {
            params.put("recaptchatype", d.get("type"));
            // enterprise
            if (d.get("type").equals("4") || d.get("type").equals("5")) url = RECAPTCHA_ENTERPRISE_SUBMIT_ENDPOINT;
            if (d.get("type").equals("5")) params.put("enterprise_type", "v3");
        }
        if (d.containsKey("domain")) params.put("domain", d.get("domain"));
        if (d.containsKey("v3_action")) params.put("captchaaction", d.get("v3_action"));
        if (d.containsKey("v3_min_score")) params.put("score", d.get("v3_min_score"));
        if (d.containsKey("data-s")) params.put("data-s", d.get("data-s"));
        if (d.containsKey("cookie_input")) params.put("cookie_input", d.get("cookie_input"));
        // do request
        String response = Utils.post(url, params, USER_AGENT);

        // check if error
        int i = response.indexOf("ERROR:");
        if(i != -1)     // it's an error
        {
            String resp_err = response.substring(6, response.length()).trim();
            throw new Exception(resp_err);
        }

        return response;        // return response
    }

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

        // add proxy
        if(d.containsKey("proxy")){
            d.put("proxytype", "HTTP");
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
            throw new Exception(resp_err);
        }

        return response;        // return response
    }

    public String submit_geetest_v4(HashMap<String, String> d) throws Exception {
        // check vars first
        if(!d.containsKey("domain")) throw new Exception("domain variable is missing");
        if(!d.containsKey("geetestid")) throw new Exception("geetestid variable is missing");

        // create params with request
        String url = "";
        d.put("action", "UPLOADCAPTCHA");

        if(this._username != null && !this._username.isEmpty())
        {
            d.put("username", this._username);
            d.put("password", this._password);
        }
        else
        {
            d.put("token", this._access_token);
        }
        url = GEETEST_V4_SUBMIT_ENDPOINT;

        // add proxy
        if(d.containsKey("proxy")){
            d.put("proxytype", "HTTP");
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
            throw new Exception(resp_err);
        }

        return response;        // return response
    }

    public String submit_capy(HashMap<String, String> d) throws Exception {
        Map<String,Object> params = new LinkedHashMap<>();
        // check vars first
        if(!d.containsKey("page_url")) throw new Exception("page_url variable is missing");
        if(!d.containsKey("sitekey")) throw new Exception("sitekey variable is missing");

        // create params with request
        params.put("action", "UPLOADCAPTCHA");
        params.put("pageurl", d.get("page_url"));
        params.put("sitekey", d.get("sitekey"));
        params.put("captchatype", "12");

        if(this._username != null && !this._username.isEmpty())
        {
            params.put("username", this._username);
            params.put("password", this._password);
        }
        else params.put("token", this._access_token);

        // proxy
        if(d.containsKey("proxy")){
            params.put("proxy", d.get("proxy"));                 // with proxy
            params.put("proxytype", "HTTP");
        }
        // user agent
        if(d.containsKey("user_agent")){
            params.put("useragent", d.get("user_agent"));                 // with proxy
        }

        // affiliate
        if(!this._affiliate_id.equals("0")) params.put("affiliateid", this._affiliate_id);

        // do request
        String response = Utils.post(CAPY_ENDPOINT, params, USER_AGENT);

        // check if error
        int i = response.indexOf("ERROR:");
        if(i != -1)     // it's an error
        {
            String resp_err = response.substring(6, response.length()).trim();
            throw new Exception(resp_err);
        }
        JSONObject jsobj = new JSONObject(response.substring(1, response.length() - 1));
        return jsobj.getString("CaptchaId");        // return response
    }

    public String submit_hcaptcha(HashMap<String, String> d) throws Exception {
        Map<String,Object> params = new LinkedHashMap<>();
        // check vars first
        if(!d.containsKey("page_url")) throw new Exception("page_url variable is missing");
        if(!d.containsKey("sitekey")) throw new Exception("sitekey variable is missing");

        // create params with request
        params.put("action", "UPLOADCAPTCHA");
        params.put("pageurl", d.get("page_url"));
        params.put("sitekey", d.get("sitekey"));
        params.put("captchatype", "11");

        if(this._username != null && !this._username.isEmpty())
        {
            params.put("username", this._username);
            params.put("password", this._password);
        }
        else params.put("token", this._access_token);

        // proxy
        if(d.containsKey("proxy")){
            params.put("proxy", d.get("proxy"));                 // with proxy
            params.put("proxytype", "HTTP");
        }
        // user agent
        if(d.containsKey("user_agent")){
            params.put("useragent", d.get("user_agent"));                 // with proxy
        }
        // invisible
        if(d.containsKey("invisible")){
            params.put("invisible", "1");
        }
        // enterprise
        if(d.containsKey("HcaptchaEnterprise")){
            params.put("HcaptchaEnterprise", d.get("HcaptchaEnterprise"));
        }
        // domain
        if(d.containsKey("domain")){
            params.put("apiEndpoint", d.get("domain"));
        }

        // affiliate
        if(!this._affiliate_id.equals("0")) params.put("affiliateid", this._affiliate_id);

        // do request
        String response = Utils.post(HCAPTCHA_ENDPOINT, params, USER_AGENT);

        // check if error
        int i = response.indexOf("ERROR:");
        if(i != -1)     // it's an error
        {
            String resp_err = response.substring(6, response.length()).trim();
            throw new Exception(resp_err);
        }
        JSONObject jsobj = new JSONObject(response.substring(1, response.length() - 1));
        return jsobj.getString("CaptchaId");        // return response
    }

    public String submit_tiktok(HashMap<String, String> d) throws Exception {
        Map<String,Object> params = new LinkedHashMap<>();
        // check vars first
        if(!d.containsKey("page_url")) throw new Exception("page_url variable is missing");
        if(!d.containsKey("cookie_input")) throw new Exception("cookie_input variable is missing");

        // create params with request
        params.put("action", "UPLOADCAPTCHA");
        params.put("pageurl", d.get("page_url"));
        params.put("cookie_input", d.get("cookie_input"));
        params.put("captchatype", "10");

        if(this._username != null && !this._username.isEmpty())
        {
            params.put("username", this._username);
            params.put("password", this._password);
        }
        else params.put("token", this._access_token);

        // proxy
        if(d.containsKey("proxy")){
            params.put("proxy", d.get("proxy"));                 // with proxy
            params.put("proxytype", "HTTP");
        }
        // user agent
        if(d.containsKey("user_agent")){
            params.put("useragent", d.get("user_agent"));                 // with proxy
        }

        // affiliate
        if(!this._affiliate_id.equals("0")) params.put("affiliateid", this._affiliate_id);

        // do request
        String response = Utils.post(TIKTOK_ENDPOINT, params, USER_AGENT);

        // check if error
        int i = response.indexOf("ERROR:");
        if(i != -1)     // it's an error
        {
            String resp_err = response.substring(6, response.length()).trim();
            throw new Exception(resp_err);
        }
        JSONObject jsobj = new JSONObject(response.substring(1, response.length() - 1));
        return jsobj.getString("CaptchaId");        // return response
    }

    public String submit_funcaptcha(HashMap<String, String> d) throws Exception {
        Map<String,Object> params = new LinkedHashMap<>();
        // check vars first
        if(!d.containsKey("page_url")) throw new Exception("page_url variable is missing");
        if(!d.containsKey("sitekey")) throw new Exception("sitekey variable is missing");

        // create params with request
        params.put("action", "UPLOADCAPTCHA");
        params.put("pageurl", d.get("page_url"));
        params.put("sitekey", d.get("sitekey"));
        params.put("captchatype", "13");

        if(this._username != null && !this._username.isEmpty())
        {
            params.put("username", this._username);
            params.put("password", this._password);
        }
        else params.put("token", this._access_token);

        // s_url
        if(d.containsKey("s_url")){
            params.put("surl", d.get("s_url"));
        }
        // extra data
        if(d.containsKey("data")){
            params.put("data", d.get("data"));
        }

        // proxy
        if(d.containsKey("proxy")){
            params.put("proxy", d.get("proxy"));                 // with proxy
            params.put("proxytype", "HTTP");
        }

        // user agent
        if(d.containsKey("user_agent")){
            params.put("useragent", d.get("user_agent"));                 // with proxy
        }

        // affiliate
        if(!this._affiliate_id.equals("0")) params.put("affiliateid", this._affiliate_id);

        // do request
        String response = Utils.post(FUNCAPTCHA_ENDPOINT, params, USER_AGENT);

        // check if error
        int i = response.indexOf("ERROR:");
        if(i != -1)     // it's an error
        {
            String resp_err = response.substring(6, response.length()).trim();
            throw new Exception(resp_err);
        }
        JSONObject jsobj = new JSONObject(response.substring(1, response.length() - 1));
        return jsobj.getString("CaptchaId");        // return response
    }

    public String submit_turnstile(HashMap<String, String> d) throws Exception {
        Map<String,Object> params = new LinkedHashMap<>();
        // check vars first
        if(!d.containsKey("page_url")) throw new Exception("page_url variable is missing");
        if(!d.containsKey("sitekey")) throw new Exception("sitekey variable is missing");

        // create params with request
        params.put("action", "UPLOADCAPTCHA");
        params.put("pageurl", d.get("page_url"));
        params.put("sitekey", d.get("sitekey"));

        if(this._username != null && !this._username.isEmpty())
        {
            params.put("username", this._username);
            params.put("password", this._password);
        }
        else params.put("token", this._access_token);

        // proxy
        if(d.containsKey("proxy")){
            params.put("proxy", d.get("proxy"));                 // with proxy
            params.put("proxytype", "HTTP");
        }
        // user agent
        if(d.containsKey("user_agent")){
            params.put("useragent", d.get("user_agent"));                 // with proxy
        }
        // domain
        if(d.containsKey("domain")){
            params.put("apiEndpoint", d.get("domain"));                 // with proxy
        }
        // action
        if(d.containsKey("action")){
            params.put("taction", d.get("action"));                 // with proxy
        }
        // cdata
        if(d.containsKey("cdata")){
            params.put("data", d.get("cdata"));                 // with proxy
        }

        // affiliate
        if(!this._affiliate_id.equals("0")) params.put("affiliateid", this._affiliate_id);

        // do request
        String response = Utils.post(TURNSTILE_ENDPOINT, params, USER_AGENT);

        // check if error
        int i = response.indexOf("ERROR:");
        if(i != -1)     // it's an error
        {
            String resp_err = response.substring(6, response.length()).trim();
            throw new Exception(resp_err);
        }
        JSONObject jsobj = new JSONObject(response.substring(1, response.length() - 1));
        return jsobj.getString("CaptchaId");        // return response
    }

    public String submit_task(HashMap<String, String> d) throws Exception {
        Map<String,Object> params = new LinkedHashMap<>();
        // check vars first
        if(!d.containsKey("page_url")) throw new Exception("page_url variable is missing");
        if(!d.containsKey("template_name")) throw new Exception("template_name variable is missing");

        // create params with request
        params.put("action", "UPLOADCAPTCHA");
        params.put("pageurl", d.get("page_url"));
        params.put("captchatype", "16");
        params.put("template_name", d.get("template_name"));

        if(this._username != null && !this._username.isEmpty())
        {
            params.put("username", this._username);
            params.put("password", this._password);
        }
        else params.put("token", this._access_token);

        // variables
        if(d.containsKey("variables")){
            params.put("variables", d.get("variables"));
        }

        // proxy
        if(d.containsKey("proxy")){
            params.put("proxy", d.get("proxy"));                 // with proxy
            params.put("proxytype", "HTTP");
        }
        // user agent
        if(d.containsKey("user_agent")){
            params.put("useragent", d.get("user_agent"));                 // with proxy
        }

        // affiliate
        if(!this._affiliate_id.equals("0")) params.put("affiliateid", this._affiliate_id);

        // do request
        String response = Utils.post(TASK_ENDPOINT, params, USER_AGENT);

        // check if error
        int i = response.indexOf("ERROR:");
        if(i != -1)     // it's an error
        {
            String resp_err = response.substring(6, response.length()).trim();
            throw new Exception(resp_err);
        }
        JSONObject jsobj = new JSONObject(response.substring(1, response.length() - 1));
        return jsobj.getString("CaptchaId");        // return response
    }

    // push variables for task captcha
    public String task_push_variables(String captcha_id, String pushVariables) throws Exception {
        // create params of request
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("action", "GETTEXT");
        params.put("captchaid", captcha_id);
        params.put("pushVariables", pushVariables);

        if(this._username != null && !this._username.isEmpty())
        {
            params.put("username", this._username);
            params.put("password", this._password);
        }
        else
        {
            params.put("token", this._access_token);
        }

        // do request
        String response = Utils.post(TASK_PUSH_ENDPOINT, params, USER_AGENT);

        // check if error
        int i = response.indexOf("ERROR:");
        if(i != -1)     // it's an error
        {
            String resp_err = response.substring(6, response.length()).trim();
            throw new Exception(resp_err);
        }

        return response;        // return response
    }

    // retrieve captcha response (works with all types)
    public HashMap<String, String> retrieve_response(String captcha_id) throws Exception {
        Map<String,Object> d = new LinkedHashMap<>();
        d.put("action", "GETTEXT");
        d.put("captchaid", captcha_id);

        if(this._username != null && !this._username.isEmpty())
        {
            d.put("username", this._username);
            d.put("password", this._password);
        }
        else
        {
            d.put("token", this._access_token);
        }

        // affiliate
        if(!this._affiliate_id.equals("0")) {
            d.put("affiliateid", this._affiliate_id);
        }

        // do request
        String response = Utils.post(RETRIEVE_JSON_ENDPOINT, d, USER_AGENT);

        // check if error
        int i = response.indexOf("ERROR:");
        if(i != -1)     // it's an error
        {
            String resp_err = response.substring(6, response.length()).trim();
            throw new Exception(resp_err);
        }

        response = response.substring(1, response.length() - 1);
        JSONObject jsobj = new JSONObject(response);
        String status = jsobj.getString("Status");
        if (status.equals("Pending")) return null;
        HashMap<String, String> m = new HashMap<String, String>();
        Iterator<String> keys = jsobj.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            m.put(key, jsobj.getString(key));
        }
        return m;
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
}
