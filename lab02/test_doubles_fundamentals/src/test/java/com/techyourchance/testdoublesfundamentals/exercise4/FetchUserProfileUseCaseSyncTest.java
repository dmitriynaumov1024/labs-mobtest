package com.techyourchance.testdoublesfundamentals.exercise4;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import com.techyourchance.testdoublesfundamentals.exercise4.users.*;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;

public class FetchUserProfileUseCaseSyncTest {

    UserProfileHttpEndpointSyncImpl http;
    UsersCacheImpl cache;
    FetchUserProfileUseCaseSync sut;
    String httpReportedId = null;
    String cacheReportedId = null;
    Integer cacheCallCount = 0;

    User oneUser;

    @Before
    public void setup() {
        this.http = new UserProfileHttpEndpointSyncImpl((id)-> this.httpReportedId = id);
        this.cache = new UsersCacheImpl((id)-> this.cacheReportedId = id, (a)-> this.cacheCallCount++);
        this.sut = new FetchUserProfileUseCaseSync(this.http, this.cache);

        this.oneUser = new User("5", "John Doe", "https://example.com/v124ha5f23.png");
        this.http.data().put(oneUser.getUserId(), oneUser);
    }

    @Test
    public void itSubmitsCorrectIdToHttp() {
        String id = this.oneUser.getUserId();
        this.sut.fetchUserProfileSync(id);
        assertEquals(id, this.httpReportedId);
    }

    @Test
    public void itSubmitsCorrectIdToCache() {
        String id = this.oneUser.getUserId();
        this.sut.fetchUserProfileSync(id);
        assertEquals(id, this.cacheReportedId);
    }

    @Test
    public void itPutsToCacheOnce() {
        String id = this.oneUser.getUserId();
        this.cache.remove(id);
        this.cacheCallCount = 0;
        this.sut.fetchUserProfileSync(id);
        Integer expectedCount = 1;
        assertEquals(this.cacheCallCount, expectedCount);
    }

    @Test
    public void itReturnsSuccessOnSuccess() {
        String id = this.oneUser.getUserId();
        FetchUserProfileUseCaseSync.UseCaseResult r = this.sut.fetchUserProfileSync(id);
        assertEquals(r, FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS);
    }

    @Test
    public void itReturnsFailureOnNotFound() {
        String id = "some-id-that-100%-not-exists";
        FetchUserProfileUseCaseSync.UseCaseResult r = this.sut.fetchUserProfileSync(id);
        assertEquals(r, FetchUserProfileUseCaseSync.UseCaseResult.FAILURE);
    }

}