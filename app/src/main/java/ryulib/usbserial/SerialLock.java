package ryulib.usbserial;

import java.util.concurrent.atomic.AtomicBoolean;

import ryulib.StopWatch;

/** 시리얼 포트가 준비되기 전에 시리얼 write로 패킷을 전송하지 못하도록 브레이크 기능을 제공한다.
*/
public class SerialLock {
    public SerialLock() {
        stopWatch.start();
    }

    /** 시리얼 포트에서 수신된 문자열의 조건이 맞으면 브레이크를 푼다.
     * 조건에 맞는 문자열이 정해진 시간 이상 수신되지 않으면 브레이크를 풀어준다.
     * @param packet 시리얼 포트에서 수신한 데이터
    */
    public void readPacket(final String packet) {
        if (released.get()) return;

        // exec(_codes_) 부분은 AsomeCode & AsomeBlock에서만 필요하다.
        // 이것을 분리하고 싶으나 AsomeCode & AsomeBlock 이외 사용하는 곳이 없기 때문에 방치해두었다.
        // 재 사용성이 필요하다면 걸러내야할 논리 코드를 분리해야 한다.
        if ((packet.indexOf("\n") >= 0) || findPrompt.find(packet) || (packet.indexOf("exec(_codes_)") >= 0)) {
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

        // 특정 문자가 입렫되지 않으면 0.5초 후에 락을 푼다
        if (stopWatch.estimate() > 500) {
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
