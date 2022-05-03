package tw.com.showsky.demo.model

import androidx.lifecycle.MutableLiveData

/**
 * Created by showsky on 2022/5/2
 */
class DataStatusLiveData<T> : MutableLiveData<DataStatus<T>>() {

    fun setLoading(data: T) {
        val dataStatus = DataStatus<T>()
        dataStatus.setData(data)
        value = dataStatus.loading()
    }

    fun setLoading() {
        value = DataStatus<T>().loading()
    }

    fun postSuccess(data: T) {
        postValue(DataStatus<T>().success(data))
    }

    fun postEmpty() {
        postValue(DataStatus<T>().empty())
    }

    fun postEmpty(data: T) {
        postValue(DataStatus<T>().empty(data))
    }

    fun postError(error: Exception) {
        postValue(DataStatus<T>().error(error))
    }

    fun postError(data: T, error: Exception) {
        val dataStatus = DataStatus<T>()
        dataStatus.setData(data)
        postValue(dataStatus.error(error))
    }
}
