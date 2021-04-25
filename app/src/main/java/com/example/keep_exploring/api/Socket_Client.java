package com.example.keep_exploring.api;


import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Socket_Client {
    static final String URL_LOCAL = "http://192.168.0.100:3000/";
    static final String URL_GLOBAL = "http://ec2-18-223-15-195.us-east-2.compute.amazonaws.com:3000";
    static Socket mSocket;
    static {
        try {
            mSocket = IO.socket(URL_GLOBAL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Socket getSocket() {
        return mSocket;
    }
}
