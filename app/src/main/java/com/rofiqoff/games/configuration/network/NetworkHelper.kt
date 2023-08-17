package com.rofiqoff.games.configuration.network

import com.rofiqoff.games.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object NetworkHelper {

    fun <T> createRequest(fetchCall: suspend () -> T): Flow<AppResponse<T>> = flow {
        emit(AppResponse.createSuccess(fetchCall.invoke()))
    }.catch {
        emit(AppResponse.createError(Constants.GENERAL_ERROR_MESSAGE))
    }.flowOn(Dispatchers.IO)

}
