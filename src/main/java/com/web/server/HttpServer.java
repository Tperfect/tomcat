package com.web.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * web服务器
 */
public class HttpServer {

    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    private boolean shutdown = false;

    public void await(){
        ServerSocket serverSocket = null;
        int port = 8080;
        try {
            /**
             * 创建ServerSocket实例
             */
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("localhost"));
        } catch (IOException e) {
            e.printStackTrace();
            //System.exit(1); 非正常退出   System.exit(0);正常退出
            System.exit(1);
        }
        while (!shutdown){
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            try {
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();

                //创建request对象
                Request request = new Request(input);
                request.parse();

                //创建response对象
                Response response = new Response(output);
                response.setRequest(request);

                //判断是请求静态资源还是请求servlet
                if (request.getUri().startsWith("/servlet/")){
                    ServletProcessor processor = new ServletProcessor();
                    processor.process(request,response);
                }else {
                    StaticResourceProcesser processer = new StaticResourceProcesser();
                    processer.process(request,response);
                }

                //关闭当前socket
                socket.close();

                //判断是否为停止容器的请求
                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer();
        httpServer.await();
    }
}

