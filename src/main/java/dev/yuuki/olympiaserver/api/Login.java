package dev.yuuki.olympiaserver.api;

import com.google.gson.GsonBuilder;
import dev.yuuki.olympiaserver.services.Authorization;
import dev.yuuki.olympiaserver.services.UserManager;
import dev.yuuki.olympiaserver.utils.User;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class Login {

	private static final Logger logger = LoggerFactory.getLogger(Login.class);

	private final Authorization authorization;
	private final UserManager userManager;

	public Login(
			final Authorization authorization,
			final UserManager userManager
	) {
		this.authorization = authorization;
		this.userManager = userManager;
	}

	@GetMapping("/accounts")
	public ResponseEntity<String> getUserList() {
		ArrayList<UserInfo> userInfoList = new ArrayList<>();
		for (UUID userId : userManager.getAllUserIds()) {
			userInfoList.add(new UserInfo(userId, Objects.requireNonNull(userManager.getUser(userId))));
		}
		return ResponseEntity
				.status(200)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new GsonBuilder()
						.setPrettyPrinting()
						.create()
						.toJson(userInfoList)
				);
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(HttpServletRequest request, @RequestBody LoginRequestBody body) {
		try {
			String token = authorization.generateToken(body.getTargetUserUuid(), body.getPassword());
			logger.info("Authorized a client accessing [%s] from %s".formatted(
					userManager.getName(body.getTargetUserUuid()),
					request.getRemoteAddr()
			));
			return ResponseEntity
					.status(200)
					.contentType(MediaType.APPLICATION_JSON)
					.body("{ \"token\": \"%s\" }".formatted(token));
		} catch (Exception e) {
			return ResponseEntity.status(401).body(e.getMessage());
		}
	}

	public static class LoginRequestBody {

		private String id;
		private String password;


		public String getPassword() {
			return password;
		}

		public void setPassword(String loginCode) {
			this.password = loginCode;
		}

		public String getId() {
			return id;
		}

		public UUID getTargetUserUuid() {
			return UUID.fromString(id);
		}

		public void setId(String targetUserId) {
			this.id = targetUserId;
		}

	}

	public static class UserInfo {

		private String id;
		private String name;

		public UserInfo(UUID userId, User user) {
			id = userId.toString();
			name = user.name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}

}
