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

    public void addPage(String indexName) {
        indexMap.get(indexName).add(new Page());
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
}
