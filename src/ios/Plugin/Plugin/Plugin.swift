import Foundation
import Capacitor
import MessageUI
import MobileCoreServices
@objc(EmailPlugin)
public class EmailPlugin: CAPPlugin , MFMailComposeViewControllerDelegate, UINavigationControllerDelegate{
    private var defaults:[String:Any] = [:]
    private var aliases:[String:String] = [:]

    public override func load() {
        let emptyArray:[String] = []
        aliases["gmail"] = "googlegmail:///co"
        aliases["outlook"] = "ms-outlook://compose"
        aliases["yahoo"] = "ymail://mail/compose"
        defaults["to"] = emptyArray
        defaults["cc"] = emptyArray
        defaults["bcc"] = emptyArray
        defaults["attachments"] = emptyArray
        defaults["subject"] = ""
        defaults["isHtml"] = true
        defaults["type"] = "message/rfc822"
        defaults["chooserHeader"] = "Open with"
    }

    @objc func isAvailable(_ call: CAPPluginCall) {
        var obj: [String:Any] = [:]
        let alias = call.getString("alias") ?? "mailto://"
        if(!alias.contains("mailto") ){
            let app = aliases[alias]
            obj["hasApp"] = UIApplication.shared.canOpenURL(URL(string: app!)!)
        }
        obj["hasAccount"] = MFMailComposeViewController.canSendMail()
        call.success(obj)
    }

    public func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: true, completion: nil)
    }




    @objc func open(_ call: CAPPluginCall) {
        let emptyArray:[String] = []
        let to = call.getArray("to", String.self) ?? emptyArray
        let cc = call.getArray("cc", String.self) ?? emptyArray
        let bcc = call.getArray("bcc", String.self) ?? emptyArray
        let attachments = call.getArray("attachments", String.self) ?? emptyArray
        var subject = call.getString("subject") ?? ""
        var body = call.getString("body") ?? ""
        let isHtml = call.getBool("isHtml") ?? true
        _ = call.getString("type")
        let app = aliases[call.getString("app") ?? ""] ?? "mailto://"
        let chooserHeader = call.getString("chooserHeader") ?? "Open with"
        if(!app.contains("mailto") ){
            var toString = ""
            var ccString = ""
            var bccString = ""
            if(subject != ""){
                subject = "subject=" + subject + "&"
            }

            if(body != ""){
                body = "body=" + body + "&"
            }
            if(to.count > 0 ){
                for email in to{
                    toString.append(email + ";")
                }
                toString.append("&")
                toString = "to=" + toString
            }

            if(cc.count > 0){
                for emailCC in cc {
                    ccString.append(emailCC + ";")
                }
                ccString.append("&")
                ccString = "cc=" + ccString
            }

            if(bcc.count > 0){
                for emailBCC in bcc {
                    bccString.append(emailBCC + ";")
                }
                bccString.append("&")
                bccString = "bcc=" + bccString
            }

            let url = URL(string: app + "?" + toString + ccString + bccString + body + subject)

            DispatchQueue.main.async {
                UIApplication.shared.open(url!, options: [:], completionHandler: nil)
            }

        }else{
            DispatchQueue.main.async {
                let mail = MFMailComposeViewController()
                mail.title = chooserHeader
                mail.delegate = self;
                mail.setSubject(subject)
                mail.setToRecipients(to)
                mail.setCcRecipients(cc)
                mail.setBccRecipients(bcc)
                mail.setMessageBody(body, isHTML: isHtml)
                for attachment in attachments {
                    var path: String?
                    if(attachment.starts(with: "_capacitor_")){
                        path = attachment.replacingOccurrences(of: "_capacitor_", with: "file://")
                    }else if(attachment.starts(with: "/")){
                        path = "file://" + attachment
                    }
                    let file = NSURL(string: path!)
                    let ext = file?.pathExtension
                    let uti = UTTypeCreatePreferredIdentifierForTag(
                        kUTTagClassFilenameExtension,
                        ext! as CFString,
                        nil
                    )
                    let mime = uti?.takeRetainedValue() as! String
                    let data = NSData(contentsOf: (file?.absoluteURL!)!)
                    mail.addAttachmentData(data as! Data, mimeType: mime, fileName: (file?.lastPathComponent)!)
                }

                self.bridge.viewController.present(mail, animated: true, completion: nil)

            }

        }

        call.resolve()
    }

    @objc func openDraft(_ call: CAPPluginCall) {
        self.open(call)
    }


    @objc func requestPermission(_ call: CAPPluginCall) {
        call.success()
    }


    @objc func hasPermission(_ call: CAPPluginCall) {
        call.success()
    }

    @objc func getDefaults(_ call: CAPPluginCall) {
        call.success(defaults)
    }

    @objc func getAliases(_ call: CAPPluginCall) {
        call.success(aliases)
    }
}
