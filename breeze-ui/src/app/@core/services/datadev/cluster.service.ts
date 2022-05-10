import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Dict, PageResponse, ResponseBody } from '../../data/app.data';
import { DiClusterConfig } from '../../data/datadev.data';

@Injectable({
  providedIn: 'root',
})
export class ClusterService {
  private url = 'api/datadev/cluster';
  constructor(private http: HttpClient) {}

  listByPage(queryParam): Observable<PageResponse<DiClusterConfig>> {
    const params: HttpParams = new HttpParams({ fromObject: queryParam });
    return this.http.get<PageResponse<DiClusterConfig>>(`${this.url}`, { params });
  }

  listAll(): Observable<Dict[]> {
    return this.http.get<Dict[]>(`${this.url}/all`);
  }

  delete(row: DiClusterConfig): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteBatch(rows: DiClusterConfig[]): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + 'batch';
    let params = rows.map((row) => row.id);
    return this.http.post<ResponseBody<any>>(delUrl, { ...params });
  }

  add(row: DiClusterConfig): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(this.url, row);
  }

  update(row: DiClusterConfig): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(this.url, row);
  }
}
