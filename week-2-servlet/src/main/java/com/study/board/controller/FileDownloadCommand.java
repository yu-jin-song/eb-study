package com.study.board.controller;

import com.study.board.util.HttpHeaders;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Objects;

public class FileDownloadCommand implements Command{
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 한글깨짐 처리
        String fileName = URLDecoder.decode(request.getParameter("file_name"));
        String filePath = URLDecoder.decode(request.getParameter("file_path"));
        String encodingFileName = new String(fileName.getBytes("8859_1"), HttpHeaders.CHARSET_UTF_8);

        String contentType = request.getServletContext().getMimeType(fileName);
        if (Objects.isNull(contentType)) {
            contentType = "application/octet-stream";
        }

        response.setContentType(contentType);
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"" + encodingFileName
                        + "\""
        );

        File downloadFile = new File(filePath + File.separator + fileName);

        try (FileInputStream fileInputStream = new FileInputStream(downloadFile);
        OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int read;
            while((read = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
        }
    }
}
