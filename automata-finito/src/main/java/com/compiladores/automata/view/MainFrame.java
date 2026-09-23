package com.compiladores.automata.view;

import com.compiladores.automata.controller.AutomatonController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public final class MainFrame extends JFrame {
    private final AutomatonController controller = new AutomatonController();
    private final JLabel statusLabel = new JLabel("Define el alfabeto y crea los estados.");
    private final AutomatonCanvas canvas = new AutomatonCanvas(controller);
    private final PropertiesPanel propertiesPanel;

    public MainFrame() {
        super("Autómata Finito — Editor y simulador de AFD");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(AppTheme.BACKGROUND);
        setMinimumSize(new Dimension(1120, 720));
        setSize(1320, 820);
        setLocationRelativeTo(null);

        propertiesPanel = new PropertiesPanel(controller, this::showStatus, canvas::refresh);
        canvas.setSelectionListener(propertiesPanel::setSelection);
        canvas.setStatusListener(this::showStatus);
        canvas.setModelChangeListener(propertiesPanel::refreshSelection);

        setJMenuBar(createMenu());
        add(createHeader(), BorderLayout.NORTH);
        add(createWorkspace(), BorderLayout.CENTER);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(AppTheme.SURFACE);
        statusLabel.setForeground(AppTheme.MUTED);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.BORDER),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.GRAPHITE);
        header.setBorder(BorderFactory.createEmptyBorder(13, 20, 13, 20));
        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("AUTÓMATA FINITO");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        JLabel subtitle = new JLabel("Editor visual y simulador paso a paso de AFD");
        subtitle.setForeground(new Color(202, 205, 202));
        titles.add(title);
        titles.add(Box.createVerticalStrut(2));
        titles.add(subtitle);
        JLabel guide = new JLabel("ALFABETO  →  DIBUJAR  →  CONFIGURAR  →  VALIDAR");
        guide.setForeground(new Color(220, 206, 232));
        guide.setFont(guide.getFont().deriveFont(Font.BOLD, 12f));
        guide.setHorizontalAlignment(SwingConstants.RIGHT);
        header.add(titles, BorderLayout.WEST);
        header.add(guide, BorderLayout.EAST);
        return header;
    }

    private JSplitPane createWorkspace() {
        ToolPalette palette = new ToolPalette(canvas::setTool);
        JPanel editor = new JPanel(new BorderLayout());
        editor.setBackground(AppTheme.BACKGROUND);
        editor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        editor.add(palette, BorderLayout.WEST);
        JScrollPane canvasScroll = new JScrollPane(canvas);
        AppTheme.scrollPane(canvasScroll);
        canvasScroll.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER, 1, true));
        editor.add(canvasScroll, BorderLayout.CENTER);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AppTheme.BACKGROUND);
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 7, 10, 10));
        sidebar.add(new AlphabetPanel(controller, this::showStatus, canvas::refresh));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(propertiesPanel);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(new ValidationPanel(controller, canvas, this::showStatus));
        sidebar.add(Box.createVerticalGlue());

        JScrollPane sidebarScroll = new JScrollPane(sidebar);
        AppTheme.scrollPane(sidebarScroll);
        sidebarScroll.setBorder(null);
        sidebarScroll.setPreferredSize(new Dimension(350, 650));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editor, sidebarScroll);
        split.setBorder(null);
        split.setBackground(AppTheme.BACKGROUND);
        split.setResizeWeight(1.0);
        split.setDividerLocation(930);
        return split;
    }

    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu help = new JMenu("Ayuda");
        JMenuItem instructions = new JMenuItem("Cómo usar");
        instructions.addActionListener(event -> JOptionPane.showMessageDialog(this,
                "1. Escribe el alfabeto sin separadores (ejemplo: 01ab).\n"
                        + "2. Usa Estado y haz clic en el tablero.\n"
                        + "3. Selecciona un estado para marcarlo inicial o final.\n"
                        + "4. Usa Transición, elige origen, destino y símbolos.\n"
                        + "5. Ingresa una cadena y observa el recorrido.\n\n"
                        + "Consejo: selecciona y arrastra los estados para organizar el diagrama.",
                "Cómo usar Autómata Finito", JOptionPane.INFORMATION_MESSAGE));
        help.add(instructions);
        menuBar.add(help);
        return menuBar;
    }

    private void showStatus(String message) {
        statusLabel.setText(message);
    }
}
