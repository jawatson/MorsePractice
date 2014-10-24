package org.minow.sound;import java.util.*;import java.net.*;import java.io.*;/* * javax.sound is not guaranteed to work on all Java implementations. */import javax.sound.sampled.*;/** * JavaxSynthesizer uses the javax.sound classes * <p> * This module only works with the javax.sound classes and has * only been tested on Windows/98 running Java 1.3. * </p> * <p> * Copyright &copy; 1999-2000  *      <a href="mailto:minow@pobox.com">Martin Minow</a>. *      All Rights Reserved. * </p> * <p> * <small> * Permission to use, copy, modify, and redistribute this software and its * documentation for personal, non-commercial use is hereby granted provided that * this copyright notice and appropriate documentation appears in all copies. This * software may not be distributed for fee or as part of commercial, "shareware," * and/or not-for-profit endevors including, but not limited to, CD-ROM collections, * online databases, and subscription services without specific license.  The * author makes no expressed or implied warranty of any kind and assumes no * responsibility for errors or omissions. No liability is assumed for any incidental * or consequental damages in connection with or arising out of the use of the * information or program. * </small> * </p> * * @author <a href="mailto:minow@pobox.com">Martin Minow</a> * @version 1.2 * This module is Java 1.1 compatible. */public class JavaxSynthesizer implements Runnable{    private static final boolean DEBUG  = false;        /*     * We can't use a Clip as our messages are too long for     * the default buffer size.     */    private AudioFormat format          = null;    private DataLine.Info info          = null;    private LineListener listener       = null;    private SourceDataLine line         = null;    private Thread runner               = null;    private byte[] data                 = null;    private boolean stopNow             = false;    /*     * Only PCM_SIGNED works on Windows/98.     */    public static final int SAMPLES_PER_SECOND  = 8000;    public static final int BUFFER_SIZE         = 65536;    /*     * Note that, if you change these values, you will have to modify     * the synthesizer methods.     */     public static final int SAMPLE_SIZE_BITS    = 8;    public static final int SAMPLE_SIZE_BYTES   = SAMPLE_SIZE_BITS / 8;    public static final int CHANNELS            = 1;    public static final int BYTES_PER_FRAME     = SAMPLE_SIZE_BYTES * CHANNELS;    /**     * The constructor will fail if the javax.sun classes are not available.     * @param listener      The (only) LineListener that will receive     *                      START and STOP events. (This is a hack to     *                      prevent thread contention).     * @throws ClassNotFoundException if the javax.sun classes cannot be     *      initialized.     */    public JavaxSynthesizer(            LineListener        listener        )        throws ClassNotFoundException    {        try {            format          = new AudioFormat(                    AudioFormat.Encoding.PCM_SIGNED, /* Format      */                    SAMPLES_PER_SECOND,     /* Sample Rate          */                    SAMPLE_SIZE_BITS,       /* Sample size in bits  */                    CHANNELS,               /* Channels (stereo)    */                    BYTES_PER_FRAME,        /* Bytes per frame      */                    SAMPLES_PER_SECOND,     /* Frame rate           */                    false                   /* Big-Endian (no)      */                );            info            = new DataLine.Info(                                    SourceDataLine.class,                                    format,                                    AudioSystem.NOT_SPECIFIED                                );            if (AudioSystem.isLineSupported(info) == false) {                throw new ClassNotFoundException("Unsupported line: " + info.toString());            }            this.listener   = listener;        }        catch (Exception e) {            e.printStackTrace();            throw new ClassNotFoundException(e.toString());        }    }    /**     * Return the number of samples per second     */    public final int getSamplesPerSecond()    {        return (SAMPLES_PER_SECOND);    }    /*     * Start a new synthesis.     * @param data      The data buffer to synthesize.     */    public void startSynthesis(            byte[]          data        )    {        stopSynthesis();        stopNow             = false;        this.data           = data;        runner              = new Thread(this);        runner.start();    }    /**     * Stop any current synthesis. This will signal listeners that     * synthesis is no longer active.     */    public synchronized void stopSynthesis()    {        stopNow             = true;        try {            if (runner != null) {                runner.interrupt();                try {                    while (isSynthesizerActive()) {                        Thread.sleep(10);                    }                }                catch (InterruptedException ignore) {}            }        }        finally {            runner          = null;        }    }    /**     * Implement the Runnable interface     */    public void run()    {        try {            line            = (SourceDataLine) AudioSystem.getLine(info);            if (listener != null) {                line.addLineListener(listener);            }            if (false) {                line.open();            }            else {                line.open(format, BUFFER_SIZE);            }            line.start();            for (int offset = 0, segment;                    stopNow == false && offset < data.length;                    offset += segment) {                boolean onceOnly    = DEBUG;stallLoop:      for (;;) {                    segment     = Math.min(BUFFER_SIZE, data.length - offset);                    if (line.available() >= (segment / 2)) {                        break stallLoop;                    }                    if (DEBUG && onceOnly) {                        System.out.println("Stalling, available = " + line.available());                        onceOnly = false;                    }                    Thread.sleep(1);                }                if (DEBUG) {                    System.out.println("Writing " + segment + " from " + offset);                }                segment     = line.write(data, offset, segment);                if (DEBUG) {                    System.out.println("Wrote " + segment);                }            }            if (stopNow == false) {                line.drain();            }        }        catch (Exception e) {            if (DEBUG) {                e.printStackTrace();            }        }        finally {            if (DEBUG) {                System.out.println("Done");            }            if (line != null) {                try { line.stop(); } catch (Exception ignore) {}                try { line.flush(); } catch (Exception ignore) {}                try { line.close(); } catch (Exception ignore) {}                if (listener != null) {                    line.removeLineListener(listener);                }                line            = null;            }        }    }    /**      * Inform the caller whether the synthesis is active.     * @return true if the synthesizer is making a sound.     */    public boolean isSynthesizerActive()    {        boolean result      = (line != null);        // System.out.println("isSynthesizerActive: " + result);        return (result);    }        public void finalize()        throws Throwable    {        try { line.stop(); } catch (Throwable ignore) {}        try { line.flush(); } catch (Throwable ignore) {}        try { line.close(); } catch (Throwable ignore) {}        format          = null;        info            = null;        listener        = null;        line            = null;        super.finalize();    }    }