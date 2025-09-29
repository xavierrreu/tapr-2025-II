package br.com.tapr.infra.mail;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!prod")
public class LogMailSenderService implements MailSenderService {
    @Override
    public void send(String to, String subject, String body) {
        System.out.printf("LOG Email -> To: %s | Subject: %s | Body: %s%n", to, subject, body);
    }
}
