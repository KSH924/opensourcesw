import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

class IndexDataManager {
    private static final String DATA_FILE = "index_data.ser";
    private HashMap<String, ArrayList<Page>> indexMap;

    public IndexDataManager() {
        loadData();
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(indexMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof HashMap) {
                indexMap = (HashMap<String, ArrayList<Page>>) obj;
            }
        } catch (Exception e) {
            indexMap = new HashMap<>();
        }
    }

    public HashMap<String, ArrayList<Page>> getIndexMap() {
        return indexMap;
    }

    public boolean addIndex(String name) {
        if (name == null || name.trim().isEmpty() || indexMap.containsKey(name)) {
            return false;
        }

        indexMap.put(name, new ArrayList<>());
        saveData();
        return true;
    }

    public boolean removeIndex(String name) {
        if (name == null || !indexMap.containsKey(name)) {
            return false;
        }

        indexMap.remove(name);
        saveData();
        return true;
    }

    public ArrayList<Page> getPages(String indexName) {
        return indexMap.get(indexName);
    }

    // 현재 페이지의 다음에 빈 페이지 추가 (기존에 있던 메서드 - 특정 위치에 추가)
    public void addPage(String indexName, int currentPageIndex) {
        ArrayList<Page> pages = indexMap.get(indexName);
        Page currentPage = pages.get(currentPageIndex);
        Page newPage = new Page(currentPage); // 폰트 크기만 복사하고 내용은 빈 페이지
        pages.add(currentPageIndex + 1, newPage);
        saveData();
    }

    // 수정된 메서드: 빈 페이지를 맨 뒤에 추가
    public void addPage(String indexName) {
        ArrayList<Page> pages = indexMap.get(indexName);
        Page newPage = new Page(); // 완전히 빈 페이지 생성 (기본 폰트 크기 사용)
        pages.add(newPage); // 맨 뒤에 추가
        saveData();
    }

    public boolean deletePage(String indexName, int pageIndex) {
        ArrayList<Page> pages = indexMap.get(indexName);
        if (pages.size() <= 1) {
            return false;
        }

        pages.remove(pageIndex);
        saveData();
        return true;
    }

    public void updatePageText(String indexName, int pageIndex, String text) {
        indexMap.get(indexName).get(pageIndex).text = text;
        saveData();
    }

    public void updatePageImage(String indexName, int pageIndex, String imagePath) {
        indexMap.get(indexName).get(pageIndex).imagePath = imagePath;
        saveData();
    }

    public void updatePageFontSize(String indexName, int pageIndex, int fontSize) {
        indexMap.get(indexName).get(pageIndex).fontSize = fontSize;
        saveData();
    }

    public void updatePageHighlights(String indexName, int pageIndex,
                                     java.util.List<TextHighlighter.HighlightInfo> highlights) {
        indexMap.get(indexName).get(pageIndex).highlights = new ArrayList<>(highlights);
        saveData();
    }
}