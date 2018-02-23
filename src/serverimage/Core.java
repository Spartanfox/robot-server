/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package serverimage;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO; 
import static serverimage.ServerImage.webcam;
/**
 *
 * @author Ben
 */
public class Core {
    //public ServerSocket serverSocket = null;
    

    public String Message = "workies probs";
    public Socket socket;
    public boolean exists = false;
    
    public  Thread readThread,writeThread;
    public  OutputStream outStream;
    public  boolean Disconnected = false;
    public  InputStream  inStream ;
    private DataOutputStream dOut = null;
    private DataInputStream dIn = null;
    public String Image;
    public int Viewsize = 0;
    public int Direction = 0;
   // private BufferedReader inputReader;
    private BufferedImage img;
    private Color[] oldColors;
    
    public Core() {
        //inputReader = new BufferedReader(new InputStreamReader(System.in));
        
       
        createSocket();
    }

    public void createSocket(){
        try {
            System.out.println("Starting server");
            ServerSocket serverSocket = new ServerSocket(25566);
            
                System.out.println("Waiting for client");
                socket = serverSocket.accept();
                socket.setTcpNoDelay(true);
                System.out.println("New user "+socket.getInetAddress().toString());
                inStream = socket.getInputStream();
                outStream = socket.getOutputStream();
                dOut = new DataOutputStream(socket.getOutputStream());
                dIn = new DataInputStream(socket.getInputStream());
                //ServerImage.webcam.open();
                createWriteThread();
                System.out.println("Waiting for exit");
                while(Disconnected == false){Thread.sleep(10);}
                System.out.println("Client disconnected");
                //ServerImage.webcam.close();
                socket.close();
                serverSocket.close();
                writeThread = null;
               //webcam.close();
                //ServerImage.restart();
                
            //}
        } catch (Exception io) {
            System.out.println(io.toString());
        }
    }
    

  
      public void createReadThread() {
        readThread = new Thread() {
            public void run() {
                while (socket.isConnected()) {

                    try {
                        //Direction = dIn.readChar();
                        System.out.println(Direction);
                    }catch (Exception se){
                        //System.exit(0);
                        System.out.println("exited");
                        Disconnected = true;
                        break;

                    }


                }
                System.out.println("Disconnected read");
            }
        };
        readThread.setPriority(Thread.MAX_PRIORITY);
        readThread.start();
    }

        public void createWriteThread() {
        writeThread = new Thread() {
            public void run() {
                int input;
                img = webcam.getImage();
                while (socket.isConnected()) {
                    try {
                        //sleep(1000);
                        synchronized (socket) {
                            input = dIn.readInt();
                            if(input == 1){
                                img = webcam.getImage();
                                ByteArrayOutputStream byteArrayO = new ByteArrayOutputStream();
                                ImageIO.write(img,"JPG",byteArrayO);
                                byte [] byteArray =  byteArrayO.toByteArray();
                                dOut.writeInt(byteArray.length);
                                dOut.write(byteArray);
                            }
                            else if(input == 9){
                                System.out.println("Silent mode : "+ServerImage.Motors.Silent);
                                ServerImage.Motors.Silent^=true;
                            }
                            else if(input == 6){
                               
                                System.out.println("Changing camera");
                               /* Viewsize++;
                                if(Viewsize==webcam.getViewSizes().length)Viewsize=0;
                                System.out.println(Viewsize);
                                System.out.println("Closing");
                                webcam.close();
                              
                                webcam = null;
                                System.out.println("Making new webcam");
                                webcam = Webcam.getDefault();
                                System.out.println("Setting viewsize");
                                //webcam.setViewSize(webcam.getViewSizes()[Viewsize]);
                                System.out.println("Opening");
                                webcam.close();
                                webcam.open();
                               */
                            }
                            else if(input == 7){
                                System.out.println("Capturing image");
                            }
                            else if(input == 8){
                                socket.close();
                            }
                            else if(input == 100){
                                System.out.println("Exiting");
                                System.exit(0);
                            }
                            else{
                                Direction = input;
                                System.out.println("input: "+Direction);
                                ServerImage.Motors.Move(Direction);
                            }
                        }
                        
                        //System.arraycopy();

                    } catch (Exception i) {
                        System.err.println(i);
                        System.out.println("exited");
                        Disconnected = true;
                        break;
                       // System.exit(0);
                    }
                    //System.out.println("Disconnected write");
                    //outStream.write("Disconnected".getBytes("UTF-8"));

                }
                Disconnected = true;
            }
        };
        writeThread.setPriority(Thread.MAX_PRIORITY);
        writeThread.start();
    }
    
    /*
                                if(false){
                                     BufferedImage NewImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
                                    NewImg.getGraphics().drawImage(img, 0, 0, null);
                                    int chunk = 10;
                                    int Pixels = img.getWidth()*img.getHeight();
                                    int[] PixelsArray = new  int[Pixels];
                                    PixelsArray = img.getRGB(0, 0, img.getWidth(), img.getHeight(), PixelsArray,0, img.getWidth());
                                    if(oldColors==null)
                                    oldColors = new Color[Pixels];
                                    Graphics2D g = (Graphics2D)NewImg.getGraphics();                                   
                                    for(int i = 0 ; i < img.getWidth();i+=chunk){
                                        for (int i2 = 0; i2 < img.getHeight(); i2+=chunk){
                                            if(i+i2 >PixelsArray.length){break;}
                                            try{
                                                Color newColor = new Color(PixelsArray[i+i2]);
                                                if(oldColors[i] != null){
                                                    if(isColorSame(newColor, oldColors[i+i2])){
                                                        g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
                                                        g.setColor(new Color(0, 0, 0, 0));
                                                        System.out.println("X: "+i%img.getWidth()+"  Y: "+i/img.getHeight());
                                                        g.fillRect(i,i2,chunk,chunk);
                                                    }
                                                }
                                                oldColors[i] = newColor; 
                                            }catch(Exception e){
                                                System.err.println(e);
                                            }
                                        }
                                    }
                                    img = NewImg;
                                }
                                */
    
    
        public boolean isColorSame(Color Color1,Color Color2){
        if(Color1 != null){
            int[] R = new int[]{Color1.getRed  (),Color2.getRed  ()};
            int[] G = new int[]{Color1.getGreen(),Color2.getGreen()};
            int[] B = new int[]{Color1.getBlue (),Color2.getBlue ()};
            int[] RGB = new int[]{difference(R[0],R[1]),difference(G[0],G[1]),difference(B[0],B[1])};
            double Percentage = (RGB[0]+RGB[1]+RGB[2])/3;
            if(Percentage >= 99.9){
                return true;
            }
        }
        
        return false;
    }
        
    public int difference(int i,int i2){
        double percent = Math.abs((i-i2));
        percent  = percent/255;
        percent *= 100;
        percent  = 100 - percent;
        return (int)percent;
    }
    

    
   /* public void Cleanup(){
        System.out.println("Cleaning up");
        int i = 0;
         for(Client c : client){
            i++; 
            if(c.Disconnected){
                try {                                                        
                    System.out.println("Client disconnected removing them.");
                    c.readThread.interrupt();
                    c.readThread = null;
                    c.writeThread.interrupt();
                    c.writeThread = null;
                    c = null;
                    c.socket.close();
                    client.remove(i);
                } catch (IOException ex) {
                    //Logger.getLogger(ChatSocketServer.class.getName()).log(Level.SEVERE, null, ex);
                }
               
            }
        }
    }*/
}
