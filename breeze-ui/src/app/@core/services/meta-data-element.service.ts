import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PageResponse, ResponseBody } from '../data/app.data';
import { MetaDataElement } from '../data/meta.data';

@Injectable({
  providedIn: 'root',
})
export class MetaDataElementService {
  private url = 'api/meta/element';
  constructor(private http: HttpClient) {}

  listByPage(queryParam): Observable<PageResponse<MetaDataElement>> {
    const params: HttpParams = new HttpParams({ fromObject: queryParam });
    return this.http.get<PageResponse<MetaDataElement>>(`${this.url}`, { params });
  }

  delete(row: MetaDataElement): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteBatch(rows: MetaDataElement[]): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + 'batch';
    let params = rows.map((row) => row.id);
    return this.http.post<ResponseBody<any>>(delUrl, { ...params });
  }

  add(row: MetaDataElement): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(this.url, row);
  }

  update(row: MetaDataElement): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(this.url, row);
  }
}
