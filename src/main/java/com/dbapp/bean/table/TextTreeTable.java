package com.dbapp.bean.table;

import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;
import java.util.Objects;

/**
 * j-text-util
 *
 * @author j-text-util
 */
public class TextTreeTable extends TextTable {

    private static final Integer MAX_NAME_LENGTH = 100;
    private static final Integer TAIL_LENGTH = 3;
    private static final String TRUNCATION_BODY = "..";

    int hierarchicalColumn;

    public TextTreeTable(String[] columnNames, String[][] data) {
        super(columnNames, truncateDataLength(data));
        addSeparatorPolicy(new TreeTableSeparatorPolicy());
    }

    private static String[][] truncateDataLength(String[][] data) {
        if (Objects.isNull(data) || (data.length == 0)) {
            return data;
        }
        int x = data.length;
        for (int i = 0; i < x; i++) {
            int y = data[i].length;
            for (int j = 0; j < y; j++) {
                String str = data[i][j];
                if (StringUtils.isNotEmpty(str) && (str.length() > MAX_NAME_LENGTH)) {
                    // 名称过长的时候，进行截断
                    String header = str.substring(0, MAX_NAME_LENGTH - TAIL_LENGTH - 2);
                    String tail = str.substring(str.length() - TAIL_LENGTH);
                    data[i][j] = header + TRUNCATION_BODY + tail;
                }
            }
        }
        return data;
    }

    @Override
    public void printTable(PrintStream ps, int indent) {
        TextTableRenderer renderer = new TextTreeTableRenderer(this);
        renderer.render(ps, indent);
    }

    private class TreeTableSeparatorPolicy extends AbstractSeparatorPolicy {

        @Override
        boolean hasSeparatorAt(int row) {
            if (row == 0) {
                return false;
            }
            Object rowAgo = getValueAt(row - 1, hierarchicalColumn);
            Object hierarchicalColumnVal = getValueAt(row, hierarchicalColumn);
            return !hierarchicalColumnVal.equals(rowAgo);
        }

    }
}
