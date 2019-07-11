package com.web.server;

import java.io.IOException;

/**
 * 该类用于处理静态资源的请求
 */
public class StaticResourceProcesser {

    public void process(Request request,Response response){
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
