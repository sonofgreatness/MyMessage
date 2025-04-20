package project.mymessage.ui.viewModels

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import project.mymessage.domain.repository.REST_API.data_classes.Asset
import project.mymessage.domain.repository.REST_API.data_classes.ReleaseResponse
import project.mymessage.domain.repository.REST_API.interfaces.GithubApi
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val context : Application,
    val sharedPreferences: SharedPreferences,
    private val api: GithubApi
) : ViewModel() {


    private val _downloadUrl = MutableLiveData<String>()
    private val downloadUrl: LiveData<String>  get() = _downloadUrl

    private val _latestVersion = MutableLiveData<String>()
    val latestVersion: LiveData<String> get() = _latestVersion

    private val _isUpdateAvailable = MutableLiveData(false)
    val isUpdateAvailable: LiveData<Boolean> get() = _isUpdateAvailable

    init{
        checkForUpdates()
    }


    fun checkForUpdates() {

        viewModelScope.launch {
            try {
                Log.d("breakpoints", "checkForUpdates")
                val response = api.getLatestRelease()
                if (response.isSuccessful) {
                    response.body()?.string()?.let { json ->
                        val jsonResponse = JSONObject(json)
                        Log.d("breakpoints", " json  => $json")
                        val assetsList = jsonResponse.getJSONArray("assets")
                        val assets = mutableListOf<Asset>()
                        for (i in 0 until assetsList.length()) {
                            val assetJson = assetsList.getJSONObject(i)
                            assets.add(Asset(assetJson.getString("browser_download_url")))
                        }
                        val releaseResponse = ReleaseResponse(
                            tag_name = jsonResponse.getString("tag_name"),
                            assets = assets
                        )

                        _latestVersion.value = releaseResponse.tag_name
                        _downloadUrl.value = releaseResponse.assets.firstOrNull()?.browser_download_url ?: ""

                        val currentVersion = sharedPreferences.getString("app_version", "v1.0") ?: "v1.0"
                        _isUpdateAvailable.value = _latestVersion.value!! > currentVersion
                    }
                }else{
                    Log.d("breakpoints", "errroLite ${response.message()}")
                }
            } catch (e: Exception) {
                Log.d("breakpoints", "error ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun downloadUpdate() {
        val request = DownloadManager.Request(Uri.parse(downloadUrl.value))
            .setTitle("Downloading Update")
            .setDescription("Downloading new version...")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "app-latest.apk")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }
}