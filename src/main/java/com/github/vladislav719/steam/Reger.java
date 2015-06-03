package com.github.vladislav719.steam;

import com.github.vladislav719.help.CaptchaApi;
import com.github.vladislav719.help.GeneratorApi;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vladislav on 02.06.2015.
 */
public class Reger {

    public static void main(String[] args) {
        final HashMap mapCaptcha = getCaptchaUrl();
        System.out.println(mapCaptcha);

        final String[] captchaText = {null};
        CaptchaApi captchaApi = new CaptchaApi();
        String id = captchaApi.upload((String) mapCaptcha.get("url"));
        boolean resolved = false;
        String text = null;

        while (resolved == false) {
            try {
                Thread.sleep(5000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            text = captchaApi.getCaptchaText(id);
            if (text != null)
                resolved = true;
        }
        captchaText[0] = text;
        System.out.println(captchaText[0]);

        String login = GeneratorApi.loginGenerator(8);
        String email = GeneratorApi.emailGenerator("@yopmail.com");
        String pass = GeneratorApi.passwordGenerator(8);

        System.out.println("login = " + login);
        System.out.println("pass = " + pass);
        System.out.println("email = " + email);



        System.out.println(accountRegistration(login, captchaText[0], (String) mapCaptcha.get("gid"), email, pass));
    }


    public static String accountRegistration(String login, String captcha, String captchagid, String email, String password) {
        HttpResponse<JsonNode> jsonresponse = null;
        try {
            jsonresponse = Unirest.post("https://store.steampowered.com/join/createaccount/")
                    .field("accountname", login)
                    .field("captcha_text", captcha)
                    .field("captchagid", captchagid)
                    .field("count", String.valueOf(4))
                    .field("email", email)
                    .field("i_agree", String.valueOf(1))
                    .field("password", password)
                    .field("ticket", "")
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }


        System.out.println(jsonresponse.getHeaders().toString());

//        System.out.println(jsonresponse.getHeaders().getFirst("steamLogin"));
//        System.out.println(jsonresponse.getHeaders().getFirst("steamLoginSecure"));
        Boolean res = (Boolean) jsonresponse.getBody().getObject().get("bSuccess");
        return String.valueOf(res);
    }

    public static HashMap getCaptchaUrl() {
        HashMap map = new HashMap();
        Document doc = null;
        try {
            doc = Jsoup.connect("https://store.steampowered.com/join/?")
                    .userAgent("Mozilla")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String url = doc.select("img#captchaImg").attr("src");

        List<NameValuePair> params = null;
        try {
            params = URLEncodedUtils.parse(new URI(url), "UTF-8");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        for (NameValuePair param : params) {
            if (param.getName().equals("gid")) {
                map.put("url", url);
                map.put("gid", param.getValue());
//                System.out.println(param.getName() + " : " + param.getValue());
            }
        }


        return map;

    }


}
