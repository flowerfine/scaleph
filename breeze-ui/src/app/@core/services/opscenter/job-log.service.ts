import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PageResponse } from '../../data/app.data';
import { DiJobLog } from '../../data/opscenter.data';

@Injectable({
  providedIn: 'root',
})
export class JobLogService {
  private url = 'api/opscenter/log';

  constructor(private http: HttpClient) {}

  listBatchByPage(queryParam): Observable<PageResponse<DiJobLog>> {
    return this.http.post<PageResponse<DiJobLog>>(`${this.url}/batch`, queryParam);
  }

  listRealtimeByPage(queryParam): Observable<PageResponse<DiJobLog>> {
    return this.http.post<PageResponse<DiJobLog>>(`${this.url}/realtime`, queryParam);
  }
}
