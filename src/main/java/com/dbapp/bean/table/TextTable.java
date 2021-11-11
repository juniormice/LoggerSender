package com.dbapp.bean.table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 来自j-text-util
 *
 * @author Daniel Orr
 */
public class TextTable {

    RowSorter<?> rowSorter;
    private TableModel tableModel;
    private List<AbstractSeparatorPolicy> separatorPolicies = new ArrayList<>();

    TextTable(String[] columnNames, Object[][] data) {
        this.tableModel = new DefaultTableModel(data, columnNames);
    }

    TableModel getTableModel() {
        return tableModel;
    }

    void addSeparatorPolicy(AbstractSeparatorPolicy separatorPolicy) {
        separatorPolicies.add(separatorPolicy);
    }

    public void printTable() {
        printTable(System.out, 0);
    }


    void printTable(PrintStream ps, int indent) {
        TextTableRenderer renderer = new TextTableRenderer(this);
        renderer.render(ps, indent);
    }

    Object getValueAt(int row, int column) {
        int rowIndex = row;
        if (rowSorter != null) {
            rowIndex = rowSorter.convertRowIndexToModel(row);
        }
        return tableModel.getValueAt(rowIndex, column);
    }

    boolean hasSeparatorAt(int row) {
        for (AbstractSeparatorPolicy separatorPolicy : separatorPolicies) {
            if (separatorPolicy.hasSeparatorAt(row)) {
                return true;
            }
        }
        return false;
    }

}
