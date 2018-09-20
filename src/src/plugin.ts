import { IEmail, EmailOptions, AvailableOptions, AvailableResults } from './definitions';
import { Plugins } from '@capacitor/core';

const { EmailPlugin } = Plugins;

export class Email implements IEmail {

  isAvailable(options: AvailableOptions): Promise<AvailableResults> {
    return EmailPlugin.isAvailable(options);
  }
  open(options: EmailOptions | null): Promise<{}> {
    return EmailPlugin.open(options);
  }
  openDraft(): Promise<{}> {
    return EmailPlugin.openDraft();
  }
  requestPermission(): Promise<{}> {
    return EmailPlugin.requestPermission();
  }
  hasPermission(): Promise<{}> {
    return EmailPlugin.hasPermission();
  }
  getDefaults(): Promise<{}> {
    return EmailPlugin.getDefaults();
  }
  getAliases(): Promise<{}> {
   return EmailPlugin.getAliases();
  }
}
