import java.util.HashMap;
import java.util.Map;

class UserManager {
    private Map<String, String> users = new HashMap<>();

    public boolean authenticate(String id, String password) {
        return users.containsKey(id) && users.get(id).equals(password);
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
}
