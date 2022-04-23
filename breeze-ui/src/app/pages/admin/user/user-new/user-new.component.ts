import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DValidateRules, FormLayout } from 'ng-devui';
import { of } from 'rxjs';
import { delay } from 'rxjs/operators';
import { User } from 'src/app/@core/data/admin.data';
import { Dict, DICT_TYPE } from 'src/app/@core/data/app.data';
import { DictDataService } from 'src/app/@core/services/dict-data.service';
import { UserService } from 'src/app/@core/services/user.service';

@Component({
  selector: 'app-user-new',
  templateUrl: './user-new.component.html',
  styleUrls: ['./user-new.component.scss'],
})
export class UserNewComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  idCardTypeList: Dict[] = [];
  genderList: Dict[] = [];
  nationList: Dict[] = [];
  birthdayDp = null;
  formLayout = FormLayout.Vertical;
  formConfig: { [Key: string]: DValidateRules } = {
    rule: { message: this.translate.instant('app.error.formValidateError'), messageShowType: 'text' },
    userNameRules: {
      validators: [
        { required: true },
        { maxlength: 30 },
        { minlength: 5 },
        { pattern: /^[a-zA-Z0-9_]+$/, message: this.translate.instant('app.common.validate.characterWord') },
      ],
      asyncValidators: [
        {
          sameName: this.sameName.bind(this),
          message: this.translate.instant('app.common.validate.sameUserName'),
        },
      ],
    },
    nickNameRules: {
      validators: [{ maxlength: 50 }],
    },
    emailRules: {
      validators: [{ required: true }, { maxlength: 100 }, { email: true }],
      asyncValidators: [
        {
          sameName: this.sameEmail.bind(this),
          message: this.translate.instant('app.common.validate.sameEmail'),
        },
      ],
    },
    realNameRules: {
      validators: [{ maxlength: 30 }],
    },
    idCardNoRules: {
      validators: [{ maxlength: 18 }],
    },
    qqRules: {
      validators: [{ maxlength: 15 }],
    },
    wechatRules: {
      validators: [{ maxlength: 50 }],
    },
    mobilePhoneRules: {
      validators: [{ maxlength: 11 }],
    },
    summaryeRules: {
      validators: [{ maxlength: 500 }],
    },
  };

  formData: User = {
    userName: null,
    nickName: null,
    email: null,
    realName: null,
    idCardType: null,
    idCardNo: null,
    gender: null,
    nation: null,
    birthday: null,
    qq: null,
    wechat: null,
    mobilePhone: null,
    userStatus: null,
    summary: null,
  };

  constructor(
    private elr: ElementRef,
    private translate: TranslateService,
    private userService: UserService,
    private dictDataService: DictDataService
  ) {}

  ngOnInit(): void {
    this.parent = this.elr.nativeElement.parentElement;
    this.dictDataService.listByType(DICT_TYPE.idCardType).subscribe((d) => {
      this.idCardTypeList = d;
    });
    this.dictDataService.listByType(DICT_TYPE.gender).subscribe((d) => {
      this.genderList = d;
    });
    this.dictDataService.listByType(DICT_TYPE.nation).subscribe((d) => {
      this.nationList = d;
    });
  }

  submitForm({ valid }) {
    if (valid) {
      this.userService.add(this.formData).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }

  sameName(value) {
    return this.userService.isUserExists(value);
  }

  sameEmail(value) {
    return this.userService.isEmailExists(value);
  }
}
