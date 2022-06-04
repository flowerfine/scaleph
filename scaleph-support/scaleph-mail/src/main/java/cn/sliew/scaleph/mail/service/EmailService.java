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

package cn.sliew.scaleph.mail.service;

import cn.sliew.scaleph.mail.service.vo.EmailConfigVO;

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
