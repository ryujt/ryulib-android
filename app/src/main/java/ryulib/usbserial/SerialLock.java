package ryulib.usbserial;

import java.util.concurrent.atomic.AtomicBoolean;

import ryulib.StopWatch;

/// 시리얼 포트가 준비되기 전에 시리얼 write로 패킷을 전송하지 못하도록 브레이크 기능을 제공한다.
public class SerialLock {
    public SerialLock() {
        stopWatch.start();
    }

    /// 시리얼 포트에서 >>> 메시지가 들어오면 즉각 브레이크를 푼다.
    public void readPacket(final String packet) {
        if (released.get()) return;

        if ((packet.indexOf("\n") >= 0) || findPrompt.find(packet)) {
            stopWatch.start();
            released.set(true);
        }
    }

    public boolean isReleased() {
        boolean released_copy = released.getAndSet(false);

        if (released_copy) {
            stopWatch.start();
            return true;
        }

        // 특정 문자가 입렫되지 않으면 1초 후에 락을 푼다
        if (stopWatch.estimate() > 1000) {
            stopWatch.start();
            return true;
        }

        return false;
    }

    private AtomicBoolean released = new AtomicBoolean(false);
    private StopWatch stopWatch = new StopWatch();
    private FindPrompt findPrompt = new FindPrompt();

    private class FindPrompt {
        public boolean find(final String packet) {
            for (char ch : packet.toCharArray()) {
                if (ch == '>') {
                    count++;
                } else {
                    count = 0;
                }

                if (count == 3) return true;
            }

            return false;
        }

        private int count = 0;
    }

}
