package tw.com.showsky.demo.model

import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by showsky on 2022/5/2
 */
class DataStatus<T> {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(DEFAULT, SUCCESS, ERROR, LOADING, EMPTY)
    annotation class Status

    companion object {
        const val DEFAULT = 1
        const val SUCCESS = 2
        const val ERROR = 3
        const val LOADING = 4
        const val EMPTY = 5
    }

    @Status
    private var mStatus = DEFAULT
    private var mData: T? = null
    private var mException: Exception? = null

    fun status(): Int {
        return mStatus
    }

    fun data(): T? {
        return mData
    }

    fun setData(data: T) {
        mData = data
    }

    fun success(data: T?): DataStatus<T> {
        mStatus = SUCCESS
        mData = data
        return this
    }

    fun error(): Exception? {
        return mException
    }

    fun error(exception: Exception?): DataStatus<T> {
        mStatus = ERROR
        mException = exception
        return this
    }

    fun loading(): DataStatus<T> {
        mStatus = LOADING
        return this
    }

    fun empty(): DataStatus<T> {
        mStatus = EMPTY
        return this
    }

    fun empty(data: T): DataStatus<T> {
        mStatus = EMPTY
        mData = data
        return this
    }
}
