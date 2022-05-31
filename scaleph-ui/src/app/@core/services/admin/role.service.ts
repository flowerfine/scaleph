import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SecRole } from '../../data/admin.data';
import { ResponseBody } from '../../data/app.data';

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  private url = 'api/admin/role';
  constructor(private http: HttpClient) {}

  listAll(): Observable<SecRole[]> {
    return this.http.get<SecRole[]>(`${this.url}`);
  }

  add(row: SecRole): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(`${this.url}`, row);
  }

  update(row: SecRole): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(this.url, row);
  }

  delete(row: SecRole): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  grant(roleId: string, userIds: string[]): Observable<ResponseBody<any>> {
    const params: HttpParams = new HttpParams().set('roleId', roleId).set('userIds', JSON.stringify(userIds));
    return this.http.post<ResponseBody<any>>(`${this.url}` + '/grant', params);
  }

  listRoleByDept(deptId: string): Observable<SecRole[]> {
    return this.http.get<SecRole[]>(`${this.url}` + '/dept?grant=1&deptId=' + deptId);
  }

  listGrantRoleByDept(deptId: string): Observable<SecRole[]> {
    return this.http.get<SecRole[]>(`${this.url}` + '/dept?deptId=' + deptId);
  }

  grantDeptRole(deptId, roleId): Observable<ResponseBody<any>> {
    return this.http.get<ResponseBody<any>>(`${this.url}` + '/dept/grant?deptId=' + deptId + '&roleId=' + roleId);
  }

  revokeDeptRole(deptId, roleId): Observable<ResponseBody<any>> {
    return this.http.get<ResponseBody<any>>(`${this.url}` + '/dept/revoke?deptId=' + deptId + '&roleId=' + roleId);
  }
}
