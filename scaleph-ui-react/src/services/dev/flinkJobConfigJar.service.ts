import { PageResponse, ResponseBody } from '@/app.d';
import { FlinkJobConfigJar, FlinkJobConfigJarParam } from '@/services/dev/typings';
import { request } from '@@/exports';

export const FlinkJobConfigJarService = {
  url: '/api/flink/job-config/jar',

  list: async (queryParam: FlinkJobConfigJarParam) => {
    return request<PageResponse<FlinkJobConfigJar>>(`${FlinkJobConfigJarService.url}`, {
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

  add: async (row: FlinkJobConfigJar) => {
    return request<ResponseBody<any>>(`${FlinkJobConfigJarService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: FlinkJobConfigJar) => {
    return request<ResponseBody<any>>(`${FlinkJobConfigJarService.url}/` + row.id, {
      method: 'POST',
      data: row,
    });
  },

  deleteOne: async (row: FlinkJobConfigJar) => {
    return request<ResponseBody<any>>(`${FlinkJobConfigJarService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: FlinkJobConfigJar[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${FlinkJobConfigJarService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },
};
