package org.minow.MorsePractice;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

    public static final long serialVersionUID = 13040901L;
	     setText(sentText);
        char[] sentChars        = sentText.toCharArray();


        MutableAttributeSet matchedAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(matchedAttr, Color.black);
        StyleConstants.setBackground(matchedAttr, Color.white);
                        
        MutableAttributeSet errorAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(errorAttr, Color.black);
        StyleConstants.setBackground(errorAttr, Color.red);
                
        StyledDocument sd = getStyledDocument();
        sd.setCharacterAttributes(0,sentLength,errorAttr, true);

                sd.setCharacterAttributes(i,1,matchedAttr, true);
            }
    
    