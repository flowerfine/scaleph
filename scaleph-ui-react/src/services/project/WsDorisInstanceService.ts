import {PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {WsDorisInstance, WsDorisInstanceAddParam, WsDorisInstanceParam, WsDorisInstanceUpdateParam} from './typings';

export const WsDorisInstanceService = {
  url: '/api/doris/instance',

  list: async (queryParam: WsDorisInstanceParam) => {
    return request<PageResponse<WsDorisInstance>>(`${WsDorisInstanceService.url}`, {
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

  fromTemplate: async (templateId: number) => {
    return request<ResponseBody<WsDorisInstance>>(`${WsDorisInstanceService.url}/fromTemplate`, {
      method: 'GET',
      params: {templateId: templateId},
    });
  },

  asYaml: async (param: WsDorisInstance) => {
    return request<ResponseBody<any>>(`${WsDorisInstanceService.url}/asYaml`, {
      method: 'POST',
      data: param,
    });
  },

  add: async (param: WsDorisInstanceAddParam) => {
    return request<ResponseBody<any>>(`${WsDorisInstanceService.url}`, {
      method: 'PUT',
      data: param,
    });
  },

  update: async (param: WsDorisInstanceUpdateParam) => {
    return request<ResponseBody<any>>(`${WsDorisInstanceService.url}`, {
      method: 'POST',
      data: param,
    });
  },

  delete: async (row: WsDorisInstance) => {
    return request<ResponseBody<any>>(`${WsDorisInstanceService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: WsDorisInstance[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${WsDorisInstanceService.url}/` + 'batch', {
      method: 'DELETE',
      data: params,
    });
  },
};
