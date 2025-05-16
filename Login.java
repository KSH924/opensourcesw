import javax.swing.*;
import java.awt.*;

class LoginPanel extends JPanel {
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private UserManager userManager;
    private LoginListener loginListener;

    public interface LoginListener {
        void onLoginSuccess();
        void onSignupSuccess();
    }

    public LoginPanel(UserManager userManager, LoginListener listener) {
        this.userManager = userManager;
        this.loginListener = listener;

        setLayout(new GridLayout(6, 1));
        initComponents();
        setupListeners();
    }

    private void initComponents() {
        idField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("로그인");
        signupButton = new JButton("회원가입");

        add(new JLabel("아이디:"));
        add(idField);
        add(new JLabel("비밀번호:"));
        add(passwordField);
        add(loginButton);
        add(signupButton);
    }

    private void setupListeners() {
        loginButton.addActionListener(e -> {
            String id = idField.getText();
            String pw = new String(passwordField.getPassword());
            if (userManager.authenticate(id, pw)) {
                loginListener.onLoginSuccess();
            } else {
                JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 틀렸습니다.");
            }
        });

        signupButton.addActionListener(e -> {
            String newId = JOptionPane.showInputDialog("새 아이디:");
            if (newId != null && !newId.trim().isEmpty()) {
                if (userManager.userExists(newId)) {
                    JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.");
                    return;
                }
                String newPw = JOptionPane.showInputDialog("새 비밀번호:");
                if (userManager.registerUser(newId, newPw)) {
                    JOptionPane.showMessageDialog(this, "회원가입 성공: 로그인 해주세요.");
                    loginListener.onSignupSuccess();
                }
            }
        });
    }
}