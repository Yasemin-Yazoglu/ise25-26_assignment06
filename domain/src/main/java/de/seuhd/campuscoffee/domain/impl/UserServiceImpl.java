package de.seuhd.campuscoffee.domain.impl;

import de.seuhd.campuscoffee.domain.model.User;
import de.seuhd.campuscoffee.domain.ports.UserDataService;
import de.seuhd.campuscoffee.domain.ports.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDataService userDataService;

    // TODO: Implement user service
    @Override
    public void clear() {
        userDataService.clear();
    }

    @Override
    public @NonNull User getById(@NonNull Long id) {
        return userDataService.getById(id);
    }

    @Override
    public @NonNull List<User> getAll() {
        return userDataService.getAll();
    }

    @Override
    public @NonNull User getByLoginName(@NonNull String loginName) {
        return userDataService.getByLoginName(loginName);
    }

    @Override
    public @NonNull User upsert(@NonNull User user) {
        return userDataService.upsert(user);
    }

    @Override
    public void delete(@NonNull Long id) {
        userDataService.delete(id);
    }
}