import java.util.HashMap;
import java.util.Map;

class UserManager {
    private Map<String, String> users = new HashMap<>();
    private String currentUserId = null; // 현재 로그인된 사용자 추적

    public boolean authenticate(String id, String password) {
        if (users.containsKey(id) && users.get(id).equals(password)) {
            currentUserId = id; // 로그인 성공 시 현재 사용자 설정
            return true;
        }
        return false;
    }

    public boolean registerUser(String id, String password) {
        if (id == null || id.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }

        if (users.containsKey(id)) {
            return false;
        }

        users.put(id, password);
        return true;
    }

    public boolean userExists(String id) {
        return users.containsKey(id);
    }

    // 현재 로그인된 사용자 ID 반환
    public String getCurrentUserId() {
        return currentUserId;
    }

    // 로그아웃 처리
    public void logout() {
        currentUserId = null;
    }

    // 현재 로그인 상태 확인
    public boolean isLoggedIn() {
        return currentUserId != null;
    }

    // 디버깅용: 등록된 모든 사용자 확인
    public Map<String, String> getAllUsers() {
        return new HashMap<>(users);
    }
}