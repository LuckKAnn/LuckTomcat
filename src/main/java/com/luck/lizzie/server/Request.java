package com.luck.lizzie.server;

import com.sun.xml.internal.ws.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @Author liukun.inspire
 * @Date 2023/12/27 10:31
 * @PackageName: com.luck.lizzie.server
 * @ClassName: Reqeuest
 * @Version 1.0
 */
@Slf4j
public class Request {
    private InputStream inputStream;

    private String requestMethod;

    public Request(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void parse() throws IOException {
        StringBuffer request = new StringBuffer(2048);
        char[] buffer = new char[2048];
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            reader.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.append(buffer);
        requestMethod = parseUri(request.toString());
    }

    public void parse02() throws IOException {
        // StringBuffer request = new StringBuffer(2048);
        // int i;
        // byte[] buffer = new byte[2048];
        // try {
        //     i = inputStream.read(buffer);
        // } catch (IOException e) {
        //     e.printStackTrace();
        //     i = -1;
        // }
        // for (int j = 0; j < i; j++) {
        //     request.append((char) buffer[j]);
        // }
        // requestMethod = parseUri(request.toString());
        doParse();
    }


    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1) return requestString.substring(index1 + 1, index2);
        }
        return null;
    }

    private void doParse() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line = reader.readLine();
        int idx = 0;
        // 简单的判断 line != null 看起来会导致网络长连接下面的inputstream出现长期的阻塞？？
        while (line != null && line.length() != 0) {
            stringBuilder.append(line);
            if (idx == 0) {
                requestMethod = parseHttpRequestLine(line);
                System.out.println(requestMethod);
            }
            idx++;
            // 这里readline还是有可能产生阻塞的问题的
            line = reader.readLine();
        }
        // close reader or stream necessary, you may use try-with-resource
        reader.close();
        System.out.println(stringBuilder.toString());

    }

    private String parseHttpRequestLine(String request) {
        // 解析http请求行
        String[] requestLine = request.split(" ");
        System.out.println(Arrays.toString(requestLine));
        return requestLine[1];
    }


}
