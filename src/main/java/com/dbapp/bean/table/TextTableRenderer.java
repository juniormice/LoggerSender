package com.dbapp.bean.table;

import org.apache.commons.lang3.StringUtils;

import javax.swing.table.TableModel;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

/**
 * j-text-util
 * 表格打印执行类（使用tab作对齐）
 *
 * @author xutao
 * @date 2020/03/04
 */
public class TextTableRenderer implements TableRenderer {

    private static final Charset GBK = Charset.forName("GBK");

    private static final Integer TAB_SPACE = 8;
    private static final Integer NEW_TAB_SPACE_LENGTH = 6;
    TextTable textTable;
    private int[] tabs;
    private TableModel tableModel;

    private boolean showNulls = false;

    TextTableRenderer(TextTable textTable) {
        this.textTable = textTable;
        this.tableModel = textTable.getTableModel();
    }

    @Override
    public void render(OutputStream os, int indent) {
        PrintStream ps;
        if (os instanceof PrintStream) {
            ps = (PrintStream) os;
        } else {
            ps = new PrintStream(os);
        }
        TableModel tableModel = textTable.getTableModel();
        String indentStr = StringUtils.repeat(" ", indent);

        // Find the maximum length of a string in each column
        resolveColumnLengths();

        // Generate a format string for each column and calc totalLength
        int totLength = resolveFormats();
        String separator = resolveSeparator(totLength);

        String headerStartSep = StringUtils.repeat("_", totLength + 1);
        ps.print(indentStr);
        ps.println(headerStartSep);

        ps.print(indentStr);
        for (int j = 0; j < tableModel.getColumnCount(); j++) {
            String value = StringUtils.isEmpty(tableModel.getColumnName(j)) ? "" : tableModel.getColumnName(j);
            printLine(ps, j, String.valueOf(value));
        }
        ps.print("|");
        ps.print("\n");

        String headerSep = StringUtils.repeat("=", totLength);
        ps.print(indentStr);
        ps.print("|");
        ps.print(headerSep);
        ps.println();

        // Print 'em out
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (i != 0) {
                ps.println(separator);
            }
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                printValue(ps, i, j, false);
            }
            ps.print("|");
            ps.print("\n");
        }
    }

    private void resolveColumnLengths() {
        tabs = new int[tableModel.getColumnCount()];
        for (int col = 0; col < tableModel.getColumnCount(); col++) {
            int length = tableModel.getColumnName(col).getBytes(GBK).length;
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                Object val = tableModel.getValueAt(row, col);
                String valStr = String.valueOf(val);
                if (!showNulls && val == null) {
                    valStr = "";
                }
                length = Math.max(valStr.getBytes(GBK).length, length);
            }
            tabs[col] = (int) Math.ceil((float) length / TAB_SPACE);
            int mod = Math.floorMod(length, TAB_SPACE);
            if (mod == 0 || mod > NEW_TAB_SPACE_LENGTH) {
                tabs[col] += 1;
            }
        }
    }

    private String resolveSeparator(int totLength) {
        return "|" + StringUtils.repeat("-", totLength - 1) + "|";
    }

    protected void printValue(PrintStream ps, int row, int col, boolean empty) {
        int rowIndex = row;
        if (textTable.rowSorter != null) {
            rowIndex = textTable.rowSorter.convertRowIndexToModel(row);
        }
        Object val = tableModel.getValueAt(rowIndex, col);
        if (val == null && !showNulls) {
            val = "";
        }
        Object value = (empty ? "" : val);
        printLine(ps, col, String.valueOf(value));
    }

    private void printLine(PrintStream ps, int col, String value) {
        int tabNum = tabs[col];
        int myLength = String.valueOf(value).getBytes(GBK).length;
        ps.print("|");
        ps.print(value);
        // 本字节已有的tab数
        int alreadyTab = Math.floorDiv(myLength, TAB_SPACE);
        ps.print("\t");
        // 先给一个tab
        int mod = Math.floorMod(myLength, TAB_SPACE);
        if (mod > NEW_TAB_SPACE_LENGTH) {
            alreadyTab += 1;
        }
        int lastTab = tabNum - alreadyTab - 1;
        for (int i = 0; i < lastTab; i++) {
            ps.print("\t");
        }
    }

    private int resolveFormats() {
        int totLength = 0;
        for (int length : tabs) {
            totLength += length;
        }
        return totLength * TAB_SPACE;
    }
}
