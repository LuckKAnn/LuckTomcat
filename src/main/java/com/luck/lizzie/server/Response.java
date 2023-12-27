package com.luck.lizzie.server;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author liukun.inspire
 * @Date 2023/12/27 10:39
 * @PackageName: com.luck.lizzie.server
 * @ClassName: Response
 * @Version 1.0
 */
@Slf4j
public class Response {
    /**
     * 缓冲区大小
     */
    private static final int BUFFER_SIZE = 1024;
    private OutputStream outputStream;

    private Request request;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    /**
     * @param request to set
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        // how could this do
        byte[] buffers = new byte[BUFFER_SIZE];
        String requestMethod = request.getRequestMethod();
        System.out.println(requestMethod);
        // 提前声明，方便后续finally关闭
        FileInputStream fileInputStream = null;
        try {
            File targetFile = new File(HttpServer.WEB_ROOT, requestMethod);
            if (targetFile.exists()) {
                // 读取对应的文件
                fileInputStream = new FileInputStream(targetFile);
                int read = fileInputStream.read(buffers, 0, BUFFER_SIZE);
                while (read != -1) {
                    //  不写成buffer是为了不这么长
                    outputStream.write(buffers, 0, read);
                    read = fileInputStream.read(buffers, 0, BUFFER_SIZE);
                }
                // flush
                outputStream.flush();
            } else {
                String errorMessage = "HTTP/1.1 404 FIle Not Found\r\n" + "Content-Type: text/html\r\n" + "Content-Length: 23\r\n" + "\r\n" + " File Not Found ";
                outputStream.write(errorMessage.getBytes());
            }
        } catch (Exception e) {
            log.error("Response static failed ", e);
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }


    }
}
