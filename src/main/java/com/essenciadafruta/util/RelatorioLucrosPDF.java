package com.essenciadafruta.util;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class RelatorioLucrosPDF {
    
    private static String pastaRelatorios() {
        String pasta = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Relatorios_Essencia";
        File f = new File(pasta);
        if (!f.exists()) f.mkdirs();
        return pasta;
    }
    
    public static String gerar(double totalVendas, double totalCustos, String periodo) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate hoje = LocalDate.now();
            
            String nomeArquivo = "Lucro_" + periodo + "_" + System.currentTimeMillis() + ".pdf";
            String caminhoCompleto = pastaRelatorios() + File.separator + nomeArquivo;
            
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, new FileOutputStream(caminhoCompleto));
            document.open();
            
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
            Paragraph titulo = new Paragraph("RELATÓRIO DE LUCRO - " + periodo.toUpperCase(), fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10);
            document.add(titulo);
            
            Font fontData = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
            Paragraph data = new Paragraph("Gerado em: " + hoje.format(formatter), fontData);
            data.setAlignment(Element.ALIGN_CENTER);
            data.setSpacingAfter(30);
            document.add(data);
            
            document.add(new Paragraph("_________________________________________________________________"));
            document.add(new Paragraph(" "));
            
            Font fontLabel = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fontVendas = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, new BaseColor(76, 175, 80));
            Font fontCustos = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, new BaseColor(244, 67, 54));
            Font fontMargem = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, new BaseColor(255, 152, 0));
            
            // Vendas
            Paragraph vendas = new Paragraph("Total de Vendas (Receitas):", fontLabel);
            document.add(vendas);
            Paragraph vendasValor = new Paragraph("R$ " + String.format("%.2f", totalVendas), fontVendas);
            vendasValor.setIndentationLeft(20);
            document.add(vendasValor);
            document.add(new Paragraph(" "));
            
            // Custos
            Paragraph custos = new Paragraph("Total de Custos (Despesas):", fontLabel);
            document.add(custos);
            Paragraph custosValor = new Paragraph("R$ " + String.format("%.2f", totalCustos), fontCustos);
            custosValor.setIndentationLeft(20);
            document.add(custosValor);
            document.add(new Paragraph(" "));
            
            // Lucro
            double lucroLiquido = totalVendas - totalCustos;
            Paragraph lucro = new Paragraph("Lucro Líquido:", fontLabel);
            document.add(lucro);
            BaseColor lucroColor = lucroLiquido >= 0 ? new BaseColor(33, 150, 243) : new BaseColor(255, 67, 54);
            Font fontLucro = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, lucroColor);
            Paragraph lucroValor = new Paragraph("R$ " + String.format("%.2f", lucroLiquido), fontLucro);
            lucroValor.setIndentationLeft(20);
            document.add(lucroValor);
            document.add(new Paragraph(" "));
            
            // Margem de Lucro
            double margemLucro = totalVendas > 0 ? (lucroLiquido / totalVendas) * 100 : 0;
            Paragraph margem = new Paragraph("Margem de Lucro:", fontLabel);
            document.add(margem);
            Paragraph margemValor = new Paragraph(String.format("%.2f %%", margemLucro), fontMargem);
            margemValor.setIndentationLeft(20);
            document.add(margemValor);
            
            document.add(new Paragraph(" "));
            document.add(new Paragraph("_________________________________________________________________"));
            
            Font fontRodape = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC);
            Paragraph rodape = new Paragraph("Essência da Fruta - Relatório Financeiro", fontRodape);
            rodape.setAlignment(Element.ALIGN_CENTER);
            document.add(new Paragraph(" "));
            document.add(rodape);
            
            document.close();
            return caminhoCompleto;
        } catch (Exception e) {
            System.err.println("Erro ao gerar relatório de lucro: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
