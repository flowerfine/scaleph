import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PageResponse, ResponseBody} from '../../data/app.data';
import {FlinkClusterInstance, FlinkSessionClusterAddParam} from '../../data/flink.data';

@Injectable({
  providedIn: 'root',
})
export class ClusterInstanceService {
  private url = 'api/flink/cluster-instance';

  constructor(private http: HttpClient) {
  }

  list(queryParam): Observable<PageResponse<FlinkClusterInstance>> {
    const params: HttpParams = new HttpParams({fromObject: queryParam});
    return this.http.get<PageResponse<FlinkClusterInstance>>(`${this.url}`, {params});
  }

  add(row: FlinkSessionClusterAddParam): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(this.url, row);
  }

  shutdownBatch(rows: FlinkClusterInstance[]): Observable<ResponseBody<any>> {
    let params = rows.map((row) => row.id);
    return this.http.delete<ResponseBody<any>>(`${this.url}/batch`, {body: params});
  }
}
