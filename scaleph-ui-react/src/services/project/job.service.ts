import {Dict, PageResponse, ResponseBody} from '@/app.d';
import {request} from 'umi';
import {DiJob, DiJobAddParam, DiJobGraphParam, DiJobParam, DiJobUpdateParam} from './typings';

export const JobService = {
  url: '/api/di/job',

  listJobByProject: async (queryParam: DiJobParam) => {
    return request<PageResponse<DiJob>>(`${JobService.url}`, {
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

  addJob: async (param: DiJobAddParam) => {
    return request<ResponseBody<any>>(`${JobService.url}`, {
      method: 'POST',
      data: param,
    });
  },

  updateJob: async (param: DiJobUpdateParam) => {
    return request<ResponseBody<any>>(`${JobService.url}`, {
      method: 'PUT',
      data: param,
    });
  },

  deleteJobRow: async (row: DiJob) => {
    return request<ResponseBody<any>>(`${JobService.url}/` + row.id, {
      method: 'DELETE',
    });
  },

  deleteJobBatch: async (rows: DiJob[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${JobService.url}/` + 'batch', {
      method: 'POST',
      data: params,
    });
  },

  selectJobById: async (id: number) => {
    return request<DiJob>(`${JobService.url}/detail`, {
      method: 'GET',
      params: {id: id},
    });
  },

  saveJobDetail: async (param: DiJobGraphParam) => {
    return request<ResponseBody<any>>(`${JobService.url}/detail`, {
      method: 'POST',
      data: param,
    });
  },

  saveStepAttr: async (step: Map<string, string>) => {
    let params = {};
    for (const key of step.keys()) {
      params[key] = step.get(key);
    }
    return request<ResponseBody<any>>(`${JobService.url}/step`, {
      method: 'POST',
      data: params,
    });
  },

  listJobAttr: async (jobId: number) => {
    return request<{ jobId: number; jobAttr: string; jobProp: string; engineProp: string }>(
      `${JobService.url}/attr/` + jobId,
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
    return request<ResponseBody<any>>(`${JobService.url}/attr`, {
      method: 'POST',
      data: attrs,
    });
  },

  publishJob: async (jobId: number) => {
    return request<ResponseBody<any>>(`${JobService.url}/publish/` + jobId, {
      method: 'GET',
    });
  },

  previewJob: async (jobId: number) => {
    return request<ResponseBody<string>>(`${JobService.url}/preview/` + jobId, {
      method: 'GET'
    });
  },

  runJob: async (info: any) => {
    return request<ResponseBody<any>>(`${JobService.url}/run/`, {
      method: 'POST',
      data: info,
    });
  },

  stopJob: async (jobId: number) => {
    return request<ResponseBody<any>>(`${JobService.url}/stop/`, {
      method: 'GET',
      params: {jobId: jobId},
    });
  },

  listResource: async (jobId: string) => {
    return request<Dict[]>(`${JobService.url}/resource/` + jobId, {
      method: 'GET',
    });
  },
};
