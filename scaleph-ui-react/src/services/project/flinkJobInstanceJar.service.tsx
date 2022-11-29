import {ResponseBody} from '@/app.d';
import {request} from 'umi';
import { FlinkJobForJar, FlinkJobForSeaTunnel } from './typings';

export const FLinkJobInstanceJarService = {
  url: '/api/flink/job-instance/jar',

  submit: async (row: FlinkJobForJar) => {
    return request<ResponseBody<any>>(`${FLinkJobInstanceJarService.url}`, {
      method: 'PUT',
      data: {flinkJobId: row.id},
    });
  },

  submitSeaTunnel: async (row: FlinkJobForSeaTunnel) => {
    return request<ResponseBody<any>>(`${FLinkJobInstanceJarService.url}/seatunnel`, {
      method: 'PUT',
      data: {flinkJobId: row.id},
    });
  },
};
