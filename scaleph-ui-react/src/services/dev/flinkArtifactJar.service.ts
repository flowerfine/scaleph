import {request} from "@@/exports";
import {PageResponse, ResponseBody} from "@/app.d";
import {FlinkArtifactJar, FlinkArtifactJarListParam, FlinkArtifactJarUploadParam} from "@/services/dev/typings";
import {USER_AUTH} from "@/constant";

const url: string = '/api/flink/artifact/jar';

export async function list(queryParam: FlinkArtifactJarListParam) {
  return request<PageResponse<FlinkArtifactJar>>(`${url}`, {
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
  return request<FlinkArtifactJar>(`${url}/` + id, {
    method: 'GET'
  });
}

export async function upload(uploadParam: FlinkArtifactJarUploadParam) {
  const formData = new FormData()
  formData.append("flinkArtifactId", uploadParam.flinkArtifactId)
  if (uploadParam.version) {
    formData.append("version", uploadParam.version)
  }
  formData.append("flinkVersion", uploadParam.flinkVersion)
  formData.append("entryClass", uploadParam.entryClass)
  formData.append("file", uploadParam.file)
  return request<ResponseBody<any>>(`${url}`, {
    method: 'POST',
    data: formData
  })
}

export async function download(row: FlinkArtifactJar) {
  const a = document.createElement('a');
  a.href = `${url}/download/` + row.id + '?' + USER_AUTH.token + '=' + localStorage.getItem(USER_AUTH.token);
  a.download = row.fileName + '';
  a.click();
  window.URL.revokeObjectURL(url);
}


