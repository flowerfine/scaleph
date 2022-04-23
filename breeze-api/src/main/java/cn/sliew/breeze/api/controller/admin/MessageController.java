package cn.sliew.breeze.api.controller.admin;


import cn.hutool.core.util.StrUtil;
import cn.sliew.breeze.api.annotation.AnonymousAccess;
import cn.sliew.breeze.api.annotation.Logging;
import cn.sliew.breeze.api.util.I18nUtil;
import cn.sliew.breeze.api.util.SecurityUtil;
import cn.sliew.breeze.api.vo.ResponseVO;
import cn.sliew.breeze.common.constant.DictConstants;
import cn.sliew.breeze.common.enums.BoolEnum;
import cn.sliew.breeze.service.admin.MessageService;
import cn.sliew.breeze.service.dto.admin.MessageDTO;
import cn.sliew.breeze.service.param.admin.MessageParam;
import cn.sliew.breeze.service.vo.DictVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 站内信表 前端控制器
 * </p>
 *
 * @author liyu
 */
@RestController
@RequestMapping("/api/msg")
@Api(tags = "系统管理-消息管理")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询用户的消息信息", notes = "用户登录后查询自己的消息列表")
    public ResponseEntity<Page<MessageDTO>> listMessage(MessageParam param) {
        String userName = SecurityUtil.getCurrentUserName();
        if (!StrUtil.isEmpty(userName)) {
            param.setReceiver(userName);
            Page<MessageDTO> page = this.messageService.listByPage(param);
            return new ResponseEntity<>(page, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "更新消息读取状态", notes = "更新指定消息为已读状态")
    public ResponseEntity<ResponseVO> readMessage(@RequestBody MessageDTO message) {
        String userName = SecurityUtil.getCurrentUserName();
        if (!Strings.isNullOrEmpty(userName)) {
            message.setIsRead(DictVO.toVO(DictConstants.YES_NO, BoolEnum.YES.getValue()));
            this.messageService.update(message);
            return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseVO.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED), I18nUtil.get("response.error.unauthorized")), HttpStatus.OK);
        }
    }

    @Logging
    @GetMapping(path = "/readAll")
    @ApiOperation(value = "全部标记已读", notes = "更新用户全部未读消息为已读")
    public ResponseEntity<ResponseVO> readAllMessage() {
        String userName = SecurityUtil.getCurrentUserName();
        if (!Strings.isNullOrEmpty(userName)) {
            this.messageService.readAll(userName);
            return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseVO.error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED), I18nUtil.get("response.error.unauthorized")), HttpStatus.OK);
        }
    }

    @Logging
    @GetMapping(path = "/count")
    @AnonymousAccess
    @ApiOperation(value = "查询用户的未读消息数量", notes = "用户登录后查询自己的未读消息数量")
    public ResponseEntity<Integer> countUnReadMessage() {
        String userName = SecurityUtil.getCurrentUserName();
        if (!Strings.isNullOrEmpty(userName)) {
            int result = this.messageService.countUnReadMsg(userName);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(0, HttpStatus.OK);
        }
    }

}

