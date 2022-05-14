import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthCode } from '../data/app.data';

@Injectable({
  providedIn: 'root',
})
export class CommonService {
  constructor(private http: HttpClient) {}

  getAuthImage(): Observable<AuthCode> {
    const params: HttpParams = new HttpParams().set('key', Math.random().toString());
    return this.http.get<AuthCode>('api/authCode', { params });
  }
}
