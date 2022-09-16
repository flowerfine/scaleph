import {request} from "@@/exports";
import {PageResponse, ResponseBody} from "@/app.d";
import {FlinkArtifact, FlinkArtifactListParam} from "@/services/dev/typings";

const url: string = '/api/flink/artifact';

export async function list(queryParam: FlinkArtifactListParam) {
  return request<PageResponse<FlinkArtifact>>(`${url}`, {
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

export async function add(row: FlinkArtifact) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'PUT',
    data: row,
  });
}

export async function update(row: FlinkArtifact) {
  return request<ResponseBody<any>>(`${url}/` + row.id, {
    method: 'POST',
    data: row,
  });
}

export async function deleteOne(row: FlinkArtifact) {
  return request<ResponseBody<any>>(`${url}/` + row.id, {
    method: 'DELETE'
  })
}

export async function deleteBatch(rows: FlinkArtifact[]) {
  const params = rows.map((row) => row.id);
  return request<ResponseBody<any>>(`${url}/batch`, {
    method: 'DELETE',
    data: params
  })
}
