import { Injectable, Injector } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { NotificationService } from 'src/app/@shared/components/notifications/notification.service';
import { ResponseBody, USER_AUTH } from '../data/app.data';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
  constructor(private injector: Injector, private router: Router, private notificationService: NotificationService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let token = localStorage.getItem(USER_AUTH.token);
    let req = request.clone();
    //统一增加请求header
    if (token != null && token != undefined && token != '') {
      req = request.clone({
        setHeaders: {
          u_token: token,
        },
      });
    }

    return next.handle(req).pipe(
      tap((event: any) => {
        if (event instanceof HttpResponse) {
          this.handleResp(event);
        }
      })
    );
  }

  private handleResp(event: HttpResponse<any>): Observable<any> {
    const route = this.router;
    let body: string = JSON.stringify(event.body);
    try {
      let info: ResponseBody<any> = JSON.parse(body);
      if (info.success != null && info.success != undefined && !info.success) {
        if (info.errorCode == '401') {
          this.notificationService.error(info.errorMessage, info.showType);
          route.navigate(['/login']);
        } else if (info.errorCode == '403') {
          route.navigate(['/403']);
        } else {
          this.notificationService.error(info.errorMessage, info.showType);
        }
      }
    } catch (err) {}
    return of(event);
  }
}
