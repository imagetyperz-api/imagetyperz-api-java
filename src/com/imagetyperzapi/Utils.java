package com.imagetyperzapi;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by icebox on 23/05/17.
 */
public class Utils {
    // post only with params and user agent
    public static String post(String endpoint, Map<String, Object> params, String user_agent) throws Exception {
        URL url = new URL(endpoint);    // init URL

        // params to stringbuilder
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");       // string to bytes conversion

        // init request connection
        // ------------------------
        //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));
        //HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setRequestProperty("User-Agent", user_agent);
        conn.usingProxy();
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        // create string from response
        // ------------------------------------
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0; )
            sb.append((char) c);

        return sb.toString();
    }

    public static String read_file_b64(String path) throws IOException {
            return DatatypeConverter.printBase64Binary(Files.readAllBytes(
                    Paths.get(path)));
    }
}
