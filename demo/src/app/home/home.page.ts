import { Component, OnInit } from '@angular/core';
import { Email } from '@teamhive/capacitor-email';
import { AlertController } from '@ionic/angular';

@Component({
    selector: 'app-home',
    templateUrl: 'home.page.html',
    styleUrls: ['home.page.scss']
})
export class HomePage implements OnInit {
    email: Email;

    constructor(private alertController: AlertController) {
    }

    ngOnInit(): void {
        this.email = new Email();
    }

    async openEmail(app?: string) {
        try {
            await this.email.hasPermission();
        } catch (e) {
            await this.email.requestPermission();
        }

        try {
            await this.email.hasPermission();
            const hasAccount = await this.email.isAvailable({
                alias : app
            });
            if (hasAccount.hasAccount) {
                this.email.open({
                    to: ['fortune.osei@gmail.com', 'fortune.osei@hotmail.com'],
                    cc: ['fortune.osei@yahoo.com'],
                    bcc: ['osei.fortune@outlook.com'],
                    subject: 'Test',
                    body: 'Help',
                    app: app
                });
            } else {
                const alert = await this.alertController.create({
                    header: 'Email is not setup',
                    buttons: [
                        'OK'
                    ]
                });

                await alert.present();
            }
        } catch (e) {
            const alert = await this.alertController.create({
                header: 'Accounts permission required',
                buttons: [
                    'OK'
                ]
            });

            await alert.present();
        }
    }

    open() {
        this.openEmail();
    }

    openGmail() {
        this.openEmail('gmail');
    }

    openYahoo() {
        this.openEmail('yahoo');
    }

    openOutlook() {
        this.openEmail('outlook');
    }

    openDraft() {
        this.email.openDraft();
    }
}
