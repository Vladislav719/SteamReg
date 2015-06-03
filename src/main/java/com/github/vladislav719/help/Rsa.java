package com.github.vladislav719.help;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created by vladislav on 02.06.2015.
 */
public class Rsa {
    public String exponentM;
    private byte[] exponent;
    private byte[] modulus;

    public static void main(String[] args) {
        String t = new Rsa().encrypt("WiLqTldx", "010001", "AFC5F0B75F5F48024EFC51A68A160DBB11749A6493D38911352DB69179033729EC53F7C8012FC3CD2CDA25CFF7F369BFF2E2C2E0C5B524362FC3DEDA1D92FFD824310E0F14EAAA14B7F7AF59E74757C3E777F9F99BD8851B82959DA7873555A6F9F70CE5D3B1A645B212C410B537AC1A3B67A56F51348541FB8D0F68FD0A85BBCE0CEF95EAC82665945882E6F412D58B8171BDA78A99BB338A9D9B0BAFFF79F503096661F91DEF7020590308C5613781C2E71F05D7D4FBFE1AA067BAD85578B4009517D3BA2F9A44E8A087B1205D8ED0EC3652EA77C0DCD55835D7959EFCD62964D957ACEA9B79A56C2A2FCFC95CC760E2616B6E1B1863552158B5D434C7B64D");
        System.out.println(t);
    }

    public String encrypt(String data, String exp, String mod) {
        String encrypted = null;
        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(mod, 16), new BigInteger(exp, 16));
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            result = cipher.doFinal(data.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        String res = Base64.encodeBase64String(result);
//        return new String(Base64.encodeBase64(new String(result).getBytes()));
        return res;
    }


    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
    }

    private int getHexVal(char hex) {
        int val = hex;
        return val - (val < 58 ? 48 : 55);
    }
}
