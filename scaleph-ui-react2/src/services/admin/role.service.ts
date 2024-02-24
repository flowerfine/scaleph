import {PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {SecRole, SecRoleParam} from './typings';

export const RoleService = {
  url: '/api/admin/role',

  listByPage: async (param: SecRoleParam) => {
    return request<PageResponse<SecRole>>(`${RoleService.url}/list`, {
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

  listAllRole: async () => {
    return request<SecRole[]>(`${RoleService.url}`, {
      method: 'GET',
    });
  },

  addRole: async (row: SecRole) => {
    return request<ResponseBody<any>>(`${RoleService.url}`, {
      method: 'POST',
      data: row,
    });
  },
  updateRole: async (row: SecRole) => {
    return request<ResponseBody<any>>(`${RoleService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  deleteRole: async (row: SecRole) => {
    return request<ResponseBody<any>>(`${RoleService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: SecRole[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${RoleService.url}/batch`, {
      method: 'DELETE',
      data: params
    });
  },

  grantRoleToUsers: async (roleId: string, userIds: string[]) => {
    return request<ResponseBody<any>>(`${RoleService.url}/grant`, {
      method: 'POST',
      data: {roleId: roleId, userIds: JSON.stringify(userIds)},
      headers: {'Content-Type': 'multipart/form-data'},
    });
  },

  listRoleByDept: async (deptId: string) => {
    return request<SecRole[]>(`${RoleService.url}/dept`, {
      method: 'GET',
      params: {grant: 1, deptId: deptId},
    });
  },

  listGrantRoleByDept: async (deptId: string) => {
    return request<SecRole[]>(`${RoleService.url}/dept`, {
      method: 'GET',
      params: {deptId: deptId},
    });
  },

  grantDeptRole: async (deptId: string, roleId: string) => {
    return request<ResponseBody<any>>(`${RoleService.url}/dept/grant`, {
      method: 'GET',
      params: {deptId: deptId, roleId: roleId},
    });
  },

  revokeDeptRole: async (deptId: string, roleId: string) => {
    return request<ResponseBody<any>>(`${RoleService.url}/dept/revoke`, {
      method: 'GET',
      params: {deptId: deptId, roleId: roleId},
    });
  },
};
