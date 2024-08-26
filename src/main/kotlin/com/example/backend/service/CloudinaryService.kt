package com.example.backend.service


import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class CloudinaryService(
    @Value("\${cloudinary.cloud-name}") cloudName: String,
    @Value("\${cloudinary.api-key}") apiKey: String,
    @Value("\${cloudinary.api-secret}") apiSecret: String
) {

    private val cloudinary: Cloudinary = Cloudinary(ObjectUtils.asMap(
        "cloud_name", cloudName,
        "api_key", apiKey,
        "api_secret", apiSecret
    ))

    fun uploadImage(file: MultipartFile): String {
        val uploadResult = cloudinary.uploader().upload(file.bytes, ObjectUtils.emptyMap())
        return uploadResult["url"].toString()
    }
}
