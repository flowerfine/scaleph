import {request} from "@@/exports";
import {PageResponse, ResponseBody} from "@/app.d";
import {FlinkArtifact, FlinkArtifactListParam, FlinkArtifactUploadParam} from "@/services/dev/typings";
import {USER_AUTH} from "@/constant";

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

export async function upload(uploadParam: FlinkArtifactUploadParam) {
  const formData = new FormData()
  formData.append("name", uploadParam.name)
  formData.append("entryClass", uploadParam.entryClass)
  formData.append("file", uploadParam.file)
  if (uploadParam.remark) {
    formData.append("remark", uploadParam.remark)
  }
  return request<ResponseBody<any>>(`${url}`, {
    method: 'POST',
    data: formData
  })
}

export async function download(row: FlinkArtifact) {
  const a = document.createElement('a');
  a.href = `${url}/` + row.id + '?' + USER_AUTH.token + '=' + localStorage.getItem(USER_AUTH.token);
  a.download = row.name + '';
  a.click();
  window.URL.revokeObjectURL(url);
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





