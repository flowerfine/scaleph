package cn.sliew.breeze.api.security;

import cn.sliew.breeze.api.util.I18nUtil;
import cn.sliew.breeze.common.enums.UserStatusEnum;
import cn.sliew.breeze.service.admin.UserService;
import cn.sliew.breeze.service.dto.admin.PrivilegeDTO;
import cn.sliew.breeze.service.dto.admin.RoleDTO;
import cn.sliew.breeze.service.dto.admin.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gleiyu
 */
@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    /**
     * 根据用户名查询登录用户信息
     *
     * @param userName 用户名
     * @return 用户信息
     * @throws UsernameNotFoundException UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserDTO userDTO = userService.selectOne(userName);
        boolean flag = userDTO.getUserStatus() != null
                && !(userDTO.getUserStatus().getValue().equals(UserStatusEnum.UNBIND_EMAIL.getValue()) ||
                userDTO.getUserStatus().getValue().equals(UserStatusEnum.BIND_EMAIL.getValue()));
        if (userDTO == null) {
            throw new BadCredentialsException(I18nUtil.get("response.error.login.password"));
        } else if (flag) {
            throw new BadCredentialsException(I18nUtil.get("response.error.login.disable"));
        } else {
            UserDetailInfo user = new UserDetailInfo();
            user.setUser(userDTO);
            //查询用户角色权限信息
            List<RoleDTO> privileges = this.userService.getAllPrivilegeByUserName(userName);
            user.setAuthorities(this.toGrantedAuthority(privileges));
            return user;
        }
    }

    private List<GrantedAuthority> toGrantedAuthority(List<RoleDTO> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        } else {
            List<GrantedAuthority> list = new ArrayList<>();
            for (RoleDTO role : roles) {
                if (role.getPrivileges() == null) {
                    continue;
                }
                for (PrivilegeDTO privilege : role.getPrivileges()) {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(privilege.getPrivilegeCode());
                    list.add(grantedAuthority);
                }
            }
            return list;
        }
    }
}
