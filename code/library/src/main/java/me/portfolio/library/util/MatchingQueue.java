package me.portfolio.library.util;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class MatchingQueue {
    private static final Queue<String> queue = new PriorityBlockingQueue<>();

    private MatchingQueue() {}

    public static Queue<String> getQueue() {
        return queue;
    }

}
