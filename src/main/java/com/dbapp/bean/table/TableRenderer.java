package com.dbapp.bean.table;

import java.io.OutputStream;

/**
 * 来自j-text-util
 *
 * @author j-text-util
 */
public interface TableRenderer {
    /**
     * ss
     *
     * @param ps     输出流
     * @param indent 位置
     */
    void render(OutputStream ps, int indent);
}
