imagetyperzapi - Imagetyperz API wrapper
=========================================
imagetyperzapi is a super easy to use bypass captcha API wrapper for imagetyperz.com captcha service

## Installation

    git clone https://github.com/imagetyperz-api/imagetyperz-api-java

## How to use?

Simply import the library, set the auth details and start using the captcha service:

``` java
import com.imagetyperzapi.ImageTyperzAPI;
```
Set access_token or username and password (legacy) for authentication

``` java
String access_key = "your_access_key";
ImageTyperzAPI i = new ImageTyperzAPI(access_token);
```
legacy authentication, will get deprecated at some point
``` java
i.set_user_and_password("your_username", "your_password");
```
Once you've set your authentication details, you can start using the API

**Get balance**

``` java
String balance = i.account_balance();
System.out.println(String.format("Balance: %s", balance));
```

**Submit image captcha**

``` java
String captcha_text = i.solve_captcha("captcha.jpg", false);
System.out.println(String.format("Captcha text: %s", captcha_text));
```
2nd argument is a boolean which represents if captcha is case sensitive

**URL instead of captcha image**
``` java
String captcha_text = i.solve_captcha("http://abc.com/captcha.jpg", false);
```
**OBS:** URL instead of image file path works when you're authenticated with access_key.
 For those that are still using username & password, retrieve your access_key from 
 imagetyperz.com

**Submit recaptcha details**

For recaptcha submission there are two things that are required.
- page_url
- site_key
``` java
String page_url = "your_pageurl_here";
String sitekey = "your_sitekey_here";
String captcha_id = i.submit_recaptcha(page_url, sitekey);
```
This method returns a captchaID. This ID will be used next, to retrieve the g-response, once workers have
completed the captcha. This takes somewhere between 10-80 seconds.

**Retrieve captcha response**

Once you have the captchaID, you check for it's progress, and later on retrieve the gresponse.

The ***in_progress(captcha_id)*** method will tell you if captcha is still being decoded by workers.
Once it's no longer in progress, you can retrieve the gresponse with ***retrieve_recaptcha(captcha_id)***

``` java
while(i.in_progress(captcha_id))
{
    TimeUnit.SECONDS.sleep(10);     // sleep for 10 seconds
}
// completed at this point
String recaptcha_response = i.retrieve_captcha(captcha_id);
```

## Other methods/variables

**Affiliate id**

The constructor accepts a 2nd parameter, as the affiliate id.
``` java
ImageTyperzAPI i = new ImageTyperzAPI("your_token", "123");
```


**Submit recaptcha with proxy**

When a proxy is submitted with the recaptcha details, the workers will complete the captcha using
the provided proxy/IP.

``` java
i.submit_recaptcha(page_url, sitekey, "12.34.56.78:1234");
```
Proxy with authentication is also supported
``` java
i.submit_recaptcha(page_url, sitekey, "12.34.56.78:1234:user:pass");
```

**Set captcha bad**

When a captcha was solved wrong by our workers, you can notify the server with it's ID,
so we know something went wrong

``` java
i.set_captcha_bad(captcha_id);
```

## Examples
Check the com.example package

## Command-line client
For those that are looking for a command-line client, check out the **com.cli** project in solution
It's a tool that allows you to do pretty much all the API offers, from the command-line
Check it's README-cli.txt file for more details

## Binary
If you don't want to compile your own library, you can check the binary folder for a compiled version.
**Keep in mind** that this might not be the latest version with every release

## License
API library is licensed under the MIT License

## More information
More details about the server-side API can be found [here](http://imagetyperz.com)


<sup><sub>captcha, bypasscaptcha, decaptcher, decaptcha, 2captcha, deathbycaptcha, anticaptcha, 
bypassrecaptchav2, bypassnocaptcharecaptcha, bypassinvisiblerecaptcha, captchaservicesforrecaptchav2, 
recaptchav2captchasolver, googlerecaptchasolver, recaptchasolverpython, recaptchabypassscript</sup></sub>

