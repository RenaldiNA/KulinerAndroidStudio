package com.example.belajarlogin.utils;

import static android.os.StrictMode.setThreadPolicy;

import android.os.StrictMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class Network {

    //fungsi get api
    public static String getJson(String url){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        setThreadPolicy(policy);

        HttpURLConnection koneksi = null;
        String response = null;

        try {
            URL u = new URL(url);
            koneksi = (HttpURLConnection) u.openConnection();
            koneksi.setRequestMethod("GET");
            koneksi.setRequestProperty("Content-length" , "0");
            koneksi.setUseCaches(false);
            koneksi.setAllowUserInteraction(false);
            koneksi.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(koneksi.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null){
                sb.append(line + "\n");
            }

            br.close();
            response = sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    // fungsi post api
    public static String postJson(String url, String keyEmail, String keyPassword, String email, String password){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        setThreadPolicy(policy);

        String response = null;
        try {
            URL u = new URL(url);
            JSONObject postDataparamsnya = new JSONObject();
            postDataparamsnya.put(keyEmail, email);
            postDataparamsnya.put(keyPassword, password);
            HttpURLConnection koneksi = (HttpURLConnection) u.openConnection();

            koneksi.setReadTimeout(15000); // 15 detik dengan satuan milisekon
            koneksi.setConnectTimeout(15000); // 15 detik
            koneksi.setRequestMethod("POST");
            koneksi.setDoInput(true);
            koneksi.setDoOutput(true);

            OutputStream os = koneksi.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os , "UTF-8"));
            writer.write(getPostDataString(postDataparamsnya));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = koneksi.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(koneksi.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null){
                    sb.append(line);
                    break;
                }

                in.close();
                response = sb.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    // fungsi convert data json ke string url
    public static String getPostDataString(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            //ambil key pada json
            String key = itr.next();
            // ambil value pada json sesuai key
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }

        return result.toString();
    }
}
