import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { ModalService, ToastService } from 'ng-devui';
import { NotificationsComponent } from './notifications.component';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  showTime: number = 1500;
  constructor(private toastService: ToastService, private modalService: ModalService, private translateService: TranslateService) {}

  success(info: string) {
    const results = this.modalService.open({
      id: 'notification-success',
      backDropZIndex: -1000,
      backdropCloseable: false,
      component: NotificationsComponent,
      placement: 'top',
      onClose: () => {},
      data: {
        info: info,
        type: 'success',
        onClose: (event) => {
          results.modalInstance.hide();
        },
      },
    });
    setTimeout(() => {
      results.modalInstance.hide();
    }, this.showTime);
  }

  info(info: string) {
    const results = this.modalService.open({
      id: 'notification-info',
      backDropZIndex: -1000,
      backdropCloseable: false,
      component: NotificationsComponent,
      placement: 'top',
      onClose: () => {},
      data: {
        info: info,
        type: 'info',
        onClose: (event) => {
          results.modalInstance.hide();
        },
      },
    });
    setTimeout(() => {
      results.modalInstance.hide();
    }, this.showTime);
  }

  error(info: string, showType: string) {
    if (showType == '2') {
      const results = this.modalService.open({
        id: 'notification-error',
        backDropZIndex: -1000,
        backdropCloseable: false,
        component: NotificationsComponent,
        placement: 'top',
        onClose: () => {},
        data: {
          info: info,
          type: 'danger',
          onClose: (event) => {
            results.modalInstance.hide();
          },
        },
      });
      setTimeout(() => {
        results.modalInstance.hide();
      }, this.showTime);
    } else if (showType == '4') {
      this.toastService.open({
        value: [{ severity: 'error', summary: this.translateService.instant('app.common.error.label'), content: info }],
        life: this.showTime,
      });
    } else {
    }
  }
}
