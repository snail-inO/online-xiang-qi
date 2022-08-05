package me.portfolio.log.entity;

import java.util.Date;

public class Log {
    public static final String OPERATION_LOG = "Operation Log";
    public static final String EXCEPTION_LOG = "Exception Log";

    private long logId;
    private String description;
    private String logType;
    private String method;
    private String params;
    private String requestIp;
    private long time;
    private String username;
    private String address;
    private String browser;
    private String exceptionDetail;
    private Date createTime;

    public Log(long logId, String description, String logType, String method, String params, String requestIp, long time, String username, String address, String browser, String exceptionDetail, Date createTime) {
        this.logId = logId;
        this.description = description;
        this.logType = logType;
        this.method = method;
        this.params = params;
        this.requestIp = requestIp;
        this.time = time;
        this.username = username;
        this.address = address;
        this.browser = browser;
        this.exceptionDetail = exceptionDetail;
        this.createTime = createTime;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getExceptionDetail() {
        return exceptionDetail;
    }

    public void setExceptionDetail(String exceptionDetail) {
        this.exceptionDetail = exceptionDetail;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Log{" +
                "logId=" + logId +
                ", description='" + description + '\'' +
                ", logType='" + logType + '\'' +
                ", method='" + method + '\'' +
                ", params='" + params + '\'' +
                ", requestIp='" + requestIp + '\'' +
                ", time=" + time +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", browser='" + browser + '\'' +
                ", exceptionDetail='" + exceptionDetail + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
