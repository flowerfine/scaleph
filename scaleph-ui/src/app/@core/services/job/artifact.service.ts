import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PageResponse, ResponseBody} from '../../data/app.data';
import {FlinkRelease, FlinkReleaseUploadParam} from '../../data/flink.data';
import {FlinkArtifact, FlinkArtifactUploadParam} from "../../data/job.data";

@Injectable({
  providedIn: 'root',
})
export class ArtifactService {
  private url = 'api/flink/artifact';

  constructor(private http: HttpClient) {
  }

  list(queryParam): Observable<PageResponse<FlinkArtifact>> {
    const params: HttpParams = new HttpParams({fromObject: queryParam});
    return this.http.get<PageResponse<FlinkArtifact>>(`${this.url}`, {params});
  }

  upload(uploadParam: FlinkArtifactUploadParam): Observable<ResponseBody<any>> {
    const params: FormData = new FormData();
    params.append("name", uploadParam.name)
    params.append("entryClass", uploadParam.entryClass)
    params.append("file", uploadParam.file)
    params.append("remark", uploadParam.remark)
    return this.http.post<ResponseBody<any>>(`${this.url}`, params);
  }

  delete(row: FlinkArtifact): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteBatch(rows: FlinkArtifact[]): Observable<ResponseBody<any>> {
    let params = rows.map((row) => row.id);
    return this.http.delete<ResponseBody<any>>(`${this.url}/batch`, {body: params});
  }
}
