import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BasicConfig, EmailConfig } from '../../data/admin.data';
import { ResponseBody } from '../../data/app.data';

@Injectable({
  providedIn: 'root',
})
export class SystemConfigService {
  private url = 'api/admin/config';
  constructor(private http: HttpClient) {}

  getEmailConfig(): Observable<EmailConfig> {
    return this.http.get<EmailConfig>(`${this.url}` + '/email');
  }

  configEmail(info: EmailConfig): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(`${this.url}` + '/email', info);
  }

  getBasicConfig(): Observable<BasicConfig> {
    return this.http.get<BasicConfig>(`${this.url}` + '/basic');
  }

  configBasic(info: BasicConfig): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(`${this.url}` + '/basic', info);
  }
}
