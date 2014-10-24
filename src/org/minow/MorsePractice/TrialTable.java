package org.minow.MorsePractice;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

class TrialTable extends JTable {

    public TrialTable(TableModel dm) {
        super(dm);
        setDefaultRenderer(String.class, new DefaultTrialTableCellRenderer());
        getColumnModel().getColumn(0).setCellRenderer(new GotCharTableCellRenderer());
    }
} /* end of class TrialTable */


class DefaultTrialTableCellRenderer extends DefaultTableCellRenderer{
    public DefaultTrialTableCellRenderer() {
        super();
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    public Component getTableCellRendererComponent (JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, obj, isSelected, hasFocus, row, column);
        //cell.setHorizontalAlignment(SwingConstants.CENTER);
        TableModel tm = table.getModel();
	String colName = table.getColumnName(column);
	String rowName = (String)tm.getValueAt(row, 0);
	String value = (String)tm.getValueAt(row, column);
        if ( rowName.equals(colName) && !value.equals("") ) {
            cell.setBackground(Color.green);
        } else {
            cell.setBackground(Color.white);
        }
        return cell;
    }
}


class GotCharTableCellRenderer extends DefaultTableCellRenderer {
    public GotCharTableCellRenderer() {
        super();
	setBackground(Color.lightGray);
        setHorizontalAlignment(SwingConstants.CENTER);
    }
}



