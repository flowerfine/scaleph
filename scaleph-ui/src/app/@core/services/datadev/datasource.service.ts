import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Dict, PageResponse, ResponseBody } from '../../data/app.data';
import { MetaDataSource } from '../../data/datadev.data';

@Injectable({
  providedIn: 'root',
})
export class DataSourceService {
  private url = 'api/datadev/datasource';
  constructor(private http: HttpClient) {}

  listByPage(queryParam): Observable<PageResponse<MetaDataSource>> {
    const params: HttpParams = new HttpParams({ fromObject: queryParam });
    return this.http.get<PageResponse<MetaDataSource>>(`${this.url}`, { params });
  }

  listByType(datasourceType: string): Observable<Dict[]> {
    return this.http.get<Dict[]>(`${this.url}/type/` + datasourceType);
  }

  delete(row: MetaDataSource): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteBatch(rows: MetaDataSource[]): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + 'batch';
    let params = rows.map((row) => row.id);
    return this.http.post<ResponseBody<any>>(delUrl, { ...params });
  }

  add(row: MetaDataSource): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(this.url, row);
  }

  update(row: MetaDataSource): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(this.url, row);
  }

  testConnection(row: MetaDataSource): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(`${this.url}/test`, row);
  }

  showPassword(row: MetaDataSource): Observable<ResponseBody<any>> {
    // passwd/{id}
    return this.http.get<ResponseBody<any>>(`${this.url}/passwd/` + row.id);
  }
}
