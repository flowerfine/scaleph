import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { EmailValidator } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { DValidateRules, FormLayout } from 'ng-devui';
import { User } from 'src/app/@core/data/admin.data';
import { Dict, DICT_TYPE } from 'src/app/@core/data/app.data';
import { SysDictDataService } from 'src/app/@core/services/admin/dict-data.service';
import { UserService } from 'src/app/@core/services/admin/user.service';

@Component({
  selector: 'app-user-update',
  templateUrl: './user-update.component.html',
  styleUrls: ['./user-update.component.scss'],
})
export class UserUpdateComponent implements OnInit {
  parent: HTMLElement;
  @Input() data: any;
  idCardTypeList: Dict[] = [];
  genderList: Dict[] = [];
  nationList: Dict[] = [];
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
    private dictDataService: SysDictDataService
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
    this.formData = {
      id: this.data.item.id,
      userName: this.data.item.userName,
      nickName: this.data.item.nickName,
      email: this.data.item.email,
      realName: this.data.item.realName,
      idCardType: this.data.item.idCardType,
      idCardNo: this.data.item.idCardNo,
      gender: this.data.item.gender,
      nation: this.data.item.nation,
      birthday: this.data.item.birthday,
      qq: this.data.item.qq,
      wechat: this.data.item.wechat,
      mobilePhone: this.data.item.mobilePhone,
      userStatus: this.data.item.userStatus,
      summary: this.data.item.summary,
    };
  }

  submitForm({ valid }) {
    if (valid) {
      this.userService.update(this.formData).subscribe((d) => {
        if (d.success) {
          this.data.onClose();
          this.data.refresh();
        }
      });
    }
  }
}
