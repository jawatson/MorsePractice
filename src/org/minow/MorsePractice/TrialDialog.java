package org.minow.MorsePractice;
    public static final long serialVersionUID = 13040901L;
	JTable trialTable = new TrialTable(new TrialTableModel(trialData));
        JScrollPane trialScrollPane = new JScrollPane(trialTable);
                                           JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                                           JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        c.fill                  = GridBagConstraints.NONE;