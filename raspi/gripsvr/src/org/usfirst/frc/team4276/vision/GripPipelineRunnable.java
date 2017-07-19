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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.usfirst.frc.team4276.vision.Gripsvr.TESTMONITOR_SKIP_FRAMES;
import static org.usfirst.frc.team4276.vision.Gripsvr.m_frameQueueList;
import static org.usfirst.frc.team4276.vision.Gripsvr.myVideoServer;
import static org.usfirst.frc.team4276.vision.Gripsvr.nCount;

/**
 *
 * @author acappon
 */
public class GripPipelineRunnable implements Runnable {

    boolean m_continueRunning;

    private static GripPipeline myGripPipeline = new GripPipeline();

    @Override
    public void run() {
        m_continueRunning = true;
        while (m_continueRunning) {
            try {
                CVideoFrame frame = m_frameQueueList[CVideoFrame.FRAME_QUEUE_WAIT_FOR_BLOB_DETECT].blockingRemoveHead();

                //myGripPipeline.process(frame.m_frame);
                myVideoServer.sendImage(frame.m_frame);

                m_frameQueueList[CVideoFrame.FRAME_QUEUE_FREE].addTail(frame);
            } catch (IOException ex) {
                Logger.getLogger(GripPipelineRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
