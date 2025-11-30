package de.seuhd.campuscoffee.tests.system;

import de.seuhd.campuscoffee.domain.model.User;
import de.seuhd.campuscoffee.domain.tests.TestFixtures;
import org.junit.jupiter.api.Test;

import java.util.List;

import static de.seuhd.campuscoffee.tests.SystemTestUtils.Requests.userRequests;

public class UsersSystemTests extends AbstractSysTest {

    //TODO: Uncomment once user endpoint is implemented

    @Test
    void createUser() {
        User userToCreate = TestFixtures.getUserListForInsertion().getFirst();
        User createdUser = userDtoMapper.toDomain(userRequests.create(List.of(userDtoMapper.fromDomain(userToCreate))).getFirst());

        assertEqualsIgnoringIdAndTimestamps(createdUser, userToCreate);
    }

    //TODO: Add at least two additional tests for user operations
    @Test
    void getUserById() {
        List<User> createdUserList = TestFixtures.createUsers(userService);
        User createdUser = createdUserList.getFirst();

        User retrievedUser = userDtoMapper.toDomain(
                userRequests.retrieveById(createdUser.id())
        );

        assertEqualsIgnoringIdAndTimestamps(retrievedUser, createdUser);
    }

    @Test
    void filterUserByLoginName() {
        List<User> createdUserList = TestFixtures.createUsers(userService);
        User createdUser = createdUserList.getFirst();
        String loginName = createdUser.loginName();
        User filteredUser = userDtoMapper.toDomain(
                userRequests.retrieveByFilter("login_name", loginName)
        );
        assertEqualsIgnoringIdAndTimestamps(filteredUser, createdUser);
    }
}