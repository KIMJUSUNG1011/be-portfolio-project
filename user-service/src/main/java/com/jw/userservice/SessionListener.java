package com.jw.userservice;

import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Getter
@Component
public class SessionListener implements HttpSessionListener
{
    private int currentUser = 0;

    @Override
    public void sessionCreated(HttpSessionEvent se)
    {
        HttpSessionListener.super.sessionCreated(se);
        currentUser++;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se)
    {
        HttpSessionListener.super.sessionDestroyed(se);
        currentUser--;
    }
}
