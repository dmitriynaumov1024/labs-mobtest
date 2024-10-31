package com.techyourchance.mockitofundamentals.exercise5;

import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static com.techyourchance.mockitofundamentals.exercise5.UpdateUsernameUseCaseSync.UseCaseResult;
import static com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResult;
import static com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResultStatus;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class UpdateUsernameUseCaseSyncTest {

    public static final String USER_ID = "012345678";
    public static final String USERNAME = "username";

    UpdateUsernameHttpEndpointSync fakeEndpoint;
    UsersCache fakeUsersCache;
    EventBusPoster fakeEventBusPoster;

    UpdateUsernameUseCaseSync sut;

    @Before
    public void setup() throws Exception {
        fakeEndpoint = mock(UpdateUsernameHttpEndpointSync.class);
        fakeUsersCache = mock(UsersCache.class);
        fakeEventBusPoster = mock(EventBusPoster.class);
        sut = new UpdateUsernameUseCaseSync(fakeEndpoint, fakeUsersCache, fakeEventBusPoster);
        success();
    }

    @Test
    public void updateUsername_success_userIdAndUsernamePassedToEndpoint() throws Exception {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        sut.updateUsernameSync(USER_ID, USERNAME);
        verify(fakeEndpoint, times(1)).updateUsername(ac.capture(), ac.capture());
        List<String> captures = ac.getAllValues();
        assertEquals(captures.get(0), USER_ID);
        assertEquals(captures.get(1), USERNAME);
    }

    @Test
    public void updateUsername_success_userCached() throws Exception {
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        sut.updateUsernameSync(USER_ID, USERNAME);
        verify(fakeUsersCache).cacheUser(ac.capture());
        User cachedUser = ac.getValue();
        assertEquals(cachedUser.getUserId(), USER_ID);
        assertEquals(cachedUser.getUsername(), USERNAME);
    }

    @Test
    public void updateUsername_generalError_userNotCached() throws Exception {
        generalError();
        sut.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(fakeUsersCache);
    }

    @Test
    public void updateUsername_authError_userNotCached() throws Exception {
        authError();
        sut.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(fakeUsersCache);
    }

    @Test
    public void updateUsername_serverError_userNotCached() throws Exception {
        serverError();
        sut.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(fakeUsersCache);
    }

    @Test
    public void updateUsername_success_loggedInEventPosted() throws Exception {
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        sut.updateUsernameSync(USER_ID, USERNAME);
        verify(fakeEventBusPoster).postEvent(ac.capture());
        assertThat(ac.getValue(), is(instanceOf(UserDetailsChangedEvent.class)));
    }

    @Test
    public void updateUsername_generalError_noInteractionWithEventBusPoster() throws Exception {
        generalError();
        sut.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(fakeEventBusPoster);
    }

    @Test
    public void updateUsername_authError_noInteractionWithEventBusPoster() throws Exception {
        authError();
        sut.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(fakeEventBusPoster);
    }

    @Test
    public void updateUsername_serverError_noInteractionWithEventBusPoster() throws Exception {
        serverError();
        sut.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(fakeEventBusPoster);
    }

    @Test
    public void updateUsername_success_successReturned() throws Exception {
        UseCaseResult result = sut.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(result, UseCaseResult.SUCCESS);
    }

    @Test
    public void updateUsername_serverError_failureReturned() throws Exception {
        serverError();
        UseCaseResult result = sut.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(result, UseCaseResult.FAILURE);
    }

    @Test
    public void updateUsername_authError_failureReturned() throws Exception {
        authError();
        UseCaseResult result = sut.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(result, UseCaseResult.FAILURE);
    }

    @Test
    public void updateUsername_generalError_failureReturned() throws Exception {
        generalError();
        UseCaseResult result = sut.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(result, UseCaseResult.FAILURE);
    }

    @Test
    public void updateUsername_networkError_networkErrorReturned() throws Exception {
        networkError();
        UseCaseResult result = sut.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(result, UseCaseResult.NETWORK_ERROR);
    }

    private void networkError() throws Exception {
        doThrow(new NetworkErrorException())
            .when(fakeEndpoint).updateUsername(anyString(), anyString());
    }

    private void success() throws NetworkErrorException {
        when(fakeEndpoint.updateUsername(anyString(), anyString()))
            .thenReturn(new EndpointResult(EndpointResultStatus.SUCCESS, USER_ID, USERNAME));
    }

    private void generalError() throws Exception {
        when(fakeEndpoint.updateUsername(anyString(), anyString()))
            .thenReturn(new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", ""));
    }

    private void authError() throws Exception {
        when(fakeEndpoint.updateUsername(anyString(), anyString()))
            .thenReturn(new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", ""));
    }

    private void serverError() throws Exception {
        when(fakeEndpoint.updateUsername(anyString(), anyString()))
            .thenReturn(new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", ""));
    }

}