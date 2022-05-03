package tw.com.showsky.demo.model

import com.google.gson.JsonElement
import tw.com.showsky.demo.excpetion.JsonParseException

/**
 * Created by showsky on 2022/5/2
 */
sealed class DataResult {

    class Success(val jsonElement: JsonElement): DataResult()
    class Fail(val exception: Exception, val body: String): DataResult()

    inline fun onSuccess(action: (JsonElement) -> Unit): DataResult {
        if (this is Success) {
            try {
                action(jsonElement)
            } catch (e: Exception) {
                return Fail(JsonParseException(e), jsonElement.toString())
            }
        }
        return this
    }

    inline fun onFail(action: (Exception, String) -> Unit): DataResult {
        if (this is Fail) action(exception, body)
        return this
    }
}
