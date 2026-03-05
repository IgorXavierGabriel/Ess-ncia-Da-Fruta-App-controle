package com.essenciadafruta.util;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.essenciadafruta.model.Custo;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class RelatorioCustosPDF {
    
    private static String pastaRelatorios() {
        String pasta = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Relatorios_Essencia";
        File f = new File(pasta);
        if (!f.exists()) f.mkdirs();
        return pasta;
    }
    
    public static String gerar(List<Custo> custos, String periodo) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate hoje = LocalDate.now();
            
            String nomeArquivo = "Custos_" + periodo + "_" + System.currentTimeMillis() + ".pdf";
            String caminhoCompleto = pastaRelatorios() + File.separator + nomeArquivo;
            
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, new FileOutputStream(caminhoCompleto));
            document.open();
            
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("RELATÓRIO DE CUSTOS - " + periodo.toUpperCase(), fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10);
            document.add(titulo);
            
            Font fontData = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
            Paragraph data = new Paragraph("Gerado em: " + hoje.format(formatter), fontData);
            data.setAlignment(Element.ALIGN_CENTER);
            data.setSpacingAfter(20);
            document.add(data);
            
            document.add(new Paragraph("_________________________________________________________________"));
            document.add(new Paragraph(" "));
            
            Font fontContent = new Font(Font.FontFamily.HELVETICA, 10);
            double totalCustos = 0;
            int contador = 1;
            
            for (Custo c : custos) {
                Paragraph item = new Paragraph(
                    contador + ". " + c.getTipo() + " - " + c.getDescricao() + " - " +
                    "R$ " + String.format("%.2f", c.getValor()) + " - " +
                    (c.getData() != null ? c.getData().toString() : "N/A"),
                    fontContent
                );
                item.setSpacingAfter(6);
                document.add(item);
                totalCustos += c.getValor();
                contador++;
            }
            
            document.add(new Paragraph(" "));
            document.add(new Paragraph("_________________________________________________________________"));
            
            Font fontTotal = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Paragraph totalParagrafo = new Paragraph("Total de Custos: R$ " + String.format("%.2f", totalCustos), fontTotal);
            totalParagrafo.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalParagrafo);
            
            document.close();
            return caminhoCompleto;
        } catch (Exception e) {
            System.err.println("Erro ao gerar relatório de custos: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
