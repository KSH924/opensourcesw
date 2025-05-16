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
    public void onLoginSuccess() {
        showIndexSelectionUI();
    }

    @Override
    public void onSignupSuccess() {

    }

    private void showIndexSelectionUI() {
        if (indexSelectionPanel == null) {
            indexSelectionPanel = new IndexSelectionPanel(dataManager, this);
            cardPanel.add(indexSelectionPanel, "index_selection");
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IndexStudyApp());
    }
}