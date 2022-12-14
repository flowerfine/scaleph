import { Dict, PageResponse, ResponseBody } from '@/app.d';
import { request } from 'umi';
import { WsDiJob, WsDiJobGraphParam, WsDiJobParam } from './typings';

export const WsDiJobService = {
  url: '/api/di/job',

  listJobByProject: async (queryParam: WsDiJobParam) => {
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

  addJob: async (param: WsDiJob) => {
    return request<ResponseBody<any>>(`${WsDiJobService.url}`, {
      method: 'POST',
      data: param,
    });
  },

  updateJob: async (param: WsDiJob) => {
    return request<ResponseBody<any>>(`${WsDiJobService.url}`, {
      method: 'PUT',
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
      method: 'POST',
      data: params,
    });
  },

  selectJobById: async (id: number) => {
    return request<WsDiJob>(`${WsDiJobService.url}/detail`, {
      method: 'GET',
      params: { id: id },
    });
  },

  saveJobDetail: async (param: WsDiJobGraphParam) => {
    return request<ResponseBody<any>>(`${WsDiJobService.url}/detail`, {
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
      `${WsDiJobService.url}/attr/` + jobId,
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
    return request<ResponseBody<any>>(`${WsDiJobService.url}/attr`, {
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
    return request<ResponseBody<string>>(`${WsDiJobService.url}/preview/` + jobId, {
      method: 'GET',
    });
  },

  runJob: async (info: any) => {
    return request<ResponseBody<any>>(`${WsDiJobService.url}/run/`, {
      method: 'POST',
      data: info,
    });
  },

  stopJob: async (jobId: number) => {
    return request<ResponseBody<any>>(`${WsDiJobService.url}/stop/`, {
      method: 'GET',
      params: { jobId: jobId },
    });
  },

  listResource: async (jobId: string) => {
    return request<Dict[]>(`${WsDiJobService.url}/resource/` + jobId, {
      method: 'GET',
    });
  },
};
