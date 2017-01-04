package com.umessage.letsgo.assistant.model.vo.response;

import com.github.pagehelper.Page;
import com.umessage.letsgo.assistant.model.po.WeChatOrderInfoEntity;

/**
 * Created by ZhaoYidong on 2016/12/27.
 */
public class OrderSellerOpenIdResponse {

    private Page<WeChatOrderInfoEntity> orderList;

    private Double totalReward;

    private Long total;

    private Integer page;

    public Page<WeChatOrderInfoEntity> getOrderList() {
        return orderList;
    }

    public void setOrderList(Page<WeChatOrderInfoEntity> orderList) {
        this.orderList = orderList;
    }

    public Double getTotalReward() {
        return totalReward;
    }

    public void setTotalReward(Double totalReward) {
        this.totalReward = totalReward;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
