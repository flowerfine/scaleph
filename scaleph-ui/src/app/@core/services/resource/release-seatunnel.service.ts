import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PageResponse, ResponseBody} from '../../data/app.data';
import {ReleaseSeaTunnel, ReleaseSeaTunnelUploadParam} from "../../data/resource.data";

@Injectable({
  providedIn: 'root',
})
export class ReleaseSeaTunnelService {
  private url = 'api/resource/release/seatunnel';

  constructor(private http: HttpClient) {
  }

  list(queryParam): Observable<PageResponse<ReleaseSeaTunnel>> {
    const params: HttpParams = new HttpParams({fromObject: queryParam});
    return this.http.get<PageResponse<ReleaseSeaTunnel>>(`${this.url}`, {params});
  }

  selectOne(id): Observable<ResponseBody<ReleaseSeaTunnel>> {
    return this.http.get<ResponseBody<ReleaseSeaTunnel>>(`${this.url}/` + id);
  }

  upload(uploadParam: ReleaseSeaTunnelUploadParam): Observable<ResponseBody<any>> {
    const params: FormData = new FormData();
    params.append("version", uploadParam.version)
    params.append("file", uploadParam.file)
    params.append("remark", uploadParam.remark)
    return this.http.post<ResponseBody<any>>(`${this.url}/upload`, params);
  }

  delete(row: ReleaseSeaTunnel): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteBatch(rows: ReleaseSeaTunnel[]): Observable<ResponseBody<any>> {
    let params = rows.map((row) => row.id);
    return this.http.delete<ResponseBody<any>>(`${this.url}/batch`, {body: params});
  }
}
