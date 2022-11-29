import {request} from "@@/exports";
import { FlinkJobInstance } from "./typings";

export const FlinkJobInstanceService = {
  url: '/api/flink/job-instance',

  getByCode: async (flinkJobCode: number) => {
    return request<FlinkJobInstance>(`${FlinkJobInstanceService.url}/getByCode`, {
      method: 'GET',
      params: {flinkJobCode: flinkJobCode},
    });
  },

}
