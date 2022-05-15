import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DiJobLog } from '../../data/opscenter.data';

@Injectable({
  providedIn: 'root',
})
export class DataBoardService {
  private url = 'api/studio/databoard';

  constructor(private http: HttpClient) {}

  countProject(): Observable<number> {
    return this.http.get<number>(`${this.url}` + '/project');
  }

  countCluster(): Observable<number> {
    return this.http.get<number>(`${this.url}` + '/cluster');
  }

  countJob(jobType: string): Observable<number> {
    return this.http.get<number>(`${this.url}` + '/job?jobType=' + jobType);
  }

  listBatchTop100(): Observable<DiJobLog[]> {
    return this.http.get<DiJobLog[]>(`${this.url}` + '/topBatch100');
  }

  groupRealtimeJobRuntimeStatus(): Observable<any> {
    return this.http.get<any>(`${this.url}` + '/realtimeJob');
  }
}
