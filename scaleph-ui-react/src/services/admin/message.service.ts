import { PageResponse, ResponseBody } from '@/app.d';
import { request } from 'umi';
import { LogMessage, LogMessageParam } from './typings';

const url = '/api/msg';

export async function listMessageByPage(queryParam: LogMessageParam) {
  return request<PageResponse<LogMessage>>(`${url}`, {
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
}

export async function updateMessage(row: LogMessage) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'PUT',
    data: row,
  });
}

export async function countUnReadMessage() {
  return request<number>(`${url}/count`, { method: 'GET' });
}

export async function readAllMessage() {
  return request<ResponseBody<any>>(`${url}/readAll`);
}
