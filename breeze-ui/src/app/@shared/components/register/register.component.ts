import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { DialogService, DValidateRules, FormLayout } from 'ng-devui';
import { I18nService } from 'ng-devui/i18n';
import { AuthCode, RegisterInfo } from 'src/app/@core/data/app.data';
import { AuthService } from 'src/app/@core/services/auth.service';
import { PersonalizeService } from 'src/app/@core/services/personalize.service';
import { UserService } from 'src/app/@core/services/user.service';
import { LANGUAGES } from 'src/config/language-config';
import { ThemeType } from '../../models/theme';

@Component({
  selector: 'da-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  formLayout = FormLayout.Vertical;
  showPassword = false;
  showConfirmPassword = false;

  language;
  // i18nValues;
  toastMessage;
  languages = LANGUAGES;
  authImage: AuthCode = { uuid: '', img: '' };
  formData = {
    userName: '',
    email: '',
    password: '',
    confirmPassword: '',
    authCode: '',
  };

  formRules: { [key: string]: DValidateRules } = {
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
    emailRules: {
      validators: [{ required: true }, { maxlength: 100 }, { email: true }],
      asyncValidators: [
        {
          sameName: this.sameEmail.bind(this),
          message: this.translate.instant('app.common.validate.sameEmail'),
        },
      ],
    },
    passwordRules: {
      validators: [
        { required: true },
        { minlength: 6 },
        { maxlength: 32 },
        { pattern: /^[a-zA-Z0-9\d@$!%*?&.]+(\s+[a-zA-Z0-9]+)*$/, message: this.translate.instant('app.common.validate.patternPassword') },
      ],
    },
    confirmPasswordRules: [
      { required: true },
      {
        sameToPassWord: this.sameToPassWord.bind(this),
        message: this.translate.instant('app.common.validate.samePassword'),
      },
      { minlength: 6 },
      { maxlength: 32 },
      { pattern: /^[a-zA-Z0-9\d@$!%*?&.]+(\s+[a-zA-Z0-9]+)*$/, message: this.translate.instant('app.common.validate.patternPassword') },
    ],
    authCodeRules: [{ required: true }, { maxlength: 5 }],
  };

  constructor(
    private route: Router,
    private translate: TranslateService,
    private i18n: I18nService,
    private dialogService: DialogService,
    private personalizeService: PersonalizeService,
    private userService: UserService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.language = this.translate.currentLang;
    this.personalizeService.setRefTheme(ThemeType.Default);
    this.refreshAuthCode();
  }

  refreshAuthCode() {
    this.authService.refreshAuthImage().subscribe((d) => {
      this.authImage = d;
    });
  }

  register(result) {
    if (result.valid) {
      let registerInfo: RegisterInfo = {
        userName: this.formData.userName,
        email: this.formData.email,
        password: this.formData.password,
        confirmPassword: this.formData.confirmPassword,
        authCode: this.formData.authCode,
        uuid: this.authImage.uuid,
      };
      this.authService.register(registerInfo).subscribe((d) => {
        if (d.success) {
          const results = this.dialogService.open({
            id: 'register-result',
            width: '350px',
            maxHeight: '600px',
            title: this.translate.instant('userAuth.register.success.title'),
            content: this.translate.instant('userAuth.register.success.content'),
            backdropCloseable: false,
            dialogtype: 'success',
            buttons: [
              {
                cssClass: 'primary',
                text: 'Ok',
                handler: ($event: Event) => {
                  this.goToLogin(results);
                },
              },
            ],
          });
          setTimeout(() => {
            this.goToLogin(results);
          }, 3000);
        }
      });
    }
  }

  goToLogin(dialogResult) {
    dialogResult.modalInstance.hide();
    this.route.navigate(['/login']);
  }

  onLanguageClick(language: string) {
    this.language = language;
    localStorage.setItem('lang', this.language);
    this.i18n.toggleLang(this.language);
    this.translate.use(this.language);
  }

  sameToPassWord(value: string) {
    return value === this.formData.password;
  }

  sameName(value) {
    return this.userService.isUserExists(value);
  }

  sameEmail(value) {
    return this.userService.isEmailExists(value);
  }
}
