package cz.nejakejtomas.bluemapminimap.screen.config

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import cz.nejakejtomas.bluemapminimap.theme.MinimapTheme
import org.koin.compose.KoinContext

@Composable
fun ConfigApplication(modifier: Modifier = Modifier) {
    KoinContext {
        MinimapTheme {
            NavHost(rememberNavController(), Screen.General, modifier)
        }
    }
}