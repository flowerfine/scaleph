import {PageResponse, ResponseBody} from "@/app.d";
import {request} from "umi";
import {Jar, JarListParam, JarUploadParam} from "@/services/resource/typings";
import {USER_AUTH} from "@/constant";

const url: string = '/api/resource/jar';

export async function list(queryParam: JarListParam) {
  return request<PageResponse<Jar>>(`${url}`, {
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
  return request<ResponseBody<Jar>>(`${url}/` + id, {
    method: 'GET'
  })
}

export async function upload(uploadParam: JarUploadParam) {
  const formData = new FormData()
  formData.append("group", uploadParam.group)
  formData.append("file", uploadParam.file)
  if (uploadParam.remark) {
    formData.append("remark", uploadParam.remark)
  }
  return request<ResponseBody<any>>(`${url}/upload`, {
    method: 'POST',
    data: formData
  })
}

export async function download(row: Jar) {
  const a = document.createElement('a');
  a.href = `${url}/download/` + row.id + '?' + USER_AUTH.token + '=' + localStorage.getItem(USER_AUTH.token);
  a.download = row.fileName + '';
  a.click();
  window.URL.revokeObjectURL(url);
}

export async function deleteOne(row: Jar) {
  return request<ResponseBody<any>>(`${url}/` + row.id, {
    method: 'DELETE'
  })
}

export async function deleteBatch(rows: Jar[]) {
  const params = rows.map((row) => row.id);
  return request<ResponseBody<any>>(`${url}/batch`, {
    method: 'DELETE',
    data: params
  })
}


