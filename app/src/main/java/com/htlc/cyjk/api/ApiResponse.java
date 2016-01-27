
package com.htlc.cyjk.api;

import java.util.ArrayList;

public class ApiResponse<T> {
    private String event;    // 返回码，0为成功
    private String msg;      // 返回信息
    private T obj;           // 单个对象
    private ArrayList<T> objList;       // 数组对象
    private int currentPage; // 当前页数
    private int pageSize;    // 每页显示数量
    private int maxCount;    // 总条数
    private int maxPage;     // 总页数

    public ApiResponse(String event, String msg) {
        this.event = event;
        this.msg = msg;
    }
}
