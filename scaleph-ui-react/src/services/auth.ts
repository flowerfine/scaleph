import { USER_AUTH } from '@/constant';
import { request } from 'umi';
import { AuthCode, LoginInfo, OnlineUserInfo, RegisterInfo, ResponseBody } from '../app.d';

export const AuthService = {
  login: async (loginInfo: LoginInfo) => {
    return request<ResponseBody<any>>('/api/user/login', {
      method: 'POST',
      data: loginInfo,
    });
  },

  refreshAuthImage: async () => {
    return request<AuthCode>('/api/authCode', {
      method: 'GET',
      params: { d: new Date().getTime() },
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

  isUserLoggedIn: async () => {
    return localStorage.getItem(USER_AUTH.token) ? true : false;
  },
  hasPrivilege: async (code: string) => {
    let pCodes: string[] = JSON.parse(localStorage.getItem(USER_AUTH.pCodes) + '');
    if (pCodes != null && pCodes != undefined) {
      return pCodes.includes(USER_AUTH.roleSysAdmin) || pCodes.includes(code);
    } else {
      return false;
    }
  },

  register: async (registerInfo: RegisterInfo) => {
    return request<ResponseBody<any>>('/api/user/register', {
      method: 'POST',
      data: registerInfo,
    });
  },

  logout: async () => {
    let token: string = localStorage.getItem(USER_AUTH.token) || '';
    request<ResponseBody<any>>('/api/user/logout', {
      method: 'POST',
      params: { token: token },
    });
  },
};
