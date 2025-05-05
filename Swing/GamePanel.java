package Swing;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import Manager.*;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : GamePenel.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 본격적인 게임의 패널
 */

public class GamePanel extends JPanel {
    // 게임 종료 시 호출할 콜백 함수
    public static Runnable onGameEnd;
    
    // 망치 버튼
    public static JButton button;

    // GamePanel 생성자: 펭귄 이름 리스트를 받아 게임을 초기화하고 화면을 설정
    public GamePanel(List<String> penguinNames, Runnable onGameEnd) {
        // 게임 종료 시 호출할 콜백 함수를 설정
        GamePanel.onGameEnd = onGameEnd;

        // 펭귄 등록
        GameManager.getInst().registerPenguin(penguinNames);

        // 게임 배경 색상 설정 (cyan)
        setBackground(Color.CYAN);

        // 게임 시작
        GameManager.getInst().start();

        // "🔨 망치 내려치기" 버튼 설정
        button = new JButton("🔨 망치 내려치기");
        button.setFont(new Font("SansSerif", Font.BOLD, 18)); // 글씨 크기 및 폰트 설정
        button.setBackground(new Color(255, 204, 102)); // 배경 색상 설정
        button.setForeground(Color.DARK_GRAY); // 글씨 색상 설정
        button.setFocusPainted(false); // 버튼에 포커스 표시 제거
        button.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2, true)); // 버튼 테두리 설정
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 커서 설정 (손 모양)
        button.setBounds(GameSetting.XMARGIN + GameSetting.HEX_SIZE * (GameSetting.MAP_SIZE_X + 1),
                GameSetting.YMARGIN +GameSetting.HEX_HEIGHT * (GameSetting.MAP_SIZE_Y / 2 - 1),
                GameSetting.HEX_SIZE * 4,
                GameSetting.HEX_HEIGHT);   //버튼 위치, 크기 설정

        // 버튼을 패널에 추가
        setLayout(null);
        add(button);

        // 게임 화면을 주기적으로 업데이트하기 위한 타이머 설정
        Timer timer = new Timer(1000 / GameSetting.FPS, e -> {
            repaint(); // 1초에 FPS 값 만큼 화면을 다시 그리기
        });
        timer.start();

        // 망치 버튼 클릭 시, 버튼 비활성화 후, 게임 매니저에서 망치 내려치기 실행
        button.addActionListener(e -> {
            button.setEnabled(false); // 버튼 비활성화
            GameManager.getInst().SpawnHammer(); // 망치 내려치기
        });
    }

    // 화면에 그려지는 내용을 업데이트하는 메서드
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // 기존 paintComponent 호출 (기본 그리기 작업)
        
        // 게임 매니저에서 화면을 렌더링
        GameManager.getInst().render(g, this);
    }
}
