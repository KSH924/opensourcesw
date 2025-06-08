import javax.swing.*;
import java.awt.*;

public class IndexStudyApp implements LoginPanel.LoginListener,
        IndexSelectionPanel.IndexSelectionListener,
        IndexContentPanel.IndexContentListener {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private LoginPanel loginPanel;
    private IndexSelectionPanel indexSelectionPanel;
    private IndexContentPanel indexContentPanel;

    private UserManager userManager;
    private IndexDataManager dataManager;
    private String currentUserId; // 현재 로그인된 사용자 ID 저장

    public IndexStudyApp() {
        userManager = new UserManager();
        dataManager = new IndexDataManager();

        initUI();
    }

    private void initUI() {
        frame = new JFrame("IndexStudy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 750);
        frame.setResizable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(userManager, this);
        cardPanel.add(loginPanel, "login");

        frame.getContentPane().add(cardPanel);
        cardLayout.show(cardPanel, "login");
        frame.setVisible(true);
    }

    @Override
    public void onLoginSuccess(String userId) {
        this.currentUserId = userId; // 현재 사용자 ID 저장
        System.out.println("로그인된 사용자: " + userId); // 디버깅용
        showIndexSelectionUI();
    }

    @Override
    public void onSignupSuccess() {
        // 회원가입 성공 후 필요한 처리가 있다면 여기에 추가
    }

    private void showIndexSelectionUI() {
        if (indexSelectionPanel == null) {
            indexSelectionPanel = new IndexSelectionPanel(dataManager, this);
            cardPanel.add(indexSelectionPanel, "index_selection");
        }
        // 사용자별 데이터 로드가 필요하다면 여기서 처리
        // indexSelectionPanel.loadUserData(currentUserId);

        cardLayout.show(cardPanel, "index_selection");
    }

    @Override
    public void onIndexSelected(String indexName) {
        indexContentPanel = new IndexContentPanel(dataManager, indexName, this);
        cardPanel.add(indexContentPanel, "index_content");
        cardLayout.show(cardPanel, "index_content");
    }

    @Override
    public void onBackToIndexSelection() {
        cardLayout.show(cardPanel, "index_selection");
        cardPanel.remove(indexContentPanel);
        indexContentPanel = null;
    }

    // 로그아웃 기능 (추가적으로 구현할 수 있음)
    public void logout() {
        userManager.logout();
        currentUserId = null;
        loginPanel.resetForm();
        cardLayout.show(cardPanel, "login");

        // 기존 패널들 정리
        if (indexSelectionPanel != null) {
            cardPanel.remove(indexSelectionPanel);
            indexSelectionPanel = null;
        }
        if (indexContentPanel != null) {
            cardPanel.remove(indexContentPanel);
            indexContentPanel = null;
        }
    }

    // 현재 사용자 ID 반환 (필요시 사용)
    public String getCurrentUserId() {
        return currentUserId;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IndexStudyApp());
    }
}