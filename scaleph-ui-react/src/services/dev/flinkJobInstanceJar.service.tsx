import {ResponseBody} from '@/app.d';
import {request} from '@@/exports';
import {FlinkJobForJar} from "@/pages/DEV/Job/typings";

export const FLinkJobInstanceJarService = {
  url: '/api/flink/job-instance/jar',
  submit: async (row: FlinkJobForJar) => {
    return request<ResponseBody<any>>(`${FLinkJobInstanceJarService.url}`, {
      method: 'PUT',
      data: {flinkJobId: row.id},
    });
  },
};
