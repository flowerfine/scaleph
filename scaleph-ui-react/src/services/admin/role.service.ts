import { ResponseBody } from '@/app.d';
import { request } from 'umi';
import { SecRole } from './typings';

export const RoleService = {
  url: '/api/admin/role',

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

  grantRoleToUsers: async (roleId: string, userIds: string[]) => {
    return request<ResponseBody<any>>(`${RoleService.url}/grant`, {
      method: 'POST',
      data: { roleId: roleId, userIds: JSON.stringify(userIds) },
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },

  listRoleByDept: async (deptId: string) => {
    return request<SecRole[]>(`${RoleService.url}/dept`, {
      method: 'GET',
      params: { grant: 1, deptId: deptId },
    });
  },

  listGrantRoleByDept: async (deptId: string) => {
    return request<SecRole[]>(`${RoleService.url}/dept`, {
      method: 'GET',
      params: { deptId: deptId },
    });
  },

  grantDeptRole: async (deptId: string, roleId: string) => {
    return request<ResponseBody<any>>(`${RoleService.url}/dept/grant`, {
      method: 'GET',
      params: { deptId: deptId, roleId: roleId },
    });
  },

  revokeDeptRole: async (deptId: string, roleId: string) => {
    return request<ResponseBody<any>>(`${RoleService.url}/dept/revoke`, {
      method: 'GET',
      params: { deptId: deptId, roleId: roleId },
    });
  },
};
