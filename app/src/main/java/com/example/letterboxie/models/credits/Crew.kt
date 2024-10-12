package com.example.letterboxie.models.credits

import com.example.letterboxie.models.base.MovieCredits
import com.google.gson.annotations.SerializedName

data class Crew(
    @SerializedName("adult")
    val adult : Boolean?,
    @SerializedName("credit_id")
    val creditId : String?,
    @SerializedName("department")
    val department : String?,
    @SerializedName("gender")
    val gender : Int?,
    @SerializedName("id")
    val id : Int?,
    @SerializedName("job")
    override val character : String?,
    @SerializedName("known_for_department")
    val knownForDepartment : String?,
    @SerializedName("name")
    override val name : String?,
    @SerializedName("original_name")
    val originalName : String?,
    @SerializedName("popularity")
    val popularity : Double?,
    @SerializedName("profile_path")
    override val profilePath : String?
) : MovieCredits()