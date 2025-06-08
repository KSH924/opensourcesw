import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class FontSizeController {
    private JTextArea textArea;
    private JPanel fontSizePanel;
    private JButton increaseFontBtn;
    private JButton decreaseFontBtn;
    private JLabel fontSizeLabel;
    private int currentFontSize;

    private static final int MIN_FONT_SIZE = 8;
    private static final int MAX_FONT_SIZE = 72;
    private static final int DEFAULT_FONT_SIZE = 14;

    public FontSizeController(JTextArea textArea) {
        this.textArea = textArea;
        this.currentFontSize = DEFAULT_FONT_SIZE;

        initComponents();
        setupListeners();
        updateFontSize();
    }

    private void initComponents() {
        fontSizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        fontSizePanel.setBackground(new Color(245, 245, 245));

        // 폰트 크기 감소 버튼
        decreaseFontBtn = createFontButton("-");

        // 폰트 크기 표시 라벨
        fontSizeLabel = new JLabel(String.valueOf(currentFontSize));
        fontSizeLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        fontSizeLabel.setPreferredSize(new Dimension(30, 25));
        fontSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fontSizeLabel.setBorder(BorderFactory.createLoweredBevelBorder()); // 수정된 부분
        fontSizeLabel.setOpaque(true);
        fontSizeLabel.setBackground(Color.WHITE);

        // 폰트 크기 증가 버튼
        increaseFontBtn = createFontButton("+");

        // 패널에 컴포넌트 추가
        fontSizePanel.add(new JLabel("글자크기:"));
        fontSizePanel.add(decreaseFontBtn);
        fontSizePanel.add(fontSizeLabel);
        fontSizePanel.add(increaseFontBtn);
    }

    private JButton createFontButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(30, 25));
        button.setBackground(new Color(255, 182, 193)); // 베이비 핑크
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createRaisedBevelBorder()); // 일관성을 위해 수정
        button.setFocusPainted(false);
        button.setMargin(new Insets(2, 2, 2, 2));
        return button;
    }

    private void setupListeners() {
        increaseFontBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentFontSize < MAX_FONT_SIZE) {
                    currentFontSize += 2;
                    updateFontSize();
                }
            }
        });

        decreaseFontBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentFontSize > MIN_FONT_SIZE) {
                    currentFontSize -= 2;
                    updateFontSize();
                }
            }
        });
    }

    private void updateFontSize() {
        Font currentFont = textArea.getFont();
        Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), currentFontSize);
        textArea.setFont(newFont);
        fontSizeLabel.setText(String.valueOf(currentFontSize));

        // 버튼 상태 업데이트
        decreaseFontBtn.setEnabled(currentFontSize > MIN_FONT_SIZE);
        increaseFontBtn.setEnabled(currentFontSize < MAX_FONT_SIZE);
    }

    public JPanel getFontSizePanel() {
        return fontSizePanel;
    }

    public int getCurrentFontSize() {
        return currentFontSize;
    }

    public void setFontSize(int fontSize) {
        if (fontSize >= MIN_FONT_SIZE && fontSize <= MAX_FONT_SIZE) {
            this.currentFontSize = fontSize;
            updateFontSize();
        }
    }

    public void resetToDefault() {
        this.currentFontSize = DEFAULT_FONT_SIZE;
        updateFontSize();
    }
}