/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.rest.api;

import java.time.LocalDateTime;

/**
 *
 * @author Козыро Дмитрий
 */
public final class ReqInfo {

    private String reqType;
    private String reqName;
    private String reqAddress;
    private int reqDuration;
    private String reqIp;
    private String reqUserId;
    private int reqHashCode;
    private String reqJson;
    private String reqAnswer;
    private String reqException;
    private Boolean isConfirmed;
    private LocalDateTime reqDateTime;
    private Class reqClass;

    public ReqInfo() {

    }

    public String getReqType() {
        return reqType;
    }

    public ReqInfo setReqType(final String reqType) {
        this.reqType = reqType;
        return this;
    }

    public String getReqName() {
        return reqName;
    }

    public ReqInfo setReqName(final String reqName) {
        this.reqName = reqName;
        return this;
    }

    public int getReqDuration() {
        return reqDuration;
    }

    public ReqInfo setReqDuration(final int reqDuration) {
        this.reqDuration = reqDuration;
        return this;
    }

    public String getReqIp() {
        return reqIp;
    }

    public ReqInfo setReqIp(final String reqIp) {
        this.reqIp = reqIp;
        return this;
    }

    public int getReqHashCode() {
        return reqHashCode;
    }

    public ReqInfo setReqHashCode(final int reqHashCode) {
        this.reqHashCode = reqHashCode;
        return this;
    }

    public String getReqUserId() {
        return reqUserId;
    }

    public ReqInfo setReqUserId(final String reqUserId) {
        this.reqUserId = reqUserId;
        return this;
    }

    public String getReqJson() {
        return reqJson;
    }

    public ReqInfo setReqJson(final String reqJson) {
        this.reqJson = reqJson;
        return this;
    }

    public String getReqException() {
        return reqException;
    }

    public ReqInfo setReqException(final String reqException) {
        this.reqException = reqException;
        return this;
    }

    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    public ReqInfo setIsConfirmed(final Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
        return this;
    }

    public String getReqAddress() {
        return reqAddress;
    }

    public ReqInfo setReqAddress(final String reqAddress) {
        this.reqAddress = reqAddress;
        return this;
    }

    public LocalDateTime getReqDateTime() {
        return reqDateTime;
    }

    public ReqInfo setReqDate(final LocalDateTime reqDate) {
        this.reqDateTime = reqDate;
        return this;
    }

    public Class getReqClass() {
        return reqClass;
    }

    public ReqInfo setReqClass(final Class reqClass) {
        this.reqClass = reqClass;
        return this;
    }

    public String getReqAnswer() {
        return reqAnswer;
    }

    public ReqInfo setReqAnswer(final String reqAnswer) {
        this.reqAnswer = reqAnswer;
        return this;
    }
}
