import {FlinkClusterConfig, FlinkClusterConfigParam} from "@/services/dev/typings";
import {request} from "@@/exports";
import {PageResponse, ResponseBody} from "@/app.d";

const url: string = '/api/flink/cluster-config';

export async function list(queryParam: FlinkClusterConfigParam) {
  return request<PageResponse<FlinkClusterConfig>>(`${url}`, {
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

export async function add(row: FlinkClusterConfig) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'PUT',
    data: row,
  });
}

export async function update(row: FlinkClusterConfig) {
  return request<ResponseBody<any>>(`${url}/` + row.id, {
    method: 'POST',
    data: row,
  });
}

export async function deleteOne(row: FlinkClusterConfig) {
  return request<ResponseBody<any>>(`${url}/` + row.id, {
    method: 'DELETE'
  })
}

export async function deleteBatch(rows: FlinkClusterConfig[]) {
  const params = rows.map((row) => row.id);
  return request<ResponseBody<any>>(`${url}/batch`, {
    method: 'DELETE',
    data: params
  })
}
