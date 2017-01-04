package com.umessage.letsgo.assistant.model.vo.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by gaofei on 2016/12/28.
 */
@XStreamAlias("xml")
public class WxPayOrderNotifyResponse {


    @XStreamAlias("returnCode")
    private String  return_code;

    @XStreamAlias("returnMsg")
    private String return_msg;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }
}
