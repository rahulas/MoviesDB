package com.rahulas.movies.data.network

import android.os.AsyncTask
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.rahulas.movies.data.network.Resource.Companion.error
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class NetworkBoundResource<ResultType, RequestType> {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        val dbSource = loadFromDb()
        result.addSource(dbSource, Observer { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(
                    dbSource,
                    Observer { newData ->
                        result.setValue(
                            Resource.success(newData)
                        )
                    }
                )
            }
        })

    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            result.value = Resource.loading(newData)
        }
        createCall().enqueue(object : Callback<RequestType> {
            override fun onResponse(call: Call<RequestType>, response: Response<RequestType>) {
                Log.i("Resultado de llamada: ", "ok")
                result.removeSource(dbSource)
                response.body()?.let { saveResultAndReInit(it) }
            }

            override fun onFailure(call: Call<RequestType>, t: Throwable) {
                Log.i("Resultado de llamada: ", "KO")
                onFetchFailed()
                result.removeSource(dbSource)
                result.addSource(
                    dbSource
                ) { newData: ResultType ->
                    result.setValue(
                        error(
                            t.message,
                            newData
                        )
                    )
                }

            }
        })
    }

    private fun saveResultAndReInit(response: RequestType) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                saveCallResult(response)
                return null
            }
            override fun onPostExecute(aVoid: Void?) {
                result.addSource(
                    loadFromDb()
                ) { newData: ResultType ->
                    result.setValue(
                        Resource.success(newData)
                    )
                }
            }
        }.execute()
        Log.i("Salvado en BBDD: ", "OK")
    }


    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected fun shouldFetch(data: ResultType?): Boolean{
        return true

    }

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    internal abstract fun createCall(): Call<RequestType>

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>
}