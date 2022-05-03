package tw.com.showsky.demo.data

import com.google.gson.annotations.SerializedName

/**
 * Created by showsky on 2022/5/2
 */
class AssetData {

    var collectionName: String? = null

    @SerializedName("image_url")
    var imageUrl: String? = null

    var name: String? = null

    var description: String? = null

    var permalink: String? = null
}
