import { Dict, PageResponse, ResponseBody } from '@/app.d';
import { request } from 'umi';
import { DiJob, DiJobParam, DiJobStepAttr, DiJobStepAttrType } from './typings';
const url: string = '/api/di/job';

export async function selectJobById(id: number) {
    return request<DiJob>(`${url}/detail`, {
        method: 'GET',
        params: { id: id }
    });
}

export async function listJobByProject(queryParam: DiJobParam) {
    return request<PageResponse<DiJob>>(`${url}`, {
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
    });
}

export async function deleteJobRow(row: DiJob) {
    return request<ResponseBody<any>>(`${url}/` + row.id, {
        method: 'DELETE',
    });
}

export async function deleteJobBatch(rows: DiJob[]) {
    const params = rows.map((row) => row.id);
    return request<ResponseBody<any>>(`${url}/` + 'batch', {
        method: 'POST',
        data: { ...params },
    });
}

export async function addJob(row: DiJob) {
    return request<ResponseBody<any>>(`${url}`, {
        method: 'POST',
        data: row,
    });
}

export async function updateJob(row: DiJob) {
    return request<ResponseBody<any>>(`${url}`, {
        method: 'PUT',
        data: row,
    });
}

export async function saveJobDetail(job: DiJob) {
    return request<ResponseBody<any>>(`${url}/detail`, {
        method: 'POST',
        data: job
    })
}
export async function listJobAttr(jobId: number) {
    return request<{ jobId: number; jobAttr: string; jobProp: string; engineProp: string }>(`${url}/attr/` + jobId, {
        method: 'GET'
    });
}


export async function saveJobAttr(attrs: { jobId: number; jobAttr: string; jobProp: string; engineProp: string }) {
    return request<ResponseBody<any>>(`${url}/attr`, {
        method: 'POST',
        data: attrs
    })
}

export async function listStepAttr(jobId: string, stepCode: string) {
    return request<DiJobStepAttr[]>(`${url}/step`, {
        method: 'GET',
        params: { jobId: jobId, stepCode: stepCode }
    });
}

export async function saveStepAttr(step: Map<string, string>) {
    let params = {};
    for (const key of step.keys()) {
        params[key] = step.get(key);
    }
    return request<ResponseBody<any>>(`${url}/step`, {
        method: 'POST',
        data: params
    });
}

export async function listJobAttrType(stepType: string, stepName: string) {
    return request<DiJobStepAttrType[]>(`${url}/attrType`, {
        method: 'GET',
        params: { stepType: stepType, stepName: stepName }
    });
}

export async function publishJob(jobId: number) {
    return request<ResponseBody<any>>(`${url}/publish/` + jobId, {
        method: 'GET'
    });
}

export async function runJob(info: any) {
    return request<ResponseBody<any>>(`${url}/run/`, {
        method: 'POST',
        data: info
    });
}

export async function stopJob(jobId: number) {
    return request<ResponseBody<any>>(`${url}/stop/`, {
        method: 'GET',
        params: { jobId: jobId }
    });
}

export async function listResource(jobId: string) {
    return request<Dict[]>(`${url}/resource/` + jobId, {
        method: 'GET',
    });
}

export async function listNext5FireTime(crontabStr: string) {
    return request<Date[]>(`${url}/cron/next`, {
        method: 'GET',
        params: { crontabStr: crontabStr }
    })
}