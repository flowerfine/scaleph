import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Message } from '../../data/admin.data';
import { PageResponse, ResponseBody } from '../../data/app.data';

@Injectable({
  providedIn: 'root',
})
export class MessageService {
  private url = 'api/msg';

  constructor(private http: HttpClient) {}

  listByPage(queryParam): Observable<PageResponse<Message>> {
    const params: HttpParams = new HttpParams({ fromObject: queryParam });
    return this.http.get<PageResponse<Message>>(`${this.url}`, { params });
  }

  update(row: Message): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(`${this.url}`, row);
  }

  countUnReadMessage(): Observable<number> {
    return this.http.get<number>(`${this.url}/count`);
  }

  readAll(): Observable<ResponseBody<any>> {
    return this.http.get<ResponseBody<any>>(`${this.url}/readAll`);
  }
}
