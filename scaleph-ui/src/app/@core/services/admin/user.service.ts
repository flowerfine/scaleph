import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SecUser } from '../../data/admin.data';
import { Dict, OnlineUserInfo, PageResponse, ResponseBody, TransferData } from '../../data/app.data';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private url = 'api/admin/user';
  constructor(private http: HttpClient) {}

  listByPage(queryParam): Observable<PageResponse<SecUser>> {
    const params: HttpParams = new HttpParams({ fromObject: queryParam });
    return this.http.get<PageResponse<SecUser>>(`${this.url}`, { params });
  }

  delete(row: SecUser): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteBatch(rows: SecUser[]): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/` + 'batch';
    let params = rows.map((row) => row.id);
    return this.http.post<ResponseBody<any>>(delUrl, { ...params });
  }

  add(row: SecUser): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(this.url, row);
  }

  update(row: SecUser): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(this.url, row);
  }

  isUserExists(userName: string): Observable<boolean> {
    const params: HttpParams = new HttpParams().set('userName', userName);
    return this.http.get<boolean>('api/user/validation/userName', { params });
  }

  isEmailExists(email: string): Observable<boolean> {
    const params: HttpParams = new HttpParams().set('email', email);
    return this.http.get<boolean>('api/user/validation/email', { params });
  }

  listByUserNameAndDept(userName: string, deptId: string, direction: string): Observable<TransferData[]> {
    const params: HttpParams = new HttpParams().set('userName', userName).set('deptId', deptId).set('direction', direction);
    return this.http.post<TransferData[]>('/api/user/dept', params);
  }

  listByUserNameAndRole(userName: string, roleId: string, direction: string): Observable<TransferData[]> {
    const params: HttpParams = new HttpParams().set('userName', userName).set('roleId', roleId).set('direction', direction);
    return this.http.post<TransferData[]>('/api/user/role', params);
  }

  getOnlineUserInfo(token: string): Observable<ResponseBody<OnlineUserInfo>> {
    return this.http.get<ResponseBody<OnlineUserInfo>>('api/user/get/' + token);
  }

  getUserInfo(): Observable<SecUser> {
    return this.http.get<SecUser>('api/user/info');
  }

  editPassword({ oldPassword, password, confirmPassword }): Observable<ResponseBody<any>> {
    const params: HttpParams = new HttpParams()
      .set('oldPassword', oldPassword)
      .set('password', password)
      .set('confirmPassword', confirmPassword);
    return this.http.post<ResponseBody<any>>('api/user/passwd/edit', params);
  }

  getAuthCode(email): Observable<ResponseBody<any>> {
    return this.http.get<ResponseBody<any>>('api/user/email/getAuth?email=' + email);
  }

  bindEmail({ email, authCode }): Observable<ResponseBody<any>> {
    const params: HttpParams = new HttpParams().set('email', email).set('authCode', authCode);
    return this.http.get<ResponseBody<any>>('api/user/email/auth', { params });
  }
}
