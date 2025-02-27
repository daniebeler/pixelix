package com.daniebeler.pfpixelix.domain.service.preferences

import com.daniebeler.pfpixelix.domain.model.AppThemeMode
import com.russhwolf.settings.Settings
import com.russhwolf.settings.boolean
import com.russhwolf.settings.int
import me.tatarka.inject.annotations.Inject

@Inject
class UserPreferences(settings: Settings) {

    var hideSensitiveContent by settings.boolean("k_hide_sensitive_content", true)
    var focusMode by settings.boolean("k_focus_mode", false)
    var hideAltTextButton by settings.boolean("k_hide_alt_text_button", false)
    var useInAppBrowser by settings.boolean("k_use_in_app_browser", true)

    var appThemeMode by settings.int("k_theme_mode", AppThemeMode.FOLLOW_SYSTEM)
}