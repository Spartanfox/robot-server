/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverimage;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinDirection;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import com.pi4j.io.gpio.trigger.GpioPulseStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.event.PinEventType;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RobotOutput {
    final GpioPinDigitalOutput LL,LR,RL,RR,Light;
    public final GpioController gpio = GpioFactory.getInstance();
    public boolean Silent = false;
    public int silence = 5;
 
    public boolean F,B,L,R;
    Thread Runnable;
    RobotOutput(){
        System.out.println("Starting up motors");
        LR     = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26, PinState.HIGH);
        LL     = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, PinState.HIGH);
        RL     = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, PinState.HIGH);
        RR     = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, PinState.HIGH);
        Light = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, PinState.HIGH);             
        LL.setShutdownOptions(true, PinState.LOW);
        LR.setShutdownOptions(true, PinState.LOW);
        RL.setShutdownOptions(true, PinState.LOW);
        RR.setShutdownOptions(true, PinState.LOW);
        Light.setShutdownOptions(true, PinState.LOW);
        RL.low();
        LL.low();
        LR.low();
        RR.low();
        Light.high();
        System.out.println("Sequence done");
        runThread();
    }
    
    public void Shutdown(){
        
    }
    void runThread(){
        Runnable = new Thread(){
            @Override
            public void run(){
                int cooldown=0;
                while(true){
                    if(cooldown>=0)cooldown--;
                    if(cooldown <=0){
                        if(F){
                            Forward();
                            if(Silent)cooldown=silence;
                        }
                        else if(B){
                            Backward();
                            if(Silent)cooldown=silence;
                        }
                        else if(L){
                            Left();
                            if(Silent)cooldown=silence;
                        }
                        else if(R){
                            Right();
                            if(Silent)cooldown=silence;
                        }
                    }else{
                        Stop();
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RobotOutput.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
            public void Stop(){
                 RL.low();
                 LL.low();
                 LR.low();
                 RR.low();
             }
             public void Forward(){
                 LL.high();
                 RR.high();
             }
             public void Backward(){
                LR.high();
                RL.high();
             }
             public void Left(){
                LR.high();
                RR.high();
             }
             public void Right(){
                LL.high();
                RL.high();
             }

        };
        Runnable.setPriority(Thread.MIN_PRIORITY);
        Runnable.start();
    }
    public void Move(int i){
        B = false;
        F = false;
        L = false;
        R = false;
        switch(i){
            case 2:
                B = true;
                break;
            case 3:
                F = true;
                break;
            case 4:
                L = true;
                break;
            case 5:
                R = true;
                break;
            case 10:
                Light.toggle();
            case 0:
                Stop();
                break;
        }
    } 
    public void Stop(){
                 B = false;
                 F = false;
                 L = false;
                 R = false;
                 RL.low();
                 LL.low();
                 LR.low();
                 RR.low();
             }
}
