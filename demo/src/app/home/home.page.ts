import { Component, OnInit } from '@angular/core';
import { Email } from '@teamhive/capacitor-email';
@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss']
})
export class HomePage implements OnInit {
  email: Email;
  ngOnInit(): void {
    this.email = new Email();
  }

  async openEmail(app?: string) {
    const has = await this.email.hasPermission();
    if (!has) {
      await this.email.requestPermission();
    }
    this.email.open({
      to: ['fortune.osei@gmail.com', 'fortune.osei@hotmail.com'],
      cc: ['fortune.osei@yahoo.com'],
      bcc: ['osei.fortune@outlook.com'],
      subject: 'Test',
      body: 'Help',
      app: app
    });
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
