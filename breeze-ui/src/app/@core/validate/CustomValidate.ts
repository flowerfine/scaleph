import { AbstractControl, ValidationErrors, AsyncValidatorFn, FormGroup } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { UserService } from '../services/user.service';

@Injectable({ providedIn: 'root' })
export class CustomValidate {
  /**
   * 唯一账号名验证
   * @param userService 用户服务类
   */
  static UniqueAccountValidate(userService: UserService): AsyncValidatorFn {
    return (control: AbstractControl): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> => {
      return userService.isUserExists(control.value).pipe(
        map((d) => {
          return d ? { uniqueAccountValidate: true } : null;
        })
      );
    };
  }
  /**
   * 用户邮箱为唯一验证
   * @param userService 用户服务类
   */
  static UniqueEmailValidate(userService: UserService): AsyncValidatorFn {
    return (control: AbstractControl): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> => {
      return userService.isEmailExists(control.value).pipe(
        map((d) => (d ? { uniqueEmailValidate: true } : null)),
        catchError(() => of(null))
      );
    };
  }

  /**
   * 校验表单
   * 触发表单验证，存在异步验证时，校验总是为false，可不调用
   * @param formGroup
   */
  static validateForm(formGroup: FormGroup): void {
    for (const key in formGroup.controls) {
      formGroup.controls[key].markAsDirty();
      formGroup.controls[key].updateValueAndValidity();
    }
  }
}
