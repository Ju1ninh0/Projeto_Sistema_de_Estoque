package view;

import controller.EstoqueController;
import exceptions.EstoqueException;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import model.Duravel;
import model.Perecivel;
import model.Produto;
import util.DateUtils;

public class EstoqueView extends JFrame {

    private static final Color BG = new Color(18, 18, 20);
    private static final Color PANEL = new Color(27, 27, 30);
    private static final Color PANEL_SOFT = new Color(32, 33, 37);
    private static final Color TXT = new Color(235, 235, 245);
    private static final Color TXT_MID = new Color(185, 185, 195);
    private static final Color ACCENT = new Color(99, 123, 255);
    private static final Color ACCENT_HOVER = new Color(119, 143, 255);
    private static final Color DANGER = new Color(232, 93, 117);
    private static final Color GRID = new Color(45, 46, 50);
    private static final Color ZEBRA_A = new Color(30, 31, 35);
    private static final Color ZEBRA_B = new Color(38, 39, 45);
    private static final Color SELECT_BG = new Color(53, 60, 95);

    private final EstoqueController controller;
    private JTable tabela;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField tfNome, tfQtd, tfPreco, tfValidade, tfGarantia, tfBusca;
    private JLabel lblResumo;
    private JRadioButton rbPerecivel, rbDuravel;

    public EstoqueView(EstoqueController controller) {
        this.controller = controller;
        setTitle("💰 JavaTáCaro");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1080, 680);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));
        root.setBackground(BG);

        root.add(criarHeader(), BorderLayout.NORTH);
        root.add(criarCentro(), BorderLayout.CENTER);
        root.add(criarFooter(), BorderLayout.SOUTH);

        setContentPane(root);

        carregarTabela();
        atualizarResumo();
    }

    private JComponent criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);

        JLabel titulo = new JLabel("💰 JavaTáCaro");
        titulo.setForeground(TXT);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 20f));

        JPanel busca = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        busca.setOpaque(false);
        tfBusca = criarCampo("Buscar por nome");
        tfBusca.setColumns(28);
        tfBusca.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });

        JButton btnLimpar = botao("Limpar", PANEL_SOFT, TXT, ACCENT);
        btnLimpar.addActionListener(e -> { tfBusca.setText(""); filtrar(); });

        busca.add(tfBusca);
        busca.add(btnLimpar);

        header.add(titulo, BorderLayout.WEST);
        header.add(busca, BorderLayout.EAST);
        return header;
    }

    private JComponent criarCentro() {
        JPanel centro = new JPanel(new BorderLayout(12, 12));
        centro.setOpaque(false);

        modelo = new DefaultTableModel(new Object[]{"Nome", "Categoria", "Quantidade", "Preço", "Info"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tabela = new JTable(modelo) {
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) c.setBackground(row % 2 == 0 ? ZEBRA_A : ZEBRA_B);
                else c.setBackground(SELECT_BG);
                c.setForeground(TXT);
                if (c instanceof JComponent jc) jc.setBorder(new EmptyBorder(6, 10, 6, 10));
                return c;
            }
        };
        tabela.setRowHeight(28);
        tabela.setFont(tabela.getFont().deriveFont(Font.PLAIN, 14f));
        tabela.setGridColor(GRID);
        tabela.setShowVerticalLines(false);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader th = tabela.getTableHeader();
        th.setBackground(PANEL_SOFT);
        th.setForeground(TXT_MID);
        th.setFont(th.getFont().deriveFont(Font.BOLD, 13f));
        ((DefaultTableCellRenderer) th.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        sorter = new TableRowSorter<>(modelo);
        tabela.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.getViewport().setBackground(PANEL);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(PANEL);
        form.setBorder(new EmptyBorder(12, 12, 12, 12));

        rbPerecivel = radio("Perecível", true);
        rbDuravel = radio("Durável", false);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbPerecivel);
        bg.add(rbDuravel);

        tfNome = criarCampo("Nome do produto");
        tfQtd = criarCampo("Quantidade");
        tfPreco = criarCampo("Preço (R$)");
        tfValidade = criarCampo("Validade (dd-MM-yyyy)");
        tfGarantia = criarCampo("Garantia (meses)");

        JButton btnAdd = botao("Adicionar", ACCENT, Color.WHITE, ACCENT_HOVER);
        btnAdd.addActionListener(e -> adicionar());

        JButton btnAtualizar = botao("Atualizar", PANEL_SOFT, TXT, ACCENT);
        btnAtualizar.addActionListener(e -> atualizarQuantidade());

        JButton btnRemover = botao("Remover", DANGER, Color.WHITE, new Color(242, 120, 140));
        btnRemover.addActionListener(e -> remover());

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;

        int y = 0;
        gc.gridx = 0; gc.gridy = y; form.add(rbPerecivel, gc);
        gc.gridx = 1; form.add(rbDuravel, gc);
        y++;
        gc.gridx = 0; gc.gridy = y; form.add(tfNome, gc);
        gc.gridx = 1; form.add(tfQtd, gc);
        gc.gridx = 2; form.add(tfPreco, gc);
        y++;
        gc.gridx = 0; gc.gridy = y; form.add(tfValidade, gc);
        gc.gridx = 1; form.add(tfGarantia, gc);
        y++;
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        acoes.setOpaque(false);
        acoes.add(btnAdd); acoes.add(btnAtualizar); acoes.add(btnRemover);
        gc.gridx = 0; gc.gridy = y; gc.gridwidth = 3; form.add(acoes, gc);

        centro.add(scroll, BorderLayout.CENTER);
        centro.add(form, BorderLayout.SOUTH);
        return centro;
    }

    private JComponent criarFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        lblResumo = new JLabel("—");
        lblResumo.setForeground(TXT_MID);
        footer.add(lblResumo, BorderLayout.WEST);
        return footer;
    }

    private void adicionar() {
        try {
            String nome = tfNome.getText().trim();
            if (nome.isEmpty() || nome.equals("Nome do produto")) throw new EstoqueException("Informe o nome corretamente.");
            int qtd = Integer.parseInt(tfQtd.getText().replaceAll("[^0-9]", ""));
            double preco = Double.parseDouble(tfPreco.getText().replace(",", "."));
            if (rbPerecivel.isSelected()) {
                LocalDate validade = DateUtils.parseOrNull(tfValidade.getText());
                if (validade == null) throw new EstoqueException("Validade inválida. Use dd-MM-yyyy.");
                controller.adicionarProduto(new Perecivel(nome, qtd, preco, validade));
            } else {
                int garantia = Integer.parseInt(tfGarantia.getText().replaceAll("[^0-9]", ""));
                controller.adicionarProduto(new Duravel(nome, qtd, preco, garantia));
            }
            carregarTabela();
            atualizarResumo();
            limparCampos();
            sorter.setRowFilter(null);
            JOptionPane.showMessageDialog(this, "Produto adicionado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void atualizarQuantidade() {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um produto."); return; }
        String nome = (String) tabela.getValueAt(row, 0);
        String nova = JOptionPane.showInputDialog(this, "Nova quantidade:");
        if (nova == null || nova.isBlank()) return;
        controller.atualizarQuantidade(nome, Integer.parseInt(nova.replaceAll("[^0-9]", "")));
        carregarTabela();
        atualizarResumo();
        sorter.setRowFilter(null);
    }

    private void remover() {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um produto."); return; }
        String nome = (String) tabela.getValueAt(row, 0);
        controller.removerProduto(nome);
        carregarTabela();
        atualizarResumo();
        sorter.setRowFilter(null);
    }

    private void carregarTabela() {
        modelo.setRowCount(0);
        List<Produto> lista = controller.listarProdutos();

        for (Produto p : lista) {
            String info = "-";

            if (p instanceof Perecivel per) {
                info = "Validade: " + util.DateUtils.formatOrBlank(per.getValidade());
            } else if (p instanceof Duravel du) {
                info = "Garantia: " + du.getGarantiaMeses() + " meses";
            }

            modelo.addRow(new Object[]{
                p.getNome(),
                (p instanceof Perecivel) ? "Perecível" : "Durável",
                p.getQuantidade(),
                String.format("R$ %.2f", p.getPreco()),
                info
            });
        }
    }

    private void atualizarResumo() {
        int itens = controller.quantidadeTotalItens();
        int produtos = controller.listarProdutos().size();
        double soma = controller.somaPrecos();
        lblResumo.setText(String.format("Produtos: %d | Itens: %d | Soma: R$ %.2f", produtos, itens, soma));
    }

    private void filtrar() {
        String q = tfBusca.getText();
        if (q == null || q.isBlank()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(q), 0));
        }
    }

    private JTextField criarCampo(String placeholder) {
        JTextField tf = new JTextField();
        tf.setBackground(PANEL_SOFT);
        tf.setForeground(TXT);
        tf.setCaretColor(TXT);
        tf.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(GRID), new EmptyBorder(8, 10, 8, 10)));
        tf.setText(placeholder);
        tf.setForeground(new Color(160, 160, 170));
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (tf.getText().equals(placeholder)) { tf.setText(""); tf.setForeground(TXT); }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (tf.getText().isBlank()) { tf.setText(placeholder); tf.setForeground(new Color(160, 160, 170)); }
            }
        });
        return tf;
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
        b.setBorder(new EmptyBorder(8, 14, 8, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JRadioButton radio(String texto, boolean selecionado) {
        JRadioButton rb = new JRadioButton(texto);
        rb.setSelected(selecionado);
        rb.setForeground(new Color(235, 235, 245));
        rb.setBackground(new Color(32, 33, 37));
        rb.setFocusPainted(false);
        rb.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return rb;
    }

    private void limparCampos() {
        tfNome.setText("Nome do produto");
        tfQtd.setText("Quantidade");
        tfPreco.setText("Preço (R$)");
        tfValidade.setText("Validade (dd-MM-yyyy)");
        tfGarantia.setText("Garantia (meses)");
        tfNome.requestFocus();
    }
}
