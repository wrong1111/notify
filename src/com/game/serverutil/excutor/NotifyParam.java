package com.game.serverutil.excutor;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component("notifyParam")
public class NotifyParam {

    private Map<Integer, Integer> notifyParams;// 通知时间次数map
    private String successValue;// 通知后用于判断是否成功的返回值。由HttpResponse获取

    public Map<Integer, Integer> getNotifyParams() {
        return notifyParams;
    }

    public void setNotifyParams(Map<Integer, Integer> notifyParams) {
        this.notifyParams = notifyParams;
    }

    public String getSuccessValue() {
        return successValue;
    }

    public void setSuccessValue(String successValue) {
        this.successValue = successValue;
    }

    public Integer getMaxNotifyTime() {
        return notifyParams == null ? 0 : notifyParams.size();
    }

}
