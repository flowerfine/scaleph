import {SeaTunnelRelease, SeaTunnelReleaseListParam, SeaTunnelReleaseUploadParam} from "@/services/resource/typings";
import {request} from "@@/exports";
import {PageResponse, ResponseBody} from "@/app.d";
import {USER_AUTH} from "@/constant";

const url: string = '/api/resource/seatunnel-release';

export async function list(queryParam: SeaTunnelReleaseListParam) {
  return request<PageResponse<SeaTunnelRelease>>(`${url}`, {
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

export async function selectOne(id: number) {
  return request<ResponseBody<SeaTunnelRelease>>(`${url}/` + id, {
    method: 'GET'
  })
}

export async function upload(uploadParam: SeaTunnelReleaseUploadParam) {
  const formData = new FormData()
  formData.append("version", uploadParam.version)
  formData.append("file", uploadParam.file)
  if (uploadParam.remark) {
    formData.append("remark", uploadParam.remark)
  }
  return request<ResponseBody<any>>(`${url}/upload`, {
    method: 'POST',
    data: formData
  })
}

export async function download(row: SeaTunnelRelease) {
  const a = document.createElement('a');
  a.href = `${url}/download/` + row.id + '?' + USER_AUTH.token + '=' + localStorage.getItem(USER_AUTH.token);
  a.download = row.fileName + '';
  a.click();
  window.URL.revokeObjectURL(url);
}

export async function deleteOne(row: SeaTunnelRelease) {
  return request<ResponseBody<any>>(`${url}/` + row.id, {
    method: 'DELETE'
  })
}

export async function deleteBatch(rows: SeaTunnelRelease[]) {
  const params = rows.map((row) => row.id);
  return request<ResponseBody<any>>(`${url}/batch`, {
    method: 'DELETE',
    data: params
  })
}
