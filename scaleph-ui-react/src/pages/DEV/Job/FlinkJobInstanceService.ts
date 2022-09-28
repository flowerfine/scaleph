import {PageResponse} from '@/app.d';
import {FlinkJobInstance, FlinkJobInstanceListParam} from "@/pages/DEV/Job/typings";
import {request} from "@@/exports";

export const FlinkJobInstanceService = {
  url: '/api/flink/job-instance',

  list: async (queryParam: FlinkJobInstanceListParam) => {
    return request<PageResponse<FlinkJobInstance>>(`${FlinkJobInstanceService.url}`, {
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
