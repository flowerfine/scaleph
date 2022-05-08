import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Dict, PageResponse, ResponseBody } from '../data/app.data';
import { DiJob, DiJobStepAttr, DiJobStepAttrType } from '../data/studio.data';

@Injectable({
  providedIn: 'root',
})
export class DiJobService {
  private url = 'api/di/job';
  constructor(private http: HttpClient) {}

  selectById(id: number): Observable<DiJob> {
    return this.http.get<DiJob>(`${this.url}/detail?id=` + id);
  }

  listByPage(queryParam): Observable<PageResponse<DiJob>> {
    const params: HttpParams = new HttpParams({ fromObject: queryParam });
    return this.http.get<PageResponse<DiJob>>(`${this.url}`, { params });
  }

  delete(row: DiJob): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteBatch(rows: DiJob[]): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + 'batch';
    let params = rows.map((row) => row.id);
    return this.http.post<ResponseBody<any>>(delUrl, { ...params });
  }

  add(row: DiJob): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(this.url, row);
  }

  update(row: DiJob): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(this.url, row);
  }

  saveJobDetail(job: DiJob): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(`${this.url}/detail`, job);
  }

  listJobAttr(jobId: number): Observable<{ jobId: number; jobAttr: string; jobProp: string; engineProp: string }> {
    return this.http.get<{ jobId: number; jobAttr: string; jobProp: string; engineProp: string }>(`${this.url}/attr/` + jobId);
  }

  saveJobAttr(attrs: { jobId: number; jobAttr: string; jobProp: string; engineProp: string }): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(`${this.url}/attr`, attrs);
  }

  listStepAttr(jobId: string, stepCode: string): Observable<DiJobStepAttr[]> {
    const params: HttpParams = new HttpParams().set('jobId', jobId).set('stepCode', stepCode);
    return this.http.get<DiJobStepAttr[]>(`${this.url}/step`, { params });
  }

  saveStepAttr(step: Map<string, string>): Observable<ResponseBody<any>> {
    let params = {};
    for (const key of step.keys()) {
      params[key] = step.get(key);
    }
    return this.http.post<ResponseBody<any>>(`${this.url}/step`, params);
  }

  listJobAttrType(stepType: string, stepName: string): Observable<DiJobStepAttrType[]> {
    const params: HttpParams = new HttpParams().set('stepType', stepType).set('stepName', stepName);
    return this.http.get<DiJobStepAttrType[]>(`${this.url}/attrType`, { params });
  }

  publishJob(jobId: number): Observable<ResponseBody<any>> {
    return this.http.get<ResponseBody<any>>(`${this.url}/publish/` + jobId);
  }

  run(info: any): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(`${this.url}/run/`, info);
  }

  stop(jobId: number): Observable<ResponseBody<any>> {
    return this.http.get<ResponseBody<any>>(`${this.url}/stop?jobId=` + jobId);
  }

  listResource(jobId: string): Observable<Dict[]> {
    return this.http.get<Dict[]>(`${this.url}/resource/` + jobId);
  }
}
