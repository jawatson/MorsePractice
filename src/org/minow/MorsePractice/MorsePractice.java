package org.minow.MorsePractice;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
//import java.awt.datatransfer.Clipboard;
//import java.awt.datatransfer.DataFlavor;
//import java.awt.datatransfer.FlavorEvent;
//import java.awt.datatransfer.FlavorListener;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.net.URL;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.Document;
import javax.swing.TransferHandler;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.minow.sound.*;

/**
 * <p>
 * MorsePractice helps you learn Morse Code. You can use it to
 * train your ear to recognize the individual symbols, to listen
 * to randomly-generated QSO's (amateur radio contacts), or to
 * listen to any text you can type or read from a file. You can
 * also save the audio file MorsePractice generates from its
 * messages.
 * </p>
 * <p>
 * Random symbol training is based on the Koch training sequence,
 * described by David Finley in
 * <a href="http://www.qsl.net/n1irz/finley.morse.html">
 * <code>&lt;http://www.qsl.net/n1irz/finley.morse.html&gt;</code>
 * </a>
 * .
 * </p>
 * <p>
 * The Koch method is also described in the book, "The Art and Skill of
 * Radio-telegraphy," by William G. Peirpoint, N0HFF, as
 * distributed on the Web at
 * <a href="http://www.qsl.net/n9bor/n0hff.htm">
 * <code>&lt;http://www.qsl.net/n9bor/n0hff.htm&gt;</code>
 * </a>
 * </p>
 * <p>
 * According to the Koch method, you should learn the code in the
 * following sequence:
 * <blockquote>
 *          <code>K M R S U A P T L O</code>
 *      <br><code>W I . N J E F 0 Y ,</code>
 *      <br><code>V G 5 / Q 9 Z H 3 8</code>
 *      <br><code>B ? 4 2 7 C 1 D 6 X</code>
 *      <br><code>&lt;BT&gt; &lt;SK&gt; &lt;AR&gt;</code>
 * </blockquote>
 * </p>
 * <p>
 * The three "prosigns", <code>BT</code>, <code>AR</code>,
 * and <code>SK</code> are representated by the characters '=', '+', and '#'
 * respectively.
 * </p>
 * <p>
 * The QSO format and content are based on the SuperiorMorse program
 * written by Paul J. Drongowski (N2OQT). The random sentence generator
 * is based on one that I wrote for the Decus C compiler toolkit which,
 * in turn, was based on James Gimpel's "Algorithms in SNOBOL4."
 * </p>
 * <p>
 * The Sun Audio package is described (thanks, Sherlock) in
 * <a href="http://www.cooper.edu/~donahu/auformat/auFormat.html">
 * <code>&lt;http://www.cooper.edu/~donahu/auformat/auFormat.html&gt;</code>
 * </a>.
 * </p>
 * <p>
 * Additional information on audio synthesis is available in
 * <br><a href="http://www.alulabs.com/fordevelopers/article3.html">
 *   <code>&lt;http://www.alulabs.com/fordevelopers/article3.html&gt;</code>
 * </a>,
 * <br><a href="http://www.alulabs.com/fordevelopers/PlayingWave/PlayingWave.java">
 *   <code>&lt;http://www.alulabs.com/fordevelopers/PlayingWave/PlayingWave.java&gt;</code>
 * </a>,
 * <br><a href="http://java.sun.com/products/java-media/mail-archive/Framework/0298.html">
 *   <code>&lt;http://java.sun.com/products/java-media/mail-archive/Framework/0298.html&gt;</code>
 * </a>,
 * <br>and <a href="http://www.acme.com/resources/classes/Acme/SynthAudioClip.java">
 *   <code>&lt;http://www.acme.com/resources/classes/Acme/SynthAudioClip.java&gt;</code>
 * </a>.
 * </p>
 * <p>
 * Morse code sending speed is measured in words per minute, where the
 * standard word is "PARIS". The duration of the code elements (dits and dahs)
 * are defined in "ticks" as follows (a tick is essentially equal to one
 * Baud):
 * <ul>
 *  <li>a dit is one tick followed by a one tick silence.
 *  </li>
 *  <li>a dah is three ticks followed by a one tick silence..
 *  </li>
 *  <li>a letter is a sequence of dits and dahs followed by
 *        three ticks (total, i.e., the normal one tick with two
 *        additional ticks).
 *  </li>
 *  <li>a word is a sequence of letters followed by 7 ticks (the
 *        normal 3 tick letter silence and 4 additional ticks).
 * </ul>
 * Thus, the word "PARIS" "<b>.--. .- .-. .. ...</b>" contains
 * 10 dits, 4 dahs, 5 letters, and 1 word, for a total of 50 Baud.
 * To convert words per minute to milliseconds, then:
 * <br> msec/tick = (60 * 1000) / (wpm * 50)
 * <br>&nbsp;&nbsp;5 wpm&nbsp;240 msec
 * <br>&nbsp;13 wpm&nbsp;92 msec
 * <br>&nbsp;18 wpm&nbsp;67 msec
 * <br>&nbsp;20 wpm&nbsp;60 msec
 * <br>
 * Farnsworth timing helps the student learn Morse Code by sending the
 * letters at a high speed (perhaps 18 wpm) and adding silence betewen
 * letters and words to lower the aggregate speed to the target (perhaps
 * 13 wpm). This is described further in MorseCodeSender.java.
 * </p>
 * <p>
 * <p>
 * Changes from Version 1.1 (as published in QST May 2000):
 * <ul>
 *  <li>Added an options menu and a dialog to set the sounder frequency.
 *  </li>
 * </ul>
 * Copyright &copy; 1999-2000 
 *      <a href="mailto:minow@pobox.com">Martin Minow</a>.
 *      All Rights Reserved.
 * </p>
 * <p>
 * <small>
 * Permission to use, copy, modify, and redistribute this software and its
 * documentation for personal, non-commercial use is hereby granted provided that
 * this copyright notice and appropriate documentation appears in all copies. This
 * software may not be distributed for fee or as part of commercial, "shareware,"
 * and/or not-for-profit endevors including, but not limited to, CD-ROM collections,
 * online databases, and subscription services without specific license.  The
 * author makes no expressed or implied warranty of any kind and assumes no
 * responsibility for errors or omissions. No liability is assumed for any incidental
 * or consequental damages in connection with or arising out of the use of the
 * information or program.
 * </small>
 * </p>
 *
 * @author <a href="mailto:minow@pobox.com">Martin Minow</a>
 * @version 2.0
 * @this application has been revised from the original to use Swing components.
 * It has been tested on Ubuntu 8.10, using the Sun Java(TM) Runtime 
 *	Environment (JRE) 6.
 *
 */
 
 /**
  * Various updates and Swing components added by J.Watson 2003, 2009  
  */
 
public class MorsePractice extends JApplet
        implements      ActionListener,         /* Menu and button events       */
                        ItemListener,           /* Checkbox listener            */
                        Sound.Listener,         /* Synthesis active          	*/
                        CaretListener,   	/* Listens to textArea document and sets Edit Menu/Cut item     */
                        ComponentListener,      /* Watch for open/close events  */
                        DocumentListener       /* Used to listen for changes to the text area and enable the listen button*/
                        /*FlavorListener           Used to enable the paste menu item */ 
{
    public static final long serialVersionUID = 13040901L;
    public static final String copyright    =
        "Copyright \u00A9 1999-2000, Martin Minow. All Rights Reserved.";
    public static final String version      = "2.0";
    public static final String author       = 
        "Martin Minow, <minow@pobox.com> \nSwing components added by Jim Watson, <HZ1JW@qsl.net>";
    public static final boolean DEBUG       = false;
    /*
     * These are default settings for the configuration dialogs. They are
     * superseded by the user's parameter file.
     */
    public static final int     defaultRandomDuration   = 1;    /* Minutes 	*/
    public static final boolean defaultFiveCharGroups   = true;
    public static final boolean defaultRandomCharGroups	= false;
    
    /*
     * Two variables that specify the mainimum and maximum length of groups
     * if random length groups are specified. JW.
     */
    private static final int	MIN_GROUP_LENGTH	= 2;
    private static final int	MAX_GROUP_LENGTH	= 7;
    public static final int	FIVE_CHAR_GROUP	= 1; 
    public static final int	NO_GROUP		= 2; 
    public static final int	RANDOM_LEN_GROUP	= 3;
    
    /**
     * defaultParamFile is no longer declared here, the file location is found by 
     * calling getDefaultParamFile().  When running on OSX, the default prefs
     * file location requires a getProperty("user.home") that can't be placed 
     * where it will throw a security exception if run as an applet. JW
     *
     * public static String  defaultParamFile        = "MorsePractice.txt";
     */

    /*
     * If we use the Farnsworth method, code sent at less than the Farnsworth
     * rate is sent is sent with the symbols at the Farnsworth rate and the
     * symbol and letter gaps enlongated to bring the overall rate down to
     * the text rate..
     */
    public static final boolean defaultUsingFarnsworth  = true;
    public static final int     defaultTextRate         = 15;   /* Words per minute     */
    public static final int     defaultFarnsworthRate   = 18;   /* Words per minute     */
    /*
     * To avoid glitches, the program always prepends a bit of silence before the
     * first symbol to let the synthesizer settle.
     */
    public static final int    initialSilence           = 50;   /* Msec                 */
    
    public static final String  morsePracticeImageName  = "morse.gif";
    
    /**
     * kochString defines the Koch training sequence.
     */
    public static final String kochString       =
        "KMRSUAPTLOWI.NJEF0Y,VG5/Q9ZH38B?427C1D6X=+#";
    /**
     * kochChars is the Koch training sequence as a character vector.
     */
    public static final char kochChars[] = new char[kochString.length()];
    {
        for (int i = 0; i < kochChars.length; i++) {
            kochChars[i]        = MorsePractice.kochString.charAt(i);
        }
    }
  
    /*
     * Configure the user Interaction
     */
    Font dialogFont                     = new Font("Dialog", Font.PLAIN, 12);
    Font symbolFont                     = new Font("Monospaced", Font.PLAIN, 12);
    private boolean isJustCreated       = true;
    private JTextArea textArea       	 = new JTextArea();
    private final UndoManager undo = new UndoManager();
    private Document practiceDocument;
    private JScrollPane textScrollPane	= null;
    /*
     * Create the Actions that are associated with the textArea
     * and the manus
     */
    private Action undoAction = new AbstractAction("Undo") {
        public void actionPerformed(ActionEvent evt) {
            try {
                if (undo.canUndo()) {
                    undo.undo();
                }
            } catch (CannotUndoException e) {
            /* do nothing */
            }
        } };
            
    private Action redoAction = new AbstractAction("Redo") {
        public void actionPerformed(ActionEvent evt) {
            try {
                if (undo.canRedo()) {
                    undo.redo();
                }
            } catch (CannotRedoException e) {
            /* do nothing */
            }
        } };
        
    private KochTextField kochTextField = null;
    private static final String pRandomSymbols  = "Random Symbols";
    private static final String pRandomQSO      = "Random QSO";
    private static final String pRandomShortQSO = "Random Short QSO";
    private static final String pRandomMediumQSO = "Random Medium QSO";
    private static final String pRandomLongQSO  = "Random Long QSO";
    private static final String pRandomDigits   = "Random Digits";
    private static final String pRandom2Element = "Random 1 & 2-Elem Symbs";
    private static final String pRandom3Element = "Random 3-Elem Symbs";
    private static final String pRandom4Element = "Random 4-Elem Symbs";
    private static final String pRandomPunct    = "Random Punct & Signs";
    private static final String pRandomCallSign = "Random Call Signs";
    
    private static final String[] randomChoiceItems = { pRandomSymbols, pRandomQSO,
         pRandomShortQSO, pRandomMediumQSO, pRandomLongQSO, pRandomDigits, 
         pRandom3Element, pRandom4Element, pRandomPunct, pRandomCallSign};
         
    private JComboBox<String> randomChoice       = new JComboBox<String>(randomChoiceItems);

    private JButton listenButton         = new JButton(" Listen ");
    private JButton startButton          = new JButton(" Start Trial ");
    private JButton stopButton           = new JButton(" Stop ");
    private JButton checkButton          = new JButton(" Check ");
    private JButton trainButton          = new JButton(" Training Info ");
    private JButton infoButton           = new JButton(" Program Info ");
    private JButton optionsButton        = new JButton(" Options ");
    private IntegerField textRateField  = new IntegerField(
                3, defaultTextRate, 0, 50);
    private IntegerField farnsworthRateField    = new IntegerField(
                3, defaultFarnsworthRate, 0, 50);
    private IntegerField randomDurationField    = new IntegerField(
                3, defaultRandomDuration);
    private JCheckBox usingFarnsworth    = new JCheckBox("Use Farnsworth Timings");
    private JRadioButton fiveCharGroups  = new JRadioButton("5 Character Groups");
    private JRadioButton randomCharGroups= new JRadioButton("Random Length Groups");
    private JRadioButton streamChars	 = new JRadioButton("Character Stream");
    private ButtonGroup randomOptionGroup = new ButtonGroup();
    
    /*
     * The entity names (of the form "pXXX") define the values that are stored
     * in the parameter file. The random training flavor uses the actual
     * Choice strings, defined above.
     */
    public static final String pTextRate        = "TextRate";
    public static final String pFarnsworthRate  = "FarnsworthRate";
    public static final String pTrialDuration   = "TrialDuration";
    public static final String pUseFarnsworth   = "UseFarnsworth";
    public static final String pGroupType	= "RandomGroupType"; 
    public static final String pUseRandCharGroups = "UseRandCharGroups";
    public static final String pRandomChars     = "RandomCharacters";
    public static final String pRandomFlavor    = "RandomFlavor";
    /* Version 1.2 */
    public static final String pSynthFrequency  = "SynthesizerFrequency";
    
    /**
     * These classes must be present to use the JavaxSoundSynthesizer.
     * It is supported on Java 1.3 on Windows.
     */
    private static final String javaxSoundClassNames[] = {
            "javax.sound.sampled.AudioFormat",
            "javax.sound.sampled.SourceDataLine",
            "javax.sound.sampled.DataLine",
            "javax.sound.sampled.LineListener"
        };
    /* Version 1.3 */
    /**
     * These classes must be present to use the SunSoundSynthesizer.
     * It is not supported but provided on Java 1.2 on Windows and Mac.
     */
    private static final String sunSoundClassNames[] = {
            "sun.audio.AudioData",
            "sun.audio.AudioDataStream",
            "sun.audio.AudioStream",
            "sun.audio.AudioPlayer"
        };

    /*
     * Menus
     */
    private JMenuBar menuBar             = new JMenuBar();
    /*
     * The File Menu
     */
    private JMenu fileMenu               = new JMenu("File");
    private JMenuItem miOpen             = new JMenuItem("Open...", KeyEvent.VK_O);
    private JMenuItem miSaveText         = new JMenuItem("Save Text...",KeyEvent.VK_S);
    private JMenuItem miSaveAudio        = new JMenuItem("Save Audio...");
    private JMenuItem miQuit             = new JMenuItem("Quit");
    private JMenuItem miHelp             = new JMenuItem("MorsePractice Help");
    private JMenuItem miParis            = new JMenuItem("Paris text for debug");

    /*
     * The Edit Menu
     */
    private JMenu editMenu               = new JMenu("Edit", true); /* Tearoff ok */
    private JMenuItem miUndo             = new JMenuItem(undoAction);
    private JMenuItem miRedo             = new JMenuItem(redoAction);
    private JMenuItem miCut              = new JMenuItem(TransferHandler.getCutAction());
    private JMenuItem miCopy             = new JMenuItem(TransferHandler.getCopyAction());
    private JMenuItem miPaste            = new JMenuItem(TransferHandler.getPasteAction());
    private JMenuItem miClear            = new JMenuItem("Clear");
    private JMenuItem miSelectAll        = new JMenuItem("Select All");
    private JMenuItem miSelectNone       = new JMenuItem("Select None");
    
    /*
     * The user experience is contained in the following panels:
     *  codePanel       Buttons for each Morse Code symbol
     *  textEntryPanel  Type-in text, code speed (regular and Farnsworth),
     *                  listen and stop buttons.
     *  randomTextPanel The random text field, minutes of text,
     *                  Five-character code groups, and "random" button.
     *  infoButton      The info button goes into a panel by itself.
     */    
    private JPanel       codePanel;
    private JPanel       buttonPanel;
    private JPanel	 optionPanel;
    /**
     * The Help dialogs.
     */

    /*
     * The MorseCode database. buttons contains the Morse Code
     * checkbox using the letter as key.
     */
    private Hashtable<String, JCheckBox> buttons = new Hashtable<String, JCheckBox>();
    private MorseCode morseSpace        = new MorseCode(' ', " ");
    private MorseCodeSender morseSender;
    private Sound synthesizer           = null;
    
    /*
     * randomQSO contains a random sentence grammar. Because the
     * random sentence compiler is slow, it is initialized when the
     * user first tries to listen to a random QSO
     */
    private RandomQSO randomQSO         = null;
    
    /*
     * The most recent random text sequence is stored here.
     * wasRandomQSO is true to load the randomText string
     * into the text buffer when the code stops playing.
     * This is only needed for random QSO's as the user
     * types in what was received for Koch training, and
     * the text should already be present for "listen
     * to the text". This is a bit messy and could stand
     * some cleanup.
     */
    private String      randomText      = "";
    private boolean     wasRandomQSO    = false; 

    /* The cipboard is used to listen for 'pastable' data that 
     * enables the edit menu paste item
     */
    //private Clipboard clipBoard;
    
    /*
     * This is set true if MorsePractice is started as an application.
     */
    private boolean     isApplication   = false;
    
    public MorsePractice() {
        this(false);
    }
    
    /**
     * This is a special constructor that is called with
     * isApplication set true when MorsePractice runs as an
     * application. It's effect is to enable the File menu.
     * See the main() method below.
     * @param isApplication     true if an application, false if a real applet.
     */
    public MorsePractice(boolean isApplication) {
        super();
        this.isApplication      = isApplication;
    }
    /**
     * Initialize the MorsePractice applet/application.
     */
    public void init() {
        showStatus(copyright);
        synthesizer             = null;
        if (synthesizer == null && isSupported(javaxSoundClassNames)) {
            try {
                synthesizer     = new JavaxSoundSynthesizer();
                if (DEBUG) {
                    System.out.println("Using JavaxSoundSynthesizer");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.toString());
            }
        }
        // The javax synth is now well supported and the sun classes are 
        // giving deprecation errors so have been removed (jw 1 Oct 2011)
        /*
        if (synthesizer == null && isSupported(sunSoundClassNames)) {
            try {
                synthesizer     = new SunSoundSynthesizer();
                if (DEBUG) {
                    System.out.println("Using JavaxSoundSynthesizer");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.toString());
            }
        }
        */
        if (synthesizer == null) {
            /*
             * Since MorsePractice uses either javax.sound or sun.audio, it
             * will fail if run by  a browser that supports AudioClip using
             * a different audio implementation.
             */
            JOptionPane.showMessageDialog(this, 
               "Your Java implementation does not provide a supported"
               + " audio class library. MorsePractice can use the \"sun.audio\""
               + " or \"javax.sound\" libraries. Unfortunately, these are not"
               + " available on your Java implementation."
               + "\n"
               + "MorsePractice will exit when you click on the \"OK\" button.",
               "Morse Practice Failure",
                JOptionPane.ERROR_MESSAGE);


            return;
        }
        if (DEBUG) {
            System.out.println("Using " + synthesizer.getClass().getName());
        }
        try {
            morseSender         = new MorseCodeSender(synthesizer);
            randomQSO           = new RandomQSO();
       
        }
        catch (Exception e) {
            System.out.println("Can't create random QSO grammar: " + e.toString());
        }
        addComponentListener(this);
        //System.out.println("Adding a clipboard");
        //clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //clipBoard.addFlavorListener(this);        
        
        ImageIcon imageCanvas = null;
        URL imgURL = getClass().getResource(morsePracticeImageName);
        if (imgURL != null) {
            imageCanvas = new ImageIcon(imgURL);
        } 

        createCodePanel();
        createButtonPanel(new JLabel(imageCanvas));
        createOptionPanel();
        /*
         * Create the KochTextField after creating the code panel.
         */
        kochTextField = new KochTextField(this);

        /*
         * Create the menus before creating the text areas because
         * the EditTextArea may call back to enableEditMenu on
         * initialization.
         */

        createMenus();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.addCaretListener(this);
        textArea.getDocument().addDocumentListener(this);
	textScrollPane = new JScrollPane(textArea, 
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        /* Listen for undo and redo events */
        practiceDocument = textArea.getDocument();
        practiceDocument.addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent evt) {
                undo.addEdit(evt.getEdit());} });        

        textArea.getActionMap().put("Undo", undoAction);   
        /* Bind the undo action to ctl-Z */
        textArea.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
        /* Create a redo action and add it to the text component */
        textArea.getActionMap().put("Redo", redoAction);
        /* Bind the redo action to ctl-Y */
        textArea.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");

        fiveCharGroups.setSelected(defaultFiveCharGroups);
        usingFarnsworth.setSelected(defaultUsingFarnsworth);
        toggleFarnsworthSpeed();
        usingFarnsworth.addItemListener(this);
        textArea.setFont(symbolFont);
        randomChoice.setFont(dialogFont);
        startButton.setFont(dialogFont);
        startButton.addActionListener(this);
        listenButton.setFont(dialogFont);
        listenButton.addActionListener(this);
        stopButton.setFont(dialogFont);
        stopButton.addActionListener(this);
        checkButton.setFont(dialogFont);
        checkButton.addActionListener(this);
        trainButton.setFont(dialogFont);
        trainButton.addActionListener(this);
        infoButton.setFont(dialogFont);
        infoButton.addActionListener(this);
        stopButton.setEnabled(false);
        listenButton.setEnabled(false);
        checkButton.setEnabled(false);
        optionsButton.setFont(dialogFont);
        optionsButton.addActionListener(this);
        optionsButton.setEnabled(true);
        synthesizer.addListener(this);
        createUserExperience();
        
        setJMenuBar(menuBar);
        
        //enableEditMenu(false);     /* No edit until focus gained   */
        Dimension d = getPreferredSize();
        if (false) {
            showStatus("width = " + d.width + ", height = " + d.height);
            System.out.println("width = " + d.width + ", height = " + d.height);
        }
        if (isApplication) {
            readParameters();                   /* After default state was set  */
        }
    }
    /**
     * createMenus will be called by the class initializer to
     * create the various menus and add them to the MenuBar
     */
    private void createMenus() {
        /* keyboard accelerators are added here as they can't be added in a 
         * platform independant manner during declarations.
         */
        int defMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        
        miQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, defMask));

        /* file menu */
        miOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, defMask));
        miSaveText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, defMask));
        /* edit menu */
        /* All menus start out inactive.  They get enabled by listening to caret 
        /* and clipboard events 
         */   
        miCut.setText("Cut");
        miCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, defMask));
        miCut.setEnabled(false);
        miCopy.setText("Copy");
        miCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, defMask));
        miCopy.setEnabled(false);
        miPaste.setText("Paste");      
        miPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, defMask));

        /*miUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, defMask));*/

        if (isApplication) {
            fileMenu.add(miOpen);
            fileMenu.add(miSaveText);
            fileMenu.addSeparator();
            fileMenu.add(miSaveAudio);
            fileMenu.addSeparator();
            fileMenu.add(miQuit);
            menuBar.add(fileMenu);
        }
        /* */
        editMenu.add(miUndo);
        editMenu.add(miRedo);
        editMenu.add(miCut);
        editMenu.add(miCopy);
        editMenu.add(miPaste);
        editMenu.addSeparator();
        editMenu.add(miClear);
        editMenu.addSeparator();
        editMenu.add(miSelectAll);
        editMenu.add(miSelectNone);
        if (false) {    /* Debugging */
            editMenu.addSeparator();
            editMenu.add(miParis);
        }
        /* */
        menuBar.add(editMenu);
        /* */
        /*
        JW. The getHelpMenu is not yet implemented in Swing 1.3 so a quick
        kludge is used here.  The original line was
        
        JMenu helpMenu   = menuBar.getHelpMenu();
        */
        JMenu helpMenu = null; // todo quick fix
        if (helpMenu != null) {
            helpMenu.addSeparator();
        }
        else {
            helpMenu    = new JMenu("Help", true);
            /* Again, the setHelpMenu is not yet implemented and throws 
            an exception at runtime.
            */
            //menuBar.setHelpMenu(helpMenu); //todo
        }
        helpMenu.add(miHelp);
        miHelp.addActionListener(this);
        for (int i = 0; i < fileMenu.getItemCount(); i++) {
            if (fileMenu.getItem(i) != null) {
                fileMenu.getItem(i).addActionListener(this);
            }
        }
        for (int i = 0; i < editMenu.getItemCount(); i++) {
            if (editMenu.getItem(i) != null) {
                editMenu.getItem(i).addActionListener(this);
            }
        }
        /*for (int i = 0; i < optionsMenu.getItemCount(); i++) {
            optionsMenu.getItem(i).addActionListener(this);
        }*/
    }
    /**
     * createUserExperience will be called by the class
     * initializer to organize the components on the display.
     */
    private void createUserExperience() {
        JPanel buttonAndCodePanel = new JPanel();
        buttonAndCodePanel.setLayout(new BorderLayout());
        buttonAndCodePanel.add(codePanel, BorderLayout.WEST);
        buttonAndCodePanel.add(buttonPanel, BorderLayout.EAST);
        /* */
        textArea.setBackground(Color.white);
        /*
        JW.  Swing supports borders so these lines may be replaced.
        
        EtchedBorder textPanel  =
                        new EtchedBorder(textArea, "Listen or Practice Text");
        textPanel.setBackground(getBackground());
        EtchedBorder kochPanel  =
                        new EtchedBorder(kochTextField, "Koch Training Sequence");
        */

        textScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Listen or Practice Text"));
        kochTextField.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Koch Training Sequence"));
        GridBagLayout gridbag   = new GridBagLayout();
        
        GridBagConstraints c    = new GridBagConstraints();
        getContentPane().setLayout(gridbag);
        c.gridy                 = 0;
        c.gridx                 = 0;
        c.weightx               = 1.0;
        c.weighty               = 0.0;
        c.fill                  = GridBagConstraints.HORIZONTAL;
        c.anchor                = GridBagConstraints.SOUTHEAST;
        c.insets                = new Insets(2, 2, 2, 2);
        gridbag.setConstraints(buttonAndCodePanel, c);
        getContentPane().add(buttonAndCodePanel);
        c.gridy                 += 1;
        c.weighty               = 1.0;
        c.fill                  = GridBagConstraints.BOTH;
        gridbag.setConstraints(textScrollPane, c);
        getContentPane().add(textScrollPane);
        c.gridy                 += 1;
        c.weighty               = 0.0;
        c.fill                  = GridBagConstraints.HORIZONTAL;
        gridbag.setConstraints(kochTextField, c);
        getContentPane().add(kochTextField);
        c.gridy                 += 1;
        gridbag.setConstraints(optionPanel, c);
        getContentPane().add(optionPanel);
    }
    /**
     * createButtonPanel creates the user control options.
     */
    private void createButtonPanel(JLabel imageCanvas) {
        GridBagLayout gridbag   = new GridBagLayout();
        GridBagConstraints c    = new GridBagConstraints();
        buttonPanel             = new JPanel(gridbag);
        c.gridy                 = 0;
        c.gridx                 = 0;
        c.weightx               = 0.0;
        c.weighty               = 0.0;
        c.fill                  = GridBagConstraints.HORIZONTAL;
        c.anchor                = GridBagConstraints.LAST_LINE_END;
        c.insets                = new Insets(2, 2, 2, 2);
        c.gridwidth             = 2;
        if (imageCanvas != null) {
        		c.gridwidth          = 1;
            c.gridx             = 1; 
            c.fill              = GridBagConstraints.NONE;
            gridbag.setConstraints(imageCanvas, c);
            buttonPanel.add(imageCanvas);
            
            c.fill              = GridBagConstraints.HORIZONTAL;
            c.gridx             = 0;        	
            c.gridy             += 1;
            c.gridwidth          = 2;
        }
        gridbag.setConstraints(randomChoice, c);
        buttonPanel.add(randomChoice);
        c.gridy                 += 1;
        c.weighty               = 0.0;
        c.gridwidth             = 1;
        c.gridx                 = 0;
        gridbag.setConstraints(startButton, c);
        buttonPanel.add(startButton);
        c.gridx                 += 1;
        gridbag.setConstraints(stopButton, c);
        buttonPanel.add(stopButton);
        c.gridy                 += 1;
        c.gridx                 = 0;
        gridbag.setConstraints(listenButton, c);
        buttonPanel.add(listenButton);
        c.gridx                 += 1;
        gridbag.setConstraints(checkButton, c);
        buttonPanel.add(checkButton);
        c.gridy                 += 1;
        c.gridx                 = 0;
        gridbag.setConstraints(trainButton, c);
        buttonPanel.add(trainButton);
        c.gridx                 += 1;        
        gridbag.setConstraints(infoButton, c);
        buttonPanel.add(infoButton);
        c.gridy                 += 1;
        gridbag.setConstraints(optionsButton, c);
        buttonPanel.add(optionsButton);
    }
    /**
     * The code panel has one checkbox for every Morse Code symbol.
     */
    private void createCodePanel() {
        codePanel      		= new JPanel(new GridLayout(0, 6));
        FontMetrics fm          = getFontMetrics(symbolFont);
        int buttonWidth         = fm.stringWidth("MMM");
        /*
         * The buttons are much wider than I expected. Is this a
         * bug or a feature?
         */
        for (int i = 0; i < MorseCode.morse.length; i++) {
            MorseCode morse     = MorseCode.morse[i];
            JCheckBox button     = new JCheckBox(morse.getLabel());
            button.setFont(symbolFont);
            Dimension d         = button.getPreferredSize();
            d.width             = buttonWidth;
            button.setSize(d);
            if (morse.getCode() != null) {
                /* 
                 * Item listeners are now added after readParameters() JW
                 */
                button.addActionListener(this);
                String key      = String.valueOf(morse.getLetter());
                button.setName(Integer.toString(i));
                buttons.put(key, button);
            }
            else {
                button.setEnabled(false);
                button.setVisible(false);
            }
            codePanel.add(button);
        }
        /*
        Swing supports borders so the following line may be replaced 
        EtchedBorder border = new EtchedBorder(codeButtons, "Morse Code Symbols");
        */
        codePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Morse Code Symbols"));
        if (false) {
            Enumeration<String> e       = buttons.keys();
            while (e.hasMoreElements()) {
                System.out.println("\"" + e.nextElement() + "\"");
            }
        }
    }
    
    
    /**
     * The general option panel has the sending speed information and
     * some options to tailor the random symbol generator
     */
    private void createOptionPanel() {
        GridBagLayout gridbag   = new GridBagLayout();
        GridBagConstraints c    = new GridBagConstraints();
        optionPanel   	        = new JPanel(gridbag);
        c.gridy                 = 0;
        c.gridx                 = 0;
        c.gridwidth             = 1;
        c.weightx               = 0.0;
        c.weighty               = 0.0;
        c.insets                = new Insets(0, 0, 0, 0);       /* Left, Right  */
        c.fill                  = GridBagConstraints.NONE;
        c.anchor                = GridBagConstraints.EAST;
        JLabel overallLabel      = new JLabel("Overall speed (WPM):");
        gridbag.setConstraints(overallLabel, c);
        optionPanel.add(overallLabel);
        c.insets                = new Insets(0, 2, 0, 2);       /* Left, Right  */
        c.gridx                 += 1;
        c.fill                  = GridBagConstraints.NONE;
        c.anchor                = GridBagConstraints.WEST;
        gridbag.setConstraints(textRateField, c);
        optionPanel.add(textRateField);
        
        /* */
        c.gridy                 += 1;
        c.gridx                 = 0;
        c.weightx               = 0.0;
        c.fill                  = GridBagConstraints.NONE;
        c.anchor                = GridBagConstraints.EAST;
        JLabel farnsworthLabel   = new JLabel("Farnsworth speed (WPM):");
        gridbag.setConstraints(farnsworthLabel, c);
        optionPanel.add(farnsworthLabel);
        
        c.gridx                 += 1;
        c.fill                  = GridBagConstraints.NONE;
        c.anchor                = GridBagConstraints.WEST;
        gridbag.setConstraints(farnsworthRateField, c);
        optionPanel.add(farnsworthRateField);
        

        
        c.gridy                 += 1;
        c.gridx                 = 0;
        c.gridwidth             = 1;
        c.weightx               = 0.0;
        c.weighty               = 0.0;
        c.fill                  = GridBagConstraints.NONE;
        c.anchor                = GridBagConstraints.EAST;
        JLabel randomDuration    = new JLabel("Random Text Duration (Minutes):");
        gridbag.setConstraints(randomDuration, c);
        optionPanel.add(randomDuration);
        c.gridx                 += 1;
        c.fill                  = GridBagConstraints.NONE;
        c.anchor                = GridBagConstraints.WEST;
        //c.insets                = new Insets(0, 0, 0, 0);
        gridbag.setConstraints(randomDurationField, c);
        optionPanel.add(randomDurationField);
        
        c.gridy                 =0;
        c.gridx                 += 1;
        gridbag.setConstraints(usingFarnsworth, c);
        optionPanel.add(usingFarnsworth);        
        
        c.gridy                 += 1;
        gridbag.setConstraints(fiveCharGroups, c);
        optionPanel.add(fiveCharGroups);
        /* */
        c.gridy                 += 1;
        c.gridx                 = 2;
        c.weightx               = 0.0;
        gridbag.setConstraints(streamChars, c);
        optionPanel.add(streamChars);
        /* */
        c.gridy			+= 1;
        c.gridx                 = 2;
        gridbag.setConstraints(randomCharGroups, c);
        optionPanel.add(randomCharGroups);
        
        randomOptionGroup.add(fiveCharGroups);
        randomOptionGroup.add(randomCharGroups);
        randomOptionGroup.add(streamChars);
        
        optionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Options"));
    }

    /**
     * Return the minimum panel size so other window components
     * can be constructed. The minimum and preferred sizes are
     * identical.
     * @return the minimum window size.
     */
    public Dimension getMinimumSize() {
        return (getPreferredSize());
    }
    /**
     * Return the preferred panel size so other window components
     * can be constructed. If you add components to the window,
     * be sure to adjust the preferred size computation.
     * @return the preferred window size.
     */
    public Dimension getPreferredSize()
    {
        int codeButtonWidth     = codePanel.getPreferredSize().width
                                + buttonPanel.getPreferredSize().width;
        int codeButtonHeight    = Math.max(
                codePanel.getPreferredSize().height,
                buttonPanel.getPreferredSize().height
            );
        int width               = Math.max(
                codeButtonWidth,
                textArea.getPreferredSize().width
            );
        width                   += 2;
        int height              =
                codeButtonHeight
                + textArea.getPreferredSize().height
                + optionPanel.getPreferredSize().height
                + 16;
        return (new Dimension(width, height));
    }
    /**
     * The Java runtime event manager calls actionPerformed whenever
     * the user clicks in one of the display components.
     * @param event     Describes the user's action.
     */
    public void actionPerformed(ActionEvent event) {
        /*
         * Note that the File Menu is installed when MorsePractice
         * is run as an application.
         */
        Object source = event.getSource();
        if (source == listenButton) {
            listenToText();
        } else if (source == startButton) {
            String thisFlavor   = (String) randomChoice.getSelectedItem();
            if (thisFlavor == pRandomSymbols) {
                setRandomText();
                checkButton.setEnabled(true);
            } else if (thisFlavor == pRandomQSO) {
                int wpm         = textRateField.getValue();
                listenToRandomQSO(wpm);
                checkButton.setEnabled(false);
            } else if (thisFlavor == pRandomShortQSO) {
                listenToRandomQSO(5);
                checkButton.setEnabled(false);
            } else if (thisFlavor == pRandomMediumQSO) {
                listenToRandomQSO(13);
                checkButton.setEnabled(false);
            } else if (thisFlavor == pRandomLongQSO) {
                listenToRandomQSO(20);
                checkButton.setEnabled(false);
            } else if (thisFlavor == pRandomDigits
                  || thisFlavor == pRandom2Element
                  || thisFlavor == pRandom3Element
                  || thisFlavor == pRandom4Element
                  || thisFlavor == pRandomPunct
                  || thisFlavor == pRandomCallSign) {
                setRandomSymbols(thisFlavor);
                checkButton.setEnabled(true);
            }
            else {
                System.out.println("No training choice selected");
            }
        }
        else if (source == stopButton) {
            synthesizer.stopSynthesizer();
        }
        else if (source == checkButton) {
            synthesizer.stopSynthesizer();         /* Stop sending */
            checkRandomTrial();
        }
        else if (source == trainButton) {
            synthesizer.stopSynthesizer();         /* Stop sending */
            //showAboutTrainingDialog();
            showInformationDialog("About Morse Training", "Training.html");
        }
        else if (source == infoButton) {
            synthesizer.stopSynthesizer();         /* Stop sending */
            showInformationDialog("About MorsePractice", "About.html");
        }
        else if (source == optionsButton) {
            synthesizer.stopSynthesizer();         /* Stop sending */
            setFrequencyDialog();
        }
        else if (source == miOpen) {
            synthesizer.stopSynthesizer();         /* Stop sending */
            readTextFile();
        }
        else if (source == miSaveText) {
            synthesizer.stopSynthesizer();         /* Stop sending */
            saveTextFile();
        }
        else if (source == miSaveAudio) {
            synthesizer.stopSynthesizer();         /* Stop sending */
            saveAudio();
        }
        else if (source == miQuit) {
            synthesizer.stopSynthesizer();
            writeParameters();
            System.exit(0);
        }
        
//        else if (source == miUndo) {
//            if (activeTextArea != null) {
                //activeTextArea.performUndo();*/
//            }
//        }
        
        else if (source == miCut) {
                textArea.cut();
        }
        else if (source == miCopy) {
                textArea.copy();
        }
        else if (source == miPaste) {
                textArea.paste();
        }
        else if (source == miClear) {
                //activeTextArea.clearSelection();
        }
        else if (source == miSelectAll) {
            for (int i = 0; i < kochChars.length; i++) {
                setMorseButtonState(kochChars[i], true, false);
            }
        }
        else if (source == miSelectNone) {
            for (int i = 0; i < kochChars.length; i++) {
                setMorseButtonState(kochChars[i], false, false);
            }
        }
        else if (source == miParis) {
            morseSender.testDurationComputation();
        }
        else if (source == miHelp) {
            showInformationDialog("About MorsePractice", "About.html");
        } else if (source instanceof JCheckBox) {
            /*One of the morse buttons*/
            try {
                JCheckBox button = (JCheckBox)source;
                boolean selected = button.isSelected();
                if (selected) {
                    MorseCode symbol = getSymbolFromCheckbox(button);
                    listenToOneSymbol(symbol);
                }
            } catch (NumberFormatException e) {
                System.out.println("Bogus checkbox: " + event.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            if (DEBUG) {
                System.out.println("Unknown actionEvent: " + event.toString());
            }
        }
    }
    
            
    /**
     * The item listener now only listens to the 
     */
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();

        if (source == fiveCharGroups) {
            if ( ( e.getStateChange() == ItemEvent.SELECTED) && (randomCharGroups.isSelected()) ) {
                randomCharGroups.setSelected(false);
            }
        } else if (source == randomCharGroups) {
            if ( ( e.getStateChange() == ItemEvent.SELECTED) && (fiveCharGroups.isSelected()) ) {
                fiveCharGroups.setSelected(false);
            }
        } else if (source == usingFarnsworth) {
        		toggleFarnsworthSpeed();
        }
    }

    /**
     * Enables/Disable Farnsworth speed entry box according to state of the enable
     * farnsworth (usingFarnsworth) radio button.
     */    
    private void toggleFarnsworthSpeed() {
         farnsworthRateField.setEnabled(usingFarnsworth.isSelected());
    }
    
    /**
     * The user clicked on the "Info" button or the help
     * menu item. Display information about MorsePractice.
     */
    private void showInformationDialog(String dialogTitle, String textSource) {
        JEditorPane aboutPane = new JEditorPane();
        aboutPane.setEditable(false);
        try {
            Class<?> mainClass = Class.forName("org.minow.MorsePractice.MorsePractice");
            URL aboutURL    = mainClass.getResource(textSource);
            if (aboutURL != null) {
                aboutPane.setPage(aboutURL);
            } else {
                System.err.println("Couldn't find file: "+textSource);
            }
        } catch (Exception e) {
            System.out.println("Caught exception: "+e.toString());
        }
        
        JScrollPane aboutScrollPane = new JScrollPane(aboutPane);
        aboutScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        aboutScrollPane.setPreferredSize(new Dimension(350, 200));
        aboutScrollPane.setMinimumSize(new Dimension(10, 10));

        JOptionPane.showMessageDialog(this,
            aboutScrollPane,
            dialogTitle,
            JOptionPane.INFORMATION_MESSAGE);


    }

    /**
     * The parameter reader calls setMorseButtonState to restore
     * the user's previous selection. This method is also called
     * by the Koch text field when the user clicks on a symbol.
     * @param letter    A Morse Code sumbol.
     * @param newState  True to select this symbol
     * @param listen    True to listen to this symbol if it's selected.
     *                  (This is set false by the parameter readers
     *                  and true by the checkbox and Koch field listeners).
     * @see #readParameters
     * @see KochTextField#mouseClicked
     */
    public void setMorseButtonState(
            char                letter,
            boolean             newState,
            boolean             listen
        )
    {
        JCheckBox button = getMorseButton(letter);
        if (button != null) {
            boolean oldState = getMorseButtonState(letter);
            if (oldState != newState) {
                button.setSelected(newState);
                kochTextField.setKochLabelState(button);
                if (newState && listen) {
                    MorseCode symbol    = getSymbolFromCheckbox(button);
                    listenToOneSymbol(symbol);
                }
            }
        }
    }
    
    /**
     * Given a Checkbox, get the MorseCodeSymbol it describes.
     * @param button            One of our MorseCode checkboxes.
     * @return The symbol this checkbox defines.
     */
    private MorseCode getSymbolFromCheckbox(JCheckBox button) {
        String name             = button.getName();
        int i                   = Integer.parseInt(name);
        MorseCode symbol        = MorseCode.morse[i];
        return (symbol);
    }

    /**
     * Return the selection state of a particular symbol..
     * @param letter    A Morse Code sumbol.
     * @return true if the symbol is selected.
     */
    public boolean getMorseButtonState(char letter) {
        boolean result  = false;
        JCheckBox button = getMorseButton(letter);
        if (button != null) {
            result = button.isSelected();
        }
        return (result);
    }
    /**
     * Return the button that controls this symbol's
     * selection state. Return null if this is not a
     * valid symbol. The buttons are stored in a
     * Hashtable for speed.
     * @param letter    A Morse Code sumbol.
     * @return this letter's checkbox.
     */
    public JCheckBox getMorseButton(char letter) {
        String key      = String.valueOf(letter);
        JCheckBox button = buttons.get(key);
        if (button == null) {
            System.out.println("No button for '" + letter + "', key = \"" + key + "\"");
        }
        return (button);
    }
    /**
     * Return a string consisting of the selected random
     * text characters.
     * @result The selected characters.
     */
    public String getSelectedText()
    {
        StringBuffer work       = new StringBuffer(kochChars.length);
        for (int i = 0; i < kochChars.length; i++) {
            if (getMorseButtonState(kochChars[i])) {
                work.append(kochChars[i]);
            }
        }
        return (work.toString());
    }
    /**
     * The user has just completed a trial and clicked the "check"
     * button. Grab what the user typed into the text area and
     * throw it at the trial display dialog.
     * @see TrialDialog
     */
    private void checkRandomTrial()
    {
        Trial trial                     = new  Trial();
        long startTime                  = System.currentTimeMillis();
        // MorsePractice.log("Trial: \"" + randomText + "\"");
        trial.setTrialText(randomText); /* What we just sent    */
        String trialText                = textArea.getText();
        int goodCount                   = 0;
        int sentCount                   = 0;
        int i                           = 0;
        for (; i < randomText.length(); i++) {
            char sentChar               = Character.toUpperCase(randomText.charAt(i));
            if (i < trialText.length()) {
                char gotChar            = Character.toUpperCase(trialText.charAt(i));
                if (fiveCharGroups.isSelected()
                 && sentChar == ' '
                 && gotChar == ' ') {
                    ; /* Skip the expected space */
                }
                else {
                    ++sentCount;
                    trial.addPair(sentChar, gotChar);
                    if (sentChar == gotChar) {
                        ++goodCount;
                    }
                }
            }
            else {
                ++sentCount;
                trial.addPair(randomText.charAt(i), '*');
            }
        }
        for (; i < trialText.length(); i++) {
            trial.addPair('*', trialText.charAt(i));
        }
        int percent                     = (goodCount * 100) / sentCount;
        String titleText                = Integer.toString(percent) + "% Correct";
        String headingText              = titleText
                        + ", "
                        + Integer.toString(goodCount)
                        + " correct, "
                        + Integer.toString(sentCount - goodCount)
                        + " incorrect, "
                        + Integer.toString(sentCount)
                        + " total";
        // MorsePractice.log("Text pairs added, " + percent + "% Correct"); 
        
        new TrialDialog(
                    this,
                    titleText,
                    headingText,
                    trial,
                    randomText,
                    trialText
                );
                
    }
    /**
     * The user clicked the Random QSO button. Synthesize a QSO
     * then load it into the text area so the user can verify
     * the results.
     */
    private void listenToRandomQSO(int wpm) {
        textArea.setText("");
        randomText                      = randomQSO.getQSO(wpm);
        wasRandomQSO                    = true;
        listenToText(randomText, true);
    }
    /**
     * The user has just typed some text into the text area
     * and wants to listen to it.
     */
    private void listenToText() {
        wasRandomQSO                    = false;
        listenToText(textArea.getText(), false);
    }
    /**
     * Listen to a string of text. Optionally, prefix the message
     * with five "pips" to give the user time to pick up a pencil.
     * @param text              What to say.
     * @param prefixPips        True to synthesize ticks.
     */
    private void listenToText(String text, boolean prefixPips) {
        try {
            startSending(prefixPips);
            morseSender.synthesize(text);
            synthesizer.silence(initialSilence);
            synthesizer.startSynthesizer();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Listen to an example of a Morse symbol. This is called by the
     * ItemStateListener when the user enables a Checkbox, and by
     * the setMorseButtonState when the user clicks on a Koch symbol.
     */
    protected void listenToOneSymbol(MorseCode symbol) {
        try {
            if (synthesizer.isSynthesizerActive()) {
                long sleep      = (synthesizer.getDuration() + 100) / 10;
                for (int i = 0; synthesizer.isSynthesizerActive() && i < 12; i++) {
                    /* deb("Going to sleep"); */
                    Thread.sleep(sleep);
                }
            }
        }
        catch (InterruptedException e) {}
        catch (Exception e) {}
        startSending(false);
        morseSender.synthesize(symbol);
        synthesizer.startSynthesizer();
    }

    /**
     * Build a random text sequence using the characters that the
     * user selected. This could probably be improved. In particular,
     * it would be useful to get more equal distribution, with
     * emphasis on character pairs that the student finds confusing.
     */
    private void setRandomText() {
        textArea.setText("");
        String kochText                 = getSelectedText();
        boolean useKochText             = kochText.length() > 0;
        boolean useFiveCharGroups       = fiveCharGroups.isSelected();
        int charGroupLength		= 5;
        boolean useRandomLengthGroups	= randomCharGroups.isSelected();
        if (useRandomLengthGroups) {
            charGroupLength = getRandomGroupLength();
        }
        try {
            startSending(true);
            MorseCode[] selection       = MorseCode.getCodeTokens(kochText);
            if (useKochText == false) {
                selection               = MorseCode.getCodeTokens(kochString);
            }
            StringBuffer work           = new StringBuffer();
            long randomDuration         = (long) (randomDurationField.getValue()) * 60000;
            long duration               = 0;
            /*
             * If the user selected five character groups, we will
             * complete the last group. Count only counts "real"
             * characters for 5 character groups.
             */
            int count                   = 0;
            for (;;) {
                MorseCode codeElement   = null;
                do {
                    int index           = (int) (Math.random() * selection.length);
                    codeElement         = selection[index];
                    /*
                     * Skip over dummy place-holders.
                     */
                    if (codeElement.getCode() == null) {
                        continue;
                    }
                    
                } while ((!useFiveCharGroups && !useRandomLengthGroups)  
                                    && codeElement.getLetter() == ' ');
                
                duration                += morseSender.synthesize(codeElement);
                char letter             = codeElement.getLetter();
                work.append(letter);
                ++count;
                if (!useFiveCharGroups && !useRandomLengthGroups) {
                    if (letter != ' ' && duration >= randomDuration) {
                        break;
                    }
                } else {
                    if ((count % charGroupLength) == 0) {
                        duration            += morseSender.synthesize(morseSpace);
                        if (duration >= randomDuration) {
                            break;
                        }
                        /*
                        * Appending the space after checking for duration
                        * prevents the program from checking for a trailing
                        * space, which the user won't type..
                        */
                        work.append(" ");
                        if (useRandomLengthGroups) {
                            charGroupLength = getRandomGroupLength();
                        }
                    }
                }
            }
            randomText                  = work.toString();
            if (false) {
                System.out.println("\"" + randomText
                        + "\" for " + duration + " msec."
                    );
            }
            synthesizer.startSynthesizer();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns a random length.
     */
    private int getRandomGroupLength() {
        return (int)((Math.random() * (MAX_GROUP_LENGTH - MIN_GROUP_LENGTH)) + 						MIN_GROUP_LENGTH + 1);
    }
    
    /**
     * Returns the group type of characters sent in the random mode.  Possible 
     * values are, MorsePractice.FIVE_CHAR_GROUP, MorsePractice.NO_GROUP or
     * MorsePractice.RANDOM_LEN_GROUP. JW.
     */

    private int getRandomGroupType() {
        if (fiveCharGroups.isSelected()) {
            return FIVE_CHAR_GROUP;
        }else if (randomCharGroups.isSelected()) {
            return RANDOM_LEN_GROUP;
        } else if (streamChars.isSelected()) {
            return NO_GROUP;
        } else {
            return -1; //it's a dud.
        }
    }
    
    /**
     * Sets the group type of characters sent in the random mode.  Possible 
     * values are, MorsePractice.FIVE_CHAR_GROUP, MorsePractice.NO_GROUP or
     * MorsePractice.RANDOM_LEN_GROUP. JW.
     */
    
    private void setRandomGroupType(int groupType) {
        if (groupType == FIVE_CHAR_GROUP) {
            fiveCharGroups.setSelected(true);
        } else if (groupType == RANDOM_LEN_GROUP) {
            randomCharGroups.setSelected(true);
        } else if (groupType == NO_GROUP) {
            streamChars.setSelected(true);
        }
    }
    
    
    /**
     * Build a random text sequence using the grammar root symbol
     * indicated by the menu option.
     */
    private void setRandomSymbols(String menuOption) {
        textArea.setText("");
        String root             = null;
        if (menuOption == pRandomDigits) {
            root                = "<RandomDigits>";
        }
        else if (menuOption == pRandom2Element) {
            root                = "<Random2Element>";
        }
        else if (menuOption == pRandom3Element) {
            root                = "<Random3Element>";
        }
        else if (menuOption == pRandom4Element) {
            root                = "<Random4Element>";
        }
        else if (menuOption == pRandomPunct) {
            root                = "<RandomPunct>";
        }
        else if (menuOption == pRandomCallSign) {
            root                = "<AnyCallSign>";
        }
        else {
            System.out.println("Illegal menu option \"" + menuOption + "\"");
            root                = "<RandomDigits>";
        }
        long randomDuration     = (long) (randomDurationField.getValue()) * 60000;
        long duration           = 0;
        StringBuffer work       = new StringBuffer();
        try {
            startSending(true);
            while (duration < randomDuration) {
                String word     = randomQSO.expand(root);
                if (duration > 0) {
                    work.append(" ");
                    duration    += morseSender.synthesize(" ");
                }
                work.append(word);
                duration        += morseSender.synthesize(word);
            }
            randomText = work.toString();
            // textArea.setText(randomText);
            synthesizer.startSynthesizer();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
            
        
    /**
     * Initialize the Morse code audio generator for a new message.
     * @param prefixPips        True to prefix the message with
     *                          five short beeps.
     */
    private void startSending(
            boolean             prefixPips
        )
    {
        synthesizer.stopSynthesizer();          /* Paranoia                     */
        synthesizer.resetSynthesizer();         /* Rewind the data              */
        morseSender.setTextRate(textRateField.getValue());
        morseSender.setFarnsworthRate(farnsworthRateField.getValue());
        morseSender.setUsingFarnsworth(usingFarnsworth.isSelected());
        /*
         * Begin by sending a brief silence to let the synthesizer
         * settle down. This helps prevent an initial click.
         */
        synthesizer.silence(initialSilence); /* Synthesizer settle  */
        if (prefixPips) {
            for (int i = 0; i < 5; i++) {
                synthesizer.tick();
            }
        }
    }
    /**
     * Read a text file into the textArea.
     */
    private void readTextFile()
    {
        String text     = MorsePracticeFile.readTextFile(
                                getFrame(this),
                                "Text to read"
                            );
        if (text != null) {
            textArea.setText(text);
            /* textArea.killUndo(); jw todo */
        }
    }
    /**
     * Save the current text area into a file.
     */
    private void saveTextFile()
    {
        MorsePracticeFile.saveTextFile(
                getFrame(this),
                "Text to write",
                textArea.getText()
            );
    }
    /**
     * Save the Morse Code audio into a file.
     */
    private void saveAudio()
    {
        DataOutputStream out    = MorsePracticeFile.createBinaryFile(
                                        getFrame(this),
                                        "Write Audio File"
                                    );
        if (out != null) {
            try {
                synthesizer.writeAudio(out);
                out.flush();
                out.close();
            }
            catch (IOException e) {}
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                }
                catch (IOException e) {}
            }
        }
    }
    
    /*
     * enableEditMenu implements the EditTextAreaListener interface.
     * It sets the Edit Menu options to conform to the text area's
     * current state.     
     * @param activeTextArea            The current text area. It is null
     *                                  if there is no currently-active text
     *                                  area.
     * @param hasSelection              True if some text is selected.
     * @param hasClipboardContent       True if there is text on the clipboard.
     */
/*
    private void enableEditMenu(boolean isEnabled) {
        System.out.println("Enabling the menus "+isEnabled);
        miCut.setEnabled(isEnabled);
        miCopy.setEnabled(isEnabled);
        miClear.setEnabled(isEnabled);
        miPaste.setEnabled(isEnabled);
//        EditTextArea.setUndoMenuItem(activeTextArea, miUndo); // jw todo
    }
*/
    /**
     * The user just changed the active text area. Enable the listen
     * buttion if there is something to listen to.
     * @param event             The current text event. This will
     *                          contain the current text area.
     */
// this is not called from here.....
/*    public void editTextValueChanged(DocumentEvent event) {
        listenButton.setEnabled(textArea.getText().length() != 0);
    }
*/
    /**
     * soundSynthesis implements the AudioSynthesizerListener interface.
     * @param active    True if the program is synthesizing sound.
     */
    public void soundSynthesis(
            boolean             active
        )
    {
        stopButton.setEnabled(active);
        if (active == false && wasRandomQSO) {
            /*
             * The program just stopped synthesizing a random QSO.
             * Load the QSO into the text area so the student can
             * check comprehension.
             */
            textArea.setText(randomText);
            wasRandomQSO                = false;
        }
    }
    
    /**
     * Manage the Synthesizer Frequency dialog.
     */
    private void setFrequencyDialog()
    {
        SetFrequencyDialog dialog       = new SetFrequencyDialog(
                                this,
                                synthesizer.getSynthesizerFrequency()
                            );
        if (dialog.isSuccess()) {
            double newValue     = dialog.getSynthesizerFrequency();
            synthesizer.setSynthesizerFrequency(newValue);
        }
    }
    
    /**
     * Read the parameter file. This could be changed to use an
     * XML parser. Note that this may not be called from an Applet
     * context.
     */
    private void readParameters() {
        Reader reader                   = null;
        LineNumberReader in             = null;
        /*        
        System.out.println("About to read from:"+getDefaultParamFile());
        */
        try {
            reader 			= new FileReader(getDefaultParamFile());
            in                          = new LineNumberReader(reader);
            String s;
            while ((s = in.readLine()) != null) {
                if (s.startsWith("#") == false) {
                    /*
                     * Note: don't use = as a token separator: it's in
                     * the Morse alphabet.
                     */
                    StringTokenizer st  = new StringTokenizer(s, ":");
                    try {
                        String name     = st.nextToken().trim();
                        String value    = "";
                        if (st.hasMoreTokens()) {
                            value       = st.nextToken().trim();
                        }
                        if (name.equalsIgnoreCase(pTextRate)) {
                            int rate    = Integer.parseInt(value);
                            textRateField.setValue(rate);
                        }
                        else if (name.equalsIgnoreCase(pFarnsworthRate)) {
                            int rate    = Integer.parseInt(value);
                            farnsworthRateField.setValue(rate);
                        }
                        else if (name.equalsIgnoreCase(pTrialDuration)) {
                            int rate    = Integer.parseInt(value);
                            randomDurationField.setValue(rate);
                        }
                        else if (name.equalsIgnoreCase(pUseFarnsworth)) {
                            boolean val = Boolean.valueOf(value).booleanValue();
                            usingFarnsworth.setSelected(val);
                        }
                        else if (name.equalsIgnoreCase(pGroupType)) {
                            int type 	= Integer.parseInt(value);	
                            setRandomGroupType(type);
                        }
                        else if (name.equalsIgnoreCase(pRandomChars)) {
                            char[] chars        = value.toCharArray();
                            for (int i = 0; i < chars.length; i++) {
                                if (chars[i] != '"') {
                                    setMorseButtonState(chars[i], true, false);
                                }
                            }
                        }
                        else if (name.equalsIgnoreCase(pRandomFlavor)) {
                            randomChoice.setSelectedItem(value);
                        }
                        else if (name.equalsIgnoreCase(pSynthFrequency)) {
                            double val = Double.valueOf(value).doubleValue();
                            synthesizer.setSynthesizerFrequency(val);
                        }
                        else {
                            System.err.print("[MorsePractice] Unknown param \"" + s + "\" ignored");
                            System.err.println(" at line " + in.getLineNumber());
                        }
                    }
                    catch (NumberFormatException e) {
                        System.err.print("Number format error, \"" + s + "\" ignored");
                        System.err.println(" at line " + in.getLineNumber());
                    }
                    catch (NoSuchElementException e) {
                        System.err.print("Param line error, \"" + s + "\" ignored");
                        System.err.println(" at line " + in.getLineNumber());
                    }
                    catch (Exception e) {
                        System.err.print("Strange Param line error, \"" + s + "\" ignored");
                        System.err.println(" at line " + in.getLineNumber());
                    }
                }
            }
        }
        catch (FileNotFoundException e) { /* Ignored: no param file */ }
        catch (IOException e) {
            System.err.println("Param file error: " + e.toString());
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (Exception e) { }
            try {
                if (reader != null) {
                    reader.close();
                }
            }
            catch (Exception e) {}
        }
    }
    /**
     * Write the parameter file. This could be changed to generate
     * and XML document. Note that this may not be called from
     * an Applet context
     */
    private void writeParameters() {
        File                    outFile         = null;
        FileOutputStream        outStream       = null;
        PrintWriter             out             = null;
        System.out.println("About to write to:"+getDefaultParamFile());
        try {
            outFile = new File(getDefaultParamFile());
            outStream           = new FileOutputStream(outFile);
            out                 = new PrintWriter(outStream);
            MacFileUtilities.setCreatorAndFileType(outFile, "R*ch", "TEXT");
            out.println("# " + (new Date()).toString());
            out.println(pTextRate       + ":" + textRateField.getValue());
            out.println(pFarnsworthRate + ":" + farnsworthRateField.getValue());
            out.println(pTrialDuration  + ":" + randomDurationField.getValue());
            out.println(pUseFarnsworth  + ":" + usingFarnsworth.isSelected());
            out.println(pGroupType 	+ ":" + getRandomGroupType());
            out.println(pRandomFlavor   + ":" + randomChoice.getSelectedItem());
            out.println(pSynthFrequency + ":" + synthesizer.getSynthesizerFrequency());
            out.print(pRandomChars + ":\"");
            for (int i = 0; i < MorseCode.morse.length; i++) {
                char letter             = MorseCode.morse[i].getLetter();
                if (letter != '\u0000' && getMorseButtonState(letter)) {
                    out.print(letter);
                }
            }
            out.println("\"");
            out.flush();
        }
        catch (IOException e) {
            System.err.println("Error writing param file: " + e.toString());
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                    if (out.checkError()) {
                        System.err.println("Error writing param file");
                    }
                }
            }
            catch (Exception e) {}
            try {
                if (out != null) {
                    out.close();
                    if (out.checkError()) {
                        System.err.println("Error writing param file");
                    }
                }
            }
            catch (Exception e) {}
            try {
                if (outStream != null) {
                    outStream.close();
                }
            }
            catch (Exception e) {
                System.err.println("Error closing param file (OutputStream): " + e);
            }
        }
        /* try { Thread.sleep(5000); } catch (InterruptedException e) {} */
    }
    
    /*
     * Returns a String representing the default parameter filename.  This was originally
     * hardcoded but was broken out to a method as the System.getProperty(user.home) was
     * throwing a security exception when run as an applet.
     */
     
    private String getDefaultParamFile() {
        if ( System.getProperty("mrj.version") != null) { /* create an OSX compliant prefs file */
            /*Apple Tech Q&A 1170 suggests the following location for the prefs file.*/
            return System.getProperty("user.home") + "/Library/Preferences/MorsePractice.prefs";
        } else if (System.getProperty("os.name").compareToIgnoreCase("linux")==0) {
            return System.getProperty("user.home") + "/.morsepractice.txt";
        } else {
            return "MorsePractice.txt";
        }
    }
    
    /*
     * Load the decorative image. Return null if unsuccessful.
     * The image is in progress.
     * @param imageFileName     The name of the image file
     * @return the image.
     */
    private Image getMorsePracticeImage(String imageFileName)
    {
        Image result            = null;
        String failure          = "Unknown";
        try {
            if (isApplication) {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Class<?> mainClass = Class.forName("org.minow.MorsePractice.MorsePractice");
                URL imageURL    = mainClass.getResource(imageFileName);
                result          = toolkit.getImage(imageURL);
            }
            else {
                result          = getImage(getCodeBase(), imageFileName);
            }
            if (result != null) {
                MediaTracker tracker = new MediaTracker(this);
                tracker.addImage(result, 0);
                tracker.waitForID(0);
                if (tracker.isErrorID(0)) {
                    result      = null;
                    int status  = tracker.statusID(0, false);
                    failure     = "Image load error: " + Integer.toString(status);
                }
            }
        }
        catch (InterruptedException e) {
            /* The user cancelled the applet during startup */
        }
        catch (Exception e) {
            failure             = e.toString();
            e.printStackTrace();
        }
        if (result == null) {
            JOptionPane.showMessageDialog(this,
                "Can't load image: " +imageFileName + "\n" + failure,
                "Morse Practice Failure",
                JOptionPane.ERROR_MESSAGE);
        }
        return (result);
    }
    
    /* Implement the Caret listener interface.  The edit menu 'cut' and 
     * 'copy' menuitems are enabled if text is selected.
     */
    public void caretUpdate(CaretEvent e) {
    //System.out.println("Got Caret Event "+e.getDot()+" and "+e.getMark());
        if (e.getDot() == e.getMark()) {
            //System.out.println("Got Caret Event");
            miCut.setEnabled(false);
            miCopy.setEnabled(false);
        } else {
            miCut.setEnabled(true);
            miCopy.setEnabled(true);
        } 
    }
    /* Implement the flavorListener interface to enable/disable 
     * the paste menu item.
     */

    /*
    public void flavorsChanged(FlavorEvent e) {
        miPaste.setEnabled(isClipboardValid((Clipboard)e.getSource()));
    }
    */
    /*
     * This function enables the 'paste' menu item if the data held
     * on the clipboard passed as the single argumnet is text that
     * may be displayed / transmitted as Morse.
     */
    /*
    private boolean isClipboardValid(Clipboard c) {
        // make this final and static
        DataFlavor df = new DataFlavor(Object.class,"text/plain; charset=unicode");
        boolean avail = c.isDataFlavorAvailable(df);
        System.out.println("Lookng at "+c.toString()+avail);
        return avail;
    }
    */
    /* Implement the DocumentListener Interface use to control the 
     * state of the 'Listen' button.
     */
    public void insertUpdate(DocumentEvent e) {
        listenButton.setEnabled(true);
    }

    public void removeUpdate(DocumentEvent e) {
        if (e.getDocument().getLength() > 0) {
            listenButton.setEnabled(true);
        } else {
            listenButton.setEnabled(false);
        }
    }

    public void changedUpdate(DocumentEvent e) {
        if (e.getDocument().getLength() > 0) {
            listenButton.setEnabled(true);
        } else {
            listenButton.setEnabled(false);
        }        
    }

    /*
     * Implement the ComponentListener interface. This is a hack to
     * convince the Java environment to choose the appropriate text
     * field. requestFocus is ignored when the Component is first created.
     * Since Components that become visible when their ancestors are made
     * visible (who thought up that piece of cleverness?), we catch the
     * resized event. Currently, there is only one text area, so this
     * is overkill, but that was not true previously, and may be changed
     * in the future.
     */
    
    /**
     * componentHidden is part of the ComponentListener interface.
     * @param event     The component event.
     * @see java.awt.event.ComponentEvent
     */
    public void componentHidden(ComponentEvent event)   { }
    /**
     * componentMoved is part of the ComponentListener interface.
     * @param event     The component event.
     * @see java.awt.event.ComponentEvent
     */
    public void componentMoved(ComponentEvent event)    { }
    /**
     * componentShown is part of the ComponentListener interface.
     * @param event     The component event.
     * @see java.awt.event.ComponentEvent
     */
    public void componentShown(ComponentEvent event)    { }
    /**
     * componentResized is part of the ComponentListener interface.
     * When called, it ensures that the textArea has the focus.
     * @param event     The component event.
     * @see java.awt.event.ComponentEvent
     */

    public void componentResized(
            ComponentEvent      event
        )
    {
        if (isJustCreated) {
            isJustCreated       = false;
            textArea.requestFocus();
        }
    }

    /**
     * Throw a ClassNotFoundException if one of the classes we need
     * is not supported by this Java implementation.
     */
    public boolean isSupported(
            String[]            classNames
        )
    {
        boolean result          = true;
test:   do {
            for (int i = 0; i < classNames.length; i++) {
                try {
                    /* System.out.println("Checking \"" + classNames[i] + "\""); */
                    if (Class.forName(classNames[i]) == null) {
                        result      = false;
                        break test;
                    }
                }
                catch (NoClassDefFoundError e) {
                    /* System.out.println("Failed \"" + classNames[i] + "\": " + e.toString());*/
                    result          = false;
                    break test;
                }
                catch (ClassNotFoundException e) {
                    /* System.out.println("Failed \"" + classNames[i] + "\": " + e.toString());*/
                    result          = false;
                    break test;
                }
            }
        } while (false);
        return (result);
    }

    /**
     * This method locates the Frame that encloses a
     * specified component.  Originally part of 
     * @param component The component that needs its frame.
     * @return the component's frame or null if there is none.
     */
     
          
    public static JFrame getFrame(Component component) {
        while (component != null && !(component instanceof JFrame)) {
            component = component.getParent();
        }
        return ((JFrame)component);
    }

    /**
     * main constructs and launches the MorsePractice panel.
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        try {
            /*
             * On the Mac, the menus are in the menu bar, so
             * the main window should be a bit smaller. The
             * idea is to fit (if possible) on a 640x480 display.
             */
            int frameWidth              = 600;
            int frameHeight             = 540;
            JApplet morsePractice        = new MorsePractice(true);
            org.minow.applets.AppletFramework frame =
                    new org.minow.applets.AppletFramework(
                            morsePractice,
                            "MorsePractice",
                            frameWidth,
                            frameHeight,
                            args,
                            false   /* No status bar */
                     );
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame("Morse Code Practice"), 
               "MorsePractice failed during execution:"+ "\n" + e.toString(),
               "Morse Practice Failure",
                JOptionPane.ERROR_MESSAGE);
/*
            new AlertDialog(
                    new JFrame("Morse Code Practice"),
                    "Morse Practice Failure",
                    "MorsePractice failed during execution:"
                    + "\n" + e.toString(),
                    true
                );
*/
        }
        /*System.exit(0);*/
    }
    
    /**
     * Serious debugging only
     */
    public static void log(String text) {
        System.out.println(text);
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {}
    }
    
    /*My favorite debug routine... (JW)*/
    private void deb(String str){
        System.out.println(str);
    }
}

