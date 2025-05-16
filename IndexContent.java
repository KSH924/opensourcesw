import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

    public interface IndexContentListener {
        void onBackToIndexSelection();
    }

    public IndexContentPanel(IndexDataManager dataManager, String indexName, IndexContentListener listener) {
        this.dataManager = dataManager;
        this.currentIndex = indexName;
        this.listener = listener;

        setLayout(new BorderLayout());
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
        textArea = new JTextArea(10, 40);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane textScroll = new JScrollPane(textArea);
        textScroll.setPreferredSize(new Dimension(400, 200));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(400, 300));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setPreferredSize(new Dimension(400, 500));
        centerPanel.add(textScroll);
        centerPanel.add(imageLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 4));
        imageButton = new JButton("이미지 추가");
        nextPage = new JButton("다음 페이지");
        prevPage = new JButton("이전 페이지");
        addPage = new JButton("페이지 추가");
        deletePage = new JButton("페이지 삭제");
        backButton = new JButton("목록으로");
        hideButton = new JButton("단어 가리기");
        pageSelector = new JComboBox<>();

        buttonPanel.add(prevPage);
        buttonPanel.add(nextPage);
        buttonPanel.add(addPage);
        buttonPanel.add(deletePage);
        buttonPanel.add(pageSelector);
        buttonPanel.add(imageButton);
        buttonPanel.add(hideButton);
        buttonPanel.add(backButton);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
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
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                Image scaled = icon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
                dataManager.updatePageImage(currentIndex, currentPage, file.getAbsolutePath());
            }
        });

        addPage.addActionListener(e -> {
            dataManager.addPage(currentIndex);
            currentPage = dataManager.getPages(currentIndex).size() - 1;
            updatePageSelector();
            loadPage();
        });

        deletePage.addActionListener(e -> {
            if (dataManager.deletePage(currentIndex, currentPage)) {
                currentPage = Math.max(0, currentPage - 1);
                updatePageSelector();
                loadPage();
            } else {
                JOptionPane.showMessageDialog(this, "최소 한 페이지는 있어야 합니다.");
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
            textArea.setForeground(hidden ? textArea.getBackground() : Color.BLACK);
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
        if (page.imagePath != null) {
            ImageIcon icon = new ImageIcon(page.imagePath);
            Image scaled = icon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } else {
            imageLabel.setIcon(null);
        }
    }

    private void saveCurrentPage() {
        if (currentIndex != null && !currentIndex.isEmpty()) {
            dataManager.updatePageText(currentIndex, currentPage, textArea.getText());
        }
    }
}