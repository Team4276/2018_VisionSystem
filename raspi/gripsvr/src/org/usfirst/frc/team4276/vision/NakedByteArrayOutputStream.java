/* 
 * Copyright (c) 2016 RobotsByTheC. All rights reserved.
 *
 * Open Source Software - may be modified and shared by FRC teams. The code must
 * be accompanied by the BSD license file in the root directory of the project.
 */
package org.usfirst.frc.team4276.vision;

import java.io.ByteArrayOutputStream;

/**
 * I needed this functionality once, but I don't think it is used anymore.
 *
 * @author Ben Wolsieffer
 */
public class NakedByteArrayOutputStream extends ByteArrayOutputStream {

    private int mark = 0;

    public byte[] getBuffer() {
        return buf;
    }

    public void mark() {
        mark = count;
    }

    public void markReset() {
        count = mark;
    }
}
