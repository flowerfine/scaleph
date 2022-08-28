import { Dict, ResponseBody, PageResponse } from "@/app.d";
import { request } from "umi";
import { DiClusterConfig, DiClusterConfigParam } from "./typings";

const url: string = '/api/di/cluster';

export async function listClusterByPage(queryParam: DiClusterConfigParam) {
    return request<PageResponse<DiClusterConfig>>(`${url}`, {
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

export async function listAllCluster() {
    return request<Dict[]>(`${url}/all`, {
        method: 'GET'
    })
}

export async function deleteClusterRow(row: DiClusterConfig) {
    return request<ResponseBody<any>>(`${url}/` + row.id, {
        method: 'DELETE',
    });
}

export async function deleteClusterBatch(rows: DiClusterConfig[]) {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${url}/` + 'batch', {
        method: 'POST',
        data: { ...params },
    });
}

export async function addCluster(row: DiClusterConfig) {
    return request<ResponseBody<any>>(`${url}`, {
        method: 'POST',
        data: row,
    });
}

export async function updateCluster(row: DiClusterConfig) {
    return request<ResponseBody<any>>(`${url}`, {
        method: 'PUT',
        data: row,
    });
}