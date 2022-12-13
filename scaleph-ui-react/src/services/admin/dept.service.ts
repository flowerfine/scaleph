import {PageResponse, ResponseBody, TreeNode} from '@/app.d';
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

  buildTree: (data: SecDeptTreeNode[]): TreeNode[] => {
    let tree: TreeNode[] = [];
    data.forEach((dept) => {
      const node: TreeNode = {
        key: '',
        title: '',
        origin: {
          id: dept.deptId,
          deptCode: dept.deptCode,
          deptName: dept.deptName,
          pid: dept.pid,
        },
      };
      if (dept.children) {
        node.key = dept.deptId;
        node.title = dept.deptName;
        node.children = DeptService.buildTree(dept.children);
        node.showOpIcon = false;
      } else {
        node.key = dept.deptId;
        node.title = dept.deptName;
        node.showOpIcon = false;
      }
      tree.push(node);
    });
    return tree;
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

  deleteDept: async (row: SecDeptTree) => {
    return request<ResponseBody<any>>(`${DeptService.url}/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: SecDeptTree[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${DeptService.url}/batch`, {
      method: 'DELETE',
      data: params
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
