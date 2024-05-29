package site.kkrupp.subway.webhook

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class DiscordWebhookTest{

        @Autowired
        lateinit var discordWebhook: DiscordWebhook

        @Test
        fun sendMessage(){
            discordWebhook.sendMessage("Test")
        }
}