import {PageResponse, ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import {DsInfo, DsInfoParam} from './typings';

export const DsInfoService = {
  url: '/api/carp/datasource/info',

  list: async (param: DsInfoParam) => {
    return request<ResponseBody<PageResponse<DsInfo>>>(`${DsInfoService.url}/page`, {
      method: 'GET',
      params: param
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

  listByType: async (type: string) => {
    return request<ResponseBody<Array<DsInfo>>>(`${DsInfoService.url}/${type}`, {
      method: 'GET'
    });
  },

  selectOne: async (id: number | string) => {
    return request<ResponseBody<DsInfo>>(`${DsInfoService.url}/${id}`, {
      method: 'GET',
    });
  },

  add: async (param: any) => {
    return request<ResponseBody<boolean>>(`${DsInfoService.url}`, {
      method: 'PUT',
      data: param,
    });
  },

  update: async (id: number, param: any) => {
    return request<ResponseBody<boolean>>(`${DsInfoService.url}/${id}`, {
      method: 'POST',
      data: param,
    });
  },

  deleteOne: async (row: DsInfo) => {
    return request<ResponseBody<boolean>>(`${DsInfoService.url}/${row.id}`, {
      method: 'DELETE'
    });
  },

  deleteBatch: async (rows: DsInfo[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<boolean>>(`${DsInfoService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },

};
