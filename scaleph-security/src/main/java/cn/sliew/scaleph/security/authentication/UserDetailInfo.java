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

package cn.sliew.scaleph.security.authentication;

import cn.sliew.scaleph.common.dict.security.UserStatus;
import cn.sliew.scaleph.security.service.dto.SecUserDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @see org.springframework.security.core.userdetails.User
 */
@Getter
@Setter
public class UserDetailInfo implements UserDetails, CredentialsContainer {

    private static final long serialVersionUID = -7255246796611902520L;

    private SecUserDTO user;
    private List<GrantedAuthority> authorities;

    private Boolean remember;
    private String loginIpAddress;
    private Date loginTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUserName();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == UserStatus.ENABLED;
    }

    /**
     * 重写 {@link #equals(Object)} 和 {@link #hashCode()} 方法。
     * 如果不重写使用 session 的限制同一个账号只能同时有一个在线失败
     *
     * @see User
     */
    @Override
    public void eraseCredentials() {
        user.setPassword(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDetailInfo that = (UserDetailInfo) o;
        return Objects.equals(user.getUserName(), that.user.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getUserName());
    }
}
