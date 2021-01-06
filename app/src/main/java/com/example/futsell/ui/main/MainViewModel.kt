package com.example.futsell.ui.main

import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainViewModel : ViewModel(), CoroutineScope {

    val dialogFragment = SingleLiveEvent<DialogFragment>()
    val showProgress = SingleLiveEvent<Boolean>()
    val playerLiveData = MutableLiveData<Player>()
    private val errorHandler = ErrorHandler()
    private val preferencesStorage = PreferencesStorage()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        hideProgress()
        throwable.printStackTrace()
        val errorMessage = errorHandler.handleError(throwable)
        if (errorMessage.messageId != null)
            showErrorMessage(errorMessage.messageId)
        else if (errorMessage.message != null)
            showErrorMessage(errorMessage.message)
    }

    override val coroutineContext = SupervisorJob() + coroutineExceptionHandler + Dispatchers.Main

    fun showErrorMessage(message: String) {
        dialogFragment.postValue(AlertDialogFragment.getInstance(message))
    }

    fun showErrorMessage(messageId: Int) {
        dialogFragment.postValue(AlertDialogFragment.getInstance(messageId))
    }

    fun showProgress() {
        showProgress.postValue(true)
    }

    fun hideProgress() {
        showProgress.postValue(false)
    }

    fun handleCoroutineException(throwable: Throwable) {
        coroutineExceptionHandler.handleException(coroutineContext, throwable)
    }

    private var loadPlayerJob: Job? = null

//    Partner ID: 60539
//Secret key: c6a6762b54172fe5abb6515697e057b9
//    Пример запроса (HTTP GET):
//
//https://www.futsell.ru/ffa19/api/pop/id/123/ts/1513454219/sign/1b2dcde742bc1479e00665e42593cc68/sku/FFA19PS4/
//
//Параметры:
//
//id — Partner ID
//ts — текущее время (unix timestamp)
//sign — подпись запроса: md5(id + secret key + ts)
//sku — платформа: FFA19PCC, FFA19PS4 или FFA19XBO
//Дополнительные параметры (передаются в виде GET-параметров):
//
//min_buy — минимальная цена игрока
//max_buy — максимальная цена игрока
//Ответ возвращает обычный JSON. Пример:
//
//{ "error": "", "message": "1 player popped", "player": {
// "tradeId": 205974847240,
// "startingBid": 241000,
// "buyNowPrice": 248000,
// "currentBid": 241000,
// "expires": 12345,
// "assetId": 208421,
// "name": "Saúl",
// "itemId": 67317285,
// "rating": 84,
// "preferredPosition": "CM"
// } }
//
//Пример пустого ответа:
//
//{ "error": "EMPTY", "message": "Queue is empty." }
//
//error — код ошибки (EMPTY, AUTH или SKU) — может быть пустым
//message — статус запроса или дополнительная информация об ошибке
//player — информация об игроке, которого вы взяли.

    private suspend fun loadAvailablePlayer() {
        val timestamp = System.currentTimeMillis().toString()
        val signString =
            preferencesStorage.getPartnerId() + preferencesStorage.getSecretKey() + timestamp
        val sign = md5(signString)
        val response =
            NetworkManager.loadAvailablePlayer(preferencesStorage.getPartnerId(), timestamp, sign)
        if (response.player != null) {
            hideProgress()
            loadPlayerJob?.cancel()
            showPlayerInfo(response.player)
        }
    }

    private fun showPlayerInfo(player: Player) {
        playerLiveData.postValue(player)
    }

    private fun md5(s: String): String {
        try {
            // Create MD5 Hash
            val digest = MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuffer()
            for (i in messageDigest.indices) hexString.append(
                Integer.toHexString(
                    0xFF and messageDigest[i].toInt()
                )
            )
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    fun onActivityStop(){
        loadPlayerJob?.cancel()
    }

    fun onActivityStart(){
        loadPlayerJob?.cancel()
        loadPlayerJob = launch {
            repeat(100) {
                showProgress()
                loadAvailablePlayer()
                hideProgress()
                delay(3000)
            }
        }
    }

}