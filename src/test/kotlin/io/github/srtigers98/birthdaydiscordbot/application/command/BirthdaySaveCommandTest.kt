package io.github.srtigers98.birthdaydiscordbot.application.command

import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.User
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.core.entity.interaction.InteractionCommand
import dev.kord.core.entity.interaction.OptionValue
import io.github.srtigers98.birthdaydiscordbot.application.exception.BirthdayInFutureException
import io.github.srtigers98.birthdaydiscordbot.application.service.BirthdayService
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.format.DateTimeParseException

@ExtendWith(MockitoExtension::class)
internal class BirthdaySaveCommandTest {

  @InjectMocks
  private lateinit var tested: BirthdaySaveCommand

  @Mock
  private lateinit var birthdayService: BirthdayService

  @Test
  fun handleCommandTest() {
    val interaction: ChatInputCommandInteraction = mock()

    val userId = "42"
    val userMention = "@42"
    val channelId = "100"
    val userBirthday = "2000-01-01"

    val interactionCommand: InteractionCommand = mock()
    val birthdayOptionValue: OptionValue<String> = mock()
    val commandOptions = mapOf("birthday" to birthdayOptionValue)
    val interactionUser: User = mock()

    whenever(interaction.command)
      .thenReturn(interactionCommand)
    whenever(interactionCommand.options)
      .thenReturn(commandOptions)
    whenever(birthdayOptionValue.value)
      .thenReturn(userBirthday)
    whenever(interaction.user)
      .thenReturn(interactionUser)
    whenever(interactionUser.id)
      .thenReturn(Snowflake(userId))
    whenever(interactionUser.mention)
      .thenReturn(userMention)
    whenever(interaction.channelId)
      .thenReturn(Snowflake(channelId))

    whenever(birthdayService.save(userId, userMention, channelId, userBirthday))
      .thenAnswer { }

    val result = tested.handleCommand(interaction)

    assertThat(result, `is`(notNullValue()))
    assertThat(result.type, `is`(InteractionResponseType.ChannelMessageWithSource))
    assertThat(result.data.value, `is`(notNullValue()))
    assertThat(result.data.value?.content?.value, `is`(notNullValue()))

    verify(birthdayService, times(1))
      .save(userId, userMention, channelId, userBirthday)
  }

  @Test
  fun handleCommandInvalidFormatTest() {
    val interaction: ChatInputCommandInteraction = mock()

    val userId = "42"
    val userMention = "@42"
    val channelId = "100"
    val userBirthday = "2000-1-1"

    val interactionCommand: InteractionCommand = mock()
    val birthdayOptionValue: OptionValue<String> = mock()
    val commandOptions = mapOf("birthday" to birthdayOptionValue)
    val interactionUser: User = mock()

    whenever(interaction.command)
      .thenReturn(interactionCommand)
    whenever(interactionCommand.options)
      .thenReturn(commandOptions)
    whenever(birthdayOptionValue.value)
      .thenReturn(userBirthday)
    whenever(interaction.user)
      .thenReturn(interactionUser)
    whenever(interactionUser.id)
      .thenReturn(Snowflake(userId))
    whenever(interactionUser.mention)
      .thenReturn(userMention)
    whenever(interaction.channelId)
      .thenReturn(Snowflake(channelId))

    val result = tested.handleCommand(interaction)

    assertThat(result, `is`(notNullValue()))
    assertThat(result.type, `is`(InteractionResponseType.ChannelMessageWithSource))
    assertThat(result.data.value, `is`(notNullValue()))
    assertThat(result.data.value?.content?.value, `is`(notNullValue()))

    verify(birthdayService, times(0))
      .save(userId, userMention, channelId, userBirthday)
  }

  @Test
  fun handleCommandBirthdayInFutureTest() {
    val interaction: ChatInputCommandInteraction = mock()

    val userId = "42"
    val userMention = "@42"
    val channelId = "100"
    val userBirthday = "2999-12-31"

    val interactionCommand: InteractionCommand = mock()
    val birthdayOptionValue: OptionValue<String> = mock()
    val commandOptions = mapOf("birthday" to birthdayOptionValue)
    val interactionUser: User = mock()

    whenever(interaction.command)
      .thenReturn(interactionCommand)
    whenever(interactionCommand.options)
      .thenReturn(commandOptions)
    whenever(birthdayOptionValue.value)
      .thenReturn(userBirthday)
    whenever(interaction.user)
      .thenReturn(interactionUser)
    whenever(interactionUser.id)
      .thenReturn(Snowflake(userId))
    whenever(interactionUser.mention)
      .thenReturn(userMention)
    whenever(interaction.channelId)
      .thenReturn(Snowflake(channelId))

    whenever(birthdayService.save(userId, userMention, channelId, userBirthday))
      .thenThrow(BirthdayInFutureException())

    val result = tested.handleCommand(interaction)

    assertThat(result, `is`(notNullValue()))
    assertThat(result.type, `is`(InteractionResponseType.ChannelMessageWithSource))
    assertThat(result.data.value, `is`(notNullValue()))
    assertThat(result.data.value?.content?.value, `is`(notNullValue()))
  }

  @Test
  fun handleCommandParseExceptionTest() {
    val interaction: ChatInputCommandInteraction = mock()

    val userId = "42"
    val userMention = "@42"
    val channelId = "100"
    val userBirthday = "2000-13-01"

    val interactionCommand: InteractionCommand = mock()
    val birthdayOptionValue: OptionValue<String> = mock()
    val commandOptions = mapOf("birthday" to birthdayOptionValue)
    val interactionUser: User = mock()

    whenever(interaction.command)
      .thenReturn(interactionCommand)
    whenever(interactionCommand.options)
      .thenReturn(commandOptions)
    whenever(birthdayOptionValue.value)
      .thenReturn(userBirthday)
    whenever(interaction.user)
      .thenReturn(interactionUser)
    whenever(interactionUser.id)
      .thenReturn(Snowflake(userId))
    whenever(interactionUser.mention)
      .thenReturn(userMention)
    whenever(interaction.channelId)
      .thenReturn(Snowflake(channelId))

    whenever(birthdayService.save(userId, userMention, channelId, userBirthday))
      .thenThrow(DateTimeParseException("", "", -1))

    val result = tested.handleCommand(interaction)

    assertThat(result, `is`(notNullValue()))
    assertThat(result.type, `is`(InteractionResponseType.ChannelMessageWithSource))
    assertThat(result.data.value, `is`(notNullValue()))
    assertThat(result.data.value?.content?.value, `is`(notNullValue()))
  }
}
