import {PageResponse, ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import {SecResourceWeb, SecResourceWebAddParam, SecResourceWebParam, SecResourceWebUpdateParam} from '../typings';

export const ResourceWebService = {
  url: '/api/carp/security/resource/web',

  listByPage: async (param: SecResourceWebParam) => {
    return request<ResponseBody<PageResponse<SecResourceWeb>>>(`${ResourceWebService.url}/page`, {
      method: 'GET',
      params: param,
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
