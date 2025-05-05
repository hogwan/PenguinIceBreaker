package Interface;

import Manager.*;
import Swing.GamePanel;
import javax.swing.Timer;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : AfterHammerSwing.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 해머충격파가 모두 끝난 후 해야할 러너블
 */

/**
 * AfterHammerSwing 클래스는 망치 휘두르기 후 처리를 담당합니다.
 * 일정 시간이 지난 후, 게임 종료 체크 및 망치 휘두르기 후의 게임 상태를 업데이트합니다.
 */
public class AfterHammerSwing implements Runnable {

    @Override
    public void run() {
        // 500ms 후 실행되는 타이머를 생성하여 게임 상태를 처리합니다.
        new Timer(500, e -> {
            // 타이머 중지
            ((Timer) e.getSource()).stop();

            // 게임 종료 여부 체크
            if (GameManager.getInst().endCheck()) {
                // 게임 종료 처리
                new Timer(500, f -> {
                    // 타이머 중지
                    ((Timer) f.getSource()).stop();
                    // 게임 종료 후 실행되는 코드 호출
                    GamePanel.onGameEnd.run();
                }).start();
            } else {
                // 게임이 종료되지 않으면, 아이스 브레이크를 처리하고 버튼을 활성화
                new Timer(500, f -> {
                    // 타이머 중지
                    ((Timer) f.getSource()).stop();
                    // 아이스 브레이크 상태를 무시하고 아이스 상태 갱신
                    IceManager.getInst().irrelevantIceBreak();
                    // 게임 버튼 활성화
                    GamePanel.button.setEnabled(true);
                }).start();
            }
        }).start();
    }
}
