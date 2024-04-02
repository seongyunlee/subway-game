package site.kkrupp.subway.files.service

import com.amazonaws.AmazonServiceException
import com.amazonaws.SdkClientException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException


@Service
class FileService(
    private val amazonS3: AmazonS3
) {
    
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String? = null

    @Throws(IOException::class)
    fun uploadFile(multipartFile: MultipartFile): String {
        val fileName = multipartFile.originalFilename

        //파일 형식 구하기
        val ext = fileName!!.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        var contentType = ""

        when (ext) {
            "jpeg" -> contentType = "image/jpeg"
            "png" -> contentType = "image/png"
            "txt" -> contentType = "text/plain"
            "csv" -> contentType = "text/csv"
        }
        try {
            val metadata = ObjectMetadata()
            metadata.contentType = contentType

            amazonS3.putObject(
                PutObjectRequest(bucket, fileName, multipartFile.inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            )
        } catch (e: AmazonServiceException) {
            e.printStackTrace()
        } catch (e: SdkClientException) {
            e.printStackTrace()
        }

        //object 정보 가져오기
        val listObjectsV2Result = amazonS3.listObjectsV2(bucket)
        val objectSummaries = listObjectsV2Result.objectSummaries

        for (`object` in objectSummaries) {
            println("object = $`object`")
        }
        return amazonS3.getUrl(bucket, fileName).toString()
    }
}
