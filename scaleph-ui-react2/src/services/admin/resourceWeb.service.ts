import {PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {SecResourceWeb, SecResourceWebAddParam, SecResourceWebParam, SecResourceWebUpdateParam} from './typings';

export const ResourceWebService = {
  url: '/api/admin/resource/web',

  listByPage: async (param: SecResourceWebParam) => {
    return request<PageResponse<SecResourceWeb>>(`${ResourceWebService.url}/list`, {
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

  add: async (row: SecResourceWebAddParam) => {
    return request<ResponseBody<any>>(`${ResourceWebService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: SecResourceWebUpdateParam) => {
    return request<ResponseBody<any>>(`${ResourceWebService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  deleteOne: async (row: SecResourceWeb) => {
    return request<ResponseBody<any>>(`${ResourceWebService.url}/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: SecResourceWeb[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${ResourceWebService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },
};
