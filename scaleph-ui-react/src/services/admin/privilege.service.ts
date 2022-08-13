import { ResponseBody } from '@/app.d';
import { request } from 'umi';
import { SecPrivilege, SecPrivilegeTreeNode } from './typings';

const url: string = '/api/admin/privilege';

export async function listAllPrivilege(resourceType: string) {
  return request<SecPrivilegeTreeNode[]>(`${url}`, {
    method: 'GET',
    params: { resourceType: resourceType },
  });
}

export async function listPrivilegeByRole(roleId: string, resourceType: string) {
  return request<SecPrivilege[]>(`${url}/role`, {
    method: 'GET',
    params: { roleId: roleId, resourceType: resourceType },
  });
}

export async function grantPrivilegeToRole(
  roleId: string,
  privilegeIds: string[],
  resourceType: string,
) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'POST',
    data: {
      roleId: roleId,
      privilegeIds: JSON.stringify(privilegeIds),
      resourceType: resourceType,
    },
    headers: { 'Content-Type': 'multipart/form-data' },
  });
}
