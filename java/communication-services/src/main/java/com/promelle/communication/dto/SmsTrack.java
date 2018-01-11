package com.promelle.communication.dto;

import com.promelle.dto.AbstractDTO;

/**
 * This class is intended for holding upload track information.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class SmsTrack extends AbstractDTO {
	private static final long serialVersionUID = -9025776898915179075L;
	private String to;
    private String from;
    private String message;
    private String errorMessage;
    private Integer errorCode;
    private Long dateCreated;
    private Long dateSent;
    private String messageStatus;
    private Integer status;

    public SmsTrack() {
        // no argument constructor
    }

    public SmsTrack(String sId, String message, Integer status) {
        super();
        this.message = message;
        this.status = status;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getDateSent() {
        return dateSent;
    }

    public void setDateSent(Long dateSent) {
        this.dateSent = dateSent;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }
}