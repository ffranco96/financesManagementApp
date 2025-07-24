package com.example.financesmanagementapp.ui.home.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.financesmanagementapp.ui.home.data.BinanceRepository
import com.example.financesmanagementapp.ui.home.di.ServiceLocator
import com.example.financesmanagementapp.utils.Constants
import javax.inject.Inject

class UpdateCryptoactivesWorker (
    appContext: Context,
    workerParams: WorkerParameters
): CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val useCase = ServiceLocator.getBtcPriceUseCase
        // ver que valor trae, por que el useCase null es por
        return if (useCase != null) {
            val price = useCase.invoke(Constants.BTC_USDT_TICKER)
            Log.d("PriceWorker", "Precio BTC: $price")
            Result.success()
        } else {
            Log.e("PriceWorker", "UseCase no inicializado")
            Result.retry()
        }
    }
}