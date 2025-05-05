package Swing;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/*
 * 생성자 : 김관호
 * 생성일 : 25.05.05
 * 파일명 : HexDir.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 게임 시작 전 메뉴 패널
 */

public class MenuPanel extends JPanel {
    // 펭귄 이름 목록을 저장할 모델
    private DefaultListModel<String> nameListModel = new DefaultListModel<>();
    
    // 펭귄 이름 목록을 표시하는 JList
    private JList<String> nameListDisplay = new JList<>(nameListModel);
    
    // 펭귄 이름 입력 필드
    private JTextField nameInput = new JTextField(15);
    
    // 버튼들
    private JButton addButton = new JButton("추가");
    private JButton deleteButton = new JButton("선택 삭제");
    private JButton startButton = new JButton("게임 시작");

    // 생성자: 메뉴 패널을 초기화하고 구성
    public MenuPanel(List<String> nameList, Runnable onStart) {
        // 레이아웃과 패딩 설정
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // 🔹 상단 설명 레이블 설정
        JLabel label = new JLabel("펭귄 이름을 입력 후 추가 버튼을 누르세요");
        label.setFont(new Font("SansSerif", Font.BOLD, 16)); // 폰트 설정
        label.setHorizontalAlignment(SwingConstants.CENTER); // 중앙 정렬
        add(label, BorderLayout.NORTH); // 상단에 추가

        // 🔹 중앙 패널 (입력 필드와 리스트 영역)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // 세로 레이아웃 설정

        // 입력 필드와 추가 버튼을 포함하는 패널
        JPanel inputPanel = new JPanel();
        inputPanel.add(nameInput); // 이름 입력 필드
        inputPanel.add(addButton); // 추가 버튼
        centerPanel.add(inputPanel); // 중앙 패널에 추가

        // 펭귄 이름 리스트를 표시하는 스크롤 패널
        nameListDisplay.setVisibleRowCount(5); // 표시할 항목 수 설정
        nameListDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나의 항목만 선택 가능
        JScrollPane scrollPane = new JScrollPane(nameListDisplay); // 스크롤 패널로 감싸기
        centerPanel.add(scrollPane); // 중앙 패널에 추가

        // 삭제 버튼을 포함하는 패널
        JPanel deletePanel = new JPanel();
        deletePanel.add(deleteButton); // 삭제 버튼
        centerPanel.add(deletePanel); // 중앙 패널에 추가

        add(centerPanel, BorderLayout.CENTER); // 중앙에 패널 추가

        // 🔹 하단 시작 버튼
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(startButton); // 게임 시작 버튼
        add(bottomPanel, BorderLayout.SOUTH); // 하단에 추가

        // ▶ 버튼 기능 설정
        // 추가 버튼 클릭 시 입력된 이름을 리스트에 추가
        addButton.addActionListener(e -> {
            String name = nameInput.getText().trim(); // 입력된 이름 가져오기
            if (!name.isEmpty() && !nameListModel.contains(name)) { // 이름이 비어있지 않고 리스트에 없는 경우
                nameListModel.addElement(name); // 리스트에 이름 추가
                nameInput.setText(""); // 입력 필드 초기화
            }
        });

        // 선택된 펭귄 이름을 삭제하는 버튼 기능
        deleteButton.addActionListener(e -> {
            int selected = nameListDisplay.getSelectedIndex(); // 선택된 인덱스 확인
            if (selected != -1) { // 선택된 항목이 있는 경우
                nameListModel.remove(selected); // 해당 항목 삭제
            }
        });

        // 게임 시작 버튼 클릭 시 게임 시작
        startButton.addActionListener(e -> {
            // 펭귄 이름이 2명 이상일 경우
            if (nameListModel.getSize() >= 2) {
                nameList.clear(); // 기존 리스트 초기화
                for (int i = 0; i < nameListModel.size(); i++) {
                    nameList.add(nameListModel.getElementAt(i)); // 리스트에서 이름을 가져와서 nameList에 추가
                }
                onStart.run(); // 게임 시작 함수 실행
            } else {
                // 펭귄 이름이 2명 미만일 경우 경고 메시지 출력
                JOptionPane.showMessageDialog(this, "최소 2명의 펭귄 이름을 입력하세요!", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}
