package ryulib;

public class StopWatch {

    public void start() {
        count = 0;
        old_tick = System.currentTimeMillis();
    }

    public long estimate() {
        long tick = System.currentTimeMillis();
        if (tick > old_tick) {
            count = count + tick - old_tick;
        }
        old_tick = tick;
        return count;
    }

    private long count = 0;
    private long old_tick;
}
