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

import java.util.Arrays;
import org.opencv.core.Mat;

public class CVideoFrame {

    public static final int FRAME_QUEUE_TYPE_UNKNOWN = 0;
    public static final int FRAME_QUEUE_WAIT_FOR_BLOB_DETECT = 1;
    public static final int FRAME_QUEUE_WAIT_FOR_TEXT_CLIENT = 2;
    public static final int FRAME_QUEUE_WAIT_FOR_BROWSER_CLIENT = 3;
    public static final int FRAME_QUEUE_FREE = 4;
    public static final int NUMBER_OF_FRAME_QUEUES = 5;

    Mat m_frame;
    Mat m_filteredFrame;

    long m_timeAddedToQueue[];
    long m_timeRemovedFromQueue[];

    public CVideoFrame() {
        m_timeAddedToQueue = new long[NUMBER_OF_FRAME_QUEUES];
        m_timeRemovedFromQueue = new long[NUMBER_OF_FRAME_QUEUES];
        init();
    }

    public final void init() {
        m_frame = new Mat();
        m_filteredFrame = new Mat();
        Arrays.fill(m_timeAddedToQueue, 0);
        Arrays.fill(m_timeRemovedFromQueue, 0);
    }
};
