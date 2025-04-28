package cz.nejakejtomas.bluemapminimap.model

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// Minecraft URL of server
@JvmInline
@Serializable
value class ServerId(val url: String) {
    companion object {
        val ParameterType =
            object : NavType<ServerId>(isNullableAllowed = false) {
                override fun get(bundle: SavedState, key: String): ServerId? =
                    bundle.read { ServerId(getString(key)) }


                override fun parseValue(value: String): ServerId =
                    Json.decodeFromString(value)


                override fun put(bundle: SavedState, key: String, value: ServerId) =
                    bundle.write { putString(key, serializeAsValue(value)) }


                override fun serializeAsValue(value: ServerId): String = Json.encodeToString(value)
            }
    }
}