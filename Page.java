import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Page implements Serializable {
    String text = "";
    String imagePath = null;
    int fontSize = 14;
    List<TextHighlighter.HighlightInfo> highlights = new ArrayList<>();

    public Page() {

    }

    public Page(Page other) {
        this.text = "";  // 빈 페이지로 생성
        this.imagePath = null;  // 이미지도 빈 상태
        this.fontSize = other.fontSize;  // 폰트 크기만 복사
        this.highlights = new ArrayList<>();  // 하이라이트는 빈 상태
    }
}