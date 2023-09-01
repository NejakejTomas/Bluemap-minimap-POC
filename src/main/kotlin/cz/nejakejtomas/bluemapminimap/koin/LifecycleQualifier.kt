package cz.nejakejtomas.bluemapminimap.koin

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

enum class LifecycleQualifier : Qualifier {
    Server,
    World;

    override val value: QualifierValue
        get() = name
}