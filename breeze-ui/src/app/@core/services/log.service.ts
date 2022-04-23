import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginLog } from '../data/admin.data';
import { PageResponse } from '../data/app.data';

@Injectable({
  providedIn: 'root',
})
export class LogService {
  private url = 'api/admin/log';
  constructor(private http: HttpClient) {}
  listByPage(queryParam): Observable<PageResponse<LoginLog>> {
    const params: HttpParams = new HttpParams({ fromObject: queryParam });
    return this.http.get<PageResponse<LoginLog>>(`${this.url}` + '/login', { params });
  }
}
