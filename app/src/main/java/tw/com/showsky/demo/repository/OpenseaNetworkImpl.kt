package tw.com.showsky.demo.repository

import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import tw.com.showsky.demo.Config
import tw.com.showsky.demo.excpetion.ApiException
import tw.com.showsky.demo.excpetion.JsonParseException
import tw.com.showsky.demo.excpetion.NetworkException
import tw.com.showsky.demo.model.DataResult
import java.io.IOException

/**
 * Created by showsky on 2022/5/2
 */
class OpenseaNetworkImpl: OpenseaRepository {

    private var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->

            val request = chain.request().newBuilder()
                .addHeader("X-API-KEY", Config.OPENSEA_KEY)
                .build()

            return@Interceptor chain.proceed(request)

        })
        .build()

    private fun parseJson(body: String): DataResult {

        return try {

            val jsonElement = JsonParser.parseString(body)

            DataResult.Success(jsonElement)

        } catch (e: JsonSyntaxException) {

            DataResult.Fail(JsonParseException("Json parse error"), body)

        }

    }

    override suspend fun assets(offset: Int, limit: Int, owner: String): DataResult {

        val url = (Config.OPENSEA_END_POINT + "/assets").toHttpUrl()
            .newBuilder()
            .addQueryParameter("offset", offset.toString())
            .addQueryParameter("limit", limit.toString())
            .addQueryParameter("owner", owner)
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        return withContext(Dispatchers.IO) {

            try {

                return@withContext okHttpClient.newCall(request).execute().use { response ->

                    val body = response.body.let {
                        if (it == null) {
                            return@let ""
                        }

                        return@let it.string()
                    }

                    if (!response.isSuccessful) {
                        return@use DataResult.Fail(ApiException("API error"), body)
                    }

                    if (body.isEmpty()) {
                        return@use DataResult.Fail(JsonParseException("JSON body empty"), "")
                    }

                    return@use parseJson(body)
                }

            } catch (e: IOException) {

                return@withContext DataResult.Fail(NetworkException("Network error"), "")

            }

        }
    }

    override suspend fun asset(contractAddress: String, tokenId: String): DataResult {

        val request = Request.Builder()
            .url(Config.OPENSEA_END_POINT + "/asset/${contractAddress}/${tokenId}")
            .build()

        return withContext(Dispatchers.IO) {

            try {

                return@withContext okHttpClient.newCall(request).execute().use { response ->

                    val body = response.body.let {
                        if (it == null) {
                            return@let ""
                        }

                        return@let it.string()
                    }

                    if (!response.isSuccessful) {
                        return@use DataResult.Fail(ApiException("API error"), body)
                    }

                    if (body.isEmpty()) {
                        return@use DataResult.Fail(JsonParseException("JSON body empty"), "")
                    }

                    return@use parseJson(body)
                }

            } catch (e: IOException) {

                return@withContext DataResult.Fail(NetworkException("Network error"), "")

            }

        }

    }
}
