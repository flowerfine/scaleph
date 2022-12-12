import { PageResponse, ResponseBody } from '@/app.d';
import { request } from '@@/exports';
import {
  WsFlinkJob,
  WsFlinkJobForJar,
  WsFlinkJobForSeaTunnel,
  WsFlinkJobListByTypeParam,
  WsFlinkJobListParam,
} from './typings';

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

  listJobsForJar: async (queryParam: WsFlinkJobListByTypeParam) => {
    return request<PageResponse<WsFlinkJobForJar>>(`${FlinkJobService.url}/jar`, {
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

  listJobsForSeaTunnel: async (queryParam: WsFlinkJobListByTypeParam) => {
    return request<PageResponse<WsFlinkJobForSeaTunnel>>(`${FlinkJobService.url}/seatunnel`, {
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
