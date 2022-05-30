import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SysDictType } from '../../data/admin.data';
import { Dict, PageResponse, ResponseBody } from '../../data/app.data';

@Injectable({
  providedIn: 'root',
})
export class SysDictTypeService {
  private url = 'api/admin/dict/type';
  constructor(private http: HttpClient) {}

  listByPage(queryParam): Observable<PageResponse<SysDictType>> {
    const params: HttpParams = new HttpParams({ fromObject: queryParam });
    return this.http.get<PageResponse<SysDictType>>(`${this.url}`, { params });
  }

  listAll(): Observable<Dict[]> {
    return this.http.get<Dict[]>(`${this.url}/all`);
  }

  delete(row: SysDictType): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteBatch(rows: SysDictType[]): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + 'batch';
    let params = rows.map((row) => row.id);
    return this.http.post<ResponseBody<any>>(delUrl, { ...params });
  }

  add(row: SysDictType): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(this.url, row);
  }

  update(row: SysDictType): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(this.url, row);
  }
}
