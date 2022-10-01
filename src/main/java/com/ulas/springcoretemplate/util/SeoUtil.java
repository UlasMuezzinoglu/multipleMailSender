package com.ulas.springcoretemplate.util;


import com.ulas.springcoretemplate.interfaces.repository.user.UrlEntityRepository;
import com.ulas.springcoretemplate.model.entity.UrlEntity;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.ulas.springcoretemplate.constant.SeoConstants.PROFILE_PRIOITY;
import static com.ulas.springcoretemplate.constant.SeoConstants.PROFILE_URL;

@Component
public class SeoUtil {
    @Autowired
    private UrlEntityRepository urlEntityRepository;


    private static String formatXML(String xml) throws TransformerException {

        // write data to xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // pretty print by indention
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        // add standalone="yes", add line break before the root element
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

        StreamSource source = new StreamSource(new StringReader(xml));
        StringWriter output = new StringWriter();
        transformer.transform(source, new StreamResult(output));

        return output.toString();
    }

    public static void createAndSaveSeoUrlForUser(UrlEntityRepository urlEntityRepository, String url) {
        urlEntityRepository.saveAll(List.of(new UrlEntity(PROFILE_URL + url, PROFILE_PRIOITY)));
    }

    public static void deleteSeoUrl(UrlEntityRepository urlEntityRepository, String url) {
        urlEntityRepository.deleteAllByLocContains(url);
    }

    private void writeHelper(XMLStreamWriter writer, UrlEntity seoUrlsEntity) throws XMLStreamException {
        writer.writeStartElement("url");

        writer.writeStartElement("loc");
        writer.writeCharacters(seoUrlsEntity.getLoc());
        writer.writeEndElement();

        writer.writeStartElement("lastmod");
        writer.writeCharacters(seoUrlsEntity.getLastModifiedDate());
        writer.writeEndElement();

        writer.writeStartElement("changefreq");
        writer.writeCharacters(seoUrlsEntity.getChangefreq());
        writer.writeEndElement();

        writer.writeStartElement("priority");
        writer.writeCharacters(String.valueOf(seoUrlsEntity.getPriority()));
        writer.writeEndElement();


        writer.writeEndElement();
    }

    @SneakyThrows
    public String generateXml(List<UrlEntity> seo) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeXml(out, seo);
        String xml = out.toString(StandardCharsets.UTF_8);
        return formatXML(xml);
    }

    // StAX Cursor API
    private void writeXml(ByteArrayOutputStream out, List<UrlEntity> seo) throws XMLStreamException {

        XMLOutputFactory output = XMLOutputFactory.newInstance();

        XMLStreamWriter writer = output.createXMLStreamWriter(out);

        // TODO init writer
        writer.writeStartDocument("utf-8", "1.0");
        writer.writeStartElement("urlset");
        writer.writeAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");

        for (UrlEntity seoUrlsEntity : seo) {
            writeHelper(writer, seoUrlsEntity);
        }
        writer.writeEndElement();
        writer.writeEndDocument();
    }
}
