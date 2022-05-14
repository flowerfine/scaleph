import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PageResponse, ResponseBody } from '../../data/app.data';
import { MetaDataMap, MetaDataSet, MetaDataSetType } from '../../data/stdata.data';

@Injectable({
  providedIn: 'root',
})
export class RefdataService {
  private url = 'api/stdata/ref';
  constructor(private http: HttpClient) {}

  listTypeByPage(queryParam): Observable<PageResponse<MetaDataSetType>> {
    const params: HttpParams = new HttpParams({ fromObject: queryParam });
    return this.http.get<PageResponse<MetaDataSetType>>(`${this.url}/type`, { params });
  }

  deleteType(row: MetaDataSetType): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/type/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteTypeBatch(rows: MetaDataSetType[]): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/type/` + 'batch';
    let params = rows.map((row) => row.id);
    return this.http.post<ResponseBody<any>>(delUrl, { ...params });
  }

  addType(row: MetaDataSetType): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(`${this.url}/type`, row);
  }

  updateType(row: MetaDataSetType): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(`${this.url}/type`, row);
  }

  listDataByPage(queryParam): Observable<PageResponse<MetaDataSet>> {
    const params: HttpParams = new HttpParams({ fromObject: queryParam });
    return this.http.get<PageResponse<MetaDataSet>>(`${this.url}/data`, { params });
  }

  listDataByType(type: string): Observable<MetaDataSet[]> {
    return this.http.get<MetaDataSet[]>(`${this.url}/data/type/` + type);
  }

  deleteData(row: MetaDataSet): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/data/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteDataBatch(rows: MetaDataSet[]): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/data/` + 'batch';
    let params = rows.map((row) => row.id);
    return this.http.post<ResponseBody<any>>(delUrl, { ...params });
  }

  addData(row: MetaDataSet): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(`${this.url}/data`, row);
  }

  updateData(row: MetaDataSet): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(`${this.url}/data`, row);
  }

  listMapByPage(queryParam): Observable<PageResponse<MetaDataMap>> {
    const params: HttpParams = new HttpParams({ fromObject: queryParam });
    return this.http.get<PageResponse<MetaDataMap>>(`${this.url}/map`, { params });
  }

  deleteMap(row: MetaDataMap): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/map/` + row.id;
    return this.http.delete<ResponseBody<any>>(delUrl);
  }

  deleteMapBatch(rows: MetaDataMap[]): Observable<ResponseBody<any>> {
    const delUrl = `${this.url}/map/` + 'batch';
    let params = rows.map((row) => row.id);
    return this.http.post<ResponseBody<any>>(delUrl, { ...params });
  }

  addMap(row: MetaDataMap): Observable<ResponseBody<any>> {
    return this.http.post<ResponseBody<any>>(`${this.url}/map`, row);
  }

  updateMap(row: MetaDataMap): Observable<ResponseBody<any>> {
    return this.http.put<ResponseBody<any>>(`${this.url}/map`, row);
  }
}
