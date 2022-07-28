import { Dict, PageResponse, ResponseBody } from '@/app.d';
import { request } from 'umi';
import { SysDictType, SysDictTypeParam } from './typings';

const url = '/api/admin/dict/type';

export async function listDictTypeByPage(queryParam: SysDictTypeParam) {
  return request<PageResponse<SysDictType>>(`${url}`, {
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

export async function listAllDictType() {
  return request<Dict[]>(`${url}/all`, {
    method: 'GET',
  });
}

export async function deleteDictTypeRow(row: SysDictType) {
  return request<ResponseBody<any>>(`${url}/` + row.id, {
    method: 'DELETE',
  });
}

export async function deleteDictTypeBatch(rows: SysDictType[]) {
  const params = rows.map((row) => row.id);
  return request<ResponseBody<any>>(`${url}/` + 'batch', {
    method: 'POST',
    data: { ...params },
  });
}

export async function addDictType(row: SysDictType) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'POST',
    data: row,
  });
}

export async function updateDictType(row: SysDictType) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'PUT',
    data: row,
  });
}
