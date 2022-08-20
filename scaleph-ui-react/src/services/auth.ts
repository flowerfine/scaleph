import { USER_AUTH } from '@/constant';
import { request } from 'umi';
import { AuthCode, LoginInfo, OnlineUserInfo, ResponseBody } from '../app.d';

export async function login(loginInfo: LoginInfo) {
  return request<ResponseBody<any>>('/api/user/login', {
    method: 'POST',
    data: loginInfo,
  });
}

export async function refreshAuthImage() {
  return request<AuthCode>('/api/authCode', {
    method: 'GET',
    params: { d: new Date().getTime() },
  });
}

export async function getOnlineUserInfo(token: string) {
  return request<ResponseBody<OnlineUserInfo>>('/api/user/get/' + token);
}

export async function setSession(userInfo: OnlineUserInfo) {
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
}

export async function isUserLoggedIn() {
  return localStorage.getItem(USER_AUTH.token) ? true : false;
}

export function hasPrivilege(code: string) {
  let pCodes: string[] = JSON.parse(localStorage.getItem(USER_AUTH.pCodes) || '');
  if (pCodes != null && pCodes != undefined) {
    return pCodes.includes(USER_AUTH.roleSysAdmin) || pCodes.includes(code);
  } else {
    return false;
  }
}

export async function logout() {
  let token: string = localStorage.getItem(USER_AUTH.token) || '';
  request<ResponseBody<any>>('/api/user/logout', {
    method: 'POST',
    params: { token: token },
  });
}
