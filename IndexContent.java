import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.util.ArrayList;

class IndexContentPanel extends JPanel {
    private JTextArea textArea;
    private JLabel imageLabel;
    private JComboBox<String> pageSelector;
    private JButton imageButton;
    private JButton nextPage;
    private JButton prevPage;
    private JButton addPage;
    private JButton deletePage;
    private JButton backButton;
    private JButton hideButton;

    private IndexDataManager dataManager;
    private String currentIndex;
    private int currentPage = 0;
    private boolean hidden = false;
    private IndexContentListener listener;

    // 색상 정의
    private static final Color BABY_PINK = new Color(255, 218, 224);
    private static final Color LIGHT_PINK = new Color(255, 240, 245);
    private static final Color DARK_PINK = new Color(255, 192, 203);
    private static final Color BROWN = new Color(139, 117, 98);
    private static final Color LIGHT_BROWN = new Color(160, 140, 120);
    private static final Color CREAM = new Color(255, 253, 250);

    public interface IndexContentListener {
        void onBackToIndexSelection();
    }

    public IndexContentPanel(IndexDataManager dataManager, String indexName, IndexContentListener listener) {
        this.dataManager = dataManager;
        this.currentIndex = indexName;
        this.listener = listener;

        setLayout(new BorderLayout());
        setBackground(LIGHT_PINK);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initComponents();
        setupListeners();

        // 페이지가 없으면 하나 추가
        ArrayList<Page> pages = dataManager.getPages(currentIndex);
        if (pages.isEmpty()) {
            dataManager.addPage(currentIndex);
        }

        updatePageSelector();
        loadPage();
    }

    private void initComponents() {
        // 텍스트 영역
        textArea = new JTextArea(10, 40);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        textArea.setBackground(CREAM);
        textArea.setForeground(BROWN);
        textArea.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane textScroll = new JScrollPane(textArea);
        textScroll.setPreferredSize(new Dimension(450, 220));
        textScroll.setBorder(BorderFactory.createEmptyBorder());
        textScroll.getViewport().setBackground(CREAM);

        // 모서리
        JPanel textWrapper = createRoundedPanel(CREAM);
        textWrapper.setLayout(new BorderLayout());
        textWrapper.add(textScroll);
        textWrapper.setBorder(BorderFactory.createCompoundBorder(
                createShadowBorder(),
                new EmptyBorder(5, 5, 5, 5)
        ));

        // 라벨
        imageLabel = new JLabel("이미지 추가");
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(450, 300));
        imageLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        imageLabel.setForeground(Color.BLACK);

        JPanel imageWrapper = createRoundedPanel(CREAM);
        imageWrapper.setLayout(new BorderLayout());
        imageWrapper.add(imageLabel);
        imageWrapper.setBorder(BorderFactory.createCompoundBorder(
                createShadowBorder(),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // 중앙 패널
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(LIGHT_PINK);
        centerPanel.add(textWrapper);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(imageWrapper);

        // 페이지 선택기
        pageSelector = new JComboBox<>();
        styleComboBox(pageSelector);

        // 버튼
        imageButton = createStyledButton("이미지", BABY_PINK);
        nextPage = createStyledButton("다음", BABY_PINK);
        prevPage = createStyledButton("이전", BABY_PINK);
        addPage = createStyledButton("페이지 추가", LIGHT_BROWN);
        deletePage = createStyledButton("삭제", new Color(255, 160, 160));
        backButton = createStyledButton("목록", BROWN);
        hideButton = createStyledButton("가리기", DARK_PINK);

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(LIGHT_PINK);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 첫 번째 줄
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
        buttonPanel.add(prevPage, gbc);
        gbc.gridx = 1;
        buttonPanel.add(pageSelector, gbc);
        gbc.gridx = 2;
        buttonPanel.add(nextPage, gbc);

        // 두 번째 줄
        gbc.gridx = 0; gbc.gridy = 1;
        buttonPanel.add(addPage, gbc);
        gbc.gridx = 1;
        buttonPanel.add(deletePage, gbc);
        gbc.gridx = 2;
        buttonPanel.add(imageButton, gbc);

        // 세 번째 줄
        gbc.gridx = 0; gbc.gridy = 2;
        buttonPanel.add(hideButton, gbc);
        gbc.gridx = 2;
        buttonPanel.add(backButton, gbc);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createRoundedPanel(Color bgColor) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
    }

    private javax.swing.border.Border createShadowBorder() {
        return new javax.swing.border.AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 그림자 효과
                g2.setColor(new Color(0, 0, 0, 20));
                for (int i = 0; i < 4; i++) {
                    g2.drawRoundRect(x + i, y + i, width - 2*i - 1, height - 2*i - 1, 20, 20);
                }
                g2.dispose();
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(4, 4, 4, 4);
            }
        };
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 그라데이션 배경
                GradientPaint gradient = new GradientPaint(
                        0, 0, bgColor,
                        0, getHeight(), darken(bgColor, 0.9f)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // 텍스트
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), textX, textY);

                g2.dispose();
            }
        };

        button.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        button.setForeground(Color.BLACK);
        button.setBackground(bgColor);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 호버 효과
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(brighten(bgColor, 1.1f));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        comboBox.setBackground(CREAM);
        comboBox.setForeground(BROWN);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BABY_PINK, 2),
                new EmptyBorder(5, 10, 5, 10)
        ));

        // 화살표 색상
        comboBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
                button.setBackground(BABY_PINK);
                button.setForeground(BROWN);
                button.setBorder(BorderFactory.createEmptyBorder());
                return button;
            }
        });
    }

    private Color brighten(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() * factor));
        int g = Math.min(255, (int)(color.getGreen() * factor));
        int b = Math.min(255, (int)(color.getBlue() * factor));
        return new Color(r, g, b);
    }

    private Color darken(Color color, float factor) {
        int r = (int)(color.getRed() * factor);
        int g = (int)(color.getGreen() * factor);
        int b = (int)(color.getBlue() * factor);
        return new Color(r, g, b);
    }

    private void setupListeners() {
        pageSelector.addActionListener(e -> {
            int selected = pageSelector.getSelectedIndex();
            if (selected >= 0 && selected < dataManager.getPages(currentIndex).size()) {
                saveCurrentPage();
                currentPage = selected;
                loadPage();
            }
        });

        imageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) return true;
                    String name = f.getName().toLowerCase();
                    return name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                            name.endsWith(".png") || name.endsWith(".gif") || name.endsWith(".bmp");
                }

                @Override
                public String getDescription() {
                    return "이미지 파일 (*.jpg, *.png, *.gif, *.bmp)";
                }
            });

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                    Image scaled = icon.getImage().getScaledInstance(400, 280, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaled));
                    imageLabel.setText("");
                    dataManager.updatePageImage(currentIndex, currentPage, file.getAbsolutePath());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "이미지를 불러올 수 없습니다.ㅇㅁㅇ;;", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addPage.addActionListener(e -> {
            dataManager.addPage(currentIndex);
            currentPage = dataManager.getPages(currentIndex).size() - 1;
            updatePageSelector();
            loadPage();
        });

        deletePage.addActionListener(e -> {
            if (dataManager.getPages(currentIndex).size() <= 1) {
                JOptionPane.showMessageDialog(this, "최소 한 페이지는 있어야 합니다.（〜^∇^)〜;",
                        "알림", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int result = JOptionPane.showConfirmDialog(this,
                    "정말 이 페이지를 삭제하시겠습니까?(づ-̩̩̩-̩̩̩_-̩̩̩-̩̩̩)づ ",
                    "페이지 삭제", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                if (dataManager.deletePage(currentIndex, currentPage)) {
                    currentPage = Math.max(0, currentPage - 1);
                    updatePageSelector();
                    loadPage();
                }
            }
        });

        nextPage.addActionListener(e -> {
            if (currentPage < dataManager.getPages(currentIndex).size() - 1) {
                saveCurrentPage();
                currentPage++;
                pageSelector.setSelectedIndex(currentPage);
                loadPage();
            }
        });

        prevPage.addActionListener(e -> {
            if (currentPage > 0) {
                saveCurrentPage();
                currentPage--;
                pageSelector.setSelectedIndex(currentPage);
                loadPage();
            }
        });

        hideButton.addActionListener(e -> {
            hidden = !hidden;
            if (hidden) {
                textArea.setForeground(textArea.getBackground());
                hideButton.setText(" 보이기");
            } else {
                textArea.setForeground(BROWN);
                hideButton.setText("가리기");
            }
        });

        backButton.addActionListener(e -> {
            saveCurrentPage();
            listener.onBackToIndexSelection();
        });
    }

    private void updatePageSelector() {
        pageSelector.removeAllItems();
        ArrayList<Page> pages = dataManager.getPages(currentIndex);
        for (int i = 0; i < pages.size(); i++) {
            pageSelector.addItem("페이지 " + (i + 1));
        }
        pageSelector.setSelectedIndex(currentPage);
    }

    private void loadPage() {
        Page page = dataManager.getPages(currentIndex).get(currentPage);
        textArea.setText(page.text);
        if (page.imagePath != null && !page.imagePath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(page.imagePath);
                Image scaled = icon.getImage().getScaledInstance(400, 280, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
                imageLabel.setText("");
            } catch (Exception e) {
                imageLabel.setIcon(null);
                imageLabel.setText("이미지를 불러올 수 없습니다ㅠㅅㅠ");
            }
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("이미지 추가!(-ω- )");
        }
    }

    private void saveCurrentPage() {
        if (currentIndex != null && !currentIndex.isEmpty()) {
            dataManager.updatePageText(currentIndex, currentPage, textArea.getText());
        }
    }
}