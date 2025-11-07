package util;

public class AuthService {
    private final String user = "FranciscoVital";
    private final String pass = "12345678";

    public boolean authenticate(String u, String p) {
        if (u == null || p == null) return false;
        return user.equalsIgnoreCase(u.trim()) && pass.equals(p.trim());
    }
}
