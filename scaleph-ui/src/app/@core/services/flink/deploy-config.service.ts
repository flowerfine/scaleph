import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PageResponse, ResponseBody} from '../../data/app.data';
import {FileStatus, FlinkDeployConfig, FlinkDeployConfigUploadParam} from '../../data/flink.data';

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

  selectOne(id): Observable<ResponseBody<FlinkDeployConfig>> {
    return this.http.get<ResponseBody<FlinkDeployConfig>>(`${this.url}/` + id);
  }

  add(row: FlinkDeployConfig): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(this.url, row);
  }

  update(row: FlinkDeployConfig): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(this.url, row);
  }

  deleteBatch(rows: FlinkDeployConfig[]): Observable<ResponseBody<any>> {
    let params = rows.map((row) => row.id);
    return this.http.delete<ResponseBody<any>>(`${this.url}/batch`, {body: params});
  }

  upload(uploadParam: FlinkDeployConfigUploadParam): Observable<ResponseBody<any>> {
    const params: FormData = new FormData();
    params.append("configType", uploadParam.configType)
    params.append("name", uploadParam.name)
    uploadParam.files.forEach(function (file) {
      params.append("files", file)
    })
    params.append("remark", uploadParam.remark)
    return this.http.post<ResponseBody<any>>(`${this.url}/upload`, params);
  }

  getFiles(id): Observable<ResponseBody<Array<FileStatus>>> {
    return this.http.get<ResponseBody<Array<FileStatus>>>(`${this.url}/` + id + '/file');
  }




}
