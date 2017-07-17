package org.usfirst.frc.team4276.vision;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.*;
import static org.opencv.videoio.Videoio.*;

public class Gripsvr {
    
    public static CTestMonitor m_testMonitor = new CTestMonitor();

    public static VideoServer myVideoServer;
    public static Boolean isShuttingDown = false;

    private static BufferedImage image;
    private static VideoCapture camera;
    private static GripPipeline myGripPipeline;
    
    public static final int CAM_WIDTH = 640;
    public static final int CAM_HEIGHT = 480;
    public static final int CAM_BYTES_PER_PIXEL = 4;
    
    public static final int TESTMONITOR_SKIP_FRAMES = 30;  // TestMonitor examines a frame once per this many frames
    public static int nCount = TESTMONITOR_SKIP_FRAMES;

    public static Size frameSize;

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary("opencv_java320");

        //start video server thread
        try {
            myVideoServer = new VideoServer(5814, 80);
            myGripPipeline = new GripPipeline();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            myVideoServer.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        camera = new VideoCapture(0);
        double wid = camera.get(CAP_PROP_FRAME_WIDTH);
        double ht = camera.get(CAP_PROP_FRAME_HEIGHT);
        System.out.println("Video WxH = " + wid + "x" + ht);
       
        camera.set(CAP_PROP_FRAME_WIDTH, CAM_WIDTH);
        camera.set(CAP_PROP_FRAME_HEIGHT, CAM_HEIGHT);
        
        wid = camera.get(CAP_PROP_FRAME_WIDTH);    
        ht = camera.get(CAP_PROP_FRAME_HEIGHT);
        System.out.println("2 Video WxH = " + wid + "x" + ht);
        
        frameSize = new Size(wid, ht);
        Mat frame = new Mat(frameSize, CvType.CV_8U);
        camera.read(frame);

        if (!camera.isOpened()) {
            System.out.println("Camera not opened");
        } else {
            while (!isShuttingDown) {
                if (camera.read(frame)) {
                    try {
                        nCount--;
                        if(nCount <= 0) {
                            nCount = TESTMONITOR_SKIP_FRAMES;
                            //m_testMonitor.saveFrameToJpeg(frame);
                        }
                        
                        //myGripPipeline.process(frame);
                        myVideoServer.sendImage(frame);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        camera.release();

        try {
            myVideoServer.stop();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("VideoServer test stopped");

    }
}
