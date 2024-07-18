import {USER_AUTH} from '@/constants/constant';
import {AuthCode, LoginInfo, OnlineUserInfo, ResponseBody} from '@/typings';
import {request} from '@umijs/max';

export const AuthenticationService = {
  url: '/api/carp/security/authentication',

  getAuthImage: async () => {
    return request<ResponseBody<AuthCode>>(`${AuthenticationService.url}/captcha`, {
      method: 'GET'
    });
  },

  login: async (loginInfo: LoginInfo) => {
    return request<ResponseBody<any>>(`${AuthenticationService.url}/login`, {
      method: 'POST',
      data: loginInfo,
    });
  },


  setSession: async (userInfo: OnlineUserInfo) => {
    localStorage.setItem(USER_AUTH.userInfo, JSON.stringify(userInfo));
    let pCodes: string[] = [];
    if (userInfo.roles != null && userInfo.roles != undefined) {
      userInfo.roles.forEach((d) => {
        pCodes.push(d);
      });
    }
    if (userInfo.privileges != null && userInfo.privileges != undefined) {
      userInfo.privileges.forEach((d) => {
        pCodes.push(d);
      });
    }
    localStorage.setItem(USER_AUTH.expireTime, userInfo.expireTime + '');
    localStorage.setItem(USER_AUTH.pCodes, JSON.stringify(pCodes));
  },

  logout: async () => {
    let token: string = localStorage.getItem(USER_AUTH.token) || '';
    request<ResponseBody<any>>('/api/user/logout', {
      method: 'POST',
      params: {token: token},
    });
  },
};
