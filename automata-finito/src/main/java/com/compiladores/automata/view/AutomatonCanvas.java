package com.compiladores.automata.view;

import com.compiladores.automata.controller.AutomatonController;
import com.compiladores.automata.model.DomainException;
import com.compiladores.automata.model.State;
import com.compiladores.automata.model.StateVisual;
import com.compiladores.automata.model.TransitionGroup;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public final class AutomatonCanvas extends JPanel {
    public static final int STATE_RADIUS = 32;
    private static final Color GRID_COLOR = new Color(218, 221, 217);
    private static final Color INK = AppTheme.GRAPHITE;
    private static final Color ACTIVE = AppTheme.GREEN;
    private static final Color SELECTED = AppTheme.VIOLET;

    private final AutomatonController controller;
    private EditorTool tool = EditorTool.SELECT;
    private String selectedStateId;
    private TransitionSelection selectedTransition;
    private String transitionSourceId;
    private String highlightedStateId;
    private TransitionSelection highlightedTransition;
    private Consumer<String> selectionListener = ignored -> { };
    private Consumer<String> statusListener = ignored -> { };
    private Runnable modelChangeListener = () -> { };
    private Point dragOffset;

    public AutomatonCanvas(AutomatonController controller) {
        this.controller = controller;
        setBackground(new Color(250, 250, 248));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setPreferredSize(new Dimension(820, 600));
        setFocusable(true);
        installMouseHandling();
        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
        getActionMap().put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent event) {
                deleteSelection();
            }
        });
    }

    public void setTool(EditorTool tool) {
        this.tool = tool;
        transitionSourceId = null;
        statusListener.accept("Herramienta activa: " + tool + ".");
        repaint();
    }

    public void setSelectionListener(Consumer<String> listener) {
        selectionListener = listener;
    }

    public void setStatusListener(Consumer<String> listener) {
        statusListener = listener;
    }

    public void setModelChangeListener(Runnable listener) {
        modelChangeListener = listener;
    }

    public void clearSimulationHighlight() {
        highlightedStateId = null;
        highlightedTransition = null;
        repaint();
    }

    public void highlightState(String stateId) {
        highlightedStateId = stateId;
        highlightedTransition = null;
        repaint();
    }

    public void highlightStep(String sourceId, String targetId, String stateId) {
        highlightedTransition = new TransitionSelection(sourceId, targetId);
        highlightedStateId = stateId;
        repaint();
    }

    public void refresh() {
        repaint();
        modelChangeListener.run();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(g);
        if (controller.automaton().states().isEmpty()) {
            drawEmptyState(g);
        }
        for (TransitionGroup group : controller.transitionGroups()) {
            drawTransition(g, group);
        }
        for (State state : controller.automaton().states()) {
            drawState(g, state);
        }
        g.dispose();
    }

    private void drawGrid(Graphics2D g) {
        g.setColor(GRID_COLOR);
        for (int x = 18; x < getWidth(); x += 24) {
            for (int y = 18; y < getHeight(); y += 24) {
                g.fillOval(x, y, 2, 2);
            }
        }
    }

    private void drawEmptyState(Graphics2D g) {
        String title = "Tu tablero está listo";
        String subtitle = "Define el alfabeto y usa la herramienta Estado para comenzar";
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        g.setColor(new Color(230, 231, 228));
        g.fillOval(centerX - 44, centerY - 76, 88, 88);
        g.setColor(AppTheme.VIOLET);
        g.setStroke(new BasicStroke(3f));
        g.drawOval(centerX - 31, centerY - 63, 62, 62);
        g.setFont(g.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        g.setColor(AppTheme.GRAPHITE);
        FontMetrics titleMetrics = g.getFontMetrics();
        g.drawString(title, centerX - titleMetrics.stringWidth(title) / 2, centerY + 38);
        g.setFont(g.getFont().deriveFont(java.awt.Font.PLAIN, 13f));
        g.setColor(AppTheme.MUTED);
        FontMetrics subtitleMetrics = g.getFontMetrics();
        g.drawString(subtitle, centerX - subtitleMetrics.stringWidth(subtitle) / 2, centerY + 62);
    }

    private void drawState(Graphics2D g, State state) {
        StateVisual visual = controller.visual(state.id());
        int x = (int) visual.x();
        int y = (int) visual.y();
        boolean selected = state.id().equals(selectedStateId);
        boolean active = state.id().equals(highlightedStateId);

        g.setColor(new Color(0, 0, 0, 24));
        g.fillOval(x - STATE_RADIUS + 4, y - STATE_RADIUS + 6,
                STATE_RADIUS * 2, STATE_RADIUS * 2);
        g.setColor(visual.color().awtColor());
        g.fillOval(x - STATE_RADIUS, y - STATE_RADIUS, STATE_RADIUS * 2, STATE_RADIUS * 2);
        g.setColor(active ? ACTIVE : selected ? SELECTED : INK);
        g.setStroke(new BasicStroke(active ? 5f : selected ? 4f : 2.2f));
        g.drawOval(x - STATE_RADIUS, y - STATE_RADIUS, STATE_RADIUS * 2, STATE_RADIUS * 2);
        if (state.accepting()) {
            g.drawOval(x - STATE_RADIUS + 6, y - STATE_RADIUS + 6,
                    (STATE_RADIUS - 6) * 2, (STATE_RADIUS - 6) * 2);
        }
        if (controller.automaton().initialStateId().filter(state.id()::equals).isPresent()) {
            drawArrow(g, x - STATE_RADIUS - 46, y, x - STATE_RADIUS - 3, y, active ? ACTIVE : INK);
        }
        if (state.id().equals(transitionSourceId)) {
            g.setColor(AppTheme.ORANGE);
            g.setStroke(new BasicStroke(3f));
            g.drawOval(x - STATE_RADIUS - 5, y - STATE_RADIUS - 5,
                    (STATE_RADIUS + 5) * 2, (STATE_RADIUS + 5) * 2);
        }

        Color fill = visual.color().awtColor();
        int luminance = (fill.getRed() * 299 + fill.getGreen() * 587 + fill.getBlue() * 114) / 1000;
        g.setColor(luminance < 135 ? Color.WHITE : INK);
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(state.name(), x - metrics.stringWidth(state.name()) / 2,
                y + metrics.getAscent() / 2 - 2);
    }

    private void drawTransition(Graphics2D g, TransitionGroup group) {
        boolean active = highlightedTransition != null && highlightedTransition.matches(group);
        boolean selected = selectedTransition != null && selectedTransition.matches(group);
        Color color = active ? ACTIVE : selected ? SELECTED : INK;
        g.setColor(color);
        g.setStroke(new BasicStroke(active ? 4f : selected ? 3.2f : 2f));

        StateVisual source = controller.visual(group.sourceId());
        StateVisual target = controller.visual(group.targetId());
        if (group.sourceId().equals(group.targetId())) {
            int x = (int) source.x();
            int y = (int) source.y();
            Arc2D loop = new Arc2D.Double(x - 30, y - 72, 60, 58, 15, 305, Arc2D.OPEN);
            g.draw(loop);
            drawArrowHead(g, x + 27, y - 42, Math.toRadians(65), color);
            drawLabel(g, group.label(), x, y - 79);
            return;
        }

        TransitionGeometry geometry = geometryFor(group);
        drawArrow(g, geometry.startX, geometry.startY, geometry.endX, geometry.endY, color);
        drawLabel(g, group.label(), geometry.labelX, geometry.labelY);
    }

    private TransitionGeometry geometryFor(TransitionGroup group) {
        StateVisual source = controller.visual(group.sourceId());
        StateVisual target = controller.visual(group.targetId());
        double dx = target.x() - source.x();
        double dy = target.y() - source.y();
        double length = Math.max(1, Math.hypot(dx, dy));
        double ux = dx / length;
        double uy = dy / length;
        boolean reciprocal = controller.transitionGroups().stream().anyMatch(other ->
                other.sourceId().equals(group.targetId()) && other.targetId().equals(group.sourceId()));
        double offset = reciprocal ? 11 : 0;
        double nx = -uy;
        double ny = ux;
        double startX = source.x() + ux * STATE_RADIUS + nx * offset;
        double startY = source.y() + uy * STATE_RADIUS + ny * offset;
        double endX = target.x() - ux * STATE_RADIUS + nx * offset;
        double endY = target.y() - uy * STATE_RADIUS + ny * offset;
        return new TransitionGeometry(startX, startY, endX, endY,
                (startX + endX) / 2 + nx * 12, (startY + endY) / 2 + ny * 12);
    }

    private void drawArrow(Graphics2D g, double x1, double y1, double x2, double y2, Color color) {
        g.setColor(color);
        g.draw(new Line2D.Double(x1, y1, x2, y2));
        drawArrowHead(g, x2, y2, Math.atan2(y2 - y1, x2 - x1), color);
    }

    private void drawArrowHead(Graphics2D g, double x, double y, double angle, Color color) {
        double size = 11;
        double left = angle + Math.PI * 0.82;
        double right = angle - Math.PI * 0.82;
        int[] xs = {(int) x, (int) (x + Math.cos(left) * size), (int) (x + Math.cos(right) * size)};
        int[] ys = {(int) y, (int) (y + Math.sin(left) * size), (int) (y + Math.sin(right) * size)};
        g.setColor(color);
        g.fillPolygon(xs, ys, 3);
    }

    private void drawLabel(Graphics2D g, String label, double centerX, double baselineY) {
        FontMetrics metrics = g.getFontMetrics();
        int width = metrics.stringWidth(label);
        int x = (int) centerX - width / 2;
        int y = (int) baselineY;
        g.setColor(AppTheme.SURFACE);
        g.fillRoundRect(x - 4, y - metrics.getAscent(), width + 8, metrics.getHeight(), 8, 8);
        g.setColor(INK);
        g.drawString(label, x, y);
    }

    private void installMouseHandling() {
        MouseAdapter handler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                requestFocusInWindow();
                String stateId = findStateAt(event.getPoint());
                switch (tool) {
                    case STATE -> createState(event.getPoint(), stateId);
                    case SELECT -> selectAt(event.getPoint(), stateId);
                    case TRANSITION -> transitionClick(stateId);
                    case DELETE -> deleteAt(event.getPoint(), stateId);
                }
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                if (tool == EditorTool.SELECT && selectedStateId != null && dragOffset != null) {
                    double x = Math.max(STATE_RADIUS + 50,
                            Math.min(getWidth() - STATE_RADIUS, event.getX() - dragOffset.x));
                    double y = Math.max(STATE_RADIUS,
                            Math.min(getHeight() - STATE_RADIUS, event.getY() - dragOffset.y));
                    controller.moveState(selectedStateId, x, y);
                    repaint();
                    modelChangeListener.run();
                }
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                dragOffset = null;
            }
        };
        addMouseListener(handler);
        addMouseMotionListener(handler);
    }

    private void createState(Point point, String stateAtPoint) {
        if (stateAtPoint != null) {
            statusListener.accept("Ese lugar ya está ocupado por un estado.");
            return;
        }
        State state = controller.createState(point.x, point.y);
        selectState(state.id(), point);
        statusListener.accept("Estado " + state.name() + " creado.");
        refresh();
    }

    private void selectAt(Point point, String stateId) {
        if (stateId != null) {
            selectState(stateId, point);
            return;
        }
        selectedStateId = null;
        selectedTransition = findTransitionAt(point);
        selectionListener.accept(null);
        statusListener.accept(selectedTransition == null ? "Selección limpiada." : "Transición seleccionada.");
        repaint();
    }

    private void selectState(String stateId, Point point) {
        selectedStateId = stateId;
        selectedTransition = null;
        StateVisual visual = controller.visual(stateId);
        dragOffset = new Point((int) (point.x - visual.x()), (int) (point.y - visual.y()));
        selectionListener.accept(stateId);
        repaint();
    }

    private void transitionClick(String stateId) {
        if (stateId == null) {
            statusListener.accept("Selecciona un estado de origen o destino.");
            return;
        }
        if (transitionSourceId == null) {
            transitionSourceId = stateId;
            statusListener.accept("Origen seleccionado. Ahora selecciona el estado de destino.");
            repaint();
            return;
        }
        String sourceId = transitionSourceId;
        transitionSourceId = null;
        if (controller.automaton().alphabet().isEmpty()) {
            showError("Define primero el alfabeto.");
            repaint();
            return;
        }
        var selection = TransitionSymbolDialog.choose(this, controller, sourceId, stateId);
        if (selection.isEmpty()) {
            repaint();
            return;
        }
        try {
            controller.addTransitions(sourceId, stateId, selection.orElseThrow());
            statusListener.accept("Transición creada.");
            refresh();
        } catch (DomainException error) {
            showError(error.getMessage());
        }
        repaint();
    }

    private void deleteAt(Point point, String stateId) {
        if (stateId != null) {
            controller.deleteState(stateId);
            if (stateId.equals(selectedStateId)) {
                selectedStateId = null;
                selectionListener.accept(null);
            }
            statusListener.accept("Estado y sus transiciones eliminados.");
            refresh();
            return;
        }
        TransitionSelection transition = findTransitionAt(point);
        if (transition != null) {
            controller.deleteTransitionGroup(transition.sourceId, transition.targetId);
            statusListener.accept("Transición eliminada.");
            refresh();
        }
    }

    private void deleteSelection() {
        if (selectedStateId != null) {
            controller.deleteState(selectedStateId);
            selectedStateId = null;
            selectionListener.accept(null);
            statusListener.accept("Estado y sus transiciones eliminados.");
            refresh();
        } else if (selectedTransition != null) {
            controller.deleteTransitionGroup(selectedTransition.sourceId, selectedTransition.targetId);
            selectedTransition = null;
            statusListener.accept("Transición eliminada.");
            refresh();
        }
    }

    private String findStateAt(Point point) {
        List<State> states = List.copyOf(controller.automaton().states());
        for (int index = states.size() - 1; index >= 0; index--) {
            State state = states.get(index);
            StateVisual visual = controller.visual(state.id());
            if (Point.distance(point.x, point.y, visual.x(), visual.y()) <= STATE_RADIUS) {
                return state.id();
            }
        }
        return null;
    }

    private TransitionSelection findTransitionAt(Point point) {
        for (TransitionGroup group : controller.transitionGroups()) {
            StateVisual source = controller.visual(group.sourceId());
            if (group.sourceId().equals(group.targetId())) {
                if (Point.distance(point.x, point.y, source.x(), source.y() - 68) < 25) {
                    return new TransitionSelection(group.sourceId(), group.targetId());
                }
            } else {
                TransitionGeometry geometry = geometryFor(group);
                if (Line2D.ptSegDist(geometry.startX, geometry.startY,
                        geometry.endX, geometry.endY, point.x, point.y) < 9) {
                    return new TransitionSelection(group.sourceId(), group.targetId());
                }
            }
        }
        return null;
    }

    private void showError(String message) {
        statusListener.accept(message);
        JOptionPane.showMessageDialog(this, message, "No se pudo completar", JOptionPane.WARNING_MESSAGE);
    }

    private record TransitionGeometry(
            double startX, double startY, double endX, double endY, double labelX, double labelY) {
    }

    private record TransitionSelection(String sourceId, String targetId) {
        boolean matches(TransitionGroup group) {
            return sourceId.equals(group.sourceId()) && targetId.equals(group.targetId());
        }
    }
}
