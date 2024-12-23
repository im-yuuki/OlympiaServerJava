package dev.yuuki.olympiaserver.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.yuuki.olympiaserver.utils.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class Authorization {

	private Map<String, ?> generatePayload(UUID userId, String loginCode) {
		HashMap<String, String> payload = new HashMap<>();
		payload.put("userId", userId.toString());
		payload.put("loginCode", loginCode);
		return payload;
	}

	private final UserManager userManager;
	private final Algorithm key;
	private final JWTVerifier verifier;
	private final String jwtIssuer;

	public Authorization(
			final UserManager userManager,
			@Value("${jwt.secret}") final String jwtSecret,
			@Value("${jwt.issuer}") final String jwtIssuer
	) {
		this.userManager = userManager;
		this.key = Algorithm.HMAC512(jwtSecret);
		this.jwtIssuer = jwtIssuer;
		this.verifier = JWT.require(this.key).withIssuer(jwtIssuer).build();
	}

	public String generateToken(UUID userId, String loginCode) throws LoginException {
		User targetUser = userManager.getUser(userId);
		if (targetUser == null) throw new LoginException("User not found");
		if (!targetUser.validateLoginCode(loginCode)) throw new LoginException("Wrong login code");
		return JWT.create()
				.withPayload(generatePayload(userId, loginCode))
				.withIssuer(jwtIssuer)
				.sign(key);
	}

	public User validateUser(String token) throws LoginException, JWTVerificationException {
		DecodedJWT decodedJWT = verifier.verify(token);
		UUID userId = UUID.fromString(decodedJWT.getClaim("userId").asString());
		User targetUser = userManager.getUser(userId);
		if (targetUser == null) throw new LoginException("Token invalid");
		if (targetUser.validateLoginCode(decodedJWT.getClaim("loginCode").asString())) return targetUser;
		else throw new LoginException("Token invalid");
	}

}