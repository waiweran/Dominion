package computerPlayer;

import gameBase.DominionGame;
import gameBase.Player;

/**
 * Initializes the desired instance of a ComputerPlayer based on a name.
 * @author Nathaniel
 * @version 05-04-2017
 */
public class CPUFactory {
		
	private final DominionGame access;

	/**
	 * Initializes the CPU Factory.
	 * @param game the DominionGame.
	 */
	public CPUFactory(DominionGame game) {
		access = game; 
	}

	/**
	 * Creates a new computer player of the specified type.
	 * @param player the Player this will play for.
	 * @param cpuType String representing the NPC type.
	 * @return the ComputerPlayer.
	 */
	public ComputerPlayer getComputerPlayer(Player player, String cpuType) {
		if(cpuType.equals("Big Money") || cpuType.equals("BigMoney")) {
			return new BigMoneyPlayer(player, access);
		}
		if(cpuType.equals("Ratio")) {
			return new RatioPlayer(player, access);
		}
		if(cpuType.equals("Random")) {
			return new RandomPlayer(player, access);
		}
		if(cpuType.equals("AI") || cpuType.equals("ML")) {
			if(access.getClient().getPlayerNumber() > 1) {
				return new WebMLPlayer(player, access);
			}
			return new MLPlayer(player, access);
		}
		if(cpuType.equals("Stdio")) {
			return new StdioPlayer(player, access);
		}
		throw new RuntimeException("Unsupported Computer Player Type " + cpuType);
	}

}
