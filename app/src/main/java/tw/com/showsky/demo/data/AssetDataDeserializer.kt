package tw.com.showsky.demo.data

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * Created by showsky on 2022/5/3
 */
class AssetDataDeserializer : JsonDeserializer<AssetData> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): AssetData {

        val gson = Gson()

        val assetsData = gson.fromJson(
            json,
            AssetData::class.java
        )

        return assetsData

    }
}
