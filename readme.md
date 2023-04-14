imagetyperz-api-java - Imagetyperz API wrapper
=========================================

imagetyperzapi is a super easy to use bypass captcha API wrapper for imagetyperz.com captcha service

## Installation

    git clone https://github.com/imagetyperz-api/imagetyperz-api-java

## Dependencies

- org.apache.commons.cli
- org.json

## Usage

Simply require the module, set the auth details and start using the captcha service:

``` java
import com.imagetyperzapi.ImageTyperzAPI;
```

Set access_token for authentication:

``` java
ImageTyperzAPI i = new ImageTyperzAPI("your_access_token");
```

Once you've set your authentication details, you can start using the API.

**Get balance**

``` java
String balance = i.account_balance();
System.out.println(String.format("Balance: %s", balance));
```

## Solving

For solving a captcha, it's a two step process:

- **submit captcha** details - returns an ID
- use ID to check it's progress - and **get solution** when solved.

Each captcha type has it's own submission method.

For getting the response, same method is used for all types.


### Image captcha

``` java
HashMap<String, String> image_params = new HashMap<String, String>();
// image_params.put("iscase", "true");         // case sensitive captcha
// image_params.put("isphrase", "true");       // text contains at least one space (phrase)
// image_params.put("ismath", "true");         // instructs worker that a math captcha has to be solved
// image_params.put("alphanumeric", "2");      // 1 - digits only, 2 - letters only
// image_params.put("minlength", "1");         // captcha text length (minimum)
// image_params.put("maxlength", "8");         // captcha text length (maximum)
String captcha_id = i.submit_image("/tmp/captcha.jpg", image_params);
```

ID received is used to retrieve solution when solved.

**Observation**
It works with URL instead of image file too, but authentication has to be done using token.

### reCAPTCHA

For recaptcha submission there are two things that are required.

- page_url (**required**)
- site_key (**required**)
- type (optional, defaults to 1 if not given)
  - `1` - v2
  - `2` - invisible
  - `3` - v3
  - `4` - enterprise v2
  - `5` - enterprise v3
- domain - used in loading of reCAPTCHA interface, default: `www.google.com` (alternative: `recaptcha.net`) - `optional`
- v3_min_score - minimum score to target for v3 recaptcha `- optional`
- v3_action - action parameter to use for v3 recaptcha `- optional`
- proxy - proxy to use when solving recaptcha, eg. `12.34.56.78:1234` or `12.34.56.78:1234:user:password` `- optional`
- user_agent - useragent to use when solve recaptcha `- optional` 
- data-s - extra parameter used in solving recaptcha `- optional`
- cookie_input - cookies used in solving reCAPTCHA - `- optional`

``` java
HashMap<String, String> d = new HashMap<String, String>();
d.put("page_url", "https://your-site.com");
d.put("sitekey", "7LrGJmcUABBAALFtIb_FxC0LXm_GwOLyJAfbbUCL");
// d.put("type", "3");                 // optional, 1 - regular, 2 - invisible, 3 - v3, defaults to 1
// d.put("domain", "www.google.com");  // used in loading reCAPTCHA interface, default: www.google.com (alternative: recaptcha.net) - optional
// d.put("v3_min_score", "0.1");       // optional
// d.put("v3_action", "homepage");     // optional
// d.put("proxy", "126.45.34.53:123"); // or with auth 126.45.34.53:123:user:pass - optional
// d.put("user_agent", "Your user agent"); // optional
// d.put("data-s", "recaptcha data-s value"); // optional
// d.put("cookie_input", "a=b;c=d");   // optional
String captcha_id = i.submit_recaptcha(d);
```

ID will be used to retrieve the g-response, once workers have 
completed the captcha. This takes somewhere between 10-80 seconds. 

Check **Retrieve response** 

### GeeTest

GeeTest is a captcha that requires 3 parameters to be solved:

- domain
- challenge
- gt
- api_server (optional)

The response of this captcha after completion are 3 codes:

- challenge
- validate
- seccode

**Important**
This captcha requires a **unique** challenge to be sent along with each captcha.

```java
HashMap<String, String> d = new HashMap<String, String>();
d.put("domain", "https://your-site.com");
d.put("challenge", "eea8d7d1bd1a933d72a9eda8af6d15d3");
d.put("gt", "1a761081b1114c388092c8e2fd7f58bc");
// d.put("api_server", "api.geetest.com"); // geetest domain - optional
// d.put("proxy", "126.45.34.53:123"); // or with auth 126.45.34.53:123:user:pass - optional
// d.put("user_agent", "Your user agent"); // optional
String captcha_id = i.submit_geetest(d);
```

Optionally, you can send proxy and user_agent along.


### GeeTestV4

GeeTesV4 is a new version of captcha from geetest that requires 2 parameters to be solved:

- domain
- geetestid (captchaID) - gather this from HTML source of page with captcha, inside the `<script>` tag you'll find a link that looks like this: https://i.imgur.com/XcZd47y.png

The response of this captcha after completion are 5 parameters:

- captcha_id
- lot_number
- pass_token
- gen_time
- captcha_output

```java
HashMap<String, String> d = new HashMap<String, String>();
d.put("domain", "https://example.com");
d.put("geetestid", "647f5ed2ed8acb4be36784e01556bb71");
// d.put("proxy", "126.45.34.53:123"); // or with auth 126.45.34.53:123:user:pass - optional
// d.put("user_agent", "Your user agent"); // optional
String captcha_id = i.submit_geetest_v4(d);
```

Optionally, you can send proxy and user_agent along.


### hCaptcha

Requires page_url and sitekey

```java
HashMap<String, String> d = new HashMap<String, String>();
d.put("page_url", "https://your-site.com");
d.put("sitekey", "8c7062c7-cae6-4e12-96fb-303fbec7fe4f");

// if invisible hcaptcha - optional
// d.put("invisible", "1");

// domain used in loading of hcaptcha interface, default: hcaptcha.com - optional
// d.put("domain", "hcaptcha.com");

// extra parameters, useful for enterprise
// submit userAgent from requests too, when this is used
// d.put("HcaptchaEnterprise", "{\"rqdata\": \"value taken from web requests\"}");

// d.put("proxy", "126.45.34.53:123"); // or with auth 126.45.34.53:123:user:pass - optional
// d.put("user_agent", "Your user agent"); // optional
String captcha_id = i.submit_hcaptcha(d);
```

### Capy

Requires page_url and sitekey

```java
HashMap<String, String> d = new HashMap<String, String>();
d.put("page_url", "https://your-site.com");
d.put("sitekey", "Fme6hZLjuCRMMC3uh15F52D3uNms5c");
// d.put("proxy", "126.45.34.53:123"); // or with auth 126.45.34.53:123:user:pass - optional
// d.put("user_agent", "Your user agent"); // optional
String captcha_id = i.submit_capy(d);
```

### Tiktok

Requires page_url cookie_input

```java
HashMap<String, String> d = new HashMap<String, String>();
d.put("page_url", "https://tiktok.com");
// make sure `s_v_web_id` cookie is present
d.put("cookie_input", "s_v_web_id:verify_kd6243o_fd449FX_FDGG_1x8E_8NiQ_fgrg9FEIJ3f;tt_webid:612465623570154;tt_webid_v2:7679206562717014313;SLARDAR_WEB_ID:d0314f-ce16-5e16-a066-71f19df1545f;");
// d.put("proxy", "126.45.34.53:123"); // or with auth 126.45.34.53:123:user:pass - optional
// d.put("user_agent", "Your user agent"); // optional
String captcha_id = i.submit_tiktok(d);
```

### FunCaptcha

Requires page_url, sitekey and s_url

```java
HashMap<String, String> d = new HashMap<String, String>();
d.put("page_url", "https://your-site.com");
d.put("sitekey", "11111111-1111-1111-1111-111111111111");
d.put("s_url", "https://api.arkoselabs.com");
// d.put("data", "{\"a\": \"b\"}");   // optional, extra funcaptcha data in JSON format
// d.put("proxy", "126.45.34.53:123"); // or with auth 126.45.34.53:123:user:pass - optional
// d.put("user_agent", "Your user agent"); // optional
String captcha_id = i.submit_funcaptcha(d);
```

### Turnstile (Cloudflare)

Requires page_url, sitekey

```java
// solve captcha
HashMap<String, String> d = new HashMap<String, String>();
d.put("page_url", "https://your-site.com");
d.put("sitekey", "0x4ABBBBAABrfvW5vKbx11FZ");
// d.put("domain", "challenges.cloudflare.com"); // domain used in loading turnstile interface, default: challenges.cloudflare.com - optional
// d.put("action", "homepage");                  // used in loading turnstile interface, similar to reCAPTCHA - optional
// d.put("cdata", "your cdata");                 // used in loading turnstile interface - optional
// d.put("proxy", "126.45.34.53:123"); // or with auth 126.45.34.53:123:user:pass - optional
// d.put("user_agent", "Your user agent"); // optional
String captcha_id = i.submit_turnstile(d);
```

### Task

Requires template_name, page_url and usually variables

```java
HashMap<String, String> d = new HashMap<String, String>();
d.put("template_name", "Login test page");
d.put("page_url", "https://imagetyperz.net/automation/login");
d.put("variables", "{\"username\": \"abc\", \"password\": \"paZZW0rd\"}");
// d.put("proxy", "126.45.34.53:123"); // or with auth 126.45.34.53:123:user:pass - optional
// d.put("user_agent", "Your user agent"); // optional
String captcha_id = i.submit_task(d);
```

#### Task pushVariable
Update a variable value while task is running. Useful when dealing with 2FA authentication.

When template reaches an action that uses a variable which wasn't provided with the submission of the task,
task (while running on worker machine) will wait for variable to be updated through push.

You can use the pushVariables method as many times as you need, even overwriting previously set variables.
```java
String code = "24323";
i.task_push_variables(captcha_id, "{\"twofactor_code\": \"" + code + "\"}");
```

## Retrieve response

Regardless of the captcha type (and method) used in submission of the captcha, this method is used
right after to check for it's solving status and also get the response once solved.

It requires one parameter, that's the **captcha ID** gathered from first step.

```java
response = i.retrieve_response(captcha_id);
```

```java
string captcha_id = i.submit_recaptcha(d); // works with any captcha type submitted
Console.WriteLine("Waiting for captcha to be solved...");
Dictionary<string, string> response = null;
while (response == null)
{
    System.Threading.Thread.Sleep(10000);       // sleep for 10 secons before checking for response
    response = i.retrieve_response(captcha_id);
}
ImageTypers.Utils.print_response(response);
```

The response is a JSON object that looks like this:

```json
{
  "CaptchaId": 176707908, 
  "Response": "03AGdBq24PBCbwiDRaS_MJ7Z...mYXMPiDwWUyEOsYpo97CZ3tVmWzrB", 
  "Cookie_OutPut": "", 
  "Proxy_reason": "",
  "Status": "Solved"
}
```

## Other methods/variables

**Affiliate id**

The constructor accepts a 2nd parameter, as the affiliate id. 

``` java
ImageTyperzAPI i = new ImageTyperzAPI("your_access_token", "123");
```

**Set captcha bad**

When a captcha was solved wrong by our workers, you can notify the server with it's ID,
so we know something went wrong.

``` java
i.set_captcha_bad(captcha_id);
```

## Examples

Check `example/captcha` folder for examples, for each type of captcha.

## License

API library is licensed under the MIT License

## More information

More details about the server-side API can be found [here](http://imagetyperz.com)

<sup><sub>captcha, bypasscaptcha, decaptcher, decaptcha, 2captcha, deathbycaptcha, anticaptcha, 
bypassrecaptchav2, bypassnocaptcharecaptcha, bypassinvisiblerecaptcha, captchaservicesforrecaptchav2, 
recaptchav2captchasolver, googlerecaptchasolver, recaptchasolver-java, recaptchabypassscript</sup></sub>

