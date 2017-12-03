package com.yykj.oachat.common;

import com.google.common.collect.Sets;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Lee
 * @date 2017/11/28
 */
public class TokenPool {

    private static CopyOnWriteArraySet<String> tokenSet = Sets.newCopyOnWriteArraySet();

    private TokenPool(){}

    public static void addToken(String token){
        tokenSet.add(token);
    }

    public static boolean checkToken(String token){
        return tokenSet.contains(token);
    }

    public static void removeToken(String token){
        tokenSet.remove(token);
    }
}
