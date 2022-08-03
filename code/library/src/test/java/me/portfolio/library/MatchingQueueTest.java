package me.portfolio.library;


import me.portfolio.library.util.MatchingQueue;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchingQueueTest {
    private static AtomicLong counter = new AtomicLong();

    @Test
    public void inqueueTest() {
        MatchingQueue.IN_QUEUE(String.valueOf(counter.getAndIncrement()));
        assertThat(MatchingQueue.PEEK()).isEqualTo(String.valueOf(counter.get() - 1));
        assertThat(MatchingQueue.IN_QUEUE(String.valueOf(counter.getAndIncrement())))
                .contains(String.valueOf(counter.get() - 1), String.valueOf(counter.get() - 2));
        assertThat(MatchingQueue.PEEK()).isNull();
    }
}
