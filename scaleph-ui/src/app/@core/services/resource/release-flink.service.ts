import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PageResponse, ResponseBody} from '../../data/app.data';
import {FlinkRelease, FlinkReleaseUploadParam} from '../../data/flink.data';
import {ReleaseFlink, ReleaseFlinkUploadParam} from "../../data/resource.data";

@Injectable({
  providedIn: 'root',
})
export class ReleaseFlinkService {
  private url = 'api/resource/release/flink';

  constructor(private http: HttpClient) {
  }

  list(queryParam): Observable<PageResponse<ReleaseFlink>> {
    const params: HttpParams = new HttpParams({fromObject: queryParam});
    return this.http.get<PageResponse<ReleaseFlink>>(`${this.url}`, {params});
  }

  selectOne(id): Observable<ResponseBody<ReleaseFlink>> {
    return this.http.get<ResponseBody<ReleaseFlink>>(`${this.url}/` + id);
  }

  upload(uploadParam: ReleaseFlinkUploadParam): Observable<ResponseBody<any>> {
    const params: FormData = new FormData();
    params.append("version", uploadParam.version)
    params.append("file", uploadParam.file)
    params.append("remark", uploadParam.remark)
    return this.http.post<ResponseBody<any>>(`${this.url}/upload`, params);
  }

  delete(row: ReleaseFlink): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteBatch(rows: ReleaseFlink[]): Observable<ResponseBody<any>> {
    let params = rows.map((row) => row.id);
    return this.http.delete<ResponseBody<any>>(`${this.url}/batch`, {body: params});
  }
}
