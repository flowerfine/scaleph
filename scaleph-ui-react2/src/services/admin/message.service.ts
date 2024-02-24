import { PageResponse, ResponseBody } from '@/app.d';
import { request } from 'umi';
import { LogMessage, LogMessageParam } from './typings';

export const MessageService = {
  url: '/api/msg',

  listMessageByPage: async (queryParam: LogMessageParam) => {
    return request<PageResponse<LogMessage>>(`${MessageService.url}`, {
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

  updateMessage: async (row: LogMessage) => {
    return request<ResponseBody<any>>(`${MessageService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  countUnReadMessage: async () => {
    return request<number>(`${MessageService.url}/count`, { method: 'GET' });
  },

  readAllMessage: async () => {
    return request<ResponseBody<any>>(`${MessageService.url}/readAll`);
  },
};
