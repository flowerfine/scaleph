import { Dict, PageResponse, ResponseBody } from '@/app.d';
import { request } from 'umi';
import { SysDictData, SysDictDataParam } from './typings';

const url = '/api/admin/dict/data';

export async function listDictDataByPage(queryParam: SysDictDataParam) {
  return request<PageResponse<SysDictData>>(`${url}`, {
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

export async function deleteDictDataRow(row: SysDictData) {
  return request<ResponseBody<any>>(`${url}/` + row.id, {
    method: 'DELETE',
  });
}

export async function deleteDictDataBatch(rows: SysDictData[]) {
  const params = rows.map((row) => row.id);
  return request<ResponseBody<any>>(`${url}/` + 'batch', {
    method: 'POST',
    data: { ...params },
  });
}

export async function addDictData(row: SysDictData) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'POST',
    data: row,
  });
}

export async function updateDictData(row: SysDictData) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'PUT',
    data: row,
  });
}

export async function listDictDataByType(dictTypeCode: string) {
  return request<Dict[]>(`${url}/` + dictTypeCode, {
    method: 'GET',
  });
}
