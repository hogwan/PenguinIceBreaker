package Swing;

import javax.swing.*;
import java.awt.*;
import Manager.*;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : HexDir.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 게임 결과를 알리는 패널
 */

public class ResultPanel extends JPanel {
    // 게임에서 승리한 펭귄의 이름을 저장하는 변수
    private String winnerName;

    // 생성자: ResultPanel을 초기화하고 승리자 이름을 설정
    public ResultPanel() {
        // 승리한 펭귄의 이름을 GameManager에서 가져옴
        this.winnerName = GameManager.getInst().getWinnerPenguin().getName();
        
        // 레이아웃 설정: BorderLayout을 사용하여 컴포넌트 배치
        setLayout(new BorderLayout());
        
        // 배경색을 흰색으로 설정
        setBackground(Color.WHITE);

        // 승리자 이름을 표시할 레이블 생성
        JLabel label = new JLabel(winnerName + " 승리!", SwingConstants.CENTER);
        
        // 레이블 폰트 설정 (크고 굵은 서체)
        label.setFont(new Font("Serif", Font.BOLD, 64));
        
        // 레이블 텍스트 색상을 파란색으로 설정
        label.setForeground(Color.BLUE);

        // 레이블을 중앙에 배치
        add(label, BorderLayout.CENTER);
    }
}
