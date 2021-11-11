package com.dbapp.bean.table;

import java.io.PrintStream;

/**
 * j-text-util
 *
 * @author j-text-util
 */
public class TextTreeTableRenderer extends TextTableRenderer {

    TextTreeTableRenderer(TextTreeTable textTable) {
        super(textTable);
    }

    private TextTreeTable getTextTreeTable() {
        return (TextTreeTable) textTable;
    }

    @Override
    protected void printValue(PrintStream ps, int row, int col, boolean c) {
        boolean empty = false;
        if (row != 0 && col == getTextTreeTable().hierarchicalColumn && !textTable.hasSeparatorAt(row)) {
            empty = true;
        }
        super.printValue(ps, row, col, empty);
    }
}
