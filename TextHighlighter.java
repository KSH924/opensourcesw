import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TextHighlighter {
    private JTextArea textArea;
    private Color currentHighlightColor = Color.YELLOW;
    private List<HighlightInfo> highlights = new ArrayList<>();
    private boolean highlightMode = false;

    // 파스텔 색상들
    public static final Color PASTEL_RED = new Color(255, 182, 193);
    public static final Color PASTEL_YELLOW = new Color(255, 255, 186);
    public static final Color PASTEL_BLUE = new Color(173, 216, 230);

    public static class HighlightInfo {
        int start;
        int end;
        Color color;

        public HighlightInfo(int start, int end, Color color) {
            this.start = start;
            this.end = end;
            this.color = color;
        }
    }

    public TextHighlighter(JTextArea textArea) {
        this.textArea = textArea;
        setupHighlightListener();
    }

    private void setupHighlightListener() {
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (highlightMode && textArea.getSelectedText() != null) {
                    highlightSelectedText();
                }
            }
        });
    }

    public void setHighlightMode(boolean enabled) {
        this.highlightMode = enabled;
        textArea.setCursor(enabled ?
                Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR) :
                Cursor.getDefaultCursor());
    }

    public void setHighlightColor(Color color) {
        this.currentHighlightColor = color;
    }

    private void highlightSelectedText() {
        int start = textArea.getSelectionStart();
        int end = textArea.getSelectionEnd();

        if (start != end) {
            // 기존 하이라이트와 겹치는 부분 제거
            removeOverlappingHighlights(start, end);

            // 새 하이라이트 추가
            highlights.add(new HighlightInfo(start, end, currentHighlightColor));
            applyHighlights();

            // 선택 해제
            textArea.setSelectionStart(end);
            textArea.setSelectionEnd(end);
        }
    }

    private void removeOverlappingHighlights(int start, int end) {
        highlights.removeIf(highlight ->
                !(highlight.end <= start || highlight.start >= end));
    }

    public void applyHighlights() {
        Highlighter highlighter = textArea.getHighlighter();
        highlighter.removeAllHighlights();

        for (HighlightInfo highlight : highlights) {
            try {
                DefaultHighlighter.DefaultHighlightPainter painter =
                        new DefaultHighlighter.DefaultHighlightPainter(highlight.color);
                highlighter.addHighlight(highlight.start, highlight.end, painter);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearAllHighlights() {
        highlights.clear();
        textArea.getHighlighter().removeAllHighlights();
    }

    public List<HighlightInfo> getHighlights() {
        return new ArrayList<>(highlights);
    }

    public void setHighlights(List<HighlightInfo> highlights) {
        this.highlights = new ArrayList<>(highlights);
        applyHighlights();
    }
}