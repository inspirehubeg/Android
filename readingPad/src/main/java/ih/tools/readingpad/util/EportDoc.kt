package ih.tools.readingpad.util

import ih.tools.readingpad.feature_book_parsing.presentation.components.HighlightParagraph
import org.apache.poi.wp.usermodel.HeaderFooterType
import org.apache.poi.xwpf.usermodel.ParagraphAlignment
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.FileOutputStream

fun createWordDoc(text: String, filePath: String){
    val document = XWPFDocument()
    val paragraph = document.createParagraph()
    val run = paragraph.createRun()
    run.setText(text)
    val out = FileOutputStream(filePath)
    document.write(out)
    out.close()
    document.close()
}
fun createWordDoc(text:List<HighlightParagraph>, filePath: String, bookName: String) {
    val document = XWPFDocument()
    val header = document.createHeader(HeaderFooterType.DEFAULT)
    val headerContent = header.createParagraph()
    val headerRun = headerContent.createRun()
    headerRun.setText(bookName)
    headerContent.alignment = ParagraphAlignment.CENTER

    val highlightsTitle = document.createParagraph()
    val highlightsTitleRun = highlightsTitle.createRun()
    highlightsTitleRun.isBold = true
    highlightsTitleRun.fontSize= 32
    highlightsTitleRun.setText("Highlights")
    highlightsTitle.alignment = ParagraphAlignment.START

    for (item in text) {
        val heading = document.createParagraph()
        //heading.style = "Heading1"
        val headingRun = heading.createRun()
        headingRun.isItalic = true
        headingRun.fontSize= 20
        headingRun.setText("${item.chapterName}, ${item.pageNumber}")

        val body = document.createParagraph()
        //body.style = "BodyText"
        val bodyRun = body.createRun()
        bodyRun.fontSize = 15
        bodyRun.setText(item.highlightText)

        val lineBreak = document.createParagraph()
        //lineBreak.style = "Normal"
        val lineBreakRun = lineBreak.createRun()
        lineBreakRun.setText("--------------------")
        lineBreak.alignment = ParagraphAlignment.CENTER
    }

    val out = FileOutputStream(filePath)
    document.write(out)
    out.close()
    document.close()
}