import {PageResponse, ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import {DataserviceConfig, DataserviceConfigParam, DataserviceConfigSaveParam} from './typings';

export const DataserviceConfigService = {
  url: '/api/dataservice/config',

  list: async (queryParam: DataserviceConfigParam) => {
    return request<PageResponse<DataserviceConfig>>(`${DataserviceConfigService.url}`, {
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

  add: async (param: DataserviceConfigSaveParam) => {
    return request<ResponseBody<any>>(`${DataserviceConfigService.url}`, {
      method: 'PUT',
      data: param,
    });
  },

  update: async (param: DataserviceConfigSaveParam) => {
    return request<ResponseBody<any>>(`${DataserviceConfigService.url}/${param.id}`, {
      method: 'POST',
      data: param,
    });
  },

  deleteOne: async (row: DataserviceConfig) => {
    return request<ResponseBody<any>>(`${DataserviceConfigService.url}/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: DataserviceConfig[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${DataserviceConfigService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },
};
