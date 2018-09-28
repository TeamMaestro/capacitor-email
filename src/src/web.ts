import { mergeWebPlugin, Plugins, WebPlugin } from '@capacitor/core';
import { AvailableOptions, AvailableResults, EmailOptions, IEmail } from './definitions';

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

    open(options?: EmailOptions | null): Promise<any> {
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

    openDraft(): Promise<any> {
        return this.open();
    }

    requestPermission(): Promise<any> {
        return Promise.resolve({});
    }

    hasPermission(): Promise<any> {
        return Promise.resolve();
    }

    getDefaults(): Promise<any> {
        return Promise.resolve(defaults);
    }

    getAliases(): Promise<any> {
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
mergeWebPlugin(Plugins, EmailWeb);
