package cz.nejakejtomas.bluemapminimap.screen

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import cz.nejakejtomas.bluemapminimap.Mod.getKoin

// Cannot be KoinComponent because it is entrypoint...
class ModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent ->
            // TODO
            val configScreen: ConfigScreen = getKoin().get()
            configScreen.create(parent)
        }
    }
}