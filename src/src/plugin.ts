import { IEmail, EmailOptions, AvailableOptions, AvailableResults } from './definitions';
import { Plugins } from '@capacitor/core';

const { EmailPlugin } = Plugins;

export class Email implements IEmail {

  isAvailable(options?: AvailableOptions): Promise<AvailableResults> {
    return EmailPlugin.isAvailable(options);
  }
  open(options?: EmailOptions):Promise<any> {
    return EmailPlugin.open(options);
  }
  openDraft(): Promise<any> {
    return EmailPlugin.openDraft();
  }
  requestPermission(): Promise<any> {
    return EmailPlugin.requestPermission();
  }
  hasPermission(): Promise<any> {
    return EmailPlugin.hasPermission();
  }
  getDefaults(): Promise<any> {
    return EmailPlugin.getDefaults();
  }
  getAliases(): Promise<any> {
   return EmailPlugin.getAliases();
  }
}
