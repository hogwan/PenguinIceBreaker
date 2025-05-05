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
        new Timer(200, e -> {
            // 타이머 중지
            ((Timer) e.getSource()).stop();

            // 게임 종료 여부 체크
            if (GameManager.getInst().endCheck()) {
                // 게임 종료 처리
                new Timer(1000, f -> {
                    // 타이머 중지
                    ((Timer) f.getSource()).stop();
                    // 게임 종료 후 실행되는 코드 호출
                    GamePanel.onGameEnd.run();
                }).start();
            } else {
                // 펭귄과 연관없는 얼음 구역 부숨
                if(IceManager.getInst().irrelevantIceBreak()){
                    // 연관없는 구역이 있다면 딜레이를 주고 버튼 활성화(얼음이 부서지는 동안 그 얼음을 해머로 치지않기위함)
                    new Timer(200, f -> {
                        ((Timer) f.getSource()).stop();
                        GamePanel.button.setEnabled(true);
                    }).start();
                }else{
                    // 게임 버튼 활성화
                    GamePanel.button.setEnabled(true);
                }
               
                
            }
        }).start();
    }
}
