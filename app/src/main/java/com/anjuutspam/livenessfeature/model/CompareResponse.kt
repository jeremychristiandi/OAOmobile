package com.anjuutspam.livenessfeature.model

import com.google.gson.annotations.SerializedName

data class CompareResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)
