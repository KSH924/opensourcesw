import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

class IndexSelectionPanel extends JPanel {
    private DefaultComboBoxModel<String> indexModel;
    private JComboBox<String> indexSelector;
    private JButton createIndexBtn;
    private JButton deleteIndexBtn;
    private JButton enterIndexBtn;
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
    }

    private void initComponents() {
        indexModel = new DefaultComboBoxModel<>();
        HashMap<String, ArrayList<Page>> indexMap = dataManager.getIndexMap();
        for (String name : indexMap.keySet()) {
            indexModel.addElement(name);
        }

        indexSelector = new JComboBox<>(indexModel);
        createIndexBtn = new JButton("인덱스 생성");
        deleteIndexBtn = new JButton("인덱스 삭제");
        enterIndexBtn = new JButton("인덱스 열기");

        add(indexSelector);
        add(createIndexBtn);
        add(enterIndexBtn);
        add(deleteIndexBtn);
    }

    private void setupListeners() {
        createIndexBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("인덱스 이름 입력:");
            if (dataManager.addIndex(name)) {
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
}