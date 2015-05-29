package com.runescape.api.hiscores;

import com.runescape.api.Client;
import com.runescape.api.hiscores.model.Player;
import com.runescape.api.hiscores.model.Skill;
import com.runescape.api.hiscores.model.Table;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestHiscores {
	private final Hiscores hiscores = new Hiscores(new Client());

	@Test
	public void testPlayerInformation() throws IOException {
		assertFalse(hiscores.playerInformation("Andrew", Table.DEFAULT).isPresent());

		Player player = hiscores.playerInformation("Zezima", Table.DEFAULT).get();
		assertEquals(player.getSkills().get("Attack").getLevel(), 99);

		Skill thieving = player.getSkills().get("Thieving");
		int rank = thieving.getRank().get();
		long experience = thieving.getExperience().get();

		assertEquals(rank, 5);
		assertEquals(experience, 200_000_000);
	}
}