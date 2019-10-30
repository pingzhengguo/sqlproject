package com.pzg.code.sqlproject.service;


import com.pzg.code.sqlproject.utils.RequestResult;

import java.util.Map;

public interface SqlQueryService {
    public RequestResult sqlQueryByStoreListConnection(Map<String, Object> params);

    public RequestResult getFieldsByTableName(String tableName);
}
