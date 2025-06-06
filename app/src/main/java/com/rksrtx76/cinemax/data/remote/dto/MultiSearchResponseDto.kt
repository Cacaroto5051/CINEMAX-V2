package com.rksrtx76.cinemax.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.rksrtx76.CINEMAX.model.Search

data class MultiSearchResponseDto(
    @SerializedName("page")
    val page : Int,
    @SerializedName("results")
    val results : List<Search>,
    @SerializedName("total_pages")
    val totalPages : Int,
    @SerializedName("total_results")
    val totalResults : Int,
)
