import {PageResponse} from '@/app.d';
import {FlinkJobLog, FlinkJobLogListParam} from "@/pages/DEV/Job/typings";
import {request} from "@@/exports";

export const FlinkJobLogService = {
  url: '/api/flink/job-log',

  list: async (queryParam: FlinkJobLogListParam) => {
    return request<PageResponse<FlinkJobLog>>(`${FlinkJobLogService.url}`, {
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

}
