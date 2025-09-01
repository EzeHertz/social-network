package com.ezehertz.socialnetwork.infrastructure.metrics;

import com.ezehertz.socialnetwork.domain.timeline.TimelineMetrics;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MicrometerTimelineMetrics implements TimelineMetrics {
    private final Counter fanoutCounter;

    public MicrometerTimelineMetrics(MeterRegistry registry) {
        this.fanoutCounter = registry.counter("timeline.fanout");
    }

    @Override
    public void incrementFanout() {
        fanoutCounter.increment();
    }
}
