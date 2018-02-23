/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package serverimage;

import com.github.sarxos.webcam.Webcam;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import java.util.logging.Level;
import java.util.logging.Logger;
//import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;

//import java.awt.Dimension;


/**
 *
 * @author Ben
 */
public class ServerImage {
    static Core core;
    public static RobotOutput Motors;
    public static Webcam webcam;

    public static int i = 4;

    public static void main(String[] args) {
        try {
            PlatformManager.setPlatform(Platform.RASPBERRYPI);
        } catch (PlatformAlreadyAssignedException ex) {
            Logger.getLogger(ServerImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        Motors = new RobotOutput();
            webcam = Webcam.getDefault();
            System.out.println("Camera resolutions: "+webcam.getViewSizes().length);
           
        webcam.setViewSize(webcam.getViewSizes()[
                1
                //webcam.getViewSizes().length-1
        ]);
        webcam.close();
        webcam.open();
        
        core = new Core();
        
        core = new Core();
        while(true){
            System.out.println("Connection closed");
            restart();
        }
    }
    public static void restart(){
        i--;
        if(i==0)System.exit(0);
        
        System.out.println(i);
        //System.exit(0);
        core = null;
        Motors.Stop();
        core = new Core();
        
    }
    public static void startWebcam(){
    
    }
}
