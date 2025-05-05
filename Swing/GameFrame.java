package Swing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : GameFrame.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 게임 프레임
 */

public class GameFrame extends JFrame {
    // 화면 전환을 위한 CardLayout 객체
    private CardLayout layout = new CardLayout();
    
    // 화면을 전환하는 메인 패널 (CardLayout을 사용)
    private JPanel mainPanel = new JPanel(layout);
    
    // 사용자 이름 목록을 저장하는 리스트
    private List<String> nameList = new ArrayList<>();
    
    // 게임 화면을 나타내는 GamePanel 객체
    private GamePanel gamePanel;

    // GameFrame 생성자
    public GameFrame() {
        // 메뉴 화면을 생성하고, 게임 시작 시 게임 화면으로 전환하는 동작을 설정
        MenuPanel menu = new MenuPanel(nameList, () -> {
            // 게임 패널 생성 후, 게임 화면으로 전환
            gamePanel = new GamePanel(nameList, () -> {
                // 게임 종료 후 결과 화면을 추가하고, 결과 화면으로 전환
                ResultPanel resultPanel = new ResultPanel();
                mainPanel.add(resultPanel, "RESULT");
                layout.show(mainPanel, "RESULT");
            });
            // 게임 화면을 메인 패널에 추가
            mainPanel.add(gamePanel, "GAME");
            layout.show(mainPanel, "GAME"); // 게임 화면으로 전환
        });

        // 메뉴 화면을 메인 패널에 추가
        mainPanel.add(menu, "MENU");
        
        // 메인 패널을 JFrame에 추가
        add(mainPanel);

        // 프레임 설정
        setTitle("펭귄 게임"); // 창 제목
        setSize(1280, 768); // 창 크기
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 종료 시 애플리케이션 종료
        setVisible(true); // 프레임을 화면에 표시
    }
}
