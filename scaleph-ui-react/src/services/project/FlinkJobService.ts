import {PageResponse, ResponseBody} from '@/app.d';
import {request} from "@@/exports";
import { FlinkJob, FlinkJobForJar, FlinkJobForSeaTunnel, FlinkJobListByCodeParam, FlinkJobListByTypeParam, FlinkJobListParam } from './typings';

export const FlinkJobService = {
  url: '/api/flink/job',

  list: async (queryParam: FlinkJobListParam) => {
    return request<PageResponse<FlinkJob>>(`${FlinkJobService.url}`, {
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

  listVersions: async (queryParam: FlinkJobListByCodeParam) => {
    return request<PageResponse<FlinkJob>>(`${FlinkJobService.url}/version`, {
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

  add: async (row: FlinkJob) => {
    return request<ResponseBody<any>>(`${FlinkJobService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  update: async (row: FlinkJob) => {
    return request<ResponseBody<any>>(`${FlinkJobService.url}/` + row.code, {
      method: 'POST',
      data: row,
    });
  },

  listJobsForJar: async (queryParam: FlinkJobListByTypeParam) => {
    return request<PageResponse<FlinkJobForJar>>(`${FlinkJobService.url}/jar`, {
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

  listJobsForSeaTunnel: async (queryParam: FlinkJobListByTypeParam) => {
    return request<PageResponse<FlinkJobForSeaTunnel>>(`${FlinkJobService.url}/seatunnel`, {
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

}
