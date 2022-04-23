import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ITreeItem } from 'ng-devui';
import { Observable } from 'rxjs';
import { Privilege } from '../data/admin.data';
import { ResponseBody } from '../data/app.data';

@Injectable({
  providedIn: 'root',
})
export class PrivilegeService {
  private url = 'api/admin/privilege';
  constructor(private http: HttpClient) {}

  listAll(resourceType: string): Observable<ITreeItem[]> {
    const params: HttpParams = new HttpParams().set('resourceType', resourceType);
    return this.http.get<ITreeItem[]>(`${this.url}`, { params });
  }

  listByRole(roleId: string, resourceType: string): Observable<Privilege[]> {
    const params: HttpParams = new HttpParams().set('roleId', roleId).set('resourceType', resourceType);
    return this.http.get<Privilege[]>(`${this.url}` + '/role', { params });
  }

  grant(roleId: string, privilegeIds: string[], resourceType: string): Observable<ResponseBody<any>> {
    const params: HttpParams = new HttpParams()
      .set('roleId', roleId)
      .set('privilegeIds', JSON.stringify(privilegeIds))
      .set('resourceType', resourceType);
    return this.http.post<ResponseBody<any>>(`${this.url}`, params);
  }
}
