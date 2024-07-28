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
    return request<ResponseBody<OnlineUserInfo>>(`${AuthenticationService.url}/login`, {
      method: 'POST',
      data: loginInfo,
    });
  },

  storeSession: async (userInfo: OnlineUserInfo) => {
    localStorage.setItem(USER_AUTH.userInfo, JSON.stringify(userInfo));
    let pCodes: string[] = [];
    if (userInfo.roles) {
      userInfo.roles.forEach((role) => {
        pCodes.push(role.code);
      });
    }

    if (userInfo.resourceWebs) {
      userInfo.resourceWebs.forEach((resourceWeb) => {
        pCodes.push(resourceWeb.name);
      });
    }

    localStorage.setItem(USER_AUTH.pCodes, JSON.stringify(pCodes));
  },

  getOnlineUserInfo: async () => {
    return request<ResponseBody<OnlineUserInfo>>(`${AuthenticationService.url}/onlineUser`, {
      method: 'GET',
    });
  },

  logout: async () => {
    let token: string = localStorage.getItem(USER_AUTH.token) || '';
    request<ResponseBody<any>>('/api/user/logout', {
      method: 'POST',
      params: {token: token},
    });
  },
};
