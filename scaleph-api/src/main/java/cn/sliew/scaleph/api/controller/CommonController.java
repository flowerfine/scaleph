package cn.sliew.scaleph.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.api.annotation.AnonymousAccess;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.api.util.I18nUtil;
import cn.sliew.scaleph.api.util.SecurityUtil;
import cn.sliew.scaleph.api.vo.ResponseVO;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.exception.Rethrower;
import cn.sliew.scaleph.service.util.RedisUtil;
import cn.sliew.scaleph.storage.service.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author gleiyu
 */
@Controller
@RequestMapping("/api")
@Api(tags = "公共模块")
public class CommonController {
    @Autowired
    private RedisUtil redisUtil;

    @Resource(name = "${app.resource.type}")
    private StorageService storageService;

    /**
     * 生成验证码
     *
     * @param req  request
     * @param resp response
     */
    @AnonymousAccess
    @ApiOperation(value = "查询验证码", notes = "查询验证码信息")
    @GetMapping(path = {"/authCode"})
    public ResponseEntity<Object> authCode(HttpServletRequest req, HttpServletResponse resp) {
        LineCaptcha lineCaptcha =
            CaptchaUtil.createLineCaptcha(150, 32, 5, RandomUtil.randomInt(6, 10));
        Font font = new Font("Stencil", Font.BOLD + Font.ITALIC, 20);
        lineCaptcha.setFont(font);
        lineCaptcha.setBackground(new Color(246, 250, 254));
        lineCaptcha.createCode();
        String uuid = Constants.AUTH_CODE_KEY + UUID.randomUUID().toString();
        redisUtil.set(uuid, lineCaptcha.getCode(), 10 * 60);
        Map<String, Object> map = new HashMap<>(2);
        map.put("uuid", uuid);
        map.put("img", lineCaptcha.getImageBase64Data());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/file/upload")
    @ApiOperation(value = "上传文件", notes = "上传文件到公共目录")
    public ResponseEntity<ResponseVO> upload(@RequestParam("file") MultipartFile file)
        throws IOException {
        String userName = SecurityUtil.getCurrentUserName();
        if (StrUtil.isNotEmpty(userName)) {
            this.storageService.upload(file.getInputStream(), "", file.getName());
            return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                ResponseVO.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED),
                    I18nUtil.get("response.error.unauthorized")), HttpStatus.OK);
        }
    }

    @Logging
    @GetMapping(path = "/file/download")
    @ApiOperation(value = "下载文件", notes = "从公共目录下载文件")
    public ResponseEntity<ResponseVO> download(@NotNull String fileName,
                                               HttpServletResponse response) throws IOException {
        String userName = SecurityUtil.getCurrentUserName();
        if (StrUtil.isEmpty(userName)) {
            return new ResponseEntity<>(
                ResponseVO.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED),
                    I18nUtil.get("response.error.unauthorized")), HttpStatus.OK);
        }
        response.setHeader("Content-Disposition",
            "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        InputStream is = this.storageService.get("", fileName);
        if (is != null) {
            try (BufferedInputStream bis = new BufferedInputStream(is);
                 BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())
            ) {
                FileCopyUtils.copy(bis, bos);
            } catch (Exception e) {
                Rethrower.throwAs(e);
            }
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/file/delete")
    @ApiOperation(value = "删除文件", notes = "从公共目录删除文件")
    public ResponseEntity<ResponseVO> deleteFile(@NotNull String fileName) {
        String userName = SecurityUtil.getCurrentUserName();
        if (StrUtil.isEmpty(userName)) {
            return new ResponseEntity<>(
                ResponseVO.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED),
                    I18nUtil.get("response.error.unauthorized")), HttpStatus.OK);
        }
        this.storageService.delete("", fileName);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }


}
