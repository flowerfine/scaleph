import {request} from '@umijs/max';
import {PageResponse, ResponseBody} from '@/typings';
import {MetaSystem, MetaSystemParam} from "@/services/stdata/typings";

export const MetaSystemService = {
  url: '/api/stdata/system',

  list: async (param: MetaSystemParam) => {
    return request<PageResponse<MetaSystem>>(`${MetaSystemService.url}`, {
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

  add: async (row: MetaSystem) => {
    return request<ResponseBody<any>>(`${MetaSystemService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: MetaSystem) => {
    return request<ResponseBody<any>>(`${MetaSystemService.url}`, {
      method: 'POST',
      data: row,
    });
  },

  deleteOne: async (row: MetaSystem) => {
    return request<ResponseBody<any>>(`${MetaSystemService.url}/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: MetaSystem[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${MetaSystemService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },
};
