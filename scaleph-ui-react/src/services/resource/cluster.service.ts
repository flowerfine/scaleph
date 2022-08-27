import { Dict, ResponseBody, PageResponse } from "@/app.d";
import { request } from "umi";
import { DiResourceFile, DiResourceFileParam } from "./typings";

const url: string = '/api/datadev/resource';

export async function listResourceFileByPage(queryParam: DiResourceFileParam) {
    return request<PageResponse<DiResourceFile>>(`${url}`, {
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

export async function listResourceFileByProjectId(projectId: string) {
    return request<Dict[]>(`${url}/project`, {
        method: 'GET',
        params: { projectId: projectId }
    })
}

export async function deleteResourceRow(row: DiResourceFile) {
    return request<ResponseBody<any>>(`${url}/` + row.id, {
        method: 'DELETE',
    });
}

export async function deleteResourceBatch(rows: DiResourceFile[]) {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${url}/` + 'batch', {
        method: 'POST',
        data: { ...params },
    });
}

export async function addResourceFile(row: DiResourceFile) {
    return request<ResponseBody<any>>(`${url}`, {
        method: 'POST',
        data: row,
    });
}


export async function downloadResourceFile(row: DiResourceFile) {
    return request(`${url}/download`, {
        method: 'GET',
        params: {
            projectId: row.projectId,
            fileName: row.fileName
        }
    });
}