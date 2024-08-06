import {Dict, PageResponse, ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import {SysDictDefinition, SysDictDefinitionParam, SysDictInstance, SysDictInstanceParam} from '../typings';

export const SysDictService = {
  url: '/api/carp/system/dict',

  listDefinition: async (param: SysDictDefinitionParam) => {
    return request<ResponseBody<PageResponse<SysDictDefinition>>>(`${SysDictService.url}/definition/page`, {
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

  listAllDefinition: async () => {
    return request<ResponseBody<Array<Dict>>>(`${SysDictService.url}/definition/all`, {
      method: 'GET',
    });
  },

  listInstance: async (param: SysDictInstanceParam) => {
    return request<ResponseBody<PageResponse<SysDictInstance>>>(`${SysDictService.url}/definition/instance/page`, {
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

  listDictByDefinition: async (code: string) => {
    return request<ResponseBody<Array<Dict>>>(`${SysDictService.url}/definition/instance`, {
      method: 'GET',
      params: {dictDefinitionCode: code},
    }).then((res) => {
      if (res.data) {
        return res.data;
      }
      return []
    });
  },

};
