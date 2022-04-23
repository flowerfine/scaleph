import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Role } from '../data/admin.data';
import { ResponseBody } from '../data/app.data';

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  private url = 'api/admin/role';
  constructor(private http: HttpClient) {}

  listAll(): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.url}`);
  }

  add(row: Role): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(`${this.url}`, row);
  }

  update(row: Role): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(this.url, row);
  }

  delete(row: Role): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  grant(roleId: string, userIds: string[]): Observable<ResponseBody<any>> {
    const params: HttpParams = new HttpParams().set('roleId', roleId).set('userIds', JSON.stringify(userIds));
    return this.http.post<ResponseBody<any>>(`${this.url}` + '/grant', params);
  }

  listRoleByDept(deptId: string): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.url}` + '/dept?grant=1&deptId=' + deptId);
  }

  listGrantRoleByDept(deptId: string): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.url}` + '/dept?deptId=' + deptId);
  }

  grantDeptRole(deptId, roleId): Observable<ResponseBody<any>> {
    return this.http.get<ResponseBody<any>>(`${this.url}` + '/dept/grant?deptId=' + deptId + '&roleId=' + roleId);
  }

  revokeDeptRole(deptId, roleId): Observable<ResponseBody<any>> {
    return this.http.get<ResponseBody<any>>(`${this.url}` + '/dept/revoke?deptId=' + deptId + '&roleId=' + roleId);
  }
}
