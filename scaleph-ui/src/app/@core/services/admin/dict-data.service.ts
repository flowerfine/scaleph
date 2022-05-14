import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DictData } from '../../data/admin.data';
import { Dict, PageResponse, ResponseBody } from '../../data/app.data';

@Injectable({
  providedIn: 'root',
})
export class DictDataService {
  private url = 'api/admin/dict/data';
  constructor(private http: HttpClient) {}

  listByPage(queryParam): Observable<PageResponse<DictData>> {
    const params: HttpParams = new HttpParams({ fromObject: queryParam });
    return this.http.get<PageResponse<DictData>>(`${this.url}`, { params });
  }

  delete(row: DictData): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteBatch(rows: DictData[]): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + 'batch';
    let params = rows.map((row) => row.id);
    return this.http.post<ResponseBody<any>>(delUrl, { ...params });
  }

  add(row: DictData): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(this.url, row);
  }

  update(row: DictData): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(this.url, row);
  }

  listByType(dictTypeCode: string): Observable<Dict[]> {
    const listUrl = `${this.url}/` + dictTypeCode;
    return this.http.get<Dict[]>(listUrl);
  }
}
