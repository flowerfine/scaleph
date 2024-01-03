// @ts-ignore
import { request } from '@umijs/max';
import { topBatch100 } from "./typings"

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
  /** 查询项目数量 GET /api/studio/databoard/project */
  project: async (options?: { [key: string]: any }) => {
    return request<number>(`${DataboardService.url}/project`, {
      method: 'GET',
      ...(options || {}),
    });
  },
  // topBatch100: async (queryParam: topBatch100) => {
  topBatch100: async (options?: { [key: string]: any }) => {
    return request<Array<topBatch100>>(`${DataboardService.url}/topBatch100`, {
      method: 'GET',
      ...(options || {}),
      // params: queryParam,
    });
  },
};
