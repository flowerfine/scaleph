import {request} from 'umi';

export const SchedulerService = {
  url: '/api/scheduler',

  listNext5FireTime: async (crontabStr: string) => {
    return request<Date[]>(`${SchedulerService.url}/cron/next`, {
      method: 'GET',
      params: {crontabStr: crontabStr},
    });
  },
};
