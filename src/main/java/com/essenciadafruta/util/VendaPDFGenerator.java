package com.essenciadafruta.util;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class VendaPDFGenerator {
    
    public static String gerarPDF(String cliente, String sabor, String unidade, int quantidade, 
                                   double valor, String whatsapp, String endereco, String pagamento, LocalDate data) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataFormatada = data.format(formatter);
            
            // Criar pasta de PDFs se não existir
            String pastaDesktop = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Vendas_PDF";
            File pasta = new File(pastaDesktop);
            if (!pasta.exists()) {
                pasta.mkdirs();
            }
            
            // Nome do arquivo com timestamp
            String nomeArquivo = cliente.replace(" ", "_") + "_" + System.currentTimeMillis() + ".pdf";
            String caminhoCompleto = pastaDesktop + File.separator + nomeArquivo;
            
            // Criar documento PDF
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, new FileOutputStream(caminhoCompleto));
            document.open();
            
            // Título
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
            Paragraph titulo = new Paragraph("COMPROVANTE DE VENDA", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);
            
            // Linha separadora
            document.add(new Paragraph("_________________________________________________________________"));
            document.add(new Paragraph(" "));
            
            // Informações da venda
            Font fontLabel = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
            Font fontValor = new Font(Font.FontFamily.HELVETICA, 11);
            
            document.add(new Paragraph("DADOS DO CLIENTE", fontLabel));
            document.add(new Paragraph("Cliente: " + cliente, fontValor));
            document.add(new Paragraph("WhatsApp: " + whatsapp, fontValor));
            document.add(new Paragraph("Endereço: " + endereco, fontValor));
            document.add(new Paragraph(" "));
            
            document.add(new Paragraph("DADOS DO PRODUTO", fontLabel));
            document.add(new Paragraph("Sabor: " + sabor, fontValor));
            document.add(new Paragraph("Unidade: " + unidade, fontValor));
            document.add(new Paragraph("Quantidade: " + quantidade + " un", fontValor));
            document.add(new Paragraph(" "));
            
            document.add(new Paragraph("INFORMAÇÕES DE PAGAMENTO", fontLabel));
            document.add(new Paragraph("Valor Unitário: R$ " + String.format("%.2f", valor / quantidade), fontValor));
            document.add(new Paragraph("Valor Total: R$ " + String.format("%.2f", valor), fontValor));
            document.add(new Paragraph("Forma de Pagamento: " + pagamento, fontValor));
            document.add(new Paragraph(" "));
            
            document.add(new Paragraph("DATA DA VENDA", fontLabel));
            document.add(new Paragraph("Data: " + dataFormatada, fontValor));
            document.add(new Paragraph(" "));
            
            // Linha separadora final
            document.add(new Paragraph("_________________________________________________________________"));
            document.add(new Paragraph(" "));
            
            Font fontRodape = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC);
            Paragraph rodape = new Paragraph("Obrigado pelas compras! - Essência da Fruta", fontRodape);
            rodape.setAlignment(Element.ALIGN_CENTER);
            document.add(rodape);
            
            document.close();
            
            return caminhoCompleto;
            
        } catch (Exception e) {
            System.err.println("Erro ao gerar PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
