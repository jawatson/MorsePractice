package org.minow.MorsePractice;import java.awt.Color;import java.awt.Font;import javax.swing.JTextPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
/** * <p> * ResultPanel displays the random (correct) text, noting incorrect * </p> * <p> * Copyright &copy; 2009 *        <a href="hz1jw@qsl.net">Jim Watson</a>. *        All Rights Reserved. * </p> * <p> * <small> * Permission to use, copy, modify, and redistribute this software and its * documentation for personal, non-commercial use is hereby granted provided that * this copyright notice and appropriate documentation appears in all copies. This * software may not be distributed for fee or as part of commercial, "shareware," * and/or not-for-profit endevors including, but not limited to, CD-ROM collections, * online databases, and subscription services without specific license.  The * author makes no expressed or implied warranty of any kind and assumes no * responsibility for errors or omissions. No liability is assumed for any incidental * or consequental damages in connection with or arising out of the use of the * information or program. * </small> * </p> * * @author Jim Watson</a> * @version 1.0 * 18 Apr 09 */class ResultPanel extends JTextPane {        
    public static final long serialVersionUID = 13040901L;    /**     * Displays this trial confusion matrix result.     * @param scrollPane        The ScrollPane that contains     *                          this matrix.     * @param textFont          The drawing font     * @param sentText          The text that we sent     * @param receivedText      The text that the student heard     */    public ResultPanel(            Font                textFont,            String              sentText,            String              receivedText) {        super();
	     setText(sentText);
        char[] sentChars        = sentText.toCharArray();        char[] gotChars         = receivedText.toCharArray();        int sentLength          = sentText.length();        int receivedLength      = receivedText.length();        setFont(textFont);
        int shortLength         = Math.min(sentLength, receivedLength);

        MutableAttributeSet matchedAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(matchedAttr, Color.black);
        StyleConstants.setBackground(matchedAttr, Color.white);
                        
        MutableAttributeSet errorAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(errorAttr, Color.black);
        StyleConstants.setBackground(errorAttr, Color.red);
                
        StyledDocument sd = getStyledDocument();
        sd.setCharacterAttributes(0,sentLength,errorAttr, true);        for (int i = 0; i < shortLength; i++) {            char sent           = Character.toUpperCase(sentChars[i]);            char got            = Character.toUpperCase(gotChars[i]);
            if (sent == got) {
                sd.setCharacterAttributes(i,1,matchedAttr, true);
            }        }    }
    
    }
