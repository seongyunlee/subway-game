package site.kkrupp.subway.files.dto

import org.springframework.web.multipart.MultipartFile

data class FileDto(
    val title: String,
    val url: String,
    val file: MultipartFile
)