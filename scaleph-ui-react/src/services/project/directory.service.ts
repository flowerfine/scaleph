import { ResponseBody } from '@/app.d';
import { request } from 'umi';
import { DiDirectory, DiDirectoryTreeNode } from './typings';

const url: string = '/api/di/dir';

export async function listProjectDir(projectId: string) {
    return request<DiDirectoryTreeNode[]>(`${url}/` + projectId, {
        method: 'GET'
    });
}

export async function addDir(row: DiDirectory) {
    return request<ResponseBody<any>>(`${url}`, {
        method: 'POST',
        data: row,
    });
}

export async function updateDir(row: DiDirectory) {
    return request<ResponseBody<any>>(`${url}`, {
        method: 'PUT',
        data: row,
    });
}

export async function deleteDir(row: DiDirectory) {
    return request<ResponseBody<any>>(`${url}/` + row.id, {
        method: 'DELETE',
    });
}