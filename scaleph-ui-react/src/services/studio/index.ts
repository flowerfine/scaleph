// @ts-ignore
import { request } from 'umi';

export const DataboardService = {
  url: '/api/studio/databoard',

  /** 查询集群数量 GET /api/studio/databoard/cluster */
  cluster: async (options?: { [key: string]: any }) => {
    return request<number>(`${DataboardService.url}/cluster`, {
      method: 'GET',
      ...(options || {}),
    });
  },
  /** 查询集群数量 GET /api/studio/databoard/job */
  job: async (params: Studio.JobParams, options?: { [key: string]: any }) => {
    return request<number>(`${DataboardService.url}/job`, {
      method: 'GET',
      params: params,
      ...(options || {}),
    });
  },
};
