package tw.com.showsky.demo.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import tw.com.showsky.demo.data.AssetData
import tw.com.showsky.demo.data.AssetDataDeserializer
import tw.com.showsky.demo.data.AssetsData
import tw.com.showsky.demo.data.AssetsDataDeserializer
import tw.com.showsky.demo.repository.OpenseaRepository

/**
 * Created by showsky on 2022/5/2
 */
class MainViewModel(private val openseaRepository: OpenseaRepository) : ViewModel() {

    companion object {
        const val SIZE = 20
    }

    private val mGson = GsonBuilder()
        .registerTypeAdapter(AssetsData::class.java, AssetsDataDeserializer())
        .registerTypeAdapter(AssetData::class.java, AssetDataDeserializer())
        .create()

    val assetsDataLiveData: DataStatusLiveData<List<AssetsData>> = DataStatusLiveData()
    val assetDataLiveData: DataStatusLiveData<AssetData> = DataStatusLiveData()

    class MainViewModelFactory(var openseaRepository: OpenseaRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(openseaRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    public fun loadAsset(contactAddress: String, tokenId: String) {

        assetDataLiveData.setLoading()

        viewModelScope.launch {
            openseaRepository.asset(contactAddress, tokenId)
                .onSuccess {

                    val data: AssetData = mGson.fromJson(
                        it,
                        AssetData::class.java
                    )

                    assetDataLiveData.postSuccess(data)

                }
                .onFail { exception, _ ->

                    assetDataLiveData.postError(exception)

                }
        }

    }

    public fun loadAssets(offset: Int = 0, limit: Int = SIZE) {

        assetsDataLiveData.setLoading()

        viewModelScope.launch {
            openseaRepository.assets(offset, limit, "0x19818f44faf5a217f619aff0fd487cb2a55cca65")
                .onSuccess {

                    val data: List<AssetsData> = mGson.fromJson(
                        it.asJsonObject["assets"],
                        object : TypeToken<ArrayList<AssetsData>>(){}.type
                    )

                    if (data.isEmpty()) {

                        assetsDataLiveData.postEmpty()

                    } else {

                        assetsDataLiveData.postSuccess(data)

                    }

                }
                .onFail { exception, _ ->

                    assetsDataLiveData.postError(exception)

                }
        }

    }
}