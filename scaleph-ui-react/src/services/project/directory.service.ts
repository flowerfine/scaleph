import { ResponseBody } from '@/app.d';
import { request } from 'umi';
import { DiDirectory, DiDirectoryTreeNode } from './typings';

export const DirectoryService = {
  url: '/api/di/dir',

  listProjectDir: async (projectId: string) => {
    return request<DiDirectoryTreeNode[]>(`${DirectoryService.url}/` + projectId, {
      method: 'GET',
    });
  },

  addDir: async (row: DiDirectory) => {
    return request<ResponseBody<any>>(`${DirectoryService.url}`, {
      method: 'POST',
      data: row,
    });
  },
  updateDir: async (row: DiDirectory) => {
    return request<ResponseBody<any>>(`${DirectoryService.url}`, {
      method: 'PUT',
      data: row,
    });
  },

  deleteDir: async (row: DiDirectory) => {
    return request<ResponseBody<any>>(`${DirectoryService.url}/` + row.id, {
      method: 'DELETE',
    });
  },
};
