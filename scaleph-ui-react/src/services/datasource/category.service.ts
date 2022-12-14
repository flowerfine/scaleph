import {ResponseBody} from '@/app.d';
import {request} from 'umi';
import {DsCategory, DsType, DsTypeParam} from './typings';

export const DsCategoryService = {
  url: '/api/ds/category',

  list: async () => {
    return request<ResponseBody<Array<DsCategory>>>(`${DsCategoryService.url}`, {
      method: 'GET'
    });
  },

  listTypes: async (param: DsTypeParam) => {
    return request<ResponseBody<Array<DsType>>>(`${DsCategoryService.url}/type`, {
      method: 'GET',
      params: param
    });
  },
};
