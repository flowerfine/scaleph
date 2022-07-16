import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PageResponse} from '../../data/app.data';
import {FlinkJobInstance} from "../../data/job.data";

@Injectable({
  providedIn: 'root',
})
export class JobInstanceService {
  private url = 'api/flink/job-instance';

  constructor(private http: HttpClient) {
  }

  list(queryParam): Observable<PageResponse<FlinkJobInstance>> {
    const params: HttpParams = new HttpParams({fromObject: queryParam});
    return this.http.get<PageResponse<FlinkJobInstance>>(`${this.url}`, {params});
  }
}
