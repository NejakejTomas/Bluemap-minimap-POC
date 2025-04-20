package cz.nejakejtomas.bluemapminimap

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

class GlobalViewModelStoreOwner : ViewModelStoreOwner {
    override val viewModelStore = ViewModelStore()
}