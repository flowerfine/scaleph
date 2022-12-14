import {request} from "@@/exports";
import { WsFlinkJobInstance } from "./typings";

export const FlinkJobInstanceService = {
  url: '/api/flink/job-instance',

  getByCode: async (flinkJobCode: number) => {
    return request<WsFlinkJobInstance>(`${FlinkJobInstanceService.url}/getByCode`, {
      method: 'GET',
      params: {flinkJobCode: flinkJobCode},
    });
  },

}
