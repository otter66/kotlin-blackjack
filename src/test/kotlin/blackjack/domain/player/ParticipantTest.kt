package blackjack.domain.player

import blackjack.domain.card.Card
import blackjack.domain.card.CardNumber
import blackjack.domain.card.CardShape
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ParticipantTest {

    @Test
    fun `카드를 발급 받을 수 있는지 확인한다`() {
        val participant = Participant("aaa")
        participant.addCard(Card(CardNumber.EIGHT, CardShape.CLOVER))
        val actual = participant.canHit()
        assertThat(actual).isEqualTo(true)
    }
}
