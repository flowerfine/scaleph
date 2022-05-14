import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthCode, LoginInfo, OnlineUserInfo, RegisterInfo, ResponseBody, USER_AUTH } from '../data/app.data';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}

  setSession(userInfo: OnlineUserInfo) {
    localStorage.setItem(USER_AUTH.userInfo, JSON.stringify(userInfo));
    let pCodes: string[] = [];
    if (userInfo.roles != null && userInfo.roles != undefined) {
      userInfo.roles.forEach((d) => {
        pCodes.push(d);
      });
    }
    if (userInfo.privileges != null && userInfo.privileges != undefined) {
      userInfo.privileges.forEach((d) => {
        pCodes.push(d);
      });
    }
    localStorage.setItem(USER_AUTH.pCodes, JSON.stringify(pCodes));
  }

  isUserLoggedIn() {
    if (localStorage.getItem(USER_AUTH.token)) {
      return true;
    } else {
      return false;
    }
  }

  hasPrivilege(code: string): boolean {
    let pCodes: string[] = JSON.parse(localStorage.getItem(USER_AUTH.pCodes));
    if (pCodes != null && pCodes != undefined) {
      return pCodes.includes(USER_AUTH.roleSysAdmin) || pCodes.includes(code);
    } else {
      return false;
    }
  }

  refreshAuthImage(): Observable<AuthCode> {
    return this.http.get<AuthCode>('/api/authCode?d=' + new Date().getTime());
  }

  register(registerInfo: RegisterInfo) {
    return this.http.post<ResponseBody<any>>('/api/user/register', registerInfo);
  }

  login(loginInfo: LoginInfo) {
    return this.http.post<ResponseBody<any>>('/api/user/login', loginInfo);
  }

  logout() {
    let token: string = localStorage.getItem(USER_AUTH.token);
    this.http.post<ResponseBody<any>>('/api/user/logout', token).subscribe((d) => {
      if (d.success) {
        localStorage.removeItem(USER_AUTH.token);
        localStorage.removeItem(USER_AUTH.userInfo);
        localStorage.removeItem(USER_AUTH.pCodes);
      }
    });
  }
}
