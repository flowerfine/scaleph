package cn.sliew.scaleph.service.admin.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.service.admin.EmailService;
import cn.sliew.scaleph.service.vo.EmailConfigVO;
import cn.sliew.scaleph.system.service.SystemConfigService;
import cn.sliew.scaleph.system.service.dto.SystemConfigDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


/**
 * @author gleiyu
 */
@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SystemConfigService systemConfigService;

    public void setFrom(String from) {
        this.from = from;
    }

    @PostConstruct
    void initEmailInfo() {
        SystemConfigDTO config = this.systemConfigService.selectByCode(Constants.CFG_EMAIL_CODE);
        if (config != null && !StrUtil.isEmpty(config.getCfgValue())) {
            EmailConfigVO emailInfo = JSONUtil.toBean(config.getCfgValue(), EmailConfigVO.class);
            this.configEmail(emailInfo);
        }
    }

    /**
     * 异步发送邮件
     *
     * @param sendTo  收件人
     * @param subject 邮件主题
     * @param context 邮件内容
     */
    @Async
    @Override
    public void sendSimpleEmail(String[] sendTo, String subject, String context) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(subject);
            message.setText(context);
            message.setTo(sendTo);
            message.setFrom(this.from);
            javaMailSender.send(message);
        } catch (MailException e) {
            log.error("email send error");
        }
    }

    /**
     * 异步发送HTML邮件
     *
     * @param sendTo  收件人
     * @param subject 邮件主题
     * @param html    HTML邮件内容
     */
    @Async
    @Override
    public void sendHtmlEmail(String[] sendTo, String subject, String html) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(sendTo);
            mimeMessageHelper.setFrom(this.from);
            mimeMessageHelper.setText(html, true);
            javaMailSender.send(message);
        } catch (MessagingException | MailException e) {
            log.error("email send error");
        }
    }

    @Override
    public void configEmail(EmailConfigVO emailConfig) {
        if (emailConfig != null) {
            this.setFrom(emailConfig.getEmail());
            JavaMailSenderImpl jms = new JavaMailSenderImpl();
            jms.setUsername(emailConfig.getEmail());
            jms.setPassword(Base64.decodeStr(emailConfig.getPassword()));
            jms.setHost(emailConfig.getHost());
            jms.setPort(emailConfig.getPort());
            jms.setDefaultEncoding(emailConfig.getEncoding());
            Properties props = new Properties();
            props.setProperty("mail.smtp.auth", "true");
            jms.setJavaMailProperties(props);
            this.javaMailSender = jms;
        }
    }

}
