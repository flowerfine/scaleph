import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PageResponse, ResponseBody} from '../../data/app.data';
import {FlinkJobConfig} from "../../data/job.data";

@Injectable({
  providedIn: 'root',
})
export class JobConfigService {
  private url = 'api/flink/job-config';

  constructor(private http: HttpClient) {
  }

  list(queryParam): Observable<PageResponse<FlinkJobConfig>> {
    const params: HttpParams = new HttpParams({fromObject: queryParam});
    return this.http.get<PageResponse<FlinkJobConfig>>(`${this.url}`, {params});
  }

  add(row: FlinkJobConfig): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(this.url, row);
  }

  update(row: FlinkJobConfig): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(`${this.url}/` + row.id, row);
  }

  deleteBatch(rows: FlinkJobConfig[]): Observable<ResponseBody<any>> {
    let params = rows.map((row) => row.id);
    return this.http.delete<ResponseBody<any>>(`${this.url}/batch`, {body: params});
  }
}
