package com.ulas.springcoretemplate.util;


import com.ulas.springcoretemplate.constant.FileExtensionConstants;
import com.ulas.springcoretemplate.constant.GeneralConstants;
import com.ulas.springcoretemplate.model.util.CustomMultipartFile;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;

import static com.ulas.springcoretemplate.util.SHA256Utils.toSHA512;
import static javax.imageio.ImageWriteParam.MODE_EXPLICIT;

public final class FileUtil {

    public static boolean isImageFileFormatApplicable(final MultipartFile file) {
        if (file == null) return false;
        String extension = FileUtil.getFileExtension(file);
        return FileExtensionConstants.PERMITTED_IMAGE_EXTENSIONS.contains(extension);
    }

    public static boolean isImageFileFormatApplicable(final String extension) {
        return FileExtensionConstants.PERMITTED_IMAGE_EXTENSIONS.contains(extension);
    }

    public static boolean isFileFormatApplicable(final MultipartFile file) {
        if (file == null) return false;
        String extension = FileUtil.getFileExtension(file);
        return FileExtensionConstants.PERMITTED_IMAGE_EXTENSIONS.contains(extension);
    }

    public static boolean isFileFormatApplicable(final String extension) {
        if (MethodUtils.isBlank(extension)) return false;
        return FileExtensionConstants.PERMITTED_IMAGE_EXTENSIONS.contains(extension);
    }

    public static boolean isGeneralFileFormatApplicable(final MultipartFile file) {
        if (file == null) return false;
        String extension = FileUtil.getFileExtension(file);
        if (!FileExtensionConstants.PERMITTED_IMAGE_EXTENSIONS.contains(extension))
            return isImageFileFormatApplicable(extension);
        return true;
    }

    public static String getFileExtension(final MultipartFile file) {
        if (file == null) return null;
        String detectedType = null;
        try {
            detectedType = new Tika().detect(file.getBytes());
            return detectedType.substring(detectedType.lastIndexOf("/") + 1).toLowerCase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isSupportedImageSize(final MultipartFile file) {
        return getFileSizeInMB(file.getSize()) < GeneralConstants.IMAGE_SIZE_LIMIT;
    }

    public static boolean isImageSizeExtreme(final MultipartFile file) {
        return getFileSizeInMB(file.getSize()) > GeneralConstants.EXTREME_IMAGE_SIZE_LIMIT_MB;
    }

    public static boolean isFileSizeExtreme(final MultipartFile file) {
        return getFileSizeInMB(file.getSize()) > GeneralConstants.EXTREME_FILE_SIZE_LIMIT_MB;
    }

    public static boolean isSupportedFileSize(final MultipartFile file) {
        return getFileSizeInMB(file.getSize()) < GeneralConstants.FILE_SIZE_LIMIT;
    }

    public static double getFileSizeInMB(final double bytes) {
        return bytes * 0.00000095367432;
    }

    public static double getFileSizeInKB(final double bytes) {
        return bytes * 0.0009765625;
    }

    public static void displayText(final InputStream input) throws IOException {
        // Read one text line at a time and display.
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            System.out.println("    " + line);
        }
    }

    public static CustomMultipartFile compressFile(MultipartFile multipartFile, final float compressionQuality) {
        String imageName = multipartFile.getOriginalFilename();
        assert imageName != null;
        String imageExtension = imageName.substring(imageName.lastIndexOf(".") + 1);
        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName(imageExtension).next();
        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
        imageWriteParam.setCompressionMode(MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(compressionQuality);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(baos);
        imageWriter.setOutput(imageOutputStream);
        BufferedImage originalImage = null;
        try (InputStream inputStream = multipartFile.getInputStream()) {
            originalImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            String info = String.format("compressImage - bufferedImage (file %s)- IOException - message: %s ", imageName, e.getMessage());
            System.out.println(info);
            return new CustomMultipartFile(baos.toByteArray());
        }
        IIOImage image = new IIOImage(originalImage, null, null);
        try {
            imageWriter.write(null, image, imageWriteParam);
        } catch (IOException e) {
            String info = String.format("compressImage - imageWriter (file %s)- IOException - message: %s ", imageName, e.getMessage());
            System.out.println(info);
        } finally {
            imageWriter.dispose();
        }
        return new CustomMultipartFile(baos.toByteArray());
    }

    public static void removeFileFromStorage(final String path) {
        try {
            File input = new File(path);
            if (input.delete()) {
                System.out.println(input.getName() + " is deleted!");
            } else {
                System.out.println("Delete operation is failed.");
            }
        } catch (Exception e) {
            System.out.println("ERROR : " + e.getStackTrace());
        }
    }

    public static File convertMultiPartToFile(MultipartFile file) {
        try {
            File convFile = new File(file.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            return convFile;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return null;
    }

    public static MultipartFile prepareFileToUpload(MultipartFile file) {
        if (!FileUtil.isSupportedFileSize(file)) {
            return FileUtil.compressFile(file, 0.6F);
        }
        return file;
    }

    public static String generateHashedFilename(String filename, String username) {
        String extension = filename.substring(filename.lastIndexOf("."));
        return toSHA512(username + MethodUtils.generateUuid() + extension);
    }
}
