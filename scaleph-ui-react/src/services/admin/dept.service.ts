import {PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {SecDept, SecDeptParam, SecDeptTree, SecDeptTreeNode} from './typings';

export const DeptService = {
  url: '/api/admin/dept',

  listByPage: async (param: SecDeptParam) => {
    return request<PageResponse<SecDeptTree>>(`${DeptService.url}/list`, {
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

  listAllDept: async () => {
    return request<SecDeptTreeNode[]>(`${DeptService.url}`);
  },

  listChildDept: async (pid: string) => {
    return request<SecDeptTreeNode[]>(`${DeptService.url}/` + pid);
  },

  addDept: async (row: SecDept) => {
    return request<ResponseBody<any>>(`${DeptService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  updateDept: async (row: SecDept) => {
    return request<ResponseBody<any>>(`${DeptService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  deleteDept: async (row: SecDept) => {
    return request<ResponseBody<any>>(`${DeptService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  grantDeptToUsers: async (deptId: string, userIds: string[]) => {
    return request<ResponseBody<any>>(`${DeptService.url}/grant`, {
      method: 'POST',
      data: {deptId: deptId, userIds: JSON.stringify(userIds)},
      headers: {'Content-Type': 'multipart/form-data'},
    });
  },
};
