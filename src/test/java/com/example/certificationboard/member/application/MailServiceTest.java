package com.example.certificationboard.member.application;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Disabled
class MailServiceTest {

    @Autowired
    MailService mailService;

    @Test
    @DisplayName("메일 발송되는지 테스트")
    public void send_mail_test() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("csy7792@naver.com");
            message.setSubject("test");
            message.setText("test");

            mailService.send(message);
        } catch (MailException e) {
            fail();
        }
    }
}