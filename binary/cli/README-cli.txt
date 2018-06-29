============================================================================================
Linux command-line client
============================================================================================

============================================================================================
=== UPDATE ===
--------------------------------------------------------------------------------------------
Instead of -u and -p use -a (access_token)
Access token can be gathered from: http://www.imagetyperz.com/Forms/ClientHome.aspx 

-u and -p (legacy) way still works, but might get deprecated at some point, use access_token
============================================================================================
mode = 2

 -a "your_token" -m 2 -pageurl "page_url" -sitekey "sitekey" -o "output_file" -refid "ref_id"
 -proxy "IP:Port:[user:pass]" -user_agent "Your user_agent" -type "recaptcha type"
 -v3_min_score "minimim v3 score" -v3_action "action to use when solving v3"

./program.sh -a "124GAD23t34" -m 2 -pageurl "http://test.com" -sitekey "adsvvv"
--------------------------------------------------------------------------------------------
Recaptcha (retrieve) - retrieve catpcha after it was solved from server
--------------------------------------------------------------------------------------------
mode = 3
-a "access_token" -m 3 -captchaid "captcha_id"
-a "access_token" -m 3 -captchaid "captcha_id" -o "output_file"

./program.sh -a "AS232RSDG2" -m 3 -id "321"
============================================================================================

OTHER
-------

Balance - get account balance (credit)
--------------------------------------------------------------------------------------------
mode = 4
-a "access_token" -m 4
-a "access_token" -m 4 -o "output_file" 

./program.sh -a "2f43g3g3" -m 4
--------------------------------------------------------------------------------------------

Set bad captcha - set a bad captcha by using the captcha ID
--------------------------------------------------------------------------------------------
mode = 5
-a "access_token" -m 5 -captchaid "captcha_id"
-a "access_token" -m 5 -captchaid "captcha_id" -o "output_file"

./program.sh -a "d23gg3g334" -m 5 -captchaid "321"

--------------------------------------------------------------------------------------------
Check if proxy was used in solving (in case proxy was submitted with recaptcha details)
--------------------------------------------------------------------------------------------
mode = 6
-t "your_token" -m 6 -captchaid "captcha_id"
============================================================================================

[*] The -o parameter is optional. The response/reply will be always printed to STDOUT.
    In case you want to have it stored into a file as well, use the -o parameter.
[*] Same goes with the -affiliateid parameter
[*] -case parameter for mode 1 (normal captcha) is optional as well, tells if the solving
    of the captcha should keep account of lower/upper case. It can be either 1 (enabled) or 0

[*] In case error occurs, details will be saved to error.txt

[*] Requires Java installed in order to run
    Developed using Java 1.8


