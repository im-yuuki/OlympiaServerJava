package dev.yuuki.olympiaserver.services;

import dev.yuuki.olympiaserver.utils.Role;
import dev.yuuki.olympiaserver.utils.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ContestRuntime {
    
    private final static Logger logger = LoggerFactory.getLogger(ContestRuntime.class);

    private final EventDispatcher eventDispatcher;
	private final UserManager userManager;

	public ContestRuntime(EventDispatcher eventDispatcher, UserManager userManager) {
		this.eventDispatcher = eventDispatcher;
		this.userManager = userManager;
	}

	public void startContest() {
        logger.info("Contest started");
    }

	public void getContestData() {
		return;
	}

	public void handleSignal(UUID userId) {

	}

	public void resetSignal() {
		
	}


	private class ContestantData {

		private final User user;
		private int score = 0;
		private String textAnswer;

		public ContestantData(User user) {
			this.user = user;
		}

		public int getScore() {
			return score;
		}

		public void increaseScore(int amount) {
			if (amount < 0) return;
			score += amount;
			logger.info("[%s] score: %d (+%d)".formatted(user.name, score, amount));
		}

		public void decreaseScore(int amount) {
			if (amount < 0) return;
			score -= amount;
			if (score < 0) score = 0;
			logger.info("[%s] score: %d (-%d)".formatted(user.name, score, amount));
		}

	}
}
