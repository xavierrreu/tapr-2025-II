package br.com.tapr.infra.mail;

public interface MailSenderService {
    void send(String to, String subject, String body);
}
