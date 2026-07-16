package br.com.locnf.infra.ui.controllers;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import br.com.locnf.domain.entities.Nfe;
import br.com.locnf.domain.entities.NfeItem;
import br.com.locnf.domain.entities.Term;
import br.com.locnf.domain.exceptions.BusinessException;
import br.com.locnf.domain.repositories.AppSettingsRepository;
import br.com.locnf.domain.services.TermService;
import br.com.locnf.infra.ui.commands.MainScreenActionCommand;
import br.com.locnf.infra.ui.utils.MoneyFormat;
import br.com.locnf.infra.ui.views.MainScreenFrame;
import br.com.locnf.infra.ui.views.models.NfeItemTableModel;
import br.com.locnf.infra.ui.views.models.NfeTableModel;
import br.com.locnf.infra.ui.views.models.TermListModel;

public class MainScreenController {

	private final MainScreenFrame view;
	private final TermService termService;
	private final AppSettingsRepository settings;

	private List<Nfe> nfeList;
	private List<Term> termList;

	public MainScreenController(MainScreenFrame view, TermService termService, AppSettingsRepository settings) {
		this.view = view;
		this.termService = termService;
		this.settings = settings;

		loadSettings();
		loadTermList();

		if (!settings.getNfeUri().isEmpty()) {
			updateNfe();
		}

		this.view.addActionsListeners(this::actionListener);
	}

	private void loadSettings() {
		view.setSelectedTermFilter(settings.isSelectedTermFilter());
		view.setPath(settings.getNfeUri());
	}

	private void loadTermList() {
		termList = termService.getAllTerms();
		this.view.updateTermList(new TermListModel(termList));
		this.view.setEnabledBtnRemoveTerm(false);
	}

	private void actionListener(ActionEvent actionEvent) {
		MainScreenActionCommand command = MainScreenActionCommand.valueOf(actionEvent.getActionCommand());

		switch (command) {
		case ADD_TERM:
			addTerm();
			break;
		case REMOVE_TERM:
			removeTerm();
			break;
		case TOGGLE_TERM_FILTER:
			toggleTermFilter();
			break;
		case BROWSE:
			browser();
			break;
		case UPDATE:
			updateNfe();
			break;
		case SELECT_TABLE_NF:
			selectNfe();
			break;
		case SELECT_TABLE_ITEM:
			selectNfeItems();
			break;
		case SELECT_LIST_TERM:
			selectListTerm();
			break;
		case EXPORT_EXCEL:
			exportExcel();
			break;
		default:
			System.out.println("Comando não reconhecido: " + command.name());
		}

	}

	private void exportExcel() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Salvar Relatório de NF-es");
		chooser.setSelectedFile(new File("relatorio_nfes.xlsx"));

		if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();

			if (!file.getAbsolutePath().toLowerCase().endsWith(".xlsx")) {
				file = new File(file.getAbsolutePath() + ".xlsx");
			}

			File finalFile = file;
			view.setEnabledBtnExport(false);

			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {
					termService.generateExcelNfe(nfeList, finalFile);
					return null;
				}

				@Override
				protected void done() {
					view.setEnabledBtnExport(true);
					try {
						get();
						showMessage(JOptionPane.INFORMATION_MESSAGE, "Sucesso", "NF-es exportadas com sucesso!");
					} catch (Exception e) {
						showMessage(JOptionPane.ERROR_MESSAGE, "Erro", "Erro ao exportar NF-es: " + e.getMessage());
					}
				}

			};

			worker.execute();
		}

	}

	private void selectListTerm() {
		view.setEnabledBtnRemoveTerm(true);
	}

	private void toggleTermFilter() {
		settings.setSelectedTermFilter(view.isSelectedTermFilter());
	}

	private void addTerm() {
		try {
			termService.registerNewTerm(view.getTermText());

			loadTermList();
			showMessage(JOptionPane.INFORMATION_MESSAGE, "Sucesso", "Termo cadastrado com sucesso!");
		} catch (BusinessException e) {
			showMessage(JOptionPane.WARNING_MESSAGE, "Aviso", e.getMessage());
		} catch (Exception e) {
			showMessage(JOptionPane.ERROR_MESSAGE, "Erro Crítico", "Ocorreu um erro inesperado no banco de dados.");
		}
	}

	private void removeTerm() {
		try {

			int selectedIndex = view.getListTermSelectedIndex();

			if (selectedIndex < 0) {
				showMessage(JOptionPane.WARNING_MESSAGE, "Aviso", "Selecione um termo para remover.");
				return;
			}

			Term term = termList.get(selectedIndex);
			int confirm = showConfirm(JOptionPane.YES_NO_OPTION, "Remover Termo",
					"Deseja remover o termo?\nTermo: " + term.getName());

			if (confirm == JOptionPane.YES_OPTION) {
				termService.removeTerm(term.getId());
				loadTermList();
			}

		} catch (BusinessException e) {
			showMessage(JOptionPane.WARNING_MESSAGE, "Aviso", e.getMessage());
		} catch (Exception e) {
			showMessage(JOptionPane.ERROR_MESSAGE, "Erro Crítico", "Ocorreu um erro inesperado no banco de dados.");
		}
	}

	private void browser() {
		JFileChooser chooser = new JFileChooser(new File(view.getPath()));
		chooser.setDialogTitle("Localizar Diretório");
		chooser.setApproveButtonText("Selecionar pasta");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
			view.setPath(chooser.getSelectedFile().getPath());
			updateNfe();
		}
	}

	private void updateNfe() {
		String path = view.getPath();
		File file = new File(path);

		if (!file.isDirectory()) {
			handleDirectoryNotFound(path);
			return;
		}

		settings.setNfeUri(path);

		try {
			// Criamos uma lista para acumular os caminhos válidos ignorando erros de
			// permissão
			List<Path> caminhosValidos = new ArrayList<>();

			Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					caminhosValidos.add(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) {
					// Se der AccessDeniedException, apenas ignora e continua
					return FileVisitResult.CONTINUE;
				}
			});

			nfeList = loadNfeFromXmlFiles(caminhosValidos.stream());

			if (!termList.isEmpty() && view.isSelectedTermFilter()) {
				nfeList = filterNfeByTerms(nfeList);
			}

			updateUiComponents();

		} catch (Exception e) {
			showMessage(JOptionPane.ERROR_MESSAGE, "Erro Crítico", "Ocorreu um erro inesperado ao atualizar.");
		}
	}

	private List<Nfe> loadNfeFromXmlFiles(Stream<Path> streamPath) {
		return streamPath.filter(Files::isRegularFile).filter(p -> p.toString().toLowerCase().endsWith(".xml"))
				.map(termService::readFile).collect(Collectors.toList());
	}

	private List<Nfe> filterNfeByTerms(List<Nfe> sourceList) {
		List<String> termNameList = termList.stream().map(Term::getName).map(String::toUpperCase)
				.collect(Collectors.toList());

		return sourceList.stream().filter(nfe -> nfe.getProducts().stream().anyMatch(item -> {
			HashSet<String> wordSet = new HashSet<>(Arrays.asList(item.getDescription().toUpperCase().split("\\s+")));
			return termNameList.stream().anyMatch(wordSet::contains);
		})).collect(Collectors.toList());
	}

	private void updateUiComponents() {
		view.updateNfeTable(new NfeTableModel(nfeList));
		view.setNfeSize(String.valueOf(nfeList.size()));
		view.setEnabledBtnExport(!nfeList.isEmpty());
		view.updateNfeItemTable(new NfeItemTableModel());
		view.setItemsSelectedTotal("");
	}

	private void handleDirectoryNotFound(String path) {
		new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				String message = "O diretório especificado não foi encontrado: "
						+ (path.isEmpty() ? "''\n\n" : "\n'" + path + "'\n\n") + "Deseja selecionar a pasta manualmente agora?";

				int confirm = showConfirm(JOptionPane.YES_NO_OPTION, "Diretório Não Encontrado", message);
				
				if (confirm == JOptionPane.YES_OPTION) {
					SwingUtilities.invokeAndWait(MainScreenController.this::browser);			
				}
				
				return null;
			}
			
		}.execute();
	}

	private void selectNfe() {
		int index = view.getTableNfeSelectedRow();

		if (index != -1) {

			Nfe nfe = nfeList.get(index);
			view.updateNfeItemTable(new NfeItemTableModel(nfe.getProducts()));

		}
	}

	private void selectNfeItems() {
		int index = view.getTableNfeSelectedRow();

		if (index != -1) {

			BigDecimal totalSum = BigDecimal.ZERO;
			List<NfeItem> itemList = nfeList.get(index).getProducts();
			int[] indexes = view.getTableNfeItemsSelectedRows();

			for (int i : indexes) {
				NfeItem item = itemList.get(i);
				totalSum = totalSum.add(item.getTotal());
			}

			view.setItemsSelectedTotal(MoneyFormat.format(totalSum));
		}
	}

	private void showMessage(int messageType, String title, String message) {
		JOptionPane.showMessageDialog(view, message, title, messageType);
	}

	private int showConfirm(int optionType, String title, String message) {
		return JOptionPane.showConfirmDialog(view, message, title, optionType);
	}

}
