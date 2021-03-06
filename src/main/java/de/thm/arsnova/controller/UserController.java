package de.thm.arsnova.controller;

import com.fasterxml.jackson.annotation.JsonView;
import de.thm.arsnova.model.LoginCredentials;
import de.thm.arsnova.model.UserProfile;
import de.thm.arsnova.model.serialization.View;
import de.thm.arsnova.service.RoomService;
import de.thm.arsnova.service.UserService;
import de.thm.arsnova.web.exceptions.BadRequestException;
import de.thm.arsnova.web.exceptions.ForbiddenException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserController.REQUEST_MAPPING)
public class UserController extends AbstractEntityController<UserProfile> {
	protected static final String REQUEST_MAPPING = "/user";
	private static final String REGISTER_MAPPING = "/register";
	private static final String ACTIVATE_MAPPING = DEFAULT_ID_MAPPING + "/activate";
	private static final String RESET_PASSWORD_MAPPING = DEFAULT_ID_MAPPING + "/resetpassword";
	private static final String ROOM_HISTORY_MAPPING = DEFAULT_ID_MAPPING + "/roomHistory";

	private UserService userService;
	private RoomService roomService;

	public UserController(final UserService userService, final RoomService roomService) {
		super(userService);
		this.userService = userService;
		this.roomService = roomService;
	}

	class Activation {
		private String key;

		public String getKey() {
			return key;
		}

		@JsonView(View.Public.class)
		public void setKey(final String key) {
			this.key = key;
		}
	}

	class PasswordReset {
		private String key;
		private String password;

		public String getKey() {
			return key;
		}

		@JsonView(View.Public.class)
		public void setKey(final String key) {
			this.key = key;
		}

		public String getPassword() {
			return password;
		}

		@JsonView(View.Public.class)
		public void setPassword(final String password) {
			this.password = password;
		}
	}

	@Override
	protected String getMapping() {
		return REQUEST_MAPPING;
	}

	@PostMapping(REGISTER_MAPPING)
	public void register(@RequestBody LoginCredentials loginCredentials) {
		userService.create(loginCredentials.getLoginId(), loginCredentials.getPassword());
	}

	@RequestMapping(value = ACTIVATE_MAPPING, method = RequestMethod.POST)
	public void activate(
			@PathVariable final String id,
			@RequestParam final String key) {
		UserProfile userProfile = userService.get(id, true);
		if (userProfile == null || !key.equals(userProfile.getAccount().getActivationKey())) {
			throw new BadRequestException();
		}
		userProfile.getAccount().setActivationKey(null);
		userService.update(userProfile);
	}

	@RequestMapping(value = RESET_PASSWORD_MAPPING, method = RequestMethod.POST)
	public void resetPassword(
			@PathVariable final String id,
			@RequestBody final PasswordReset passwordReset) {
		UserProfile userProfile = userService.get(id, true);
		if (userProfile == null) {
			throw new BadRequestException();
		}

		if (passwordReset.getKey() != null) {
			if (!userService.resetPassword(userProfile, passwordReset.getKey(), passwordReset.getPassword())) {
				throw new ForbiddenException();
			}
		} else {
			userService.initiatePasswordReset(id);
		}
	}

	@PostMapping(ROOM_HISTORY_MAPPING)
	public void postRoomHistoryEntry(@PathVariable final String id,
			@RequestBody final UserProfile.RoomHistoryEntry roomHistoryEntry) {
		userService.addRoomToHistory(userService.get(id), roomService.get(roomHistoryEntry.getRoomId()));
	}

	@Override
	protected String resolveAlias(final String alias) {
		return userService.getByUsername(alias).getId();
	}
}
