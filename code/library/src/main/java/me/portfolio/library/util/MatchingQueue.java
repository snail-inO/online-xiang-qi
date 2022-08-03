package me.portfolio.library.util;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class MatchingQueue {
    private static final Queue<String> queue = new PriorityBlockingQueue<>();

    private MatchingQueue() {}

    public static List<String> IN_QUEUE(String uid) {
        List<String> res = null;
        if (queue.peek() != null) {
            res = new ArrayList<>();
            res.add(queue.poll());
            res.add(uid);
        } else {
            queue.add(uid);
        }

        return res;
    }

    public static boolean REMOVE(String uid) {
        return queue.remove(uid);
    }

    public static String PEEK() {
        return queue.peek();
    }

}
