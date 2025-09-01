package com.ezehertz.socialnetwork.infrastructure.fanout;

import com.ezehertz.socialnetwork.domain.fanout.FanoutPolicy;
import org.springframework.stereotype.Component;

@Component
public class DefaultFanoutPolicy implements FanoutPolicy {
    private static final long CELEBRITY_THRESHOLD = 50_000;

    @Override
    public boolean isCelebrity(long followers) {
        return followers >= CELEBRITY_THRESHOLD;
    }
}
