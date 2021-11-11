package com.dbapp.service.handler.completer;

import com.dbapp.bean.FileModel;
import org.apache.commons.collections.CollectionUtils;
import org.jline.reader.Candidate;
import org.jline.reader.impl.completer.StringsCompleter;

import java.util.List;

/**
 * 目录类的命令补全，使用候补值为当前目录下的文件名
 *
 * @author xutao
 */
public class CatalogCompleter extends StringsCompleter {

    /**
     * 用于构建私有的目录结构的提示器
     */
    public CatalogCompleter() {
    }

    public void refresh(List<FileModel> fileModelList) {
        // 先清除旧有的候补值
        this.candidates.clear();

        //添加应当的候补值
        if (CollectionUtils.isNotEmpty(fileModelList)) {
            for (FileModel fileModel : fileModelList) {
                candidates.add(new Candidate(fileModel.getFileName()));
            }
        }
    }

}