@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
package cz.nejakejtomas.bluemapminimap.screen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.intl.Locale
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import cz.nejakejtomas.bluemapminimap.screen.config.ConfigApplication
import cz.nejakejtomas.composelibrary.RichScreen
import net.minecraft.network.chat.Component
import org.jetbrains.compose.resources.*

@OptIn(InternalResourceApi::class)
internal val MyComposeEnvironment = object : ComposeEnvironment {
    @Composable
    override fun rememberEnvironment(): ResourceEnvironment {
        val composeLocale = Locale.current
        val composeTheme = isSystemInDarkTheme()
        val composeDensity = LocalDensity.current

        //cache ResourceEnvironment unless compose environment is changed
        return remember(composeLocale, composeTheme, composeDensity) {
            ResourceEnvironment(
//                LanguageQualifier("en-us"),
                LanguageQualifier(composeLocale.language),
                RegionQualifier(composeLocale.region),
                ThemeQualifier.selectByValue(composeTheme),
                DensityQualifier.selectByDensity(composeDensity.density)
            )
        }
    }
}

class ModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent ->
            val title = ""//runBlocking { getString(Res.string.config_title) }
            RichScreen(Component.literal(title), parent) {
//                LocalComposeEnvironment.current
                CompositionLocalProvider(org.jetbrains.compose.resources.LocalComposeEnvironment provides MyComposeEnvironment) {
                    ConfigApplication()
                }
            }
        }
    }
}