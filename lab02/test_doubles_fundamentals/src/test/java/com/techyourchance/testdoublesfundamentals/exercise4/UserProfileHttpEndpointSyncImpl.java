package com.techyourchance.testdoublesfundamentals.exercise4;

import java.util.*;
import java.util.function.Consumer;

import com.techyourchance.testdoublesfundamentals.exercise4.users.*;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;

public class UserProfileHttpEndpointSyncImpl implements UserProfileHttpEndpointSync {

    Map<String, User> data; // let's pretend it is a remote storage
    Consumer<String> userIdCallback; // callback for reporting user id

    public UserProfileHttpEndpointSyncImpl(Consumer<String> userIdCallback) {
        this.data = new HashMap<>();
        this.userIdCallback = userIdCallback;
    }

    public Map<String, User> data() {
        return this.data;
    }

    @Override
    public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
        userIdCallback.accept(userId);
        User u = this.data.get(userId);
        if (u == null) {
            return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, null, null, null);
        }
        return new EndpointResult(EndpointResultStatus.SUCCESS, u.getUserId(), u.getFullName(), u.getImageUrl());
    }
}
