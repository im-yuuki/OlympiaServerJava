package dev.yuuki.olympiaserver.services;

import dev.yuuki.olympiaserver.utils.Role;
import dev.yuuki.olympiaserver.utils.User;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class UserManager {

	private static final Logger logger = LoggerFactory.getLogger(UserManager.class);

	private final HashMap<UUID, User> users = new HashMap<>();

	private void addUser(String name, Role role) {
		UUID uuid = UUID.randomUUID();
		User user = new User(name, role);
		users.put(uuid, user);
	}

	public UserManager() {
		addUser("Quản trị viên", Role.ADMINISTRATOR);
		for (int i = 1; i < 5; i++) addUser("Thí sinh " + i, Role.CONTESTANT);
		addUser("Người xem", Role.VIEWER);
	}

	@Nullable
	public User getUser(UUID userId) {
		return users.get(userId);
	}

	public String getName(UUID userId) {
		return users.get(userId).name;
	}

	public UUID[] getAllUserIds () {
		return users.keySet().toArray(new UUID[0]);
	}
}
