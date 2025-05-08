import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IndexStudyApp {
    private JFrame frame;
    private JTextArea textArea;
    private JLabel imageLabel;
    private DefaultComboBoxModel<String> indexModel;
    private HashMap<String, ArrayList<Page>> indexMap;
    private int currentPage = 0;
    private String currentIndex = "";

    private JTextField idField;
    private JPasswordField passwordField;
    private JPanel loginPanel;
    private JPanel mainPanel;
    private JPanel cardPanel;
    private JButton hideButton;
    private boolean hidden = false;

    private Map<String, String> users = new HashMap<>();
    private JComboBox<String> pageSelector;

    private static final String DATA_FILE = "index_data.ser";

    public IndexStudyApp() {

        frame = new JFrame("IndexStudy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 750);
        frame.setResizable(false);

        loadData();
        showLoginScreen();
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(indexMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof HashMap) {
                indexMap = (HashMap<String, ArrayList<Page>>) obj;
            }
        } catch (Exception e) {
            indexMap = new HashMap<>();
        }
    }

    private void showLoginScreen() {
        loginPanel = new JPanel(new GridLayout(6, 1));
        idField = new JTextField();
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("로그인");
        JButton signupButton = new JButton("회원가입");

        loginPanel.add(new JLabel("아이디:"));
        loginPanel.add(idField);
        loginPanel.add(new JLabel("비밀번호:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(signupButton);

        frame.getContentPane().add(loginPanel);

        loginButton.addActionListener(e -> {
            String id = idField.getText();
            String pw = new String(passwordField.getPassword());
            if (users.containsKey(id) && users.get(id).equals(pw)) {
                showIndexSelectionUI();
            } else {
                JOptionPane.showMessageDialog(frame, "아이디 또는 비밀번호가 틀렸습니다.");
            }
        });

        signupButton.addActionListener(e -> {
            String newId = JOptionPane.showInputDialog("새 아이디:");
            if (newId != null && !newId.trim().isEmpty()) {
                if (users.containsKey(newId)) {
                    JOptionPane.showMessageDialog(frame, "이미 존재하는 아이디입니다.");
                    return;
                }
                String newPw = JOptionPane.showInputDialog("새 비밀번호:");
                if (newPw != null && !newPw.trim().isEmpty()) {
                    users.put(newId, newPw);
                    JOptionPane.showMessageDialog(frame, "회원가입 성공: 로그인 해주세요.");
                }
            }
        });

        frame.setVisible(true);
    }

    private void showIndexSelectionUI() {
        frame.getContentPane().removeAll();

        indexModel = new DefaultComboBoxModel<>();
        for (String name : indexMap.keySet()) {
            indexModel.addElement(name);
        }

        JPanel topPanel = new JPanel();
        JComboBox<String> indexSelector = new JComboBox<>(indexModel);
        JButton createIndexBtn = new JButton("인덱스 생성");
        JButton deleteIndexBtn = new JButton("인덱스 삭제");
        JButton enterIndexBtn = new JButton("인덱스 열기");

        topPanel.add(indexSelector);
        topPanel.add(createIndexBtn);
        topPanel.add(enterIndexBtn);
        topPanel.add(deleteIndexBtn);

        createIndexBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("인덱스 이름 입력:");
            if (name != null && !name.trim().isEmpty() && !indexMap.containsKey(name)) {
                indexModel.addElement(name);
                indexMap.put(name, new ArrayList<>());
                saveData();
            }
        });

        deleteIndexBtn.addActionListener(e -> {
            String name = (String) indexSelector.getSelectedItem();
            if (name != null) {
                indexModel.removeElement(name);
                indexMap.remove(name);
                saveData();
            }
        });

        enterIndexBtn.addActionListener(e -> {
            String name = (String) indexSelector.getSelectedItem();
            if (name != null) {
                showMainUI(name);
            }
        });

        frame.getContentPane().add(topPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void showMainUI(String indexName) {
        frame.getContentPane().removeAll();
        mainPanel = new JPanel(new BorderLayout());

        currentIndex = indexName;
        ArrayList<Page> pages = indexMap.get(currentIndex);
        if (pages.isEmpty()) {
            pages.add(new Page());
        }
        currentPage = 0;

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
        JButton imageButton = new JButton("이미지 추가");
        JButton nextPage = new JButton("다음 페이지");
        JButton prevPage = new JButton("이전 페이지");
        JButton addPage = new JButton("페이지 추가");
        JButton deletePage = new JButton("페이지 삭제");
        JButton backButton = new JButton("목록으로");
        hideButton = new JButton("단어 가리기");

        pageSelector = new JComboBox<>();
        pageSelector.addActionListener(e -> {
            int selected = pageSelector.getSelectedIndex();
            if (selected >= 0 && selected < indexMap.get(currentIndex).size()) {
                saveCurrentPage();
                currentPage = selected;
                loadPage();
            }
        });

        imageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                Image scaled = icon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaled));
                getCurrentPage().imagePath = file.getAbsolutePath();
                saveData();
            }
        });

        addPage.addActionListener(e -> {
            indexMap.get(currentIndex).add(new Page());
            currentPage = indexMap.get(currentIndex).size() - 1;
            updatePageSelector();
            loadPage();
            saveData();
        });

        deletePage.addActionListener(e -> {
            if (indexMap.get(currentIndex).size() > 1) {
                indexMap.get(currentIndex).remove(currentPage);
                currentPage = Math.max(0, currentPage - 1);
                updatePageSelector();
                loadPage();
                saveData();
            } else {
                JOptionPane.showMessageDialog(frame, "최소 한 페이지는 있어야 합니다.");
            }
        });

        nextPage.addActionListener(e -> {
            if (currentPage < indexMap.get(currentIndex).size() - 1) {
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
            saveData();
            showIndexSelectionUI();
        });

        buttonPanel.add(prevPage);
        buttonPanel.add(nextPage);
        buttonPanel.add(addPage);
        buttonPanel.add(deletePage);
        buttonPanel.add(pageSelector);
        buttonPanel.add(imageButton);
        buttonPanel.add(hideButton);
        buttonPanel.add(backButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
        updatePageSelector();
        loadPage();
        frame.revalidate();
        frame.repaint();
    }

    private void updatePageSelector() {
        if (pageSelector == null) return;
        pageSelector.removeAllItems();
        ArrayList<Page> pages = indexMap.get(currentIndex);
        for (int i = 0; i < pages.size(); i++) {
            pageSelector.addItem("페이지 " + (i + 1));
        }
        pageSelector.setSelectedIndex(currentPage);
    }

    private void loadPage() {
        Page page = getCurrentPage();
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
            Page page = getCurrentPage();
            page.text = textArea.getText();
        }
    }

    private Page getCurrentPage() {
        return indexMap.get(currentIndex).get(currentPage);
    }

    class Page implements Serializable {
        String text = "";
        String imagePath = null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IndexStudyApp());
    }
}