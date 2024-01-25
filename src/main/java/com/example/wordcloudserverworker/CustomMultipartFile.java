package com.example.wordcloudserverworker;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

// Custom implementation of MultipartFile to accept a ByteArrayResource
public class CustomMultipartFile implements MultipartFile {

    private final ByteArrayResource resource;

    public CustomMultipartFile(ByteArrayResource resource) {
        this.resource = resource;
    }

    @Override
    public String getName() {
        return resource.getFilename();
    }

    @Override
    public String getOriginalFilename() {
        return resource.getFilename();
    }

    @Override
    public String getContentType() {
        return null; // Set the content type if known
    }

    @Override
    public boolean isEmpty() {
        return resource.contentLength() == 0;
    }

    @Override
    public long getSize() {
        return resource.contentLength();
    }

    @Override
    public byte[] getBytes() {
        return resource.getByteArray();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return resource.getInputStream();
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
        // You can implement this method if needed
    }
}
