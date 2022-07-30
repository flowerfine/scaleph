import { ResponseBody } from '@/app.d';
import { request } from 'umi';
import { SecRole } from './typings';

const url: string = '/api/admin/role';

export async function listAllRole() {
  return request<SecRole[]>(`${url}`, {
    method: 'GET',
  });
}

export async function addRole(row: SecRole) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'POST',
    data: row,
  });
}

export async function updateRole(row: SecRole) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'PUT',
    data: row,
  });
}

export async function deleteRole(row: SecRole) {
  return request<ResponseBody<any>>(`${url}/` + row.id, {
    method: 'DELETE',
  });
}

export async function grantRoleToUsers(roleId: string, userIds: string[]) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'POST',
    data: { roleId: roleId, userIds: JSON.stringify(userIds) },
    requestType: 'form',
  });
}

export async function listRoleByDept(deptId: string) {
  return request<SecRole[]>(`${url}/dept`, {
    method: 'GET',
    params: { grant: 1, deptId: deptId },
  });
}

export async function listGrantRoleByDept(deptId: string) {
  return request<SecRole[]>(`${url}/dept`, {
    method: 'GET',
    params: { deptId: deptId },
  });
}

export async function grantDeptRole(deptId: string, roleId: string) {
  return request<ResponseBody<any>>(`${url}/dept/grant`, {
    method: 'GET',
    params: { deptId: deptId, roleId: roleId },
  });
}

export async function revokeDeptRole(deptId: string, roleId: string) {
  return request<ResponseBody<any>>(`${url}/dept/revoke`, {
    method: 'GET',
    params: { deptId: deptId, roleId: roleId },
  });
}
