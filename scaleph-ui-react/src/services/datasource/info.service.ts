import {PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {DsInfo, DsInfoAddParam, DsInfoParam, DsInfoUpdateParam} from './typings';

export const DsInfoService = {
  url: '/api/ds/info',

  list: async (param: DsInfoParam) => {
    return request<PageResponse<DsInfo>>(`${DsInfoService.url}`, {
      method: 'GET',
      params: param
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

  add: async (param: DsInfoAddParam) => {
    return request<ResponseBody<any>>(`${DsInfoService.url}`, {
      method: 'PUT',
      data: param,
    });
  },

  update: async (id: number, param: DsInfoUpdateParam) => {
    return request<ResponseBody<any>>(`${DsInfoService.url}/` + id, {
      method: 'POST',
      data: param,
    });
  },

  deleteOne: async (row: DsInfo) => {
    return request<ResponseBody<any>>(`${DsInfoService.url}/` + row.id, {
      method: 'DELETE'
    });
  },

  deleteBatch: async (rows: DsInfo[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${DsInfoService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },

};
