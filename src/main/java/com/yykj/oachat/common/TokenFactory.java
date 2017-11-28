package com.yykj.oachat.common;

import java.util.UUID;

/**
 * @auther Lee
 * @date 2017/11/28
 */
public class TokenFactory {

    private TokenFactory() {
    }

    public static String generate(){
        String token = UUID.randomUUID().toString();
        TokenPool.addToken(token);
        return token;
    }
}
