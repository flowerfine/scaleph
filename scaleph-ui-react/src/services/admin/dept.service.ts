import { ResponseBody } from '@/app.d';
import { request } from 'umi';
import { SecDept, SecDeptTreeNode } from './typings';

const url = '/api/admin/dept';

export async function listAllDept() {
  return request<SecDeptTreeNode[]>(`${url}`);
}

export async function listChildDept(pid: string) {
  return request<SecDeptTreeNode[]>(`${url}/` + pid);
}

export async function addDept(row: SecDept) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'POST',
    data: row,
  });
}

export async function updateDept(row: SecDept) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'PUT',
    data: row,
  });
}

export async function deleteDept(row: SecDept) {
  return request<ResponseBody<any>>(`${url}/` + row.id, {
    method: 'DELETE',
  });
}

export async function grantDeptToUsers(deptId: string, userIds: string[]) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'POST',
    data: { deptId: deptId, userIds: JSON.stringify(userIds) },
    headers: { 'Content-Type': 'multipart/form-data' },
  });
}
