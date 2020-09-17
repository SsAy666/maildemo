package com.lzs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author linzishan@rtmap.com
 * @Desc 发送邮件的测试类
 * @date 2020/9/17 15:58
 */
@RestController
@RequestMapping("/mail")
public class MailController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送简单文本邮件
     */
    @RequestMapping("/send")
    public void sendMail(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lzs_17801185130@163.com");
        message.setTo("296148717@qq.com");
        message.setSubject("it is a test for spring boot");
        message.setText("你好，我是lzs，我正在测试发送邮件。");
        try {
            mailSender.send(message);
            logger.info("lzs的测试邮件已发送。");
        } catch (Exception e) {
            logger.error("lzs发送邮件时发生异常了！", e);
        }
    }

    /**
     * 发送有附件的邮件
     * @throws MessagingException
     */
    @RequestMapping("/att")
    public void sendMailWithAtt() throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("lzs_17801185130@163.com");
        helper.setTo("296148717@qq.com");
        helper.setSubject("主题：发送有附件的邮件");
        helper.setText("你好，我是lzs，我正在测试发送一封有附件的邮件。");
        FileSystemResource file1 = new FileSystemResource(new File("d:\\blue.jpg"));
        FileSystemResource file2 = new FileSystemResource(new File("d:\\runman.jpg"));
        helper.addAttachment("附件-1.jpg", file1);
        helper.addAttachment("附件-2.jpg", file2);
        try {
            mailSender.send(mimeMessage);
            logger.info("lzs的测试带附件的邮件已发送。");
        } catch (Exception e) {
            logger.error("lzs发送带附件邮件时发生异常了！", e);
        }
    }

    /**
     * 发送嵌入静态资源格式的邮件
     * @throws MessagingException
     */
    @RequestMapping("/static")
    public void sendMailWithStatic() throws MessagingException{
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("lzs_17801185130@163.com");
        helper.setTo("296148717@qq.com");
        helper.setSubject("主题：嵌入静态资源");
        helper.setText("<html><body><img src=\"cid:hello\" ></body></html>", true);
        // 注意addInline()中资源名称 hello 必须与 text正文中cid:hello对应起来
        FileSystemResource file1 = new FileSystemResource(new File("d:\\blue.jpg"));
        helper.addInline("hello", file1);
        try {
            mailSender.send(mimeMessage);
            logger.info("lzs的测试嵌入静态资源的邮件已发送。");
        } catch (Exception e) {
            logger.error("lzs发送嵌入静态资源的邮件时发生异常了！", e);
        }
    }
}
