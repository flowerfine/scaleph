import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PageResponse, ResponseBody} from '../../data/app.data';
import {SeaTunnelRelease, SeaTunnelReleaseUploadParam} from "../../data/resource.data";

@Injectable({
  providedIn: 'root',
})
export class SeatunnelReleaseService {
  private url = 'api/resource/seatunnel-release';

  constructor(private http: HttpClient) {
  }

  list(queryParam): Observable<PageResponse<SeaTunnelRelease>> {
    const params: HttpParams = new HttpParams({fromObject: queryParam});
    return this.http.get<PageResponse<SeaTunnelRelease>>(`${this.url}`, {params});
  }

  selectOne(id): Observable<ResponseBody<SeaTunnelRelease>> {
    return this.http.get<ResponseBody<SeaTunnelRelease>>(`${this.url}/` + id);
  }

  upload(uploadParam: SeaTunnelReleaseUploadParam): Observable<ResponseBody<any>> {
    const params: FormData = new FormData();
    params.append("version", uploadParam.version)
    params.append("file", uploadParam.file)
    params.append("remark", uploadParam.remark)
    return this.http.post<ResponseBody<any>>(`${this.url}/upload`, params);
  }

  delete(row: SeaTunnelRelease): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteBatch(rows: SeaTunnelRelease[]): Observable<ResponseBody<any>> {
    let params = rows.map((row) => row.id);
    return this.http.delete<ResponseBody<any>>(`${this.url}/batch`, {body: params});
  }
}
