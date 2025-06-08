import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

class IndexContentPanel extends JPanel {
    private IndexDataManager dataManager;
    private String indexName;
    private IndexContentListener listener;
    private ArrayList<Page> pages;
    private int currentPageIndex = 0;

    // UI Components
    private JTextArea textArea;
    private JLabel imageLabel;
    private JComboBox<String> pageSelector;
    private TextHighlighter textHighlighter;
    private FontSizeController fontSizeController;

    // Buttons
    private JButton prevPageBtn;
    private JButton nextPageBtn;
    private JButton addPageBtn;
    private JButton deletePageBtn;
    private JButton addImageBtn;
    private JButton backBtn;
    private JButton saveAsImageBtn;
    private JButton clearHighlightsBtn;

    // Highlight buttons
    private JButton highlightRedBtn;
    private JButton highlightYellowBtn;
    private JButton highlightBlueBtn;

    public interface IndexContentListener {
        void onBackToIndexSelection();
    }

    public IndexContentPanel(IndexDataManager dataManager, String indexName, IndexContentListener listener) {
        this.dataManager = dataManager;
        this.indexName = indexName;
        this.listener = listener;
        this.pages = dataManager.getPages(indexName);

        if (pages == null || pages.isEmpty()) {
            pages = new ArrayList<>();
            pages.add(new Page());
            dataManager.getIndexMap().put(indexName, pages);
            dataManager.saveData();
        }

        initComponents();
        setupLayout();
        setupListeners();
        loadCurrentPage();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // 텍스트 영역
        textArea = new JTextArea(15, 40);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 텍스트 하이라이터 초기화
        textHighlighter = new TextHighlighter(textArea);

        // 폰트 크기 컨트롤러 초기화
        fontSizeController = new FontSizeController(textArea);

        // 이미지 라벨
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(400, 250));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        imageLabel.setBackground(Color.WHITE);
        imageLabel.setOpaque(true);

        // 페이지 선택기
        pageSelector = new JComboBox<>();
        updatePageSelector();

        // 버튼들 초기화
        initButtons();
    }

    private void initButtons() {
        // 네비게이션 버튼
        prevPageBtn = createStyledButton("이전 페이지");
        nextPageBtn = createStyledButton("다음 페이지");
        addPageBtn = createStyledButton("페이지 추가");
        deletePageBtn = createStyledButton("페이지 삭제");
        addImageBtn = createStyledButton("이미지 추가");
        backBtn = createStyledButton("뒤로가기");
        saveAsImageBtn = createStyledButton("이미지로 저장");
        clearHighlightsBtn = createStyledButton("하이라이트 지우기");

        // 하이라이트 버튼들
        highlightRedBtn = createHighlightButton("빨강", TextHighlighter.PASTEL_RED);
        highlightYellowBtn = createHighlightButton("노랑", TextHighlighter.PASTEL_YELLOW);
        highlightBlueBtn = createHighlightButton("파랑", TextHighlighter.PASTEL_BLUE);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 11));
        button.setBackground(new Color(255, 182, 193));
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setFocusPainted(false);
        return button;
    }

    private JButton createHighlightButton(String text, Color color) {
        JButton button = createStyledButton(text);
        button.setBackground(color);
        return button;
    }

    // setupLayout() 메서드의 하단 패널 부분을 수정
    private void setupLayout() {
        // 상단 패널 - 폰트 크기 조절과 하이라이트 버튼들
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(245, 245, 245));

        topPanel.add(fontSizeController.getFontSizePanel());
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(new JLabel("하이라이트:"));
        topPanel.add(highlightRedBtn);
        topPanel.add(highlightYellowBtn);
        topPanel.add(highlightBlueBtn);
        topPanel.add(clearHighlightsBtn);

        // 중앙 패널 - 텍스트와 이미지
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane textScroll = new JScrollPane(textArea);
        textScroll.setPreferredSize(new Dimension(500, 300));
        textScroll.setBorder(BorderFactory.createTitledBorder("텍스트"));

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createTitledBorder("이미지"));
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        centerPanel.add(textScroll);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(imagePanel);

        // 하단 패널 - 컨트롤 버튼들 (2줄로 분리)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(new Color(245, 245, 245));

        // 첫 번째 줄: 페이지 관련 버튼들
        JPanel pageControlPanel = new JPanel(new FlowLayout());
        pageControlPanel.setBackground(new Color(245, 245, 245));
        pageControlPanel.add(new JLabel("페이지:"));
        pageControlPanel.add(pageSelector);
        pageControlPanel.add(prevPageBtn);
        pageControlPanel.add(nextPageBtn);
        pageControlPanel.add(addPageBtn);
        pageControlPanel.add(deletePageBtn);

        // 두 번째 줄: 기타 기능 버튼들
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setBackground(new Color(245, 245, 245));
        actionPanel.add(addImageBtn);
        actionPanel.add(saveAsImageBtn);
        actionPanel.add(backBtn); // 뒤로가기 버튼을 별도 줄에 배치

        bottomPanel.add(pageControlPanel);
        bottomPanel.add(actionPanel);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        // 페이지 선택
        pageSelector.addActionListener(e -> {
            int selectedIndex = pageSelector.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex != currentPageIndex) {
                saveCurrentPage();
                currentPageIndex = selectedIndex;
                loadCurrentPage();
            }
        });

        // 네비게이션
        prevPageBtn.addActionListener(e -> {
            if (currentPageIndex > 0) {
                saveCurrentPage();
                currentPageIndex--;
                pageSelector.setSelectedIndex(currentPageIndex);
                loadCurrentPage();
            }
        });

        nextPageBtn.addActionListener(e -> {
            if (currentPageIndex < pages.size() - 1) {
                saveCurrentPage();
                currentPageIndex++;
                pageSelector.setSelectedIndex(currentPageIndex);
                loadCurrentPage();
            }
        });

        // 페이지 관리 - 수정된 부분: 빈 페이지를 맨 뒤에 추가
        addPageBtn.addActionListener(e -> {
            saveCurrentPage();
            // 빈 페이지를 맨 뒤에 추가
            dataManager.addPage(indexName); // 기존 메서드 사용 (맨 뒤에 추가)
            pages = dataManager.getPages(indexName); // 업데이트된 페이지 목록 가져오기
            currentPageIndex = pages.size() - 1; // 새로 추가된 페이지(맨 마지막)로 이동
            updatePageSelector();
            loadCurrentPage();
        });

        deletePageBtn.addActionListener(e -> {
            if (pages.size() > 1) {
                boolean deleted = dataManager.deletePage(indexName, currentPageIndex);
                if (deleted) {
                    pages = dataManager.getPages(indexName);
                    currentPageIndex = Math.max(0, currentPageIndex - 1);
                    updatePageSelector();
                    loadCurrentPage();
                }
            } else {
                JOptionPane.showMessageDialog(this, "최소 한 페이지는 있어야 합니다.");
            }
        });

        // 이미지 추가
        addImageBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "이미지 파일", "jpg", "jpeg", "png", "gif", "bmp"));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String imagePath = file.getAbsolutePath();
                dataManager.updatePageImage(indexName, currentPageIndex, imagePath);
                loadImageFromPath(imagePath);
            }
        });

        // 하이라이트 버튼들
        highlightRedBtn.addActionListener(e -> {
            textHighlighter.setHighlightColor(TextHighlighter.PASTEL_RED);
            textHighlighter.setHighlightMode(true);
        });

        highlightYellowBtn.addActionListener(e -> {
            textHighlighter.setHighlightColor(TextHighlighter.PASTEL_YELLOW);
            textHighlighter.setHighlightMode(true);
        });

        highlightBlueBtn.addActionListener(e -> {
            textHighlighter.setHighlightColor(TextHighlighter.PASTEL_BLUE);
            textHighlighter.setHighlightMode(true);
        });

        clearHighlightsBtn.addActionListener(e -> {
            textHighlighter.clearAllHighlights();
            saveCurrentPage();
        });

        // 이미지로 저장
        saveAsImageBtn.addActionListener(e -> savePageAsImage());

        // 뒤로가기 - 수정된 부분: 현재 페이지 저장 후 돌아가기
        backBtn.addActionListener(e -> {
            saveCurrentPage(); // 현재 페이지 저장
            dataManager.saveData(); // 데이터 저장
            listener.onBackToIndexSelection();
        });

        // 텍스트 변경 감지 (하이라이트 저장용)
        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { saveHighlights(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { saveHighlights(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { saveHighlights(); }
        });
    }

    private void saveHighlights() {
        SwingUtilities.invokeLater(() -> {
            if (currentPageIndex < pages.size()) {
                dataManager.updatePageHighlights(indexName, currentPageIndex, textHighlighter.getHighlights());
            }
        });
    }

    private void updatePageSelector() {
        pageSelector.removeAllItems();
        for (int i = 0; i < pages.size(); i++) {
            pageSelector.addItem("페이지 " + (i + 1));
        }
        pageSelector.setSelectedIndex(currentPageIndex);
    }

    private void loadCurrentPage() {
        if (currentPageIndex < pages.size()) {
            Page page = pages.get(currentPageIndex);

            // 텍스트 로드
            textArea.setText(page.text);

            // 폰트 크기 설정
            fontSizeController.setFontSize(page.fontSize);

            // 하이라이트 로드
            textHighlighter.setHighlights(page.highlights);

            // 이미지 로드
            if (page.imagePath != null && !page.imagePath.isEmpty()) {
                loadImageFromPath(page.imagePath);
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("이미지가 없습니다.");
            }
        }
    }

    private void saveCurrentPage() {
        if (currentPageIndex < pages.size()) {
            // 텍스트 저장
            dataManager.updatePageText(indexName, currentPageIndex, textArea.getText());

            // 폰트 크기 저장
            dataManager.updatePageFontSize(indexName, currentPageIndex, fontSizeController.getCurrentFontSize());

            // 하이라이트 저장
            dataManager.updatePageHighlights(indexName, currentPageIndex, textHighlighter.getHighlights());
        }
    }

    private void loadImageFromPath(String imagePath) {
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(400, 250, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
            imageLabel.setText("");
        } catch (Exception e) {
            imageLabel.setIcon(null);
            imageLabel.setText("이미지를 불러올 수 없습니다.");
        }
    }

    private void savePageAsImage() {
        try {
            // 현재 페이지 컨텐츠를 이미지로 캡처
            BufferedImage image = new BufferedImage(500, 600, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();

            // 배경을 흰색으로 설정
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, 500, 600);

            // 텍스트 영역 그리기
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("맑은 고딕", Font.PLAIN, fontSizeController.getCurrentFontSize()));

            String text = textArea.getText();
            String[] lines = text.split("\n");
            int y = 30;
            int lineHeight = fontSizeController.getCurrentFontSize() + 5;

            for (String line : lines) {
                if (y > 280) break; // 텍스트 영역 제한
                g2d.drawString(line, 10, y);
                y += lineHeight;
            }

            // 이미지가 있다면 추가
            if (imageLabel.getIcon() != null) {
                ImageIcon icon = (ImageIcon) imageLabel.getIcon();
                g2d.drawImage(icon.getImage(), 50, 300, 400, 250, null);
            }

            g2d.dispose();

            // 파일 저장 다이얼로그
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(indexName + "_page" + (currentPageIndex + 1) + ".png"));
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG 이미지", "png"));

            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".png")) {
                    file = new File(file.getAbsolutePath() + ".png");
                }
                ImageIO.write(image, "png", file);
                JOptionPane.showMessageDialog(this, "이미지가 저장되었습니다: " + file.getAbsolutePath());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "이미지 저장 중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
    }
}