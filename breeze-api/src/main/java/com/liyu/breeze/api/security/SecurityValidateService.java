package com.liyu.breeze.api.security;

import com.liyu.breeze.api.util.SecurityUtil;
import com.liyu.breeze.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gleiyu
 */
@Slf4j
@Service(value = "svs")
public class SecurityValidateService {
    /**
     * 检查用户角色权限，有权限则放行。
     * 如果是SUPER ADMIN角色则直接放行
     *
     * @param privileges 权限标识字符串
     * @return true/false
     */
    public boolean validate(String... privileges) {
        UserDetails userDetails = SecurityUtil.getCurrentUser();
        if (userDetails == null) {
            return false;
        } else {
            List<String> privilegeList = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            return privilegeList.contains(Constants.ROLE_SYS_ADMIN) || Arrays.stream(privileges).anyMatch(privilegeList::contains);
        }
    }
}
