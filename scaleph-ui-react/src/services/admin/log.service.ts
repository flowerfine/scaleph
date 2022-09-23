import { PageResponse, QueryParam } from '@/app.d';
import { request } from 'umi';
import { LogLogin } from './typings';

export const LogService = {
  url: '/api/admin/log',
  listLoginLogByPage: async (queryParam: QueryParam) => {
    return request<PageResponse<LogLogin>>(`${LogService.url}` + '/login', {
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
};
