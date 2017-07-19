//*****************************************************************************************//
// The MIT License (MIT)                                                                   //
//                                                                                         //
// Copyright (c) 2017 - Marina High School FIRST Robotics Team 4276 (Huntington Beach, CA) //
//                                                                                         //
// Permission is hereby granted, free of charge, to any person obtaining a copy            //
// of this software and associated documentation files (the "Software"), to deal           //
// in the Software without restriction, including without limitation the rights            //
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell               //
// copies of the Software, and to permit persons to whom the Software is                   //
// furnished to do so, subject to the following conditions:                                //
//                                                                                         //
// The above copyright notice and this permission notice shall be included in              //
// all copies or substantial portions of the Software.                                     //
//                                                                                         //
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR              //
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,                //
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE             //
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER                  //
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,           //
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN               //
// THE SOFTWARE.                                                                           //
//*****************************************************************************************//
//*****************************************************************************************//
// We are a high school robotics team and always in need of financial support.             //
// If you use this software for commercial purposes please return the favor and donate     //
// (tax free) to "Marina High School Educational Foundation, attn: FRC team 4276"          //
// (Huntington Beach, CA)                                                                  //
//*****************************************************************************************//
package org.usfirst.frc.team4276.vision;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.*;
import static org.opencv.videoio.Videoio.*;

public class Gripsvr {

    public static CTestMonitor m_testMonitor = new CTestMonitor();
    public static CVideoFrameQueue m_frameQueueList[];

    public static VideoServer myVideoServer;
    public static Boolean isShuttingDown = false;

    private static BufferedImage image;
    private static VideoCapture camera;

    public static final int NUMBER_OF_FRAMES_IN_POOL = 8;

    public static final int CAM_WIDTH = 640;
    public static final int CAM_HEIGHT = 480;
    public static final int CAM_BYTES_PER_PIXEL = 4;

    public static final int TESTMONITOR_SKIP_FRAMES = 30;  // TestMonitor examines a frame once per this many frames
    public static int nCount = TESTMONITOR_SKIP_FRAMES;

    public static Size frameSize;

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary("opencv_java320");

        m_frameQueueList = new CVideoFrameQueue[CVideoFrame.NUMBER_OF_FRAME_QUEUES];
        int i;
        for (i = 0; i < CVideoFrame.NUMBER_OF_FRAME_QUEUES; i++) {
            m_frameQueueList[i] = new CVideoFrameQueue();
            m_frameQueueList[i].init(i);
        }
        for (i = 0; i < NUMBER_OF_FRAMES_IN_POOL; i++) {
            m_frameQueueList[CVideoFrame.FRAME_QUEUE_FREE].addTail(new CVideoFrame());
        }

        GripPipelineRunnable gripRunnable = new GripPipelineRunnable();
        Thread gripThread = new Thread(gripRunnable);
        gripThread.start();

        //start video server thread
        try {
            myVideoServer = new VideoServer(5814, 80);
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
        Mat testFrame = new Mat();
        camera.read(testFrame);

        if (!camera.isOpened()) {
            System.out.println("Camera not opened");
        } else {
            while (!isShuttingDown) {
                CVideoFrame frame = m_frameQueueList[CVideoFrame.FRAME_QUEUE_FREE].blockingRemoveHead();
                if (camera.read(frame.m_frame)) {
                    //Boolean saveFrameToJpeg = Gripsvr.m_testMonitor.saveFrameToJpeg(frame.m_frame);
                    m_frameQueueList[CVideoFrame.FRAME_QUEUE_WAIT_FOR_BLOB_DETECT].addTail(frame);
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
