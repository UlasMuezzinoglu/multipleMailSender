package com.ulas.springcoretemplate.redis;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.util.StopWatch;

@RequiredArgsConstructor
public abstract class AsyncSessionUtil<S extends Session> {
    public final FindByIndexNameSessionRepository<S> sessionRepository;

    @SneakyThrows
    protected Session getSession(String sessionId) {
        final StopWatch stopWatch = new StopWatch();
        Session session = sessionRepository.findById(sessionId);
        stopWatch.start();
        while (session == null) {
            Thread.sleep(10);
            session = sessionRepository.findById(sessionId);
            if (stopWatch.getTotalTimeSeconds() >= 3) {
                stopWatch.stop();
                break;
            }
        }
        return session;
    }

    protected void saveSession(Session session) {
        sessionRepository.save((S) session);
    }
}

