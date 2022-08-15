import { ResponseBody } from '@/app.d';
import { request } from 'umi';
import { BasicConfig, EmailConfig } from './typings';
const url = '/api/admin/config';

export async function getEmailConfig() {
    return request<EmailConfig>(`${url}` + '/email', {
        method: 'GET'
    });
}

export async function configEmail(info: EmailConfig) {
    return request<ResponseBody<any>>(`${url}` + '/email', {
        method: 'PUT',
        data: info
    });
}

export async function getBasicConfig() {
    return request<BasicConfig>(`${url}` + '/basic', {
        method: 'GET'
    });
}

export async function configBasic(info: BasicConfig) {
    return request<ResponseBody<any>>(`${url}` + '/basic', {
        method: 'PUT',
        data: info
    });
}