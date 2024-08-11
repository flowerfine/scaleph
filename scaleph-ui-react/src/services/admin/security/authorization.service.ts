import {PageResponse, ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import {SecResourceWeb, SecRole, SecUser} from "@/services/admin/typings";

export const AuthorizationService = {
  url: '/api/carp/security/authorization',

  listAuthorizedRolesByResourceWebId: async (param: any) => {
    return request<ResponseBody<PageResponse<SecRole>>>(`${AuthorizationService.url}/resource-web/authorized-roles`, {
      method: 'GET',
      params: param,
    });
  },
  listUnauthorizedRolesByResourceWebId: async (param: any) => {
    return request<ResponseBody<PageResponse<SecRole>>>(`${AuthorizationService.url}/resource-web/unauthorized-roles`, {
      method: 'GET',
      params: param,
    });
  },
  authorizeResourceWeb2Roles: async (param: any) => {
    return request<ResponseBody<any>>(`${AuthorizationService.url}/resource-web/roles`, {
      method: 'PUT',
      data: param,
    });
  },
  unauthorizeResourceWeb2Roles: async (param: any) => {
    return request<ResponseBody<any>>(`${AuthorizationService.url}/resource-web/roles`, {
      method: 'DELETE',
      data: param,
    });
  },


  listAuthorizedUsersByRoleId: async (param: any) => {
    return request<ResponseBody<PageResponse<SecUser>>>(`${AuthorizationService.url}/role/authorized-users`, {
      method: 'GET',
      params: param,
    });
  },
  listUnauthorizedUsersByRoleId: async (param: any) => {
    return request<ResponseBody<PageResponse<SecUser>>>(`${AuthorizationService.url}/role/unauthorized-users`, {
      method: 'GET',
      params: param,
    });
  },
  authorizeRole2Users: async (param: any) => {
    return request<ResponseBody<any>>(`${AuthorizationService.url}/role/users`, {
      method: 'PUT',
      data: param,
    });
  },
  unauthorizeRole2Users: async (param: any) => {
    return request<ResponseBody<any>>(`${AuthorizationService.url}/role/users`, {
      method: 'DELETE',
      data: param,
    });
  },


  listUnauthorizedRolesByUserId: async (param: any) => {
    return request<ResponseBody<PageResponse<SecRole>>>(`${AuthorizationService.url}/user/unauthorized-roles`, {
      method: 'GET',
      params: param,
    });
  },
  listAuthorizedRolesByUserId: async (param: any) => {
    return request<ResponseBody<PageResponse<SecRole>>>(`${AuthorizationService.url}/user/authorized-roles`, {
      method: 'GET',
      params: param,
    });
  },
  authorizeUser2Roles: async (param: any) => {
    return request<ResponseBody<any>>(`${AuthorizationService.url}/user/roles`, {
      method: 'PUT',
      data: param,
    });
  },
  unauthorizeUser2Roles: async (param: any) => {
    return request<ResponseBody<any>>(`${AuthorizationService.url}/user/roles`, {
      method: 'DELETE',
      data: param,
    });
  },

  //查询所有 资源-web 和指定角色绑定状态
  requestResourceWebs: async (param: any) => {
    return request<ResponseBody<Array<SecResourceWeb>>>(`${AuthorizationService.url}/role/resource-webs`, {
      method: 'GET',
      params: param,
    });
  },

  //批量为角色绑定 资源-web
  requestRoleResourceWebs: async (param: any) => {
    return request<ResponseBody<any>>(`${AuthorizationService.url}/role/resource-webs`, {
      method: 'PUT',
      data: param,
    });
  },

  //批量为角色解除 资源-web 绑定
  requestDeleteRoleResourceWebs: async (param: any) => {
    return request<ResponseBody<any>>(`${AuthorizationService.url}/role/resource-webs`, {
      method: 'DELETE',
      data: param,
    });
  },
};
