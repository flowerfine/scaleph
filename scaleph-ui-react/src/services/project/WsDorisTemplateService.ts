import {PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {WsDorisTemplate, WsDorisTemplateAddParam, WsDorisTemplateParam, WsDorisTemplateUpdateParam} from './typings';

export const WsDorisTemplateService = {
  url: '/api/doris/template',

  list: async (queryParam: WsDorisTemplateParam) => {
    return request<PageResponse<WsDorisTemplate>>(`${WsDorisTemplateService.url}`, {
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

  asYaml: async (param: WsDorisTemplate) => {
    return request<ResponseBody<any>>(`${WsDorisTemplateService.url}/asYaml`, {
      method: 'POST',
      data: param,
    });
  },

  add: async (param: WsDorisTemplateAddParam) => {
    return request<ResponseBody<any>>(`${WsDorisTemplateService.url}`, {
      method: 'PUT',
      data: param,
    });
  },

  update: async (param: WsDorisTemplateUpdateParam) => {
    return request<ResponseBody<any>>(`${WsDorisTemplateService.url}`, {
      method: 'POST',
      data: param,
    });
  },

  delete: async (row: WsDorisTemplate) => {
    return request<ResponseBody<any>>(`${WsDorisTemplateService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: WsDorisTemplate[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${WsDorisTemplateService.url}/` + 'batch', {
      method: 'DELETE',
      data: params,
    });
  },
};
