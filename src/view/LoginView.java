package view;

import controller.EstoqueController;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import util.AuthService;

public class LoginView extends JFrame {

    private static final Color BG = new Color(18, 18, 20);
    private static final Color PANEL = new Color(27, 27, 30);
    private static final Color PANEL_SOFT = new Color(32, 33, 37);
    private static final Color TXT = new Color(235, 235, 245);
    private static final Color TXT_MID = new Color(185, 185, 195);
    private static final Color ACCENT = new Color(99, 123, 255);
    private static final Color ACCENT_HOVER = new Color(119, 143, 255);
    private static final Color GRID = new Color(45, 46, 50);

    private final AuthService auth = new AuthService();
    private JTextField tfUser;
    private JPasswordField tfPass;
    private JLabel lblErro;

    public LoginView() {
        setTitle("JavaTaCaro");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 340);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(PANEL);
        card.setBorder(new EmptyBorder(16,16,16,16));

        JLabel bemVindo = new JLabel("Bem-vindo ao JavaTaCaro");
        bemVindo.setForeground(TXT_MID);
        bemVindo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        bemVindo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titulo = new JLabel("Entrar");
        titulo.setForeground(TXT);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));

        tfUser = campo("Usuário");
        tfPass = senha("Senha");

        JButton btn = botao("Acessar", ACCENT, Color.WHITE, ACCENT_HOVER);
        btn.addActionListener(e -> login());

        lblErro = new JLabel(" ");
        lblErro.setForeground(new Color(255,120,120));
        lblErro.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8,8,8,8);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 0; gc.gridy = 0; card.add(bemVindo, gc);
        gc.gridy = 1; card.add(titulo, gc);
        gc.gridy = 2; card.add(tfUser, gc);
        gc.gridy = 3; card.add(tfPass, gc);
        gc.gridy = 4; card.add(btn, gc);
        gc.gridy = 5; card.add(lblErro, gc);

        root.add(card, BorderLayout.CENTER);
        setContentPane(root);
        getRootPane().setDefaultButton(btn);
        addKeyBindings();
    }

    private void login() {
        String u = tfUser.getText().trim();
        String p = new String(tfPass.getPassword()).trim();
        if (auth.authenticate(u, p)) {
            EstoqueController c = new EstoqueController();
            new EstoqueView(c).setVisible(true);
            dispose();
        } else {
            lblErro.setText("Usuário ou senha incorretos");
        }
    }

    private void addKeyBindings() {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "QUIT");
        am.put("QUIT", new AbstractAction() { public void actionPerformed(ActionEvent e){ System.exit(0); } });
    }

    private JTextField campo(String placeholder) {
        JTextField tf = new JTextField();
        tf.setBackground(PANEL_SOFT);
        tf.setForeground(TXT);
        tf.setCaretColor(TXT);
        tf.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(GRID), new EmptyBorder(10,12,10,12)));
        tf.setText(placeholder);
        tf.setForeground(new Color(160,160,170));
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (tf.getText().equals(placeholder)) { tf.setText(""); tf.setForeground(TXT); }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (tf.getText().isBlank()) { tf.setText(placeholder); tf.setForeground(new Color(160,160,170)); }
            }
        });
        return tf;
    }

    private JPasswordField senha(String placeholder) {
        JPasswordField pf = new JPasswordField();
        pf.setBackground(PANEL_SOFT);
        pf.setForeground(TXT);
        pf.setCaretColor(TXT);
        pf.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(GRID), new EmptyBorder(10,12,10,12)));
        pf.setEchoChar('•');
        pf.putClientProperty("ph", placeholder);
        pf.setText("");
        pf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) { pf.selectAll(); }
        });
        return pf;
    }

    private JButton botao(String txt, Color base, Color fg, Color hover) {
        JButton b = new JButton(txt) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean hovering = getMousePosition() != null;
                g2.setColor(hovering ? hover : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(fg);
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setOpaque(false);
        b.setForeground(fg);
        b.setBorder(new EmptyBorder(10, 14, 10, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
