# Capacitor Email


## Usage


```ts
import { Email } from '@teamhive/capacitor-email';

const email = new Email();

const hasPermission = await email.hasPermission();

if(!hasPermission){
    await email.requestPermission();
}

const available = await email.isAvailable({
      alias: 'gmail' // gmail, outlook, yahoo *optional*,
});

// available.hasAccount  *If email is setup*
// available.hasApp  *If device has alias supplied*


if(available.hasAccount){
    email.open({
    to:['me@myemail.com'],
    cc: ['bro@hisemail.com'],
    bcc: ['sis@heremail.com'],
    subject: 'Party',
    body: 'Hi bring drinks...',
    isHtml: false,
    attachments: [SomeFilePath]
    })
}
```


## Api

| Method                                   | Default | Type                         | Description                                           |
| ---------------------------------------- | ------- | ---------------------------- | ----------------------------------------------------- |
| isAvailable(options?: AvailableOptions)            |         | `Promise<AvailableResults>`                     |  |
| open(options?: EmailOptions)                    |         | `Promise<any>`                 |                    |
| openDraft()   |         | `Promise<any>` |                               |  |
| requestPermission()                    |         | `Promise<any>`                       |                               |
| hasPermission()                      |         | `Promise<any>`                       |                              |
| getDefaults()                      |         | `Promise<any>`                       |                                |
| getAliases()                    |         | `Promise<any>`                       |                                |
