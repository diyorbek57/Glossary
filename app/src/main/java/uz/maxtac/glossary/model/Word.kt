package uz.maxtac.glossary.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
@Parcelize
data class Word(
    val id: String = "",
    val theme: String = "",
    val word_de: String = "",
    val word_uz: String = ""
) : Parcelable
