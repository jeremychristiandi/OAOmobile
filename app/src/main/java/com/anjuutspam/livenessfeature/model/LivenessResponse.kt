package com.anjuutspam.livenessfeature.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class LivenessResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
) : Parcelable {

	constructor(parcel: Parcel) : this(
		parcel.readParcelable(Data::class.java.classLoader),
		parcel.readParcelable(Meta::class.java.classLoader)
	)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeParcelable(data, flags)
		parcel.writeParcelable(meta, flags)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<LivenessResponse> {
		override fun createFromParcel(parcel: Parcel): LivenessResponse {
			return LivenessResponse(parcel)
		}

		override fun newArray(size: Int): Array<LivenessResponse?> {
			return arrayOfNulls(size)
		}
	}
}

data class Data(

	@field:SerializedName("image_path")
	val imagePath: String? = null,

	@field:SerializedName("liveness_status_conf")
	val livenessStatusConf: Any? = null,

	@field:SerializedName("liveness_status")
	val livenessStatus: String? = null,

	@field:SerializedName("image_id")
	val imageId: String? = null,

	@field:SerializedName("result_path")
	val resultPath: String? = null,

	@field:SerializedName("speed")
	val speed: Any? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString(),
		parcel.readValue(Any::class.java.classLoader),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readValue(Any::class.java.classLoader)
	)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(imagePath)
		parcel.writeValue(livenessStatusConf)
		parcel.writeString(livenessStatus)
		parcel.writeString(imageId)
		parcel.writeString(resultPath)
		parcel.writeValue(speed)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Data> {
		override fun createFromParcel(parcel: Parcel): Data {
			return Data(parcel)
		}

		override fun newArray(size: Int): Array<Data?> {
			return arrayOfNulls(size)
		}
	}
}

data class Meta(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readString()
	)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(code)
		parcel.writeString(message)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Meta> {
		override fun createFromParcel(parcel: Parcel): Meta {
			return Meta(parcel)
		}

		override fun newArray(size: Int): Array<Meta?> {
			return arrayOfNulls(size)
		}
	}
}
