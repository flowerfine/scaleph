import {PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {WsDiJob, WsDiJobAddParam, WsDiJobGraphParam, WsDiJobParam, WsDiJobSelectListParam} from './typings';

export const WsDiJobService = {
  url: '/api/di/job',

  list: async (queryParam: WsDiJobParam) => {
    return request<PageResponse<WsDiJob>>(`${WsDiJobService.url}`, {
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

  listAll: async (queryParam: WsDiJobSelectListParam) => {
    return request<ResponseBody<Array<WsDiJob>>>(`${WsDiJobService.url}/all`, {
      method: 'GET',
      params: queryParam,
    });
  },

  addJob: async (param: WsDiJobAddParam) => {
    return request<ResponseBody<any>>(`${WsDiJobService.url}`, {
      method: 'PUT',
      data: param,
    });
  },

  updateJob: async (param: WsDiJob) => {
    return request<ResponseBody<any>>(`${WsDiJobService.url}`, {
      method: 'POST',
      data: param,
    });
  },

  deleteJobRow: async (row: WsDiJob) => {
    return request<ResponseBody<any>>(`${WsDiJobService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteJobBatch: async (rows: WsDiJob[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${WsDiJobService.url}/` + 'batch', {
      method: 'DELETE',
      data: params,
    });
  },

  selectJobById: async (id: number) => {
    return request<WsDiJob>(`${WsDiJobService.url}/${id}/dag`, {
      method: 'GET'
    });
  },

  saveJobDetail: async (param: WsDiJobGraphParam) => {
    return request<ResponseBody<any>>(`${WsDiJobService.url}/${param.jobId}/dag`, {
      method: 'POST',
      data: param,
    });
  },

  saveStepAttr: async (step: Map<string, string>) => {
    let params = {};
    for (const key of step.keys()) {
      params[key] = step.get(key);
    }
    return request<ResponseBody<any>>(`${WsDiJobService.url}/step`, {
      method: 'POST',
      data: params,
    });
  },

  listJobAttr: async (jobId: number) => {
    return request<{ jobId: number; jobAttr: string; jobProp: string; engineProp: string }>(
      `${WsDiJobService.url}/${jobId}/attr`,
      {
        method: 'GET',
      },
    );
  },

  saveJobAttr: async (attrs: {
    jobId: number;
    jobAttr: string;
    jobProp: string;
    engineProp: string;
  }) => {
    return request<ResponseBody<any>>(`${WsDiJobService.url}/${attrs.jobId}/attr`, {
      method: 'POST',
      data: attrs,
    });
  },

  publishJob: async (jobId: number) => {
    return request<ResponseBody<any>>(`${WsDiJobService.url}/publish/` + jobId, {
      method: 'GET',
    });
  },

  previewJob: async (jobId: number) => {
    return request<ResponseBody<string>>(`${WsDiJobService.url}/${jobId}/preview`, {
      method: 'GET',
    });
  },
};
