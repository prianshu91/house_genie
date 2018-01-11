package com.promelle.communication.sender;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.promelle.communication.config.SmtpCredentials;
import com.promelle.exception.AbstractException;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

/**
 * This class is responsible for sending email.
 * 
 * @author Hemant Kumar
 * @version 0.0.1
 */
public class EmailSender {
	private SmtpCredentials smtpCredentials;

	public EmailSender(SmtpCredentials smtpCredentials) {
		this.smtpCredentials = smtpCredentials;
	}

	public boolean send(String to, String from, String subject, String body,
			File... files) throws AbstractException {
		SendGrid.Email email = new SendGrid.Email();
		SendGrid client = new SendGrid(smtpCredentials.getUser(),
				smtpCredentials.getPass());
		email.addTo(to.split(","));
		email.setFrom(from);
		email.setSubject(subject);
		email.setHtml(body);
		email.addHeader("Charset", "UTF-8");
		try {
			if (files != null && files.length > 0) {
				for (File file : files) {
					if (file == null) {
						continue;
					}
					email.addAttachment(file.getName(), file);
				}
			}
			client.send(email);
		} catch (IOException | SendGridException e) {
			throw new AbstractException("attachment.error", e);
		}
		return true;
	}

	/**
	 * Send Email with BCC list
	 * 
	 * @param to
	 * @param from
	 * @param bcc
	 * @param subject
	 * @param body
	 * @param files
	 * @return true if mail send successfully
	 * @throws AbstractException
	 */
	public boolean send(String to, String from, String bcc,String subject, String body, File... files) throws AbstractException {
        SendGrid.Email email = new SendGrid.Email();
        SendGrid client = new SendGrid(smtpCredentials.getUser(), smtpCredentials.getPass());
        email.addTo(to.split(","));
        email.setFrom(from);
		if (StringUtils.isNotBlank(bcc))
			email.setBcc(bcc.split(","));
		email.setSubject(subject);
		email.setHtml(body);
		email.addHeader("Charset", "UTF-8");
		try {
			if (files != null && files.length > 0) {
				for (File file : files) {
					if (file == null) {
						continue;
					}
					email.addAttachment(file.getName(), file);
				}
			}
			client.send(email);
		} catch (IOException | SendGridException e) {
			throw new AbstractException("attachment.error", e);
		}
		return true;
	}
}
