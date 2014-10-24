package org.minow.MorsePractice;import java.util.*;import java.net.*;import java.io.*;import org.minow.sound.*;/** * MorseCodeSender synthesizes a Morse Code symbol. It remembers the * proper sending rate and decomposes characters.<p> * * <p> * Copyright &copy; 1999-2000  *      <a href="mailto:minow@pobox.com">Martin Minow</a>. *      All Rights Reserved. * </p> * <p> * <small> * Permission to use, copy, modify, and redistribute this software and its * documentation for personal, non-commercial use is hereby granted provided that * this copyright notice and appropriate documentation appears in all copies. This * software may not be distributed for fee or as part of commercial, "shareware," * and/or not-for-profit endevors including, but not limited to, CD-ROM collections, * online databases, and subscription services without specific license.  The * author makes no expressed or implied warranty of any kind and assumes no * responsibility for errors or omissions. No liability is assumed for any incidental * or consequental damages in connection with or arising out of the use of the * information or program. * </small> * </p> * * @author <a href="mailto:minow@pobox.com">Martin Minow</a> * @version 1.1 * This module is Java 1.1 compatible. */public class MorseCodeSender{    private Sound               synthesizer;    /**     * textRate, fransworthRate, and usingFransworth configure the Morse     * Code synthesizer for the user's selected overall sending speed     * (textRate) and Farnsworth sending speed (farnsworthRate). If     * Farnsworth timing is enabled and the farnsworthRate is greater     * than the textRate, characters (dits and dahs) are sent at the     * Farnsworth rate (18 wpm) and additional silence is added     * between characters and words to slow the overall sending speed     * down to the textRate (13 wpm).     */    private int                 textRate        = MorsePractice.defaultTextRate;    private int                 farnsworthRate  = MorsePractice.defaultFarnsworthRate;    private boolean             usingFarnsworth = MorsePractice.defaultUsingFarnsworth;    /*     * ditDuration is the length in msec. of a '.' and the space     * between dits and dahs within a letter     */    private transient int ditDuration           = 0;    /**     * dahDuration is the length of a '-'. It is always set     * to ditDuration * 3.     */    private transient int dahDuration           = 0;    /**     * charDuration is the additional space after each     * symbol. If Farnsworth sending is disabled, it is     * ditDuration * 3.     */    private transient int charDuration          = 0;    /**     * wordDuration is the additional space after each     * word. If Farnsworth sending is disabled, it is     * ditDuration * 7.     */    private transient int wordDuration          = 0;        /*     * PARIS_TICKS, PARIS_CHARS, and PARIS_SPACE are used to     * configure the algorithm for the test word, "PARIS".     * These are magic numbers derieved from the Morse Code     * representation of the letters in "PARIS".     *  a dit is one tick followed by a one tick silence.     *  a dah is three ticks followed by a one tick silence.     *  a letter is a sequence of dits and dahs followed by     *    three ticks (total, i.e., the normal one tick with two     *    additional ticks).     *  a word is a sequence of letters followed by 7 ticks (the     *    normal 3 tick letter silence and 4 additional ticks).     * Thus, the word "PARIS" ".--. .- .-. .. ..." becomes:     *  10 dits         * (1 + 1)       = 20     *   4 dahs         * (3 + 1)       = 16     *   5 letters      * (2)           = 10     *   1 word         * (4)           =  4     *  total (PARIS_TICKS)             = 50     * PARIS_SPACE is the number of space elements in "PARIS"     * (the silence after each dit, dah, letter pause and word pause)     * (The plus and minus values are "eyeball" adjustments.)     */    private static final int    PARIS_TICKS     = 50 + 2;    private static final int    PARIS_CHARS     = 31 + 1;    private static final int    PARIS_SPACE     = 19 + 1;    /**     * Create a MorseCodeSender that can synthesize Morse code     * symbols.     */    public MorseCodeSender(            Sound           synthesizer        )    {        this.synthesizer    = synthesizer;        setDurations();    }    /**     * Return the current text rate in words per minute.     * @param the current text sending rate.     */    public int getTextRate()    {        return (textRate);    }    /**     * Set the text sending rate in words per minute.     * @param textRate  The sending rate in words per minute.     */    public void setTextRate(            int                 textRate        )    {        this.textRate   = textRate;        setDurations();    }    /**     * Return the current Farnsworth rate in words per minute.     * Individual Morse code symbols will be sent at this rate.     * @param the current Farnsworth sending rate.     */    public int getFarnsworthRate()    {        return (farnsworthRate);    }    /**     * Set the Farnsworth sending rate in words per minute.     * Individual Morse code symbols will be sent at this rate.     * @param farnsworthRate    The sending rate in words per minute.     */    public void setFarnsworthRate(            int                 farnsworthRate        )    {        this.farnsworthRate     = farnsworthRate;        setDurations();    }    /**     * Return true if MorseCodeSender uses Farnsworth sending conventions.     * @return true if using Farnsworth sending.     */    public boolean usingFarnsworth()    {        return (usingFarnsworth);    }    /**     * Set the Farnsworth sending state.     * @param usingFarnsworth   true if Farnsworth should be used.     */    public void setUsingFarnsworth(            boolean             usingFarnsworth        )    {        this.usingFarnsworth    = usingFarnsworth;        setDurations();    }    /*     * Compute the symbol element durations using an algorithm found     * in several Unix Morse Code synthesizers.     *     * From <http://www.kluft.com/~ikluft/ham/morse-intro.html>     * [This was adapted from the Ham Radio FAQ which used to be posted on UseNet.]      * The word PARIS was chosen as the standard length for CW code speed.     * Each dit counts for one count, each dah counts for three counts,     * intra-character spacing is one count, inter-character spacing is three     * counts and inter-word spacing is seven counts, so the word PARIS is exactly     * 50 counts:     * PPPPPPPPPPPPPP    AAAAAA    RRRRRRRRRR    IIIIII    SSSSSSSSSS     * dit dah dah dit   dit dah   dit dah dit   dit dit   dit dit dit     * 1 1 3 1 3 1 1  3  1 1 3  3  1 1 3 1 1  3  1 1 1  3  1 1 1 1 1  7 = 50     *  ^                      ^                                     ^     *  ^Intra-character       ^Inter-character            Inter-word^     * So 5 words-per-minute = 250 counts-per-minute / 50 counts-per-word     * or one count every 240 milliseconds. 13 words-per-minute is one count     * every ~92.3 milliseconds. This method of sending code is sometimes called     * "Slow Code", because at 5 wpm it sounds VERY SLOW.      *     * The "Farnsworth" method is accomplished by sending the dits and dahs and     * intra-character spacing at a higher speed, then increasing the inter-character     * and inter-word spacing to slow the sending speed down to the desired speed.     * For example, to send at 5 wpm with 13 wpm characters in Farnsworth method,     * the dits and intra-character spacing would be 92.3 milliseconds, the dah     * would be 276.9 milliseconds, the inter-character spacing would be 1.443     * seconds and inter-word spacing would be 3.367 seconds.     *     * The timing computation is is about 3% slow..     */    private void setDurations()    {        /*         * In the following, 60000.0 converts minutes (words per minute)         * to milliseconds.         *         * tick is the baseline "dit" duration if Farnsworth sending         * is disabled.         */        int tick                = 60000 / (textRate * PARIS_TICKS);        /*         * fTick is the actual dit duration in msec.         */        int fTick               = (usingFarnsworth && textRate < farnsworthRate)                ? 60000 / (farnsworthRate * PARIS_TICKS)                : tick;        int wordTick            =                ((PARIS_TICKS * tick) - (PARIS_CHARS * fTick)) / PARIS_SPACE;        ditDuration             = fTick;        /* Dit == one Baud      */        dahDuration             = ditDuration * 3;        charDuration            = wordTick * 3 - ditDuration;        wordDuration            = wordTick * 7 - charDuration - ditDuration;        if (false) {            System.out.print("text = " + textRate);            System.out.print(". farnsworth = " + farnsworthRate);            System.out.print(", tick = " + tick);            System.out.print(", fTick = " + fTick);            System.out.print(", wordTick = " + wordTick);            System.out.print(", dit = " + ditDuration);            System.out.print(", dah = " + dahDuration);            System.out.print(", char  = " + charDuration);            System.out.print(", word  = " + wordDuration);            System.out.println();        }    }    /**     * Synthesize a string of text.     * @param morseText         The text to synthesize.     * @return the duration of the text.     */    public long synthesize(            String              morseText        )    {        long duration           = 0;        MorseCode[] symbols     = MorseCode.getCodeTokens(morseText);        // System.out.println("Synthesizing \"" + morseText + "\": " + symbols.length);        for (int i = 0; i < symbols.length; i++) {            duration            += synthesize(symbols[i]);        }        // System.out.println(morseText + ": " + duration);        return (duration);    }    /**     * Synthesize this Morse Code character, writing the data into the     * sound stream buffer.     * @param symbol            The Morse Code character to send.     * @return the duration of this symbol     */    public int synthesize(            MorseCode           symbol        )    {        int totalDuration       = 0;        try {            String code         = symbol.getCode();            if (code == null) {                // System.out.println("synthesizing null code");                synthesizer.silence(dahDuration);                totalDuration   += (dahDuration);            }            else {                for (int i = 0; i < code.length(); i++) {                    char c      = code.charAt(i);                    // System.out.println("Synthesizing '" + c + "'");                    switch (c) {                    case '.':                        synthesizer.tone(ditDuration);                        totalDuration   += ditDuration;                        break;                    case '-':                        synthesizer.tone(dahDuration);                        synthesizer.silence(ditDuration);                        totalDuration   += dahDuration;                        break;                    case ' ':                        /*                         * Note that the total duration will be 3 + 1 + 3 == 7                         * if the space stands by itself.                         */                        synthesizer.silence(wordDuration);                        totalDuration   += wordDuration;                        break;                    default: /* Bogus character: ignored */                        break;                    }                    synthesizer.silence(ditDuration);                    totalDuration       += ditDuration;                }                /*                 * Letter gap (note the we just did a symbol gap).                 */                synthesizer.silence(charDuration);                totalDuration   += (charDuration);            }        }        catch (Exception e) {            e.printStackTrace();        /* Can't happen */        }        return (totalDuration);    }    /**     * Debug MorseCodeSender by printing the duration of a     * standard synthesis.     */    public void testDurationComputation()    {        testDurationComputation(true);        testDurationComputation(false);    }    /**     * Debug MorseCodeSender by printing the duration of a     * standard synthesis.     * @useFarnsworth           True to test Farnsworth synthesis.     */    public void testDurationComputation(            boolean             useFarnsworth        )    {        int oldTextRate                 = getTextRate();        int oldFarnsworthRate           = getFarnsworthRate();        boolean oldUsingFarnsworth      = usingFarnsworth();        setUsingFarnsworth(useFarnsworth);        System.out.println("Farnsworth " + ((usingFarnsworth) ? "enabled" : "disabled"));        for (int tWPM = 5; tWPM < 18; tWPM++) {            setTextRate(tWPM);            StringBuffer work   = new StringBuffer();            for (int i = 0; i < tWPM; i++) {                if (i > 0) {                    work.append(" ");                }                work.append("PARIS");            }            String testString   = work.toString();            System.out.println(tWPM + " " + testString);            if (usingFarnsworth) {                for (int fWPM = 15; fWPM <= 18; fWPM++) {                    setFarnsworthRate(fWPM);                    synthesizer.resetSynthesizer();                    long duration       = synthesize(testString);                    System.out.println(" " + fWPM + " " + duration + " msec.");                }            }            else {                synthesizer.resetSynthesizer();                long duration   = synthesize(testString);                System.out.println(" " + tWPM + " " + duration + " msec.");            }            System.out.println();        }        synthesizer.resetSynthesizer();        setUsingFarnsworth(oldUsingFarnsworth);        setTextRate(oldTextRate);        setFarnsworthRate(oldFarnsworthRate);    }}