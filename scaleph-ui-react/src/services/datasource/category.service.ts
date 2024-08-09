import {ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import {DsCategory, DsType, DsTypeParam} from './typings';

const {PUBLIC_PATH} = process.env

export const DsCategoryService = {
  url: '/api/carp/datasource/category',

  list: async () => {
    return request<ResponseBody<Array<DsCategory>>>(`${DsCategoryService.url}`, {
      method: 'GET'
    });
  },

  listTypes: async (param: DsTypeParam) => {
    return request<ResponseBody<Array<DsType>>>(`${DsCategoryService.url}/type`, {
      method: 'GET',
      params: param
    }).then(response => {
      if (response.data) {
        response.data = response.data.map((item) => {
          if (PUBLIC_PATH && item.logo) {
            let baseLogo = item.logo
            if (!baseLogo.startsWith("/")) {
              baseLogo = `/${baseLogo}`
            }
            let baseURL = PUBLIC_PATH
            if (baseURL.endsWith('/')) {
              baseURL = baseURL.substring(0, baseURL.length - 1)
            }
            item.logo = `${baseURL}${item.logo}`
          }
          return item
        });
      }
      return response
    });
  },
};
