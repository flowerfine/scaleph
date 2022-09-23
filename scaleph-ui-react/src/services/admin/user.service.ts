import { OnlineUserInfo, PageResponse, ResponseBody, TransferData } from '@/app.d';
import { request } from 'umi';
import { SecUser, SecUserParam } from './typings';

export const UserService = {
  url: '/api/admin/user',

  listUserByPage: async (queryParam: SecUserParam) => {
    return request<PageResponse<SecUser>>(`${UserService.url}`, {
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
  },

  deleteUserRow: async (row: SecUser) => {
    return request<ResponseBody<any>>(`${UserService.url}/` + row.id, {
      method: 'DELETE',
    });
  },
  deleteUserBatch: async (rows: SecUser[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${UserService.url}/` + 'batch', {
      method: 'POST',
      data: { ...params },
    });
  },
  addUser: async (row: SecUser) => {
    return request<ResponseBody<any>>(`${UserService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  updateUser: async (row: SecUser) => {
    return request<ResponseBody<any>>(`${UserService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  isUserExists: async (userName: string) => {
    return request<boolean>('/api/user/validation/userName', {
      method: 'GET',
      params: { userName: userName },
    });
  },
  isEmailExists: async (email: string) => {
    return request<boolean>('/api/user/validation/email', {
      method: 'GET',
      params: { email: email },
    });
  },
  listByUserNameAndDept: async (userName: string, deptId: string, direction: string) => {
    return request<TransferData[]>('/api/user/dept', {
      method: 'POST',
      data: { userName: userName, deptId: deptId, direction: direction },
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },

  listByUserNameAndRole: async (userName: string, roleId: string, direction: string) => {
    return request<TransferData[]>('/api/user/role', {
      method: 'POST',
      data: { userName: userName, roleId: roleId, direction: direction },
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },

  getOnlineUserInfo: async (token: string) => {
    return request<ResponseBody<OnlineUserInfo>>('/api/user/get/' + token, {
      method: 'GET',
    });
  },

  getUserInfo: async () => {
    return request<SecUser>('/api/user/info', {
      method: 'GET',
    });
  },

  editPassword: async (oldPassword: string, password: string, confirmPassword: string) => {
    return request<ResponseBody<any>>('/api/user/passwd/edit', {
      method: 'POST',
      data: { oldPassword: oldPassword, password: password, confirmPassword: confirmPassword },
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },

  getAuthCode: async (email: string) => {
    return request<ResponseBody<any>>('/api/user/email/getAuth', {
      method: 'GET',
      params: { email: email },
    });
  },

  bindEmail: async (email: string, authCode: string) => {
    return request<ResponseBody<any>>('/api/user/email/auth', {
      method: 'GET',
      params: { email: email, authCode: authCode },
    });
  },
};
