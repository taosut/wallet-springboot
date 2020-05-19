package org.blockchain.wallet.entity;

import java.io.Serializable;
import java.util.Date;

public class MonitorPrice implements Serializable {
    private Integer id;

    private String code;

    private Integer userId;

    private Double upPrice;

    private Double downPrice;

    private Double upChangePercent;

    private Double downChangePercent;

    private String notification;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getUpPrice() {
        return upPrice;
    }

    public void setUpPrice(Double upPrice) {
        this.upPrice = upPrice;
    }

    public Double getDownPrice() {
        return downPrice;
    }

    public void setDownPrice(Double downPrice) {
        this.downPrice = downPrice;
    }

    public Double getUpChangePercent() {
        return upChangePercent;
    }

    public void setUpChangePercent(Double upChangePercent) {
        this.upChangePercent = upChangePercent;
    }

    public Double getDownChangePercent() {
        return downChangePercent;
    }

    public void setDownChangePercent(Double downChangePercent) {
        this.downChangePercent = downChangePercent;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification == null ? null : notification.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}