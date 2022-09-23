import { ResponseBody } from '@/app.d';
import { FlinkJobConfigJar } from '@/services/dev/typings';
import { request } from '@@/exports';

export const FLinkJobInstanceJarService = {
  url: '/api/flink/job-instance/jar',
  submit: async (row: FlinkJobConfigJar) => {
    return request<ResponseBody<any>>(`${FLinkJobInstanceJarService.url}`, {
      method: 'PUT',
      data: { flinkJobConfigJarId: row.id },
    });
  },
};
