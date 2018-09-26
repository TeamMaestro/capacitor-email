declare global {
  interface PluginRegistry {
    EmailPlugin?: IEmail;
  }
}

export interface IEmail {
  isAvailable(options?: AvailableOptions): Promise<AvailableResults>;
  open(options?: EmailOptions): Promise<{}>;
  openDraft(): Promise<{}>;
  requestPermission(): Promise<{}>;
  hasPermission(): Promise<{}>;
  getDefaults(): Promise<{}>;
  getAliases(): Promise<{}>;
}

export interface AvailableOptions {
  alias?: string;
}

export interface AvailableResults {
  hasApp?: boolean;
  hasAccount: boolean;
}

export interface EmailOptions {
  to?: Array<string>; // email addresses for TO field
  cc?: Array<string>; // email addresses for CC field
  bcc?: Array<string>; // email addresses for BCC field
  attachments?: Array<string>; // file paths or base64 data streams
  subject?: String; // subject of the email
  body?: String; // email body (for HTML, set isHtml to true)
  isHtml?: Boolean; // indicates if the body is HTML or plain text
  type?: String; // content type of the email (Android only)
  app?: string;
}
