package com.pira.ccloud.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FontSettings(
    val fontType: FontType = FontType.DEFAULT
) {
    companion object {
        val DEFAULT = FontSettings()
    }
}

@Serializable
enum class FontType {
    DEFAULT,
    IRANSANS,
    IRANCELL,
    ESTEDAD,
    FARHANG,
    GANDOM,
    MAHSA_AMINI,
    VAZIRMATN
}
