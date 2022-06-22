import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PageResponse, ResponseBody} from '../../data/app.data';
import {FlinkDeployConfig, FlinkRelease, FlinkReleaseUploadParam} from '../../data/flink.data';
import {DiResourceFile} from "../../data/datadev.data";

@Injectable({
  providedIn: 'root',
})
export class DeployConfigService {
  private url = 'api/flink/deploy-config';

  constructor(private http: HttpClient) {
  }

  list(queryParam): Observable<PageResponse<FlinkDeployConfig>> {
    const params: HttpParams = new HttpParams({fromObject: queryParam});
    return this.http.get<PageResponse<FlinkDeployConfig>>(`${this.url}`, {params});
  }

  upload(uploadParam: FlinkReleaseUploadParam): Observable<ResponseBody<any>> {
    const params: FormData = new FormData();
    params.append("version", uploadParam.version)
    params.append("file", uploadParam.file)
    params.append("remark", uploadParam.remark)
    return this.http.post<ResponseBody<any>>(`${this.url}/upload`, params);
  }

  delete(row: DiResourceFile): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteBatch(rows: FlinkRelease[]): Observable<ResponseBody<any>> {
    let params = rows.map((row) => row.id);
    return this.http.delete<ResponseBody<any>>(`${this.url}/batch`, {body: params });
  }


}
