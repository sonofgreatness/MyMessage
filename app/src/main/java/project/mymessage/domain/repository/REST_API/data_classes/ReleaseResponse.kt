package project.mymessage.domain.repository.REST_API.data_classes

data class ReleaseResponse(
    val tag_name: String,
    val assets: List<Asset>
)
