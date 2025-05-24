import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

class IndexSelectionPanel extends JPanel {
    private DefaultComboBoxModel<String> indexModel;
    private JComboBox<String> indexSelector;
    private RoundButton createIndexBtn;
    private RoundButton deleteIndexBtn;
    private RoundButton enterIndexBtn;
    private IndexDataManager dataManager;
    private IndexSelectionListener listener;

    public interface IndexSelectionListener {
        void onIndexSelected(String indexName);
    }

    public IndexSelectionPanel(IndexDataManager dataManager, IndexSelectionListener listener) {
        this.dataManager = dataManager;
        this.listener = listener;

        initComponents();
        setupListeners();
        setupLayout();
    }

    private void initComponents() {
        // 배경을 투명하게 설정하여 커스텀 페인트가 보이도록 함
        setOpaque(false);

        indexModel = new DefaultComboBoxModel<>();
        HashMap<String, ArrayList<Page>> indexMap = dataManager.getIndexMap();
        for (String name : indexMap.keySet()) {
            indexModel.addElement(name);
        }

        indexSelector = new JComboBox<>(indexModel);
        styleComboBox(indexSelector);

        // 라운드 버튼들 생성
        createIndexBtn = new RoundButton("인덱스 생성");
        deleteIndexBtn = new RoundButton("인덱스 삭제");
        enterIndexBtn = new RoundButton("인덱스 열기");
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setPreferredSize(new Dimension(200, 35));
        comboBox.setBackground(new Color(255, 182, 193)); // 베이비 핑크
        comboBox.setForeground(Color.BLACK);
        comboBox.setFont(new Font("맑은 고딕", Font.BOLD, 12));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 100));

        // 상단에 콤보박스
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setOpaque(false);
        topPanel.add(indexSelector);

        // 하단에 버튼들을 일렬로
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(createIndexBtn);
        buttonPanel.add(enterIndexBtn);
        buttonPanel.add(deleteIndexBtn);

        add(topPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private void setupListeners() {
        createIndexBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "인덱스 이름 입력:");
            if (name != null && !name.trim().isEmpty() && dataManager.addIndex(name)) {
                indexModel.addElement(name);
            }
        });

        deleteIndexBtn.addActionListener(e -> {
            String name = (String) indexSelector.getSelectedItem();
            if (name != null && dataManager.removeIndex(name)) {
                indexModel.removeElement(name);
            }
        });

        enterIndexBtn.addActionListener(e -> {
            String name = (String) indexSelector.getSelectedItem();
            if (name != null) {
                listener.onIndexSelected(name);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // 안티앨리어싱 설정
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 브라운 배경
        g2d.setColor(new Color(139, 69, 19)); // 브라운 색상
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // 하트 패턴 그리기
        drawHeartPattern(g2d);

        g2d.dispose();
    }

    private void drawHeartPattern(Graphics2D g2d) {
        g2d.setColor(new Color(160, 82, 45, 80)); // 투명도가 있는 더 밝은 브라운

        int heartSize = 20;
        int spacing = 40;

        for (int x = 0; x < getWidth() + spacing; x += spacing) {
            for (int y = 0; y < getHeight() + spacing; y += spacing) {
                drawHeart(g2d, x, y, heartSize);
            }
        }
    }

    private void drawHeart(Graphics2D g2d, int x, int y, int size) {
        // 하트 모양 그리기 (간단한 버전)
        int halfSize = size / 2;

        // 왼쪽 원
        g2d.fillOval(x - halfSize, y - halfSize/2, halfSize, halfSize);
        // 오른쪽 원
        g2d.fillOval(x, y - halfSize/2, halfSize, halfSize);

        // 하단 삼각형
        int[] xPoints = {x - halfSize, x + halfSize, x};
        int[] yPoints = {y, y, y + halfSize};
        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    // 커스텀 라운드 버튼 클래스
    private class RoundButton extends JButton {
        public RoundButton(String text) {
            super(text);
            setPreferredSize(new Dimension(120, 40));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("맑은 고딕", Font.BOLD, 12));
            setForeground(Color.BLACK);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 버튼 배경색 (베이비 핑크)
            Color bgColor = new Color(255, 182, 193);
            if (getModel().isPressed()) {
                bgColor = new Color(255, 160, 170); // 눌렸을 때 조금 더 진한 색
            } else if (getModel().isRollover()) {
                bgColor = new Color(255, 200, 210); // 마우스 오버시 조금 더 밝은 색
            }

            g2d.setColor(bgColor);
            g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));

            // 테두리 추가
            g2d.setColor(new Color(139, 69, 19)); // 브라운 테두리
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, 20, 20));

            g2d.dispose();
            super.paintComponent(g);
        }
    }
}