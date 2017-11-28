package com.yykj.oachat.common;

/**
 * Created by Lee on 2017/11/27.
 * @author Lee
 */
public class Const {

    public interface Status{
        int SUCCESS = 1;
        int FAILED = 0;
    }

    public interface StatusDetail{
        String SUCCESS_DETAIL = "操作成功！";
        String FAILED_DETAIL = "操作失败！";
    }

    public interface Sign{
        int REQUEST = 1;
        int RESPONSE = 2;
        int NOTICE = 3;
    }
}
