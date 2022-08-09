import { OnlineUserInfo, PageResponse, ResponseBody, TransferData } from '@/app.d';
import { request } from 'umi';
import { SecUser, SecUserParam } from './typings';

const url: string = '/api/admin/user';

export async function listUserByPage(queryParam: SecUserParam) {
  return request<PageResponse<SecUser>>(`${url}`, {
    method: 'GET',
    params: queryParam,
  }).then((res) => {
    const result = {
      data: res.records,
      total: res.total,
      pageSize: res.size,
      current: res.current,
    };
    return result;
  });
}

export async function deleteUserRow(row: SecUser) {
  return request<ResponseBody<any>>(`${url}/` + row.id, {
    method: 'DELETE',
  });
}

export async function deleteUserBatch(rows: SecUser[]) {
  const params = rows.map((row) => row.id);
  return request<ResponseBody<any>>(`${url}/` + 'batch', {
    method: 'POST',
    data: { ...params },
  });
}

export async function addUser(row: SecUser) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'POST',
    data: row,
  });
}

export async function updateUser(row: SecUser) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'PUT',
    data: row,
  });
}

export async function isUserExists(userName: string) {
  return request<boolean>('/api/user/validation/userName', {
    method: 'GET',
    params: { userName: userName },
  });
}

export async function isEmailExists(email: string) {
  return request<boolean>('/api/user/validation/email', {
    method: 'GET',
    params: { email: email },
  });
}

export async function listByUserNameAndDept(userName: string, deptId: string, direction: string) {
  return request<TransferData[]>('/api/user/dept', {
    method: 'POST',
    data: { userName: userName, deptId: deptId, direction: direction },
    headers: { 'Content-Type': 'multipart/form-data' },
  });
}

export async function listByUserNameAndRole(userName: string, roleId: string, direction: string) {
  return request<TransferData[]>('/api/user/role', {
    method: 'POST',
    data: { userName: userName, roleId: roleId, direction: direction },
    headers: { 'Content-Type': 'multipart/form-data' },
  });
}

export async function getOnlineUserInfo(token: string) {
  return request<ResponseBody<OnlineUserInfo>>('/api/user/get/' + token, {
    method: 'GET',
  });
}

export async function getUserInfo() {
  return request<SecUser>('/api/user/info', {
    method: 'GET',
  });
}

export async function editPassword(oldPassword: string, password: string, confirmPassword: string) {
  return request<ResponseBody<any>>('/api/user/passwd/edit', {
    method: 'POST',
    data: { oldPassword: oldPassword, password: password, confirmPassword: confirmPassword },
    headers: { 'Content-Type': 'multipart/form-data' },
  });
}

export async function getAuthCode(email: string) {
  return request<ResponseBody<any>>('/api/user/email/getAuth', {
    method: 'GET',
    params: { email: email },
  });
}

export async function bindEmail(email: string, authCode: string) {
  return request<ResponseBody<any>>('/api/user/email/auth', {
    method: 'GET',
    params: { email: email, authCode: authCode },
  });
}
