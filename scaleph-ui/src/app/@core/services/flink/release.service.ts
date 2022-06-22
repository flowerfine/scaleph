import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Dict, PageResponse, ResponseBody } from '../../data/app.data';
import { FlinkRelease } from '../../data/flink.data';

@Injectable({
  providedIn: 'root',
})
export class ReleaseService {
  private url = 'api/flink/release';
  constructor(private http: HttpClient) {}

  list(queryParam): Observable<PageResponse<FlinkRelease>> {
    const params: HttpParams = new HttpParams({ fromObject: queryParam });
    return this.http.get<PageResponse<FlinkRelease>>(`${this.url}`, { params });
  }


}
