import { WebPlugin, mergeWebPlugin } from '@capacitor/core';
import {
  IEmail,
  AvailableOptions,
  AvailableResults,
  EmailOptions
} from './definitions';
import { Plugins } from '@capacitor/core';
const mailto = 'mailto:';
const defaults = {
  app: mailto,
  subject: '',
  body: '',
  to: [] as any[],
  cc: [] as any[],
  bcc: [] as any[],
  attachments: [] as any[],
  isHtml: true,
  chooserHeader: 'Open with'
};
export class EmailPluginWeb extends WebPlugin implements IEmail {
  isAvailable(options?: AvailableOptions): Promise<AvailableResults> {
    return Promise.resolve({
      hasAccount: false,
      hasApp: false
    });
  }

  open(options?: EmailOptions | null): Promise<{}> {
    let url = 'mailto:';
    if (!options) {
      window.open(url);
      return Promise.resolve({});
    }
    const keys = Object.keys(options);
    for (let key of keys) {
      let value;
      if (key === 'to') {
        url = url + `${((options as any)[key] as string[]).join(';')}?`;
      } else if (key === 'cc' || key === 'bcc') {
        value = ((options as any)[key] as string[]).join(';');
        url = url + `${key}=${value}&`;
      } else {
        value = (options as any)[key];
        url = url + `${key}=${value}&`;
      }
    }
    window.open(url);
    return Promise.resolve({});
  }

  openDraft(): Promise<{}> {
    return this.open();
  }

  requestPermission(): Promise<{}> {
    return Promise.resolve({});
  }

  hasPermission(): Promise<{ value: boolean }> {
    return Promise.resolve({ value: true });
  }

  getDefaults(): Promise<{}> {
    return Promise.resolve(defaults);
  }

  getAliases(): Promise<{}> {
    return Promise.resolve({});
  }

  constructor() {
    super({
      name: 'EmailPlugin',
      platforms: ['web']
    });
  }
}

const EmailWeb = new EmailPluginWeb();

export { EmailWeb };
mergeWebPlugin(Plugins,EmailWeb);
