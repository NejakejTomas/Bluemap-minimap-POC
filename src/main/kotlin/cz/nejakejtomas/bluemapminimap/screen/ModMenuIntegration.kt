package cz.nejakejtomas.bluemapminimap.screen

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import cz.nejakejtomas.bluemapminimap.screen.config.ConfigApplication
import cz.nejakejtomas.composelibrary.RichScreen
import net.minecraft.network.chat.Component

class ModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent ->
            val title = ""//runBlocking { getString(Res.string.config_title) }
            RichScreen(Component.literal(title), parent) {
                ConfigApplication()
            }
        }
    }
}