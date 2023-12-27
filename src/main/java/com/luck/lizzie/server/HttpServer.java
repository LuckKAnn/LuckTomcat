package com.luck.lizzie.server;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author liukun.inspire
 * @Date 2023/12/27 10:31
 * @PackageName: com.luck.lizzie.server
 * @ClassName: HttpServer
 * @Version 1.0
 */
@Slf4j
public class HttpServer {
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    public static void main(String[] args) throws Exception {
        HttpServer httpServer = new HttpServer();
        httpServer.await();
    }

    private void await() throws Exception {
        ServerSocket serverSocket = new ServerSocket(8081, 1, InetAddress.getByName("127.0.0.1"));
        System.out.println(WEB_ROOT);
        // keep accept
        while (true) {
            Socket accept = serverSocket.accept();
            try {
                InputStream inputStream = accept.getInputStream();
                OutputStream outputStream = accept.getOutputStream();
                Request request = new Request(inputStream);
                request.parse();
                // build response
                Response response = new Response(outputStream);
                response.setRequest(request);
                response.sendStaticResource();
                // close socket
                accept.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
