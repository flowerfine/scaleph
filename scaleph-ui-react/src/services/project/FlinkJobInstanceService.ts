import { PageResponse, ResponseBody } from '@/app.d';
import { request } from '@@/exports';
import {
  WsFlinkCheckPoint,
  WsFlinkCheckPointParam,
  WsFlinkJob,
  WsFlinkJobInstance,
} from './typings';

export const FlinkJobInstanceService = {
  url: '/api/flink/job-instance',

  getByCode: async (flinkJobCode: number) => {
    return request<WsFlinkJobInstance>(`${FlinkJobInstanceService.url}/getByCode`, {
      method: 'GET',
      params: { flinkJobCode: flinkJobCode },
    });
  },

  listCheckPoints: async (param: WsFlinkCheckPointParam) => {
    return request<PageResponse<WsFlinkCheckPoint>>(`${FlinkJobInstanceService.url}/checkpoints`, {
      method: 'GET',
      params: param,
    });
  },
  submit: async (row: WsFlinkJob) => {
    return request<ResponseBody<any>>(`${FlinkJobInstanceService.url}/submit`, {
      method: 'PUT',
      data: { flinkJobId: row.id },
    });
  },
};
