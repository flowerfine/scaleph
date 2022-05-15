package cn.sliew.scaleph.service.dto.admin;

import cn.sliew.scaleph.service.dto.BaseDTO;
import cn.sliew.scaleph.service.vo.DictVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户基本信息表
 * </p>
 *
 * @author liyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户信息", description = "用户基本信息表")
public class UserDTO extends BaseDTO {

    private static final long serialVersionUID = -1821402534416659344L;

    @NotBlank
    @Length(min = 5, max = 30)
    @Pattern(regexp = "\\w+$")
    @ApiModelProperty(value = "用户名")
    private String userName;

    @Length(max = 50)
    @ApiModelProperty(value = "昵称")
    private String nickName;

    @Email
    @NotNull
    @ApiModelProperty(value = "邮箱")
    private String email;

    @JsonIgnore
    @ApiModelProperty(value = "密码")
    private String password;

    @Length(max = 30)
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "证件类型")
    private DictVO idCardType;

    @Length(max = 18)
    @ApiModelProperty(value = "证件号码")
    private String idCardNo;

    @ApiModelProperty(value = "性别")
    private DictVO gender;

    @ApiModelProperty(value = "民族")
    private DictVO nation;

    @ApiModelProperty(value = "出生日期")
    private Date birthday;

    @Length(max = 15)
    @ApiModelProperty(value = "qq号码")
    private String qq;

    @Length(max = 60)
    @ApiModelProperty(value = "微信号码")
    private String wechat;

    @Length(max = 11)
    @ApiModelProperty(value = "手机号码")
    private String mobilePhone;

    @ApiModelProperty(value = "用户状态")
    private DictVO userStatus;

    @Length(max = 500)
    @ApiModelProperty(value = "用户简介")
    private String summary;

    @ApiModelProperty(value = "注册渠道")
    private DictVO registerChannel;

    @ApiModelProperty(value = "注册时间")
    private Date registerTime;

    @ApiModelProperty(value = "注册ip")
    private String registerIp;

    @ApiModelProperty(value = "角色列表")
    private List<RoleDTO> roles;
}
