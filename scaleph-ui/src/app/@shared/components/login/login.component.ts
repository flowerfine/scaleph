import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { I18nService } from 'ng-devui/i18n';
import { AuthService } from 'src/app/@core/services/auth.service';
import { PersonalizeService } from 'src/app/@core/services/personalize.service';
import { ThemeType } from '../../models/theme';
import { LANGUAGES } from 'src/config/language-config';
import { DValidateRules, FormLayout } from 'ng-devui';
import { AuthCode, LoginInfo, USER_AUTH } from 'src/app/@core/data/app.data';

@Component({
  selector: 'da-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  formLayout = FormLayout.Vertical;
  showPassword = false;
  toastMessage;
  languages = LANGUAGES;
  language;
  authImage: AuthCode = { img: '', uuid: '' };

  formData = {
    userName: '',
    password: '',
    authCode: '',
    remember: true,
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
    },
    passwordRules: {
      validators: [{ required: true }, { minlength: 6 }, { maxlength: 32 }],
    },
    authCodeRules: [{ required: true }, { maxlength: 5 }],
  };

  constructor(
    private route: Router,
    private authService: AuthService,
    private translate: TranslateService,
    private i18n: I18nService,
    private personalizeService: PersonalizeService
  ) {}

  ngOnInit(): void {
    this.language = this.translate.currentLang;
    this.personalizeService.setRefTheme(ThemeType.Default);
    localStorage.clear();
    this.refreshAuthCode();
  }

  refreshAuthCode() {
    this.authService.refreshAuthImage().subscribe((d) => {
      this.authImage = d;
    });
  }

  onLanguageClick(language) {
    this.language = language;
    localStorage.setItem('lang', this.language);
    this.i18n.toggleLang(this.language);
    this.translate.use(this.language);
  }

  login(result) {
    if (result.valid) {
      let loginInfo: LoginInfo = {
        userName: this.formData.userName,
        password: this.formData.password,
        authCode: this.formData.authCode,
        uuid: this.authImage.uuid,
        remember: this.formData.remember,
      };
      this.authService.login(loginInfo).subscribe((d) => {
        if (d.success) {
          localStorage.setItem(USER_AUTH.token, d.data);
          setTimeout(() => {
            this.route.navigate(['/']);
          }, 500);
        } else {
          this.refreshAuthCode();
        }
      });
    } else {
      this.refreshAuthCode();
    }
  }
}
