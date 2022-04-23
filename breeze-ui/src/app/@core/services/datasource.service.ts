import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Dict, PageResponse, ResponseBody } from '../data/app.data';
import { DataSourceMeta } from '../data/meta.data';

@Injectable({
  providedIn: 'root',
})
export class DataSourceService {
  private url = 'api/meta/datasource';
  constructor(private http: HttpClient) {}

  listByPage(queryParam): Observable<PageResponse<DataSourceMeta>> {
    const params: HttpParams = new HttpParams({ fromObject: queryParam });
    return this.http.get<PageResponse<DataSourceMeta>>(`${this.url}`, { params });
  }

  listByType(dataSourceType: string): Observable<Dict[]> {
    return this.http.get<Dict[]>(`${this.url}/type/` + dataSourceType);
  }

  delete(row: DataSourceMeta): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteBatch(rows: DataSourceMeta[]): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + 'batch';
    let params = rows.map((row) => row.id);
    return this.http.post<ResponseBody<any>>(delUrl, { ...params });
  }

  add(row: DataSourceMeta): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(this.url, row);
  }

  update(row: DataSourceMeta): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(this.url, row);
  }

  testConnection(row: DataSourceMeta): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(`${this.url}/test`, row);
  }

  showPassword(row: DataSourceMeta): Observable<ResponseBody<any>> {
    // passwd/{id}
    return this.http.get<ResponseBody<any>>(`${this.url}/passwd/` + row.id);
  }
}
