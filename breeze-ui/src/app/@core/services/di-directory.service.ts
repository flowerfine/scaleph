import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ITreeItem } from 'ng-devui';
import { Observable } from 'rxjs';
import { ResponseBody } from '../data/app.data';
import { DiDirectory } from '../data/studio.data';

@Injectable({
  providedIn: 'root',
})
export class DiDirectoryService {
  private url = 'api/di/dir';
  constructor(private http: HttpClient) {}

  listProjectDir(projectId: number): Observable<ITreeItem[]> {
    return this.http.get<ITreeItem[]>(`${this.url}/` + projectId);
  }

  add(row: DiDirectory): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(`${this.url}`, row);
  }

  update(row: DiDirectory): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(this.url, row);
  }

  delete(row: DiDirectory): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }
}
