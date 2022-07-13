package me.jfishing.burp.dict.content_path;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URLEncoder;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SocketHTTPRequest {

    private int port;
    private String host;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public SocketHTTPRequest(String host,int port) throws Exception{

        this.host = host;
        this.port = port;

        /**
         * http协议
         */
        socket = new Socket(this.host, this.port);

        /**
         * https协议
         */
        //socket = (SSLSocket)((SSLSocketFactory)SSLSocketFactory.getDefault()).createSocket(this.host, this.port);


    }

    public void sendGet() throws IOException{
        //String requestUrlPath = "/z69183787/article/details/17580325";
        String requestUrlPath = "/";

        OutputStreamWriter streamWriter = new OutputStreamWriter(socket.getOutputStream());
        bufferedWriter = new BufferedWriter(streamWriter);
        bufferedWriter.write("GET " + requestUrlPath + " HTTP/1.1\n");
        bufferedWriter.write("Host: " + this.host + "\n");
        bufferedWriter.write("\n");
        bufferedWriter.flush();

        BufferedInputStream streamReader = new BufferedInputStream(socket.getInputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(streamReader, "utf-8"));
        String line = null;
        while((line = bufferedReader.readLine())!= null){
            System.out.println(line);
        }
        bufferedReader.close();
        bufferedWriter.close();
        socket.close();

    }


    public String sendPost(String data) throws IOException{
        System.out.println(">>>>>>>>>>>>>>>>>>>>>"+data);
        OutputStreamWriter streamWriter = new OutputStreamWriter(socket.getOutputStream(), "utf-8");
        bufferedWriter = new BufferedWriter(streamWriter);
        bufferedWriter.write(data);
        bufferedWriter.flush();

        BufferedInputStream streamReader = new BufferedInputStream(socket.getInputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(streamReader, "utf-8"));
        StringBuffer body = new StringBuffer();
        String line = null;
        boolean isBody = false;
        while((line = bufferedReader.readLine())!= null)
        {
            if(isBody) {
                body.append(line);
                body.append('\n');
                continue;
            }
            if("".equals(line)) {
                isBody = true;
            }
        }
        bufferedReader.close();
        bufferedWriter.close();
        socket.close();
        return body.toString();
    }

}
