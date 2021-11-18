package com.wxf.utils;

import com.wxf.utils.myutils.RSAUtil;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public class RSAUtilTest {

    @Test
    public void test() throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeySpecException, BadPaddingException, IOException, NoSuchProviderException, InvalidKeyException {
        Map<String, String> keys = RSAUtil.getKeys();

        String publicKey = keys.get("publicKey");
        String privateKey = keys.get("privateKey");
        // System.out.println(privateKey);

        String encrypt = RSAUtil.encrypt("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMx7NiITi96Gk//YMv4DXef8qS3FUc4ZbSjhYCDQpxPqbLRwUVKuczCmLrSI+a5ZI/Y3Oi8geBJ7NtUcOvDJtCL+43wvApDt9P9Z8JrV0f+aQiwZTKSXSbm8VPT5sBIjIoLL37yKNEKvbRo2LA0v0CP5LFI6HVrmg71eXdkC7W9FAgMBAAECgYAQAMCO0CrNIokop7nt6UOLKVnwLBYn5BOH9IN6bfn1dA5H3NZweximwQq8tE+eFnnbBh12nLro712uNgRi3dZMFt3dTcTl60yllB+1dfhO+tv4mc1PCZSMC/NOgb/5UOlJQONa+Dzgr9nM24Vo4cnvuvtqri546/7kM6KpJR3DLQJBAPwFkH2nhH1q/BOb3SUAl/GlJptNDz/rS7p61VIiGxQhMZ5S7nI/Kfn8xdjz7kYHC45F4jP74S7ueGYTPZwQKysCQQDPtYh8SfG6CBjl76Oz0wEuLlfPD3ammbG2ndtSYVcYdVraKojvfHZbkoi0wILHGwaAzeXJetAxFC+7Gxn9jddPAkEAjd0n/8N7bgn6CaO0MW8LGOhJOiR/kBCIWVlpmUk/iu5Aewnd1Yu+ukzSphRxrROsKpMmCPMhSATO2067YI0tbwJASYgpnl6LHeG59sTkfkfH1jJ/0YTW61njyROPTlW5gKYjGLMQMUAtonN4xAyW/6e4oKbDn5VD2Owdja6lf9bUDwJAKettKw1srfNpj6JtY2mvQo8vNgumqzz8Pq1Qq8wwvgV2XT9eGiwjH0si9tB2Cc1DYqoUYnF0AFbyOZjsSkzaPQ==",
                "123456");
        System.out.println(encrypt);
    }
}