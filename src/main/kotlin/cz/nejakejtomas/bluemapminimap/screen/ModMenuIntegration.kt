package cz.nejakejtomas.bluemapminimap.screen

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import org.koin.mp.KoinPlatform.getKoin

// Cannot be KoinComponent because it is entrypoint...
class ModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent ->
            val configScreen: ConfigScreen = getKoin().get()
            configScreen.create(parent)
        }
    }
}