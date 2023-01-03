import { PageResponse, ResponseBody } from '@/app.d';
import { request } from '@@/exports';
import { WsFlinkJob, WsFlinkJobListParam } from './typings';

export const FlinkJobService = {
  url: '/api/flink/job',

  list: async (queryParam: WsFlinkJobListParam) => {
    return request<PageResponse<WsFlinkJob>>(`${FlinkJobService.url}`, {
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
    return request<ResponseBody<WsFlinkJob>>(`${FlinkJobService.url}/` + id, {
      method: 'GET',
    });
  },
  add: async (row: WsFlinkJob) => {
    return request<ResponseBody<any>>(`${FlinkJobService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: WsFlinkJob) => {
    return request<ResponseBody<any>>(`${FlinkJobService.url}`, {
      method: 'POST',
      data: row,
    });
  },
  delete: async (row: WsFlinkJob) => {
    return request<ResponseBody<any>>(`${FlinkJobService.url}/` + row.id, {
      method: 'DELETE',
    });
  },
};
