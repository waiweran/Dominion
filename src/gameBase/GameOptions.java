package gameBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameOptions {
	
	private int players;
	private List<String> npc;
	private boolean online;
	private boolean showGraphics;

	public GameOptions(boolean isOnline) {
		npc = new ArrayList<>();
		online = isOnline;
		showGraphics = true;
	}
	
	public void setNumPlayers(int numPlayers) {
		players = numPlayers;
	}
	
	public void setNPC(List<String> npcTypes) {
		npc = npcTypes;
	}
	
	public void hideGraphics() {
		showGraphics = false;
	}
	
	public int getNumPlayers() {
		return players;
	}
	
	public int getNumNPC() {
		return npc.size();
	}
	
	public List<String> getNPCTypes() {
		return Collections.unmodifiableList(npc);
	}
	
	public boolean isOnline() {
		return online;
	}
	
	public boolean showGraphics() {
		return showGraphics;
	}

}
