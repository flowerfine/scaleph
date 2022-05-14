package cn.sliew.breeze.service.admin;

import cn.sliew.breeze.service.vo.EmailConfigVO;

/**
 * @author gleiyu
 */
public interface EmailService {
    /**
     * 发送简单邮件
     *
     * @param sendTo  收件人
     * @param subject 主题
     * @param context 正文
     */
    void sendSimpleEmail(String[] sendTo, String subject, String context);

    /**
     * 发送html邮件
     *
     * @param sendTo  收件人
     * @param subject 主题
     * @param html    正文
     */
    void sendHtmlEmail(String[] sendTo, String subject, String html);

    /**
     * 配置email信息
     *
     * @param emailConfig 配置信息
     */
    void configEmail(EmailConfigVO emailConfig);
}
