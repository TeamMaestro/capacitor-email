# Capacitor Email

## Install instructions

```bash
npm install @teamhive/capacitor-email
```

### Android aditional step

Register the Plugin in `MainActivity.java` file inside `onCreate` method:

```java
import com.meetmaestro.hive.capacitor.email.EmailPlugin;

...


@Override
public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);

  // Initializes the Bridge
  this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
    // Additional plugins you've installed go here
    // Ex: add(TotallyAwesomePlugin.class);
    add(EmailPlugin.class);
  }});
}
```

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
