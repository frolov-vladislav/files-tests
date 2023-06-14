package com.vladislavfrolov;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.LocalFileHeader;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FilesTests {

    @Test
    public void txtFormatFileTest() throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("text-file.txt")) {
            assert stream != null;
            String result = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            assertThat(result).contains("Проверка на содержимое");
        }
    }

    @Test
    public void pdfFormatFileTest() throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("pdf.pdf")) {
            assert stream != null;
            PDF pdf = new PDF(stream);
            assertThat(pdf.text).contains("Пример PDF файла");
        }
    }

    @Test
    public void xlsFormatFileTest() throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("xls-file.xls")) {
            assert stream != null;
            XLS xls = new XLS(stream);
            assertThat(xls.excel.getSheetAt(0).getRow(1).getCell(4).getStringCellValue()).isEqualTo("United States");
        }
    }

    @Test
    public void zipTest() throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("archive.zip")) {
            assert stream != null;
            ZipInputStream zipInputStream = new ZipInputStream(stream);
            zipInputStream.setPassword(new char[]{'1', '2', '3'});
            LocalFileHeader zipEntry = zipInputStream.getNextEntry();
            assertThat(zipEntry.getFileName()).isEqualTo("xls-file.xls");
        }
    }

    @Test
    public void docTest() throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("file.docx")) {
            assert stream != null;
            XWPFDocument doc = new XWPFDocument(stream);
            List<XWPFParagraph> paragr = doc.getParagraphs();

            for (XWPFParagraph xwpfParagraph : paragr) {
                assertThat(xwpfParagraph.getText()).isEqualTo("Тест");
            }
        }
    }
}