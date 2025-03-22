package project.mymessage.domain.repository.REST_API.interfaces
import okhttp3.ResponseBody
import project.mymessage.util.Constants.Companion.GITHUB_REPO_URL
import retrofit2.Response
import retrofit2.http.GET

interface GithubApi {

    @GET(GITHUB_REPO_URL)
    suspend fun getLatestRelease(): Response<ResponseBody>
}