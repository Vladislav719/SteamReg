package com.github.vladislav719.steam;

import com.github.vladislav719.help.CaptchaApi;
import com.github.vladislav719.help.Rsa;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vladislav on 02.06.2015.
 */
public class AuthAzimow {

    static BasicCookieStore cookieStore;

    public static void main(String[] args) {
        System.out.println(accountAuth("bfc4SStb", "WiLqTldx"));
    }


    public static String accountAuth(String login, String password) {
        cookieStore = new BasicCookieStore();
        Unirest.setHttpClient(HttpClients.custom().setDefaultCookieStore(cookieStore).build());
        HttpResponse<JsonNode> getRsaKey = null;
        try {
            getRsaKey = Unirest.post("https://steamcommunity.com/login/getrsakey/")
                    .field("username", login)
                    .field("donotcache", String.valueOf(new Date().getTime()))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        Map<String, List<String>> headers = getRsaKey.getHeaders();
        Map<String, String> heads = new HashMap<String, String>();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            List<String> l = entry.getValue();
            String valR = (l.size() == 1) ? (String)l.get(0) : null;
            if (valR == null) {
                for (String s : l) {
                    valR += s + ";";
                }
            }

            heads.put(entry.getKey(), valR);
        }

        HashMap rsaData = new HashMap();
        JSONObject jsonObject = getRsaKey.getBody().getObject();
        rsaData.put("timestamp", jsonObject.get("timestamp"));
        rsaData.put("steamid", jsonObject.get("steamid"));
        rsaData.put("publickey_exp", jsonObject.get("publickey_exp"));
        rsaData.put("token_gid", jsonObject.get("token_gid"));
        rsaData.put("success", jsonObject.get("success"));
        rsaData.put("publickey_mod", jsonObject.get("publickey_mod"));

//        System.out.println(getRsaKey.getBody().getObject().get("timestamp"));
        System.out.println("Status " + rsaData.get("success"));
        HttpResponse<JsonNode> dologin = null;

        System.out.println(headers);

        try {
            dologin = Unirest.post("https://steamcommunity.com/login/dologin/")
                    .header("connection","keep-alive" )
//                    .headers(heads)
                    .field("captcha_text", "")
                    .field("captchagid", "-1")
                    .field("donotcache", String.valueOf(new Date().getTime()))
                    .field("emailauth", "")
                    .field("emailsteamid", "")
                    .field("loginfriendlyname", "")
                    .field("password", new Rsa().encrypt(password, (String) rsaData.get("publickey_exp"), (String) rsaData.get("publickey_mod")))
                    .field("remember_login", "false")
                    .field("rsatimestamp", String.valueOf(rsaData.get("timestamp")))
                    .field("twofactorcode", "")
                    .field("username", login)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        System.out.println("CHECK");
        System.out.println(dologin.getBody().toString());
//        System.out.println(dologin.getBody().getObject().get("captcha_needed"));
        if (dologin.getBody().getObject().get("captcha_needed").toString().contains("true")) {
            //???? ??? ??? ? ??????
            String capGid = dologin.getBody().getObject().getString("captcha_gid");
            String text = new CaptchaApi().resolveText(capGid);
            try {
                dologin = Unirest.post("https://steamcommunity.com/login/dologin/")
                        .field("captcha_text", text)
                        .field("captchagid", capGid)
                        .field("donotcache", String.valueOf(new Date().getTime()))
                        .field("emailauth", "")
                        .field("emailsteamid", "")
                        .field("loginfriendlyname", "")
                        .field("password", new Rsa().encrypt(password, (String) rsaData.get("publickey_exp"), (String) rsaData.get("publickey_mod")))
                        .field("remember_login", "false")
                        .field("rsatimestamp", String.valueOf(rsaData.get("timestamp")))
                        .field("twofactorcode", "")
                        .field("username", login)
                        .asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
        System.out.println("OMG");
        System.out.println(dologin.getBody().toString());
//        System.out.println(getRsaKey);
        return getRsaKey.getBody().toString();
    }
}
