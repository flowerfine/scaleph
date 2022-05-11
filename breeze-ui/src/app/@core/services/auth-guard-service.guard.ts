import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { NotificationService } from 'src/app/@shared/components/notifications/notification.service';
import { OnlineUserInfo, USER_AUTH } from '../data/app.data';
import { AuthService } from './auth.service';

@Injectable()
export class AuthGuardService implements CanActivate {
  i18nValues: any;

  constructor(
    private router: Router,
    private authService: AuthService,
    private translate: TranslateService,
    private notificationService: NotificationService
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (!this.authService.isUserLoggedIn()) {
      this.notificationService.info(this.translate.instant('app.common.error.noLogin'));
      this.router.navigate(['login']);
      return false;
    } else {
      //判定登录过期
      let userInfo: OnlineUserInfo = JSON.parse(localStorage.getItem(USER_AUTH.userInfo));
      if (userInfo != null && userInfo != undefined) {
        let ctime = new Date().getTime();
        if (userInfo.expireTime == null || userInfo.expireTime == undefined || ctime > userInfo.expireTime) {
          this.notificationService.info(this.translate.instant('app.common.error.expired'));
          this.router.navigate(['login']);
          return false;
        } else {
          return true;
        }
      } else {
        //登录时只有token还未获取用户权限信息，放行查询用户信息
        return true;
      }
    }
  }
}
