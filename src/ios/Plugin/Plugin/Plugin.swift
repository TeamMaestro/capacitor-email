import Foundation
import Capacitor

@objc(EmailPlugin)
public class EmailPlugin: CAPPlugin {
    private var defaults:[String:String] = [:]
    private var aliases:[String:String] = [:]

    @objc func isAvailable(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.success([
            "value": value
        ])
    }

    @objc func open(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.success([
            "value": value
            ])
    }

    @objc func openDraft(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.success([
            "value": value
            ])
    }


    @objc func requestPermission(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.success([
            "value": value
            ])
    }


    @objc func hasPermission(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.success([
            "value": value
            ])
    }

    @objc func getDefaults(_ call: CAPPluginCall) {
        call.success(defaults)
    }

    @objc func getAliases(_ call: CAPPluginCall) {
        call.success(aliases)
    }
}
