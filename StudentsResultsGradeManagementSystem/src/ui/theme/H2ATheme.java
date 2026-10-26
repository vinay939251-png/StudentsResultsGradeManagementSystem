package ui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern Design System for Students Results and Grade Management System.
 * Inspired by the award-winning agency aesthetics of https://www.h2a.lu/ :
 * - Deep luxury dark obsidian base (#09070F / #0E0B17)
 * - Signature H2A dual-tone gradient (#640028 Wine -> #FF0055 Neon Magenta -> #7B2CBF Violet)
 * - Glassmorphic card surfaces with subtle luminous borders
 * - Pill-shaped interactive action buttons with hover animations and glows
 * - Clean modern typography and vibrant status badges
 */
public final class H2ATheme {

    private H2ATheme() {}

    // ──── Warm editorial background & surface colors ────
    public static final Color BG_DARK = new Color(0xF2, 0xEF, 0xE7);          // Warm paper canvas
    public static final Color BG_SIDEBAR = new Color(0x17, 0x17, 0x16);       // Ink-black navigation rail
    public static final Color BG_SURFACE = new Color(0xF2, 0xEF, 0xE7);       // Main paper surface
    public static final Color BG_CARD = new Color(0xFF, 0xFF, 0xFC);          // Clean paper card
    public static final Color BG_CARD_HOVER = new Color(0xFF, 0xF7, 0xF1);    // Warm hover surface
    public static final Color BG_INPUT = new Color(0xFA, 0xF8, 0xF2);         // Soft input surface

    // ──── H2A-inspired restrained accent palette ────
    public static final Color GRADIENT_WINE = new Color(0x17, 0x17, 0x16);    // Ink black
    public static final Color GRADIENT_MAGENTA = new Color(0xF0, 0x5A, 0x47); // Vermilion editorial accent
    public static final Color GRADIENT_PURPLE = new Color(0xF0, 0x5A, 0x47);  // Keep legacy APIs flat, not purple
    public static final Color ACCENT_CYAN = new Color(0xE5, 0xC8, 0x4A);      // Warm yellow highlight
    public static final Color ACCENT_VIOLET = new Color(0xD4, 0x9A, 0x3A);    // Ochre secondary accent

    // ──── Borders & Overlays ────
    public static final Color BORDER_SUBTLE = new Color(23, 23, 22, 35);      // Ink hairline
    public static final Color BORDER_INPUT = new Color(23, 23, 22, 70);      // Input idle border
    public static final Color BORDER_FOCUS = GRADIENT_MAGENTA;                 // Focus glow border
    public static final Color BORDER_CARD = new Color(23, 23, 22, 60);        // Ink card border

    // ──── Typography Colors ────
    public static final Color TEXT_PRIMARY = new Color(0x17, 0x17, 0x16);     // Ink black
    public static final Color TEXT_SECONDARY = new Color(0x5D, 0x5B, 0x55);   // Warm graphite
    public static final Color TEXT_MUTED = new Color(0x8C, 0x88, 0x7E);       // Quiet stone

    // ──── Status & Badge Colors ────
    public static final Color STATUS_PASS = new Color(0x10, 0xB9, 0x81);      // Emerald green
    public static final Color STATUS_PASS_BG = new Color(16, 185, 129, 45);   // Emerald tint
    public static final Color STATUS_FAIL = new Color(0xF4, 0x3F, 0x5E);      // Coral rose red
    public static final Color STATUS_FAIL_BG = new Color(244, 63, 94, 45);    // Rose tint
    public static final Color STATUS_WARN = new Color(0xF5, 0x9E, 0x0B);      // Warm amber
    public static final Color STATUS_WARN_BG = new Color(245, 158, 11, 45);   // Amber tint

    // ──── Modern Fonts ────
    public static final Font FONT_DISPLAY_LG = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_DISPLAY_MD = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_SM = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_METRIC = new Font("Segoe UI", Font.BOLD, 34);

    // ──── Component Factory Methods ────

    /**
     * Creates a header panel with h2a-style gradient title and subtitle.
     */
    public static JPanel createHeader(String title, String subtitle) {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(BG_DARK);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // A single editorial accent rule replaces the previous neon glow.
                g2.setPaint(GRADIENT_MAGENTA);
                g2.fillRect(0, getHeight() - 2, getWidth(), 2);
                g2.dispose();
            }
        };
        headerPanel.setBorder(new EmptyBorder(22, 30, 22, 30));

        // Title on the left
        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_DISPLAY_LG);
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(FONT_BODY);
        subLabel.setForeground(TEXT_SECONDARY);
        subLabel.setBorder(new EmptyBorder(4, 0, 0, 0));

        titleBox.add(titleLabel);
        titleBox.add(subLabel);
        headerPanel.add(titleBox, BorderLayout.WEST);

        // Right side badge
        JLabel badgeLabel = new JLabel("JAVA SWING • LOCAL FIRST");
        badgeLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badgeLabel.setForeground(TEXT_MUTED);
        badgeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        headerPanel.add(badgeLabel, BorderLayout.EAST);

        return headerPanel;
    }

    /**
     * Creates a luxury glassmorphic card panel with rounded corners and glowing border.
     */
    public static JPanel createCardPanel(int radius) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Fill background
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, radius, radius));

                // Crisp hairline border; no glass sheen or purple ambient glow.
                g2.setColor(BORDER_CARD);
                g2.setStroke(new BasicStroke(1.0f));
                g2.draw(new RoundRectangle2D.Float(1, 1, w - 3, h - 3, radius, radius));

                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        return card;
    }

    /**
     * Custom Primary Action Button with the signature H2A gradient (#FF0055 to #7B2CBF).
     */
    public static JButton createGradientButton(String text) {
        JButton btn = new JButton(text) {
            private boolean isHovered = false;
            private boolean isPressed = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        isPressed = true;
                        repaint();
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        isPressed = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int arc = h; // Full pill shape

                Color fill = isPressed ? GRADIENT_WINE : (isHovered ? new Color(0xD9, 0x48, 0x38) : GRADIENT_MAGENTA);
                g2.setColor(fill);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

                g2.setColor(new Color(23, 23, 22, 80));
                g2.setStroke(new BasicStroke(1.0f));
                g2.draw(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, arc, arc));

                // Draw text
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (w - fm.stringWidth(getText())) / 2;
                int textY = (h + fm.getAscent() - fm.getDescent()) / 2;

                g2.setColor(Color.WHITE);
                g2.drawString(getText(), textX, textY);

                g2.dispose();
            }
        };

        btn.setFont(FONT_BODY_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 30, 38));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Custom Outline/Ghost Button with sleek neon glowing border.
     */
    public static JButton createOutlineButton(String text) {
        JButton btn = new JButton(text) {
            private boolean isHovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int arc = h;

                // Neutral paper background with a consistent coral hover tint.
                g2.setColor(isHovered ? new Color(255, 232, 226) : BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

                // Border
                g2.setColor(isHovered ? GRADIENT_MAGENTA : TEXT_PRIMARY);
                g2.setStroke(new BasicStroke(1.0f));
                g2.draw(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, arc, arc));

                // Text
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (w - fm.stringWidth(getText())) / 2;
                int textY = (h + fm.getAscent() - fm.getDescent()) / 2;

                g2.setColor(TEXT_PRIMARY);
                g2.drawString(getText(), textX, textY);

                g2.dispose();
            }
        };

        btn.setFont(FONT_BODY_BOLD);
        btn.setForeground(TEXT_PRIMARY);
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 24, 38));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Custom Danger Button (Crimson/Rose) for Delete actions.
     */
    public static JButton createDangerButton(String text) {
        JButton btn = new JButton(text) {
            private boolean isHovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int arc = h;

                g2.setColor(isHovered ? new Color(0xB9, 0x2F, 0x2F) : new Color(0x8F, 0x25, 0x25));
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));

                g2.setColor(new Color(255, 255, 255, 150));
                g2.setStroke(new BasicStroke(1.0f));
                g2.draw(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, arc, arc));

                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (w - fm.stringWidth(getText())) / 2;
                int textY = (h + fm.getAscent() - fm.getDescent()) / 2;

                g2.setColor(Color.WHITE);
                g2.drawString(getText(), textX, textY);

                g2.dispose();
            }
        };

        btn.setFont(FONT_BODY_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 24, 38));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Modern Dark Rounded TextField with focus glow.
     */
    public static JTextField createTextField() {
        JTextField field = new JTextField() {
            private boolean isFocused = false;

            {
                addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        isFocused = true;
                        repaint();
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        isFocused = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Background
                g2.setColor(BG_INPUT);
                g2.fill(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, 10, 10));

                // Border
                if (isFocused) {
                    g2.setColor(BORDER_FOCUS);
                    g2.setStroke(new BasicStroke(1.5f));
                } else {
                    g2.setColor(BORDER_INPUT);
                    g2.setStroke(new BasicStroke(1.0f));
                }
                g2.draw(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, 10, 10));

                g2.dispose();
                super.paintComponent(g);
            }
        };

        field.setOpaque(false);
        field.setBackground(new Color(0, 0, 0, 0));
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT_CYAN);
        field.setFont(FONT_BODY);
        field.setBorder(new EmptyBorder(8, 12, 8, 12));
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 36));
        return field;
    }

    /**
     * Stylizes JTable into the sleek dark agency table with pill badges.
     */
    public static void stylizeTable(JTable table) {
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(FONT_BODY);
        table.setRowHeight(38);
        table.setGridColor(new Color(255, 255, 255, 12));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setSelectionBackground(new Color(240, 90, 71, 70));
        table.setSelectionForeground(TEXT_PRIMARY);

        // Header
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0xE8, 0xE3, 0xD8));
        header.setForeground(TEXT_PRIMARY);
        header.setFont(FONT_BODY_BOLD);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, GRADIENT_MAGENTA));

        // Alternating row renderer with custom status pill badges
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
                lbl.setBorder(new EmptyBorder(0, 14, 0, 14));

                String strVal = value != null ? value.toString() : "";

                if (isSelected) {
                    lbl.setBackground(new Color(240, 90, 71, 70));
                    lbl.setForeground(TEXT_PRIMARY);
                } else {
                    lbl.setBackground(row % 2 == 0 ? BG_CARD : new Color(0xF7, 0xF4, 0xEC));
                    lbl.setForeground(TEXT_PRIMARY);
                }

                // Status or Grade color highlighting
                if ("PASS".equalsIgnoreCase(strVal)) {
                    lbl.setForeground(STATUS_PASS);
                    lbl.setFont(FONT_BODY_BOLD);
                } else if ("FAIL".equalsIgnoreCase(strVal)) {
                    lbl.setForeground(STATUS_FAIL);
                    lbl.setFont(FONT_BODY_BOLD);
                } else if ("INCOMPLETE".equalsIgnoreCase(strVal)) {
                    lbl.setForeground(STATUS_WARN);
                    lbl.setFont(FONT_BODY_BOLD);
                } else if (strVal.matches("A\\+|A|B|C|D|E|F")) {
                    if ("A+".equals(strVal) || "A".equals(strVal)) {
                        lbl.setForeground(ACCENT_CYAN);
                    } else if ("B".equals(strVal) || "C".equals(strVal)) {
                        lbl.setForeground(GRADIENT_MAGENTA);
                    } else if ("F".equals(strVal)) {
                        lbl.setForeground(STATUS_FAIL);
                    } else {
                        lbl.setForeground(TEXT_SECONDARY);
                    }
                    lbl.setFont(FONT_BODY_BOLD);
                }

                return lbl;
            }
        });
    }

    /**
     * Stylizes JScrollPane with sleek dark scrollbars.
     */
    public static JScrollPane createScrollPane(Component view) {
        JScrollPane sp = new JScrollPane(view);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(BorderFactory.createLineBorder(BORDER_CARD, 1));

        // Dark modern scrollbar UI
        sp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(0xB5, 0xAF, 0xA3);
                this.trackColor = new Color(0xE5, 0xE0, 0xD6);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(0, 0));
                btn.setMinimumSize(new Dimension(0, 0));
                btn.setMaximumSize(new Dimension(0, 0));
                return btn;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !sp.getVerticalScrollBar().isEnabled()) {
                    return;
                }
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
                g2.dispose();
            }
        });

        sp.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        return sp;
    }

    /**
     * Form Label helper with modern styling.
     */
    public static JLabel createFormLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_BODY_BOLD);
        lbl.setForeground(TEXT_SECONDARY);
        lbl.setBorder(new EmptyBorder(6, 0, 4, 0));
        return lbl;
    }
}
