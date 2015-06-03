package com.github.vladislav719.help;

import ru.fourqube.antigate.AntigateClient;
import ru.fourqube.antigate.AntigateClientBuilder;
import ru.fourqube.antigate.CaptchaStatus;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * Created by vladislav on 02.06.2015.
 */
public class CaptchaApi {

    private AntigateClient antigate;

    public CaptchaApi() {
        Properties prop = new Properties();
        InputStream input = null;
        String apiKey = null;

        input = getClass().getClassLoader().getResourceAsStream("config.properties");
        try {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        apiKey = prop.getProperty("api_key");

        antigate = AntigateClientBuilder.create()
                .setKey(apiKey)
                .build();
    }

    public String resolveText(String gid) {
        String url = "https://steamcommunity.com/public/captcha.php?gid="+gid;
        String id = upload(url);
        boolean resolved = false;
        String text = null;
        final String[] captchaText = {null};

        while (resolved == false) {
            try {
                Thread.sleep(3000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            text = getCaptchaText(id);
            if (text != null)
                resolved = true;
        }
        captchaText[0] = text;
        return captchaText[0];
    }

    public double getBalance() {
        return antigate.getBalance();
    }

    public String getCaptchaText(String id) {
        //id-text
//        HashMap<String, String> map = new HashMap();

        CaptchaStatus cs = antigate.checkStatus(id);
        if (cs.isReady()) {
//            map.put(id, cs.getText());
            return cs.getText();
        }
        return null;
    }

    /**
     *
     * @param imageUrl
     * @return id
     */
    public String upload(String imageUrl) {
        String id = null;
        try {
            id = antigate.upload(new URL(imageUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void report(String id) {
        antigate.reportBad(id);
    }
}
