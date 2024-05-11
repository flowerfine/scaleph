/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.mail.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.sliew.scaleph.common.codec.CodecUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.mail.service.EmailService;
import cn.sliew.scaleph.mail.service.vo.EmailConfigVO;
import cn.sliew.scaleph.system.service.SysConfigService;
import cn.sliew.scaleph.system.service.dto.SysConfigDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Properties;


/**
 * @author gleiyu
 */
@Slf4j
@Service
public class EmailServiceImpl implements EmailService, InitializingBean {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    @Autowired
    private SysConfigService sysConfigService;

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SysConfigDTO config = this.sysConfigService.selectByCode(Constants.CFG_EMAIL_CODE);
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
            String encodedPasswd = emailConfig.getPassword().substring(Constants.CODEC_STR_PREFIX.length());
            jms.setPassword(CodecUtil.decodeFromBase64(encodedPasswd));
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
