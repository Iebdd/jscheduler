import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { registerLocaleData } from '@angular/common';
import  localeEnGb  from '@angular/common/locales/en-GB';

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));

  registerLocaleData(localeEnGb, 'en-gb');
