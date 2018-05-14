package com.cli;

/**
 * Created by icebox on 25/05/17.
 */
public class Arguments {
    private String _access_token = "";
    private String _username = "";
    private String _password = "";
    private String _mode;

    private String _captcha_file = "";
    private String _output_file = "";
    private String _affiliate_id = "";
    private String _page_url = "";
    private String _site_key = "";

    private String _captcha_id = "";

    private String _proxy = "";

    private boolean _case_sensitive = false;

    public String get_username() {
        return _username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public void set_access_token(String access_token)
    {
        this._access_token = access_token;
    }
    public String get_access_token()
    {
        return this._access_token;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public String get_mode() {
        return _mode;
    }

    public void set_mode(String _mode) {
        this._mode = _mode;
    }

    public String get_captcha_file() {
        return _captcha_file;
    }

    public void set_captcha_file(String _captcha_file) {
        this._captcha_file = _captcha_file;
    }

    public String get_output_file() {
        return _output_file;
    }

    public void set_output_file(String _output_file) {
        this._output_file = _output_file;
    }

    public String get_affiliate_id() {
        return _affiliate_id;
    }

    public void set_affiliate_id(String affiliate_id) {
        this._affiliate_id = affiliate_id;
    }

    public String get_page_url() {
        return _page_url;
    }

    public void set_page_url(String _page_url) {
        this._page_url = _page_url;
    }

    public String get_site_key() {
        return _site_key;
    }

    public void set_site_key(String _site_key) {
        this._site_key = _site_key;
    }

    public String get_captcha_id() {
        return _captcha_id;
    }

    public void set_captcha_id(String _captcha_id) {
        this._captcha_id = _captcha_id;
    }

    public boolean is_case_sensitive() {
        return _case_sensitive;
    }

    public void set_case_sensitive(boolean _case_sensitive) {
        this._case_sensitive = _case_sensitive;
    }

    public String get_proxy() {
        return _proxy;
    }

    public void set_proxy(String _proxy) {
        this._proxy = _proxy;
    }
}
