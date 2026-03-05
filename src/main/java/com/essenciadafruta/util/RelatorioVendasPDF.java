package com.essenciadafruta.util;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.essenciadafruta.model.Saida;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class RelatorioVendasPDF {
    
    private static String pastaRelatorios() {
        String pasta = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Relatorios_Essencia";
        File f = new File(pasta);
        if (!f.exists()) f.mkdirs();
        return pasta;
    }
    
    public static String gerar(List<Saida> vendas, String periodo) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate hoje = LocalDate.now();
            
            String nomeArquivo = "Vendas_" + periodo + "_" + System.currentTimeMillis() + ".pdf";
            String caminhoCompleto = pastaRelatorios() + File.separator + nomeArquivo;
            
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, new FileOutputStream(caminhoCompleto));
            document.open();
            
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("RELATÓRIO DE VENDAS - " + periodo.toUpperCase(), fontTitulo);
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
            double totalVendas = 0;
            int contador = 1;
            
            for (Saida v : vendas) {
                String dataStr = v.getDataSaida() != null ? v.getDataSaida().format(formatter) : "N/A";
                Paragraph item = new Paragraph(
                    contador + ". " + dataStr + " - Cliente: " + v.getCliente() + 
                    " - " + v.getSabor() + 
                    " - Qtd: " + v.getQuantidade() + " - " +
                    "R$ " + String.format("%.2f", v.getPreco()) +
                    (v.getMetodoPagamento() != null ? " - " + v.getMetodoPagamento() : ""),
                    fontContent
                );
                item.setSpacingAfter(6);
                document.add(item);
                totalVendas += v.getPreco() * v.getQuantidade();
                contador++;
            }
            
            document.add(new Paragraph(" "));
            
            Font fontTotal = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Paragraph totalParagrafo = new Paragraph("Total de Vendas: R$ " + String.format("%.2f", totalVendas), fontTotal);
            totalParagrafo.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalParagrafo);
            
            document.close();
            return caminhoCompleto;
        } catch (Exception e) {
            System.err.println("Erro ao gerar relatório de vendas: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
