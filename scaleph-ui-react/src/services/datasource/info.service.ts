import {PageResponse} from '@/app.d';
import {request} from 'umi';
import {DsInfo, DsInfoParam} from './typings';

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

};
