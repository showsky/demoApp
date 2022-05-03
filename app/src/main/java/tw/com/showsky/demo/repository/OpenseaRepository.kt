package tw.com.showsky.demo.repository

import tw.com.showsky.demo.model.DataResult

/**
 * Created by showsky on 2022/5/2
 */
interface OpenseaRepository {

    suspend fun assets(offset: Int, limit: Int, owner: String): DataResult

    suspend fun asset(contractAddress: String, tokenId: String): DataResult
}
