package uk.co.threebugs;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DocumentRepository {

    private final Map<String, Document> documentMap;

    public DocumentRepository() {
        documentMap = new HashMap<>();
    }

    public Document get(String id) {
        return documentMap.get(id);
    }

    public void store(Document document) {
        documentMap.put(document.getId(), document);
    }

    public void clear() {
        documentMap.clear();
    }
}
