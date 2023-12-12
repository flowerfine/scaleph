import { USER_AUTH } from '@/constants/constant';
import { request } from 'umi';
import { AuthCode, LoginInfo, OnlineUserInfo, RegisterInfo, ResponseBody } from '../app.d';

export const AuthService = {
  login: async (loginInfo: LoginInfo) => {
    return request<ResponseBody<any>>('/api/user/login', {
      method: 'POST',
      data: loginInfo,
    });
  },
  menuRoutes: async () => {
    return request<ResponseBody<any>>('/api/admin/authorize/routes', {
      method: 'GET',
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

  unauthorizedRoles: async (param: any) => {
    return request<ResponseBody<any>>('/api/admin/authorize/resource-web/unauthorized-roles', {
      method: 'GET',
      params: param,
    });
  },
  authorizedRoles: async (param: any) => {
    return request<ResponseBody<any>>('/api/admin/authorize/resource-web/authorized-roles', {
      method: 'GET',
      params: param,
    });
  },
  resourceWebRoles: async (param: any) => {
    return request<ResponseBody<any>>('/api/admin/authorize/resource-web/roles', {
      method: 'PUT',
      data: param,
    });
  },
  resourceWebRolesDelete: async (param: any) => {
    return request<ResponseBody<any>>('/api/admin/authorize/resource-web/roles', {
      method: 'DELETE',
      data: param,
    });
  },
  //查询角色绑定用户列表
  requestAuthorizedUsers: async (param: any) => {
    return request<ResponseBody<any>>('/api/admin/authorize/role/authorized-users', {
      method: 'GET',
      params: param,
    });
  },
  //查询角色未绑定用户列表
  requestUnauthorizedUsers: async (param: any) => {
    return request<ResponseBody<any>>('/api/admin/authorize/role/unauthorized-users', {
      method: 'GET',
      params: param,
    });
  },
  //批量为角色绑定用户
  rolesUser: async (param: any) => {
    return request<ResponseBody<any>>('/api/admin/authorize/role/users', {
      method: 'PUT',
      data: param,
    });
  },
  //批量为角色解除用户绑定
  deleteRolesUser: async (param: any) => {
    return request<ResponseBody<any>>('/api/admin/authorize/role/users', {
      method: 'DELETE',
      data: param,
    });
  },

  //查询用户未绑定角色列表
  requestUnauthorizedRoles: async (param: any) => {
    return request<ResponseBody<any>>('/api/admin/authorize/user/unauthorized-roles', {
      method: 'GET',
      params: param,
    });
  },
  //查询角色绑定用户列表
  requestUserAuthorizedRoles: async (param: any) => {
    return request<ResponseBody<any>>('/api/admin/authorize/user/authorized-roles', {
      method: 'GET',
      params: param,
    });
  },

  //批量为用户绑定角色
  requestUserRoles: async (param: any) => {
    return request<ResponseBody<any>>('/api/admin/authorize/user/roles', {
      method: 'PUT',
      data: param,
    });
  },

  //批量为用户解除角色绑定
  requestDeleteUserRoles: async (param: any) => {
    return request<ResponseBody<any>>('/api/admin/authorize/user/roles', {
      method: 'DELETE',
      data: param,
    });
  },

  //查询所有 资源-web 和指定角色绑定状态
  requestResourceWebs: async (param: any) => {
    return request<ResponseBody<any>>('/api/admin/authorize/role/resource-webs', {
      method: 'GET',
      params: param,
    });
  },

  //批量为角色绑定 资源-web
  requestRoleResourceWebs: async (param: any) => {
    return request<ResponseBody<any>>('/api/admin/authorize/role/resource-webs', {
      method: 'PUT',
      data: param,
    });
  },

  //批量为角色解除 资源-web 绑定
  requestDeleteRoleResourceWebs: async (param: any) => {
    return request<ResponseBody<any>>('/api/admin/authorize/role/resource-webs', {
      method: 'DELETE',
      data: param,
    });
  },
};
