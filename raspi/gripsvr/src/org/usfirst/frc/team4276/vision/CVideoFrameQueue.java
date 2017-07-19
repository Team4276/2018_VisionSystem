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

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.usfirst.frc.team4276.vision.Gripsvr.m_frameQueueList;

public class CVideoFrameQueue {

    public int m_droppedFrames;

    private int m_eQueueType;
    LinkedBlockingQueue<CVideoFrame> m_queue;

    public CVideoFrameQueue() {
        m_droppedFrames = 0;
        m_eQueueType = CVideoFrame.FRAME_QUEUE_TYPE_UNKNOWN;
        m_queue = new LinkedBlockingQueue<CVideoFrame>();
    }

    public void init(int eQueueType) {
        m_eQueueType = eQueueType;
        reset();
    }

    public void reset() {
        CVideoFrame frame = new CVideoFrame();
        while (!m_queue.isEmpty()) {
            if (m_queue.remove(frame)) {
                frame.init();
            }
        }
    }

    public void addTail(CVideoFrame frame) {
        try {
            frame.m_timeAddedToQueue[m_eQueueType] = Gripsvr.m_testMonitor.getTicks();
            m_queue.put(frame);

            if (m_eQueueType != CVideoFrame.FRAME_QUEUE_FREE) {
                while (m_queue.size() > 1) {
                    CVideoFrame droppedFrame = m_queue.remove();  // Does not block
                    droppedFrame.m_timeRemovedFromQueue[m_eQueueType] = Gripsvr.m_testMonitor.getTicks();
                    Gripsvr.m_frameQueueList[CVideoFrame.FRAME_QUEUE_FREE].addTail(droppedFrame);

                    m_droppedFrames++;
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(CVideoFrameQueue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Blocks when queue is empty
    public CVideoFrame blockingRemoveHead() {
        try {
            CVideoFrame frame = m_queue.take();  // Wait here until something appears in the queue
            frame.m_timeRemovedFromQueue[m_eQueueType] = Gripsvr.m_testMonitor.getTicks();
            return frame;
        } catch (InterruptedException ex) {
            Logger.getLogger(CVideoFrameQueue.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Go this way only if the user hits Ctrl-C
        CVideoFrame frame = new CVideoFrame();
        return frame;
    }
};
