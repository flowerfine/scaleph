import {PageResponse, ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import {
  DorisClusterFeEndpoint,
  WsDorisOperatorInstance,
  WsDorisOperatorInstanceAddParam,
  WsDorisOperatorInstanceParam,
  WsDorisOperatorInstanceUpdateParam
} from './typings';

export const WsDorisOperatorInstanceService = {
  url: '/api/doris/operator/instance',

  list: async (queryParam: WsDorisOperatorInstanceParam) => {
    return request<PageResponse<WsDorisOperatorInstance>>(`${WsDorisOperatorInstanceService.url}`, {
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

  selectOne: async (id: number) => {
    return request<ResponseBody<WsDorisOperatorInstance>>(`${WsDorisOperatorInstanceService.url}/` + id, {
      method: 'GET',
    });
  },

  fromTemplate: async (templateId: number) => {
    return request<ResponseBody<WsDorisOperatorInstance>>(`${WsDorisOperatorInstanceService.url}/fromTemplate`, {
      method: 'GET',
      params: {templateId: templateId},
    });
  },

  asYaml: async (param: WsDorisOperatorInstance) => {
    return request<ResponseBody<any>>(`${WsDorisOperatorInstanceService.url}/asYaml`, {
      method: 'POST',
      data: param,
    });
  },

  add: async (param: WsDorisOperatorInstanceAddParam) => {
    return request<ResponseBody<any>>(`${WsDorisOperatorInstanceService.url}`, {
      method: 'PUT',
      data: param,
    });
  },

  update: async (param: WsDorisOperatorInstanceUpdateParam) => {
    return request<ResponseBody<any>>(`${WsDorisOperatorInstanceService.url}`, {
      method: 'POST',
      data: param,
    });
  },

  delete: async (row: WsDorisOperatorInstance) => {
    return request<ResponseBody<any>>(`${WsDorisOperatorInstanceService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: WsDorisOperatorInstance[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${WsDorisOperatorInstanceService.url}/` + 'batch', {
      method: 'DELETE',
      data: params,
    });
  },

  deploy: async (id: number) => {
    return request<ResponseBody<any>>(`${WsDorisOperatorInstanceService.url}/deploy/` + id, {
      method: 'PUT',
    });
  },

  shutdown: async (id: number) => {
    return request<ResponseBody<any>>(`${WsDorisOperatorInstanceService.url}/shutdown/` + id, {
      method: 'DELETE',
    });
  },

  status: async (row: WsDorisOperatorInstance) => {
    return request<ResponseBody<any>>(`${WsDorisOperatorInstanceService.url}/status/${row.id}`, {
      method: 'GET',
    });
  },

  feEndpoint: async (id: number) => {
    return request<ResponseBody<DorisClusterFeEndpoint>>(`${WsDorisOperatorInstanceService.url}/endpoint/fe/${id}`, {
      method: 'GET',
    });
  },
};
