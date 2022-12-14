import { PageResponse } from '@/app.d';
import { request } from '@@/exports';
import { WsFlinkJobLog, WsFlinkJobLogListParam } from './typings';

export const FlinkJobLogService = {
  url: '/api/flink/job-log',

  list: async (queryParam: WsFlinkJobLogListParam) => {
    return request<PageResponse<WsFlinkJobLog>>(`${FlinkJobLogService.url}`, {
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
};
