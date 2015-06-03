package com.github.vladislav719;

import com.github.vladislav719.help.CaptchaApi;
import com.github.vladislav719.help.GeneratorApi;
import com.github.vladislav719.steam.Reger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by vladislav on 02.06.2015.
 */
public class Main {

    private CaptchaApi captchaApi;

    public static void main(String[] args) {
        while (true) {
            new Main().start();
        }
    }

    public void start() {
        final HashMap mapCaptcha = Reger.getCaptchaUrl();
        captchaApi = new CaptchaApi();
        String id = captchaApi.upload((String) mapCaptcha.get("url"));
        boolean resolved = false;
        String captchaText = null;

        while (resolved == false) {
            try {
                Thread.sleep(5000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            captchaText = captchaApi.getCaptchaText(id);
            if (captchaText != null)
                resolved = true;
        }
        String login = GeneratorApi.loginGenerator(8);
        String email = GeneratorApi.emailGenerator("@yopmail.com");
        String pass = GeneratorApi.passwordGenerator(8);


        Boolean b = Boolean.valueOf(Reger.accountRegistration(login, captchaText, (String) mapCaptcha.get("gid"), email, pass));
        if (b) {
            PrintWriter out = null;
            try {
                out = new PrintWriter(new BufferedWriter(new FileWriter("logins.txt", true)));
                out.println(login + ":" + pass);
                System.out.println(login + ":" + pass);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println(captchaApi.getBalance());

    }
}
