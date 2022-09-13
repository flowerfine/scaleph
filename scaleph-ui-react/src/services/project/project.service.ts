import { Dict, PageResponse, ResponseBody } from '@/app.d';
import { request } from 'umi';
import { DiProject, DiProjectParam } from './typings';

const url: string = '/api/di/project';

export async function listProjectByPage(queryParam: DiProjectParam) {
  return request<PageResponse<DiProject>>(`${url}`, {
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

export async function listAllProject() {
  return request<Dict[]>(`${url}/all`, { method: 'GET' });
}

export async function deleteProjectRow(row: DiProject) {
  return request<ResponseBody<any>>(`${url}/` + row.id, {
    method: 'DELETE',
  });
}

export async function deleteProjectBatch(rows: DiProject[]) {
  const params = rows.map((row) => row.id);
  return request<ResponseBody<any>>(`${url}/` + 'batch', {
    method: 'POST',
    data: { ...params },
  });
}

export async function addProject(row: DiProject) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'POST',
    data: row,
  });
}

export async function updateProject(row: DiProject) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'PUT',
    data: row,
  });
}
