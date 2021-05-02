package com.example.keep_exploring.api;


import com.example.keep_exploring.helpers.Helper_Common;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Socket_Client {
//    static final String URL_LOCAL = "http://192.168.0.100:3000/";

    static Socket mSocket;
    static {
        try {
            mSocket = IO.socket(new Helper_Common().getBaseUrl());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Socket getSocket() {
        return mSocket;
    }
}
