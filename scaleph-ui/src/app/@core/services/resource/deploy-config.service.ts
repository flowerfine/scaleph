import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PageResponse, ResponseBody} from '../../data/app.data';
import {ClusterCredential, ClusterCredentialUploadParam, FileStatus} from "../../data/resource.data";

@Injectable({
  providedIn: 'root',
})
export class DeployConfigService {
  private url = 'api/resource/cluster-credential';

  constructor(private http: HttpClient) {
  }

  list(queryParam): Observable<PageResponse<ClusterCredential>> {
    const params: HttpParams = new HttpParams({fromObject: queryParam});
    return this.http.get<PageResponse<ClusterCredential>>(`${this.url}`, {params});
  }

  selectOne(id): Observable<ResponseBody<ClusterCredential>> {
    return this.http.get<ResponseBody<ClusterCredential>>(`${this.url}/` + id);
  }

  add(row: ClusterCredential): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(this.url, row);
  }

  update(row: ClusterCredential): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(this.url, row);
  }

  deleteBatch(rows: ClusterCredential[]): Observable<ResponseBody<any>> {
    let params = rows.map((row) => row.id);
    return this.http.delete<ResponseBody<any>>(`${this.url}/batch`, {body: params});
  }

  upload(uploadParam: ClusterCredentialUploadParam): Observable<ResponseBody<any>> {
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

  uploadFiles(id: number, files: File[]): Observable<ResponseBody<any>> {
    let uploadUrl = `${this.url}/` + id + '/file'
    const params: FormData = new FormData();
    files.forEach(function (file) {
      params.append("files", file)
    })
    return this.http.post<ResponseBody<any>>(uploadUrl, params);
  }

  deleteFiles(id: number, rows: FileStatus[]): Observable<ResponseBody<any>> {
    let deleteUrl = `${this.url}/` + id + '/file';
    let params = rows.map((row) => row.name);
    return this.http.delete<ResponseBody<any>>(deleteUrl, {body: params});
  }
}
