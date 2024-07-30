import {PageResponse, ResponseBody, TransferData} from '@/typings';
import {request} from '@umijs/max';
import {SecUser, SecUserParam} from '../typings';

export const UserService = {
  url: '/api/carp/security/user',

  listUserByPage: async (queryParam: SecUserParam) => {
    return request<ResponseBody<PageResponse<SecUser>>>(`${UserService.url}/page`, {
      method: 'GET',
      params: queryParam,
    }).then((res) => {
      const result = {
        data: res.data?.records,
        total: res.data?.total,
        pageSize: res.data?.size,
        current: res.data?.current,
      };
      return result;
    });
  },

  add: async (row: SecUser) => {
    return request<ResponseBody<any>>(`${UserService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  updateUser: async (row: SecUser) => {
    return request<ResponseBody<any>>(`${UserService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  delete: async (row: SecUser) => {
    return request<ResponseBody<any>>(`${UserService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: SecUser[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${UserService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },

  isUserExists: async (userName: string) => {
    return request<boolean>('/api/user/validation/userName', {
      method: 'GET',
      params: {userName: userName},
    });
  },
  isEmailExists: async (email: string) => {
    return request<boolean>('/api/user/validation/email', {
      method: 'GET',
      params: {email: email},
    });
  },
  listByUserNameAndDept: async (userName: string, deptId: string, direction: string) => {
    return request<TransferData[]>('/api/user/dept', {
      method: 'POST',
      data: {userName: userName, deptId: deptId, direction: direction},
      headers: {'Content-Type': 'multipart/form-data'},
    });
  },

  listByUserNameAndRole: async (userName: string, roleId: string, direction: string) => {
    return request<TransferData[]>('/api/user/role', {
      method: 'POST',
      data: {userName: userName, roleId: roleId, direction: direction},
      headers: {'Content-Type': 'multipart/form-data'},
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
      data: {oldPassword: oldPassword, password: password, confirmPassword: confirmPassword},
      headers: {'Content-Type': 'multipart/form-data'},
    });
  },

  getAuthCode: async (email: string) => {
    return request<ResponseBody<any>>('/api/user/email/getAuth', {
      method: 'GET',
      params: {email: email},
    });
  },

  bindEmail: async (email: string, authCode: string) => {
    return request<ResponseBody<any>>('/api/user/email/auth', {
      method: 'GET',
      params: {email: email, authCode: authCode},
    });
  },
};
