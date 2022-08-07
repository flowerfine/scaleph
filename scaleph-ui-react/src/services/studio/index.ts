// @ts-ignore
import { request } from '@umijs/max';
const url = '/api/studio/databoard';

/** 查询集群数量 GET /api/studio/databoard/cluster */
export async function cluster(options?: { [key: string]: any }) {
  return request<number>(`${url}/cluster`, {
    method: 'GET',
    ...(options || {}),
  });
}
/** 查询集群数量 GET /api/studio/databoard/job */
export async function job(params: Studio.JobParams, options?: { [key: string]: any }) {
  return request<number>(`${url}/job`, {
    method: 'GET',
    params: params,
    ...(options || {}),
  });
}
