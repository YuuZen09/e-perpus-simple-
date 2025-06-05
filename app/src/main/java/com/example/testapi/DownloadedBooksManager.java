package com.example.testapi;

import java.util.HashMap;

public class DownloadedBooksManager {
    private static DownloadedBooksManager instance;
    private HashMap<String, String> downloadedFileNames;

    private DownloadedBooksManager() {
        downloadedFileNames = new HashMap<>();
    }

    public static synchronized DownloadedBooksManager getInstance() {
        if (instance == null) {
            instance = new DownloadedBooksManager();
        }
        return instance;
    }

    public void addDownloadedBook(String bookId, String fileName) {
        downloadedFileNames.put(bookId, fileName);
    }

    public String getFileNameForBook(String bookId) {
        return downloadedFileNames.get(bookId);
    }

    public void removeDownloadedBook(String bookId) {
        downloadedFileNames.remove(bookId);
    }
}
