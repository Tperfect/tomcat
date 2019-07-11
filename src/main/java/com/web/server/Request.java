package com.web.server;

import java.io.IOException;
import java.io.InputStream;

/**
 * Http请求类
 */
public class Request{
    private InputStream input;
    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    public String getUri() {
        return uri;
    }

    private String parseUrl(String requestString){
        int index1,index2;
        //返回指定字符在字符串中第一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1。
        index1 = requestString.indexOf(' ');
        if (index1 != -1){
            //返回从 fromIndex 位置开始查找指定字符在字符串中第一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1。
            index2 = requestString.indexOf(' ',index1 + 1);
            if (index2 > index1){
                return requestString.substring(index1 + 1 , index2);
            }
        }
        return null;
    }

    public void parse(){
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            //从输入流中读取一定数量的字节，并将其存储在缓冲区数组 b 中。以整数形式返回实际读取的字节数。
            i = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            request.append((char) buffer[j]);
        }
        System.out.println(request.toString());
        uri = parseUrl(request.toString());
    }

}
