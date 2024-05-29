package site.kkrupp.subway.webhook

import org.aspectj.bridge.Message
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Service
class DiscordWebhook {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Value("\${discord.webhook.alert.url}")
    private val webhookUrl: String? = null


    fun sendMessage(message: String) {
        try {
            val header = HttpHeaders();
            header.set("Content-Type", "application/json; utf-8")

            val body = mapOf("content" to message)

            val request = HttpEntity(body, header)

            val template = RestTemplate()

            template.exchange(webhookUrl!!, HttpMethod.POST, request, String::class.java)

        }catch (e: Exception){
            logger.error("Failed to send message to Discord Webhook", e)
        }
    }

}