import {PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {
  SecPrivilege,
  SecPrivilegeAddParam,
  SecPrivilegeParam,
  SecPrivilegeTreeNode,
  SecPrivilegeUpdateParam
} from './typings';

export const PrivilegeService = {
  url: '/api/admin/privilege',

  listByPage: async (param: SecPrivilegeParam) => {
    return request<PageResponse<SecPrivilege>>(`${PrivilegeService.url}/list`, {
      method: 'GET',
      params: param,
    }).then((res) => {
      const result = {
        data: res.records,
        total: res.total,
        pageSize: res.size,
        current: res.current,
      };
      return result;
    });
  },

  listAllPrivilege: async (resourceType: string) => {
    return request<SecPrivilegeTreeNode[]>(`${PrivilegeService.url}`, {
      method: 'GET',
      params: {resourceType: resourceType},
    });
  },

  listByPid: async (pid: number) => {
    return request<ResponseBody<Array<SecPrivilege>>>(`${PrivilegeService.url}/list/${pid}`, {
      method: 'GET'
    });
  },

  listPrivilegeByRole: async (roleId: string, resourceType: string) => {
    return request<SecPrivilege[]>(`${PrivilegeService.url}/role`, {
      method: 'GET',
      params: {roleId: roleId, resourceType: resourceType},
    });
  },

  grantPrivilegeToRole: async (roleId: string, privilegeIds: string[], resourceType: string) => {
    return request<ResponseBody<any>>(`${PrivilegeService.url}`, {
      method: 'POST',
      data: {
        roleId: roleId,
        privilegeIds: JSON.stringify(privilegeIds),
        resourceType: resourceType,
      },
      headers: {'Content-Type': 'multipart/form-data'},
    });
  },

  add: async (row: SecPrivilegeAddParam) => {
    return request<ResponseBody<any>>(`${PrivilegeService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: SecPrivilegeUpdateParam) => {
    return request<ResponseBody<any>>(`${PrivilegeService.url}/${row.id}`, {
      method: 'POST',
      data: row,
    });
  },

  deleteOne: async (row: SecPrivilege) => {
    return request<ResponseBody<any>>(`${PrivilegeService.url}/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: SecPrivilege[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${PrivilegeService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },
};
