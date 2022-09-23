import { ResponseBody } from '@/app.d';
import { request } from 'umi';
import { SecPrivilege, SecPrivilegeTreeNode } from './typings';

export const PrivilegeService = {
  url: '/api/admin/privilege',

  listAllPrivilege: async (resourceType: string) => {
    return request<SecPrivilegeTreeNode[]>(`${PrivilegeService.url}`, {
      method: 'GET',
      params: { resourceType: resourceType },
    });
  },

  listPrivilegeByRole: async (roleId: string, resourceType: string) => {
    return request<SecPrivilege[]>(`${PrivilegeService.url}/role`, {
      method: 'GET',
      params: { roleId: roleId, resourceType: resourceType },
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
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
};
