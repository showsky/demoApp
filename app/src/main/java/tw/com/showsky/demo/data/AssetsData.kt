package tw.com.showsky.demo.data

import com.google.gson.annotations.SerializedName

/**
 * Created by showsky on 2022/5/2
 */
class AssetsData {

    @SerializedName("image_preview_url")
    var imageUrl: String? = null

    var name: String? = null

    @SerializedName("token_id")
    var tokenId: String? = null

    var contractAddress: String? = null

    var collectionName: String? = null
}
