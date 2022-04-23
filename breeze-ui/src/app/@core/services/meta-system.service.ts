import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PageResponse, ResponseBody } from '../data/app.data';
import { MetaSystem } from '../data/meta.data';

@Injectable({
  providedIn: 'root',
})
export class MetaSystemService {
  private url = 'api/meta/system';
  constructor(private http: HttpClient) {}

  listByPage(queryParam): Observable<PageResponse<MetaSystem>> {
    const params: HttpParams = new HttpParams({ fromObject: queryParam });
    return this.http.get<PageResponse<MetaSystem>>(`${this.url}`, { params });
  }

  delete(row: MetaSystem): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteBatch(rows: MetaSystem[]): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + 'batch';
    let params = rows.map((row) => row.id);
    return this.http.post<ResponseBody<any>>(delUrl, { ...params });
  }

  add(row: MetaSystem): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(this.url, row);
  }

  update(row: MetaSystem): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(this.url, row);
  }
}
