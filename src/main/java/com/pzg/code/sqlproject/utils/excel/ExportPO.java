package com.pzg.code.sqlproject.utils.excel;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 描述：多线程导出实体
 */
@Data
@AllArgsConstructor
public class ExportPO<T> {

    private int index;

    private List<T> dataList;

}

