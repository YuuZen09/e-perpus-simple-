package com.example.testapi;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;

public class DeletePdfWorker extends Worker {

    public DeletePdfWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        String bookId = getInputData().getString("book_id");
        if (bookId == null) {
            return Result.failure();
        }

        String fileName = DownloadedBooksManager.getInstance().getFileNameForBook(bookId);

        if (fileName != null) {
            File file = new File(getApplicationContext().getFilesDir(), "buku/" + fileName);
            if (file.exists() && file.delete()) {
                DownloadedBooksManager.getInstance().removeDownloadedBook(bookId);
                return Result.success();
            } else {
                return Result.failure();
            }
        } else {
            return Result.failure();
        }
    }
}
