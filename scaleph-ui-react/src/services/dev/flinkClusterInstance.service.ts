import {FlinkClusterInstance, FlinkClusterInstanceParam, FlinkSessionClusterNewParam} from "@/services/dev/typings";
import {request} from "@@/exports";
import {PageResponse, ResponseBody} from "@/app.d";

const url: string = '/api/flink/cluster-instance';

export async function list(queryParam: FlinkClusterInstanceParam) {
  return request<PageResponse<FlinkClusterInstance>>(`${url}`, {
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

export async function newSession(row: FlinkSessionClusterNewParam) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'PUT',
    data: row,
  })
}


export async function shutdown(row: FlinkClusterInstance) {
  return request<ResponseBody<any>>(`${url}/` + row.id, {
    method: 'DELETE'
  })
}

export async function shutdownBatch(rows: FlinkClusterInstance[]) {
  const params = rows.map((row) => row.id);
  return request<ResponseBody<any>>(`${url}/batch`, {
    method: 'DELETE',
    data: params
  })
}
