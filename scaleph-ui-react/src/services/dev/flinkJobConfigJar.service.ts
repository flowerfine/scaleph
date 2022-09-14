import {FlinkJobConfigJar, FlinkJobConfigJarParam} from "@/services/dev/typings";
import {request} from "@@/exports";
import {PageResponse, ResponseBody} from "@/app.d";

const url: string = '/api/flink/job-config/jar';

export async function list(queryParam: FlinkJobConfigJarParam) {
  return request<PageResponse<FlinkJobConfigJar>>(`${url}`, {
    method: 'GET',
    params: queryParam
  }).then((res) => {
    const result = {
      data: res.records,
      total: res.total,
      pageSize: res.size,
      current: res.current,
    };
    return result;
  })
}

export async function add(row: FlinkJobConfigJar) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'PUT',
    data: row,
  });
}

export async function update(row: FlinkJobConfigJar) {
  return request<ResponseBody<any>>(`${url}/` + row.id, {
    method: 'POST',
    data: row,
  });
}

export async function deleteOne(row: FlinkJobConfigJar) {
  return request<ResponseBody<any>>(`${url}/` + row.id, {
    method: 'DELETE'
  })
}

export async function deleteBatch(rows: FlinkJobConfigJar[]) {
  const params = rows.map((row) => row.id);
  return request<ResponseBody<any>>(`${url}/batch`, {
    method: 'DELETE',
    data: params
  })
}
