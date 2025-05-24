import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

class LoginPanel extends JPanel {
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private UserManager userManager;
    private LoginListener loginListener;

    // 색상 상수
    private static final Color BABY_PINK = new Color(255, 182, 193);
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color FIELD_BACKGROUND = new Color(248, 249, 250);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);

    public interface LoginListener {
        void onLoginSuccess();
        void onSignupSuccess();
    }

    public LoginPanel(UserManager userManager, LoginListener listener) {
        this.userManager = userManager;
        this.loginListener = listener;

        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(40, 40, 40, 40));

        initComponents();
        setupListeners();
    }

    private void initComponents() {
        // 메인 패널
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // 제목
        JLabel titleLabel = new JLabel("♥INDEX STUDY♥", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        titleLabel.setForeground(BABY_PINK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));

        // 입력 필드 패널
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBackground(BACKGROUND_COLOR);

        // 아이디 입력
        JLabel idLabel = new JLabel("♥아이디♥");
        idLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        idLabel.setForeground(TEXT_COLOR);
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        idField = createStyledTextField();
        idField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // 비밀번호 입력
        JLabel passwordLabel = new JLabel("♥비밀번호♥");
        passwordLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = createStyledPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        loginButton = createStyledButton("로그인", true);
        signupButton = createStyledButton("회원가입", false);

        // 컴포넌트 추가
        fieldsPanel.add(idLabel);
        fieldsPanel.add(Box.createVerticalStrut(8));
        fieldsPanel.add(idField);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(Box.createVerticalStrut(8));
        fieldsPanel.add(passwordField);

        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(signupButton);

        mainPanel.add(titleLabel);
        mainPanel.add(fieldsPanel);
        mainPanel.add(buttonPanel);

        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(BACKGROUND_COLOR);
        wrapperPanel.add(mainPanel);

        add(wrapperPanel, BorderLayout.CENTER);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        field.setForeground(TEXT_COLOR);
        field.setBackground(FIELD_BACKGROUND);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 3),
                BorderFactory.createEmptyBorder(11, 15, 12, 15)
        ));

        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        field.setForeground(TEXT_COLOR);
        field.setBackground(FIELD_BACKGROUND);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 3),
                BorderFactory.createEmptyBorder(11, 15, 13, 15)
        ));
        return field;
    }
    private JButton createStyledButton(String text, boolean isPrimary) {
        JButton button = new JButton(text);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        button.setForeground(TEXT_COLOR);
        button.setBackground(BABY_PINK);
        button.setBorder(BorderFactory.createEmptyBorder(16, 25, 16, 25));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.setPreferredSize(new Dimension(200, 50));

        if (!isPrimary) {
            button.setBackground(BACKGROUND_COLOR);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BABY_PINK, 3),
                    BorderFactory.createEmptyBorder(13, 23, 13, 23)
            ));
        }

        return button;
    }

    private void setupListeners() {
        loginButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String pw = new String(passwordField.getPassword());

            if (id.isEmpty() || pw.isEmpty()) {
                showStyledMessage("아이디와 비밀번호를 모두 입력해주세요.（￣ε￣ʃƪ）", "입력 오류");
                return;
            }

            if (userManager.authenticate(id, pw)) {
                loginListener.onLoginSuccess();
            } else {
                showStyledMessage("아이디 또는 비밀번호가 틀렸습니다.（￣□￣；）", "로그인 실패");
            }
        });

        signupButton.addActionListener(e -> {
            String newId = JOptionPane.showInputDialog(this, "새 아이디를 입력하세요", "회원가입", JOptionPane.PLAIN_MESSAGE);
            if (newId != null && !newId.trim().isEmpty()) {
                if (userManager.userExists(newId.trim())) {
                    showStyledMessage("이미 존재하는 아이디입니다.", "회원가입 실패( •̀ω•́ )");
                    return;
                }
                String newPw = JOptionPane.showInputDialog(this, "새 비밀번호를 입력하세요", "회원가입", JOptionPane.PLAIN_MESSAGE);
                if (newPw != null && !newPw.trim().isEmpty()) {
                    if (userManager.registerUser(newId.trim(), newPw)) {
                        showStyledMessage("회원가입이 완료되었습니다!\n로그인해주세요|⩊･)ﾉ⁾⁾", "회원가입 성공");
                        loginListener.onSignupSuccess();
                    }
                }
            }
        });

        // Enter 키로 로그인
        idField.addActionListener(e -> passwordField.requestFocus());
        passwordField.addActionListener(e -> loginButton.doClick());
    }

    private void showStyledMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}