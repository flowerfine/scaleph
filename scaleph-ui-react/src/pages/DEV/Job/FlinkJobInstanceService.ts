import {FlinkJobInstance} from "@/pages/DEV/Job/typings";
import {request} from "@@/exports";

export const FlinkJobInstanceService = {
  url: '/api/flink/job-instance',

  getByCode: async (flinkJobCode: number) => {
    return request<FlinkJobInstance>(`${FlinkJobInstanceService.url}/getByCode`, {
      method: 'GET',
      params: {flinkJobCode: flinkJobCode},
    });
  },

}
