package io.eugenethedev.taigamobile.dagger

import io.eugenethedev.taigamobile.TaigaApp

class DaggerAppComponent {
    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {
        fun context(taigaApp: TaigaApp): Builder {
            return this
        }

        fun build(): AppComponent {
            TODO("Not yet implemented")
        }
    }
}
