package br.com.locnf.domain.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.locnf.domain.entities.Nfe;
import br.com.locnf.domain.entities.NfeItem;
import br.com.locnf.domain.entities.Term;
import br.com.locnf.domain.exceptions.BusinessException;
import br.com.locnf.domain.repositories.NfeReaderGateway;
import br.com.locnf.domain.repositories.TermRepository;

public class TermService {

	private final TermRepository termRepository;
	private final NfeReaderGateway nfeReader;

	public TermService(TermRepository termRepository, NfeReaderGateway nfeReader) {
		this.termRepository = termRepository;
		this.nfeReader = nfeReader;
	}

	public Nfe readFile(Path path) {
		return nfeReader.readFile(path.toFile());
	}

	public void registerNewTerm(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new BusinessException("O nome do termo não pode ser vazio.");
		}

		if (termRepository.findByName(name) != null) {
			throw new BusinessException("Este termo já está cadastrado no sistema.");
		}

		Term newTerm = new Term(null, name);
		termRepository.save(newTerm);
	}

	public void updateTermName(Long id, String newName) {
		if (newName == null || newName.trim().isEmpty()) {
			throw new BusinessException("O novo nome do termo não pode estar vazio.");
		}

		Term existingTerm = termRepository.findById(id);
		if (existingTerm == null) {
			throw new BusinessException("Termo não encontrado para atualização.");
		}

		existingTerm.setName(newName);
		termRepository.save(existingTerm);
	}

	public void removeTerm(Long id) {
		termRepository.delete(id);
	}

	public Term getTermById(Long id) {
		return termRepository.findById(id);
	}

	public List<Term> getAllTerms() {
		return termRepository.findAll();
	}

	public void generateExcelNfe(List<Nfe> nfeList, File file) throws IOException {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Relatório NF-e");

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.WHITE.getIndex());

			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setFont(headerFont);
			headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);

			DataFormat format = workbook.createDataFormat();
			CellStyle estiloMoeda = workbook.createCellStyle();
			estiloMoeda.setDataFormat(format.getFormat("R$ #,##0.00"));

			String[] columns = new String[] { "NF", "Emissor", "Data de Emissão", "Total da Nota", "Total de Produtos",
					"Cliente CPF/CNPJ", "Cliente Nome", "Cliente Logradouro", "Cliente Casa", "Cliente Bairro",
					"Cliente Cidade", "Cliente Estado", "Cliente CEP", "Cód. Produto", "Descrição", "Qtd",
					"Valor Unitário", "Total Item" };

			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerStyle);
			}

			// --- PREENCHIMENTO DOS DADOS ---
			int rowIdx = 1;
			for (Nfe nf : nfeList) {
				for (NfeItem prod : nf.getProducts()) {
					Row row = sheet.createRow(rowIdx++);
					int column = 0;

					// Dados da Nota
					row.createCell(column++).setCellValue(nf.getNumero());
					row.createCell(column++).setCellValue(nf.getNomeEmitente());
					row.createCell(column++).setCellValue(nf.getDateFormat());
					
					Cell cellTotalNoto = row.createCell(column++);
					cellTotalNoto.setCellValue(nf.getTotalNota().doubleValue());
					cellTotalNoto.setCellStyle(estiloMoeda);
					
					Cell cellTotalProdutos = row.createCell(column++);
					cellTotalProdutos.setCellValue(nf.getTotalProdutos().doubleValue());
					cellTotalProdutos.setCellStyle(estiloMoeda);
					
					// Dados do Cliente
					row.createCell(column++).setCellValue(nf.getCpfCnpjDestinatario());
					row.createCell(column++).setCellValue(nf.getNomeDestinatario());		
					row.createCell(column++).setCellValue(nf.getEnderecoDestinatario().getLogradouro());
					row.createCell(column++).setCellValue(nf.getEnderecoDestinatario().getNumero());
					row.createCell(column++).setCellValue(nf.getEnderecoDestinatario().getBairro());
					row.createCell(column++).setCellValue(nf.getEnderecoDestinatario().getCidade());
					row.createCell(column++).setCellValue(nf.getEnderecoDestinatario().getEstado());
					row.createCell(column++).setCellValue(nf.getEnderecoDestinatario().getCep());
					
					// Dados do Produto
					row.createCell(column++).setCellValue(prod.getCode());
					row.createCell(column++).setCellValue(prod.getDescription());

					// Quantidade (Número)
					row.createCell(column++).setCellValue(prod.getQuantity().doubleValue());

					// Valor Unitário (Moeda)
					Cell cellValorUnit = row.createCell(column++);
					cellValorUnit.setCellValue(prod.getPrice().doubleValue());
					cellValorUnit.setCellStyle(estiloMoeda);

					// Total do Item (Fórmula do Excel: Qtd * Valor Unitário)
					// Exemplo: se estiver na linha 5, a fórmula será: G5 * H5
					Cell cellTotal = row.createCell(column++);
					// cellTotal.setCellFormula("G" + rowIdx + "*H" + rowIdx);
					cellTotal.setCellValue(prod.getTotal().doubleValue());					
					cellTotal.setCellStyle(estiloMoeda);
				}
			}

			// --- 4. AJUSTE DE COLUNAS ---
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			// --- 5. GRAVAÇÃO NO DISCO ---
			try (FileOutputStream fileOut = new FileOutputStream(file)) {
				workbook.write(fileOut);
			}
		}
	}

}
