import {ResponseBody} from '@/app.d';
import {request} from 'umi';
import {
  WorkflowSchedule,
  WorkflowScheduleAddParam,
  WorkflowScheduleListParam,
  WorkflowScheduleUpdateParam
} from "@/services/workflow/typings";

export const SchedulerService = {
  url: '/api/scheduler',

  list: async (param: WorkflowScheduleListParam) => {
    return request<ResponseBody<Array<WorkflowSchedule>>>(`${SchedulerService.url}`, {
      method: 'GET',
      params: param,
    });
  },

  add: async (param: WorkflowScheduleAddParam) => {
    return request<ResponseBody<any>>(`${SchedulerService.url}`, {
      method: 'PUT',
      data: param,
    });
  },

  update: async (id: number, param: WorkflowScheduleUpdateParam) => {
    return request<ResponseBody<any>>(`${SchedulerService.url}/${id}`, {
      method: 'POST',
      data: param,
    });
  },

  deleteOne: async (row: WorkflowSchedule) => {
    return request<ResponseBody<any>>(`${SchedulerService.url}/${row.id}`, {
      method: 'DELETE',
    });
  },

  deleteBatch: async (rows: WorkflowSchedule[]) => {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${SchedulerService.url}/batch`, {
      method: 'DELETE',
      data: params,
    });
  },

  enable: async (id: number) => {
    return request<ResponseBody<any>>(`${SchedulerService.url}/${id}/enable`, {
      method: 'POST',
    });
  },

  disable: async (id: number) => {
    return request<ResponseBody<any>>(`${SchedulerService.url}/${id}/disable`, {
      method: 'POST',
    });
  },

  listNext5FireTime: async (crontabStr: string) => {
    return request<Date[]>(`${SchedulerService.url}/cron/next`, {
      method: 'GET',
      params: {crontabStr: crontabStr},
    });
  },
};
