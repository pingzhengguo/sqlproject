package com.pzg.code.sqlproject.vo;

public enum OracleInstanceType {

    SID,
    SERVICE_NAME;

    public static void main(String[] args) {
        System.out.println(OracleInstanceType.valueOf("SID"));
        System.out.println(OracleInstanceType.valueOf("SERVICE_NAME"));
    }
}
