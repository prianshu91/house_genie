package com.promelle.communication.sender;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.promelle.communication.config.SmsCredentials;
import com.promelle.communication.dto.SmsTrack;
import com.promelle.request.tracker.AbstractRequestTracker;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.ApplicationFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Message;

public class SmsSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsSender.class.getName());
    private SmsCredentials smsCredentials;
    private static String CALLBACK_URL = "https://%s.%s/service/sms/notification";
    private TwilioRestClient client;

    public SmsSender(SmsCredentials smsCredentials) {
        this.smsCredentials = smsCredentials;
        this.client = new TwilioRestClient(smsCredentials.getAccuntSid(), smsCredentials.getAuthToken());
    }

    public SmsTrack send(AbstractRequestTracker requestTracker, String to, String from, String body) {
        CALLBACK_URL = String.format(CALLBACK_URL, requestTracker.getPortalName(), requestTracker.getDomain());
        SmsTrack smsTrack = null;
        String sentFrom = from != null && from != "" ? from : smsCredentials.getDefaultFrom();
        String sentTo = to != null && !to.startsWith("+") ? "+" + to : to;
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Body", body));
        params.add(new BasicNameValuePair("To", sentTo));
        params.add(new BasicNameValuePair("From", sentFrom));
        params.add(new BasicNameValuePair("StatusCallback", CALLBACK_URL));
        Account account = client.getAccount();
        ApplicationFactory appFactory = account.getApplicationFactory();
        try {
            appFactory.create(params);
            Message message = account.getMessageFactory().create(params);
            if (message != null) {
                smsTrack = new SmsTrack();
                smsTrack.setId(message.getSid());
                smsTrack.setTo(sentTo);
                smsTrack.setFrom(sentFrom);
                smsTrack.setMessage(body);
                smsTrack.setMessageStatus(message.getStatus());
                smsTrack.setDateCreated(message.getDateCreated().getTime());
                smsTrack.setErrorMessage(message.getErrorMessage());
                smsTrack.setErrorCode(message.getErrorCode());
                smsTrack.setStatus(message.getErrorMessage() != null && message.getErrorMessage() != "" ? 0 : 1);
                smsTrack.setMessageStatus(message.getStatus());

            }
        } catch (TwilioRestException e) {
            LOGGER.error("Error in sending message", e.getMessage());
        }

        return smsTrack;
    }

}