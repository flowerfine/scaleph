import {PageResponse, ResponseBody} from "@/app.d";
import {request} from "umi";
import {Jar, JarListParam, JarUploadParam} from "@/services/resource/typings";

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
  formData.append("remark", uploadParam.remark)
  return request<PageResponse<ResponseBody<any>>>(`${url}/upload`, {
    method: 'POST',
    data: formData
  })
}

export async function deleteOne(row: Jar) {
  return request<ResponseBody<Jar>>(`${url}/` + row.id, {
    method: 'DELETE'
  })
}

export async function deleteBatch(rows: Jar[]) {
  const params = rows.map((row) => row.id);
  return request<ResponseBody<Jar>>(`${url}/batch`, {
    method: 'DELETE',
    body: params
  })
}


