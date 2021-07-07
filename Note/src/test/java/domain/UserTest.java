package domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ua.com.bohdanprie.notes.domain.entity.User;

public class UserTest {
	@Test
	public void changeUserData() {
		User user = new User("", "");
		assertTrue(user.getLogin() == null);
		assertTrue(user.getPassword() == null);
	}
}