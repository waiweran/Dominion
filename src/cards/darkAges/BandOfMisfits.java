package cards.darkAges;

import cards.Card;
import selectors.SupplySelector;

public class BandOfMisfits extends Card {

	private static final long serialVersionUID = 157L;

	private Card whatIAm;

	public BandOfMisfits() {
		super("Band of Misfits", "Action", "Dark Ages", 5);
	}

	@Override
	public void performAction() {
		whatIAm = null;
		SupplySelector sd = new SupplySelector(getGame(), getName(), 
				"Select a Card for Band of Misfits to act as", 0, getCost() - 1);
		sd.setCardSelector(c -> c.isAction());
		whatIAm = sd.getSelectedSupply().getTopCard();
		whatIAm.passGame(getGame());
		whatIAm.passPlayer(getPlayer());
		if(whatIAm != null) whatIAm.performAction();
	}

	@Override
	public void respondGain(Card c) {
		if(whatIAm != null) whatIAm.respondGain(c);
	}

	@Override
	public void tavernAction() {
		if(whatIAm != null) whatIAm.tavernAction();
	}

	@Override
	public void trashAction() {
		if(whatIAm != null) whatIAm.trashAction();
		whatIAm = null;
	}

	@Override
	public void durationAction() {
		whatIAm.durationAction();
		whatIAm = null;
	}

	@Override
	public void cleanupAction() {
		whatIAm.cleanupAction();
		if(!whatIAm.isDuration()) whatIAm = null;
	}

	@Override
	public boolean isDuration() {
		if(whatIAm != null) return whatIAm.isDuration();
		return super.isDuration();
	}

	@Override
	public boolean isAttack() {
		return whatIAm.isAttack();
	}

}
