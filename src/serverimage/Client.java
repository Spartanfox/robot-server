/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package serverimage;

import java.awt.Frame;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ben
 */
public final class Client {
    public  static int Amount;
    private int ID=0;
    public  Socket socket;
    public  OutputStream outStream;
    public  boolean Disconnected = false;
    public  InputStream  inStream ;
    public  static String Message = "";
    public  String SentMessage = "s",MyMessage = "a";
    public  Thread readThread,writeThread;
    public  Core parent;
    Client( Core parent,Socket socket){
        this.parent = parent;
        ID = Amount;
        Amount++;
        try {
            Message = "";
            this.socket = socket;
            inStream = socket.getInputStream();
            outStream = socket.getOutputStream();
           //createReadThread();
           // createWriteThread();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
