import UIKit
import ComposeApp

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?
    let urlCallback = IosUrlCallback()

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        window = UIWindow(frame: UIScreen.main.bounds)
        if let window = window {
            window.rootViewController = AppViewControllerKt.AppViewController(urlCallback: urlCallback)
            window.makeKeyAndVisible()
        }
        return true
    }
    
    func application(
        _ application: UIApplication,
        open url: URL,
        options: [UIApplication.OpenURLOptionsKey: Any] = [:]
    ) -> Bool {
        urlCallback.onRedirect(url.absoluteString)
        return true
    }
}
