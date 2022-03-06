package io.github.srtigers98.birthdaydiscordbot.application.service

import dev.kord.common.entity.Snowflake
import dev.kord.rest.service.RestClient
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate

/**
 * Service to read the saved birthdays every day and congratulate the guild member (reminds the other guild members).
 *
 * @author Benjamin Eder
 */
@Service
class ReminderService(
  private val restClient: RestClient,
  private val birthdayService: BirthdayService
) {

  /**
   * Check every day at 12 pm if any saved birthday is today.
   * If there are birthdays today, send a congratulation message to the guild member in the configured birthday channel in the guild.
   */
  @Scheduled(cron = "0 0 12 * * *")
  fun checkForBirthday() {
    val today = LocalDate.now()
    val birthdays = birthdayService.checkForBirthdayOn(today)

    birthdays.forEach {
      runBlocking {
        val channelId = Snowflake(it.guild.birthdayChannelId)
        val msg = restClient.channel.createMessage(channelId) {
          content = """
            |Happy Birthday ${it.mention}!
            |Congratulations to your ${today.year - it.birthdayYear}. birthday!
          """.trimMargin()
        }
        restClient.channel.createReaction(channelId, msg.id, "\uD83E\uDD73")
      }
    }
  }
}
