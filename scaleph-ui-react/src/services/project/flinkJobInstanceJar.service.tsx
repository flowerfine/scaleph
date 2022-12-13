import {ResponseBody} from '@/app.d';
import {request} from 'umi';
import { WsFlinkJobForJar, WsFlinkJobForSeaTunnel } from './typings';

export const FLinkJobInstanceJarService = {
  url: '/api/flink/job-instance/jar',

  submit: async (row: WsFlinkJobForJar) => {
    return request<ResponseBody<any>>(`${FLinkJobInstanceJarService.url}`, {
      method: 'PUT',
      data: {flinkJobId: row.id},
    });
  },

  submitSeaTunnel: async (row: WsFlinkJobForSeaTunnel) => {
    return request<ResponseBody<any>>(`${FLinkJobInstanceJarService.url}/seatunnel`, {
      method: 'PUT',
      data: {flinkJobId: row.id},
    });
  },
};
