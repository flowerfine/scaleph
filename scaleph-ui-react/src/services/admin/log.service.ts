import { PageResponse, QueryParam } from '@/app.d';
import { request } from 'umi';
import { LogLogin } from './typings';

const url: string = '/api/admin/log';

export async function listLoginLogByPage(queryParam: QueryParam) {
  return request<PageResponse<LogLogin>>(`${url}` + '/login', {
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
}
