package com.msw.java;

import java.util.ArrayList;

/**
 * DBType枚举类
 */
public enum DBTypeEnum {
    MYSQL(0, "mysql", "3306", true)
    ,ORACLE(1, "oracle", "1521", false);


    /**
     * 索引
     */
    private Integer code;
    /**
     * 名称
     */
    private String name;
    /**
     * 端口
     */
    private String port;
    /**
     * dbName是否可见
     */
    private Boolean dbNameVisible;


    /**
     *
     * @param code 索引
     * @param name 名称
     * @param port 端口
     * @param dbNameVisible dbName是否可见
     */
    DBTypeEnum(Integer code, String name,String port,Boolean dbNameVisible){
        this.code = code;
        this.name = name;
        this.port = port;
        this.dbNameVisible = dbNameVisible;
    }

    public Integer getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public String getPort() {
        return port;
    }
    public Boolean getDbNameVisible() {
        return dbNameVisible;
    }
    /**
     * 根据code查找
     * @param code 枚举code
     * @return 枚举对象
     */
    public static DBTypeEnum getEnumByCode(Integer code) {
        for (DBTypeEnum typeEnum : DBTypeEnum.values()) {
            if (typeEnum.getCode().equals(code)) {
                //如果需要直接返回name则更改返回类型为String,return typeEnum.name;
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("code is invalid");
    }

    /**
     * 根据name查找
     * @param name 枚举name
     * @return 枚举对象
     */
    public static DBTypeEnum getEnumByName(String name) {
        for (DBTypeEnum typeEnum : DBTypeEnum.values()) {
            if (typeEnum.getName().equals(name)) {
                //如果需要直接返回code则更改返回类型为String,return typeEnum.code;
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("name is invalid");
    }

    /**
     * 获取Code List
     * @return ArrayList<Integer>
     */
    public static ArrayList<Integer> getCodeList() {
        ArrayList<Integer> codeList = new ArrayList<>();

        for (DBTypeEnum typeEnum : DBTypeEnum.values()) {
            codeList.add(typeEnum.getCode());
        }
        return codeList;
    }

    /**
     * 获取Name List
     * @return ArrayList<String>
     */
    public static ArrayList<String> getNameList() {
        ArrayList<String> nameList = new ArrayList<>();

        for (DBTypeEnum typeEnum : DBTypeEnum.values()) {
            nameList.add(typeEnum.getName());
        }
        return nameList;
    }

}
