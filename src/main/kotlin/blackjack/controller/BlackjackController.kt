package blackjack.controller

import blackjack.domain.card.MultiDeck
import blackjack.domain.player.Dealer
import blackjack.domain.player.Participants
import blackjack.domain.result.MatchResult
import blackjack.view.InputView
import blackjack.view.OutputView

class BlackjackController(
    private val inputView: InputView = InputView(),
    private val outputView: OutputView = OutputView()
) {

    fun run() {
        val deck: MultiDeck = MultiDeck()
        val dealer: Dealer = Dealer()
        val participants = inputView.readParticipants()

        setFirstTurnPlayersCards(dealer, participants, deck)
        hitPlayersCards(dealer, participants, deck)
        printGameResult(dealer, participants)
    }

    private fun setFirstTurnPlayersCards(dealer: Dealer, participants: Participants, deck: MultiDeck) {
        dealer.setFirstTurnCards(deck)
        participants.values.forEach { it.setFirstTurnCards(deck) }
        outputView.printFirstTurnSettingCards(dealer, participants)
    }

    private fun hitPlayersCards(dealer: Dealer, participants: Participants, deck: MultiDeck) {
        participants.values.forEach { participant ->
            while (participant.canHit() && inputView.readHitOrNot(participant.name)) {
                participant.addCard(deck.draw())
                outputView.printPlayerCards(participant, "")
            }
        }

        while (dealer.canHit()) {
            outputView.printDealerHitOrNotMessage(dealer.canHit())
            dealer.addCard(deck.draw())
        }
    }

    private fun printGameResult(dealer: Dealer, participants: Participants) {
        val participantsResults: List<MatchResult> = getParticipantsResults(dealer, participants)
        val dealerResult: MatchResult = MatchResult()
        participants.values.forEach { dealerResult.count(dealer.matchGameResult(it)) }

        outputView.printSumResult(dealer, participants)
        outputView.printPlayersResults(
            dealer, dealerResult,
            participants, participantsResults
        )
    }

    private fun getParticipantsResults(dealer: Dealer, participants: Participants): List<MatchResult> {
        val results: List<MatchResult> = List(participants.values.size) { MatchResult() }
        participants.values.forEachIndexed { index, it ->
            results[index].count(it.matchGameResult(dealer))
        }
        return results
    }
}
