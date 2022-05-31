import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { DValidateRules, FormLayout } from 'ng-devui';
import { SecUser } from 'src/app/@core/data/admin.data';
import { Dict, DICT_TYPE } from 'src/app/@core/data/app.data';
import { SysDictDataService } from 'src/app/@core/services/admin/dict-data.service';
import { UserService } from 'src/app/@core/services/admin/user.service';
import { NotificationService } from 'src/app/@shared/components/notifications/notification.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['../user-center.component.scss'],
})
export class UserProfileComponent implements OnInit {
  idCardTypeList: Dict[] = [];
  genderList: Dict[] = [];
  nationList: Dict[] = [];
  birthdayDp = null;
  birthdayTime: Date = null;
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
    },
    nickNameRules: {
      validators: [{ maxlength: 50 }],
    },
    emailRules: {
      validators: [{ required: true }, { maxlength: 100 }, { email: true }],
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

  formData: SecUser = {
    id: null,
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
    private translate: TranslateService,
    private userService: UserService,
    private dictDataService: SysDictDataService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.dictDataService.listByType(DICT_TYPE.idCardType).subscribe((d) => {
      this.idCardTypeList = d;
    });
    this.dictDataService.listByType(DICT_TYPE.gender).subscribe((d) => {
      this.genderList = d;
    });
    this.dictDataService.listByType(DICT_TYPE.nation).subscribe((d) => {
      this.nationList = d;
    });
    this.refreshUserInfo();
  }

  refreshUserInfo() {
    this.userService.getUserInfo().subscribe((d) => {
      this.formData = d;
    });
  }

  submitForm({ valid }) {
    if (valid) {
      if (this.birthdayTime) {
        this.formData.birthday = this.birthdayTime.getTime();
      }
      this.userService.update(this.formData).subscribe((d) => {
        if (d.success) {
          this.notificationService.success(this.translate.instant('app.common.operate.success'));
          this.refreshUserInfo();
        }
      });
    }
  }
}
