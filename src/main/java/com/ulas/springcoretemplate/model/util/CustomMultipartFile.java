package com.ulas.springcoretemplate.model.util;


import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

public class CustomMultipartFile implements MultipartFile {
    private final byte[] imgContent;
    private String name;

    public CustomMultipartFile(byte[] imgContent) {
        this.imgContent = imgContent;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * this method gets random uuid for filename
     *
     * @return random text + .png as String
     */
    @Override
    public String getOriginalFilename() {
        return UUID.randomUUID() + ".png";
    }

    /**
     * this is gets content type
     *
     * @return image/png
     */
    @Override
    public String getContentType() {
        return "image/png";
    }

    /**
     * this method provide controls is the imgContent null or blank
     *
     * @return result of imgContent null or empty as boolean
     */
    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    /**
     * this method provide gets image size
     *
     * @return length of imgContent
     */
    @Override
    public long getSize() {
        return imgContent.length;
    }

    /**
     * this method provides get byte of imgContent
     *
     * @return imgContent as byte array
     * @throws IOException
     */
    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    /**
     * this method gets input stream by using imgContent
     *
     * @return Input Stream
     * @throws IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    /**
     * this method transfer file to FileOutputStream object
     *
     * @param dest Used for transfer to imgContent
     * @throws IOException
     * @throws IllegalStateException
     */
    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }
    public void setName(String name){
        this.name = name;
    }
}
