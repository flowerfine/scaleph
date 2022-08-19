import { MetaDataSource, MetaDataSourceParam } from "./typings";
import { request } from 'umi';
import { Dict, PageResponse, ResponseBody } from "@/app.d";

const url: string = '/api/di/datasource';

export async function listDataSourceByPage(queryParam: MetaDataSourceParam) {
    return request<PageResponse<MetaDataSource>>(`${url}`, {
        method: 'GET',
        params: queryParam,
    }).then((res) => {
        const result = {
            data: res.records,
            total: res.total,
            pageSize: res.size,
            current: res.current,
        };
        return result;
    });
}

export async function listDataSourceByType(datasourceType: string) {
    return request<Dict[]>(`${url}/type/` + datasourceType, {
        method: 'GET'
    });
}

export async function deleteDataSourceRow(row: MetaDataSource) {
    return request<ResponseBody<any>>(`${url}/` + row.id, {
        method: 'DELETE',
    });
}

export async function deleteDataSourceBatch(rows: MetaDataSource[]) {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${url}/` + 'batch', {
        method: 'POST',
        data: { ...params },
    });
}

export async function addDataSource(row: MetaDataSource) {
    return request<ResponseBody<any>>(`${url}`, {
        method: 'POST',
        data: row,
    });
}

export async function updateDataSource(row: MetaDataSource) {
    return request<ResponseBody<any>>(`${url}`, {
        method: 'PUT',
        data: row,
    });
}

export async function testConnection(row: MetaDataSource) {
    return request<ResponseBody<any>>(`${url}/test`, {
        method: 'POST',
        data: row
    });
}

export async function showPassword(row: MetaDataSource) {
    return request<ResponseBody<any>>(`${url}/passwd/` + row.id, {
        method: 'GET',
    })
}
