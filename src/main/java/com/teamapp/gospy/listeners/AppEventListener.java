package com.teamapp.gospy.listeners;

import com.teamapp.gospy.services.UserDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AppEventListener implements ApplicationListener {

    @Autowired
    @Lazy
    UserDBService userDBService;  // circular dependency
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        //org.springframework.security.authentication.event.LogoutSuccessEvent
        //org.springframework.security.authentication.event.AuthenticationSuccessEvent
        //org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent
        //org.springframework.web.context.support.ServletRequestHandledEvent
        //org.springframework.context.event.ContextRefreshedEvent
        //org.springframework.boot.context.event.ApplicationStartedEvent
        //org.springframework.boot.availability.AvailabilityChangeEvent
        //org.springframework.boot.context.event.ApplicationReadyEvent
        //org.springframework.web.socket.messaging.SessionConnectEvent
        //org.springframework.web.socket.messaging.SessionConnectedEvent
        //org.springframework.web.socket.messaging.SessionSubscribeEvent
        //org.springframework.web.socket.messaging.SessionDisconnectEvent

        //System.out.println(" ApplicationEvent  " + event.toString());//getClass().getName());
        String username;
        String token;
        switch (event.getClass().getName()) {
            case "org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent":
                //[source=UsernamePasswordAuthenticationToken [Principal=springuser, Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=127.0.0.1, SessionId=2629FEE98586EFDF42636DAEA42AB550], Granted Authorities=[ROLE_[USER]]]]
                InteractiveAuthenticationSuccessEvent loginEvent = (InteractiveAuthenticationSuccessEvent) event;
                username = loginEvent.getAuthentication().getName();
                WebAuthenticationDetails webDetails = (WebAuthenticationDetails) loginEvent.getAuthentication().getDetails();
                token = webDetails.getSessionId();
                //debug
                //System.out.println("onApplicationEvent | InteractiveAuthenticationSuccessEvent | user=" + username + " token=" + token);
                userDBService.updateUserLoginDate(username);
                userDBService.updateUserToken(username, token);
                break;
            case "org.springframework.security.authentication.event.LogoutSuccessEvent":
                LogoutSuccessEvent logoutEvent = (LogoutSuccessEvent) event;
                username = logoutEvent.getAuthentication().getName();
                userDBService.updateUserToken(username, "loggedout");
                break;
        }
    }
}