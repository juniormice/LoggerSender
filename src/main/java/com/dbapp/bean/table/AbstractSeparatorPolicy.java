package com.dbapp.bean.table;

/**
 * j-text-util
 *
 * @author j-text-util
 */
abstract class AbstractSeparatorPolicy {

    AbstractSeparatorPolicy() {
    }

    /**
     * ss
     *
     * @param row 列
     * @return true/false
     */
    abstract boolean hasSeparatorAt(int row);
}
