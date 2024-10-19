package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.exercise4.users.*;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Consumer;

public class UsersCacheImpl implements UsersCache {

    Map<String, User> data;
    Consumer<String> idCallback;
    Consumer<Void> callCounterCallback;

    public UsersCacheImpl(Consumer<String> idCallback, Consumer<Void> callCounterCallback) {
        this.data = new HashMap<>();
        this.idCallback = idCallback;
        this.callCounterCallback = callCounterCallback;
    }

    @Override
    public void cacheUser(User user) {
        this.callCounterCallback.accept(null);
        this.idCallback.accept(user.getUserId());
        this.data.put(user.getUserId(), user);
    }

    @Nullable
    @Override
    public User getUser(String userId) {
        this.callCounterCallback.accept(null);
        this.idCallback.accept(userId);
        return this.data.get(userId);
    }

    public void remove(String userId) {
        this.data.remove(userId);
    }
}
