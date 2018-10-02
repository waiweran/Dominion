package cards.darkAges;

import java.io.File;

public class GenericKnight extends Knight {

	private static final long serialVersionUID = 172L;

	public GenericKnight() {
		super("Knights", "Action-Attack-Knight", 5, 0);
	}
	
	@Override
	public File getImage() {
		return new File("Images/Cards/Dark Ages/Knights/knightsrandomizer.jpg");
	}

	@Override
	public void gainAction() {
		throw new RuntimeException("Cannot gain generic knight card");
	}

	@Override
	public void specificAction() {}

}
