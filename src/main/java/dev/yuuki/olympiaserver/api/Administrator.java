package dev.yuuki.olympiaserver.api;


import dev.yuuki.olympiaserver.services.Authorization;
import dev.yuuki.olympiaserver.services.EventDispatcher;
import dev.yuuki.olympiaserver.services.UserManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class Administrator {

	private final Authorization authorization;
	private final UserManager userManager;
	private final EventDispatcher eventDispatcher;

	public Administrator(Authorization authorization, UserManager userManager, EventDispatcher eventDispatcher) {
		this.authorization = authorization;
		this.userManager = userManager;
		this.eventDispatcher = eventDispatcher;
	}
	
}
