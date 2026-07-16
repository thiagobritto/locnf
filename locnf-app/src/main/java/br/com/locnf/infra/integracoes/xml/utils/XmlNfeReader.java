package br.com.locnf.infra.integracoes.xml.utils;

import java.io.File;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.locnf.infra.integracoes.xml.models.NfeItemXmlModel;
import br.com.locnf.infra.integracoes.xml.models.NfeXmlModel;

public class XmlNfeReader {

	private final String STRING_DEFAULT = "N/A";

	public NfeXmlModel parse(File file) {

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			factory.setIgnoringElementContentWhitespace(true);

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);
			doc.getDocumentElement().normalize();
			
			NfeXmlModel nfeXmlModel = new NfeXmlModel();

			NodeList infNFeList = doc.getElementsByTagName("infNFe");
			if (infNFeList != null && infNFeList.getLength() > 0) {
				Element infNFe = (Element) infNFeList.item(0);
				nfeXmlModel.setChave(infNFe.getAttribute("Id").replaceAll("\\D", ""));
			}

			NodeList ideList = doc.getElementsByTagName("ide");
			if (ideList != null && ideList.getLength() > 0) {
				Element ide = (Element) ideList.item(0);
				nfeXmlModel.setNumero(getText(ide, "nNF"));
				nfeXmlModel.setSerie(getText(ide, "serie"));

				String data = getText(ide, "dhEmi");
				nfeXmlModel.setData(data.equals(STRING_DEFAULT) ? null : OffsetDateTime.parse(data));
			}

			NodeList emitList = doc.getElementsByTagName("emit");
			if (emitList != null && emitList.getLength() > 0) {
				Element emit = (Element) emitList.item(0);
				nfeXmlModel.setNomeEmitente(getText(emit, "xNome"));
				nfeXmlModel.setCnpjEmitente(getText(emit, "CNPJ"));
			}
			
			NodeList destList = doc.getElementsByTagName("dest");
			if (destList != null && destList.getLength() > 0) {
				Element dest = (Element) destList.item(0);
				nfeXmlModel.setNomeDestinatario(getText(dest, "xNome"));
				
				String cpf = getText(dest, "CPF");
				String cnpj = getText(dest, "CNPJ");
				nfeXmlModel.setCpfCnpjDestinatario(cnpj.equals(STRING_DEFAULT) ? cpf : cnpj);				
			}
			
			NodeList enderDestList = doc.getElementsByTagName("enderDest");
			if (enderDestList != null && enderDestList.getLength() > 0) {
				Element enderDest = (Element) enderDestList.item(0);
				nfeXmlModel.setLogradouroDestinatario(getText(enderDest, "xLgr"));
				nfeXmlModel.setNumeroCasaDestinatario(getText(enderDest, "nro"));
				nfeXmlModel.setBairroDestinatario(getText(enderDest, "xBairro"));
				nfeXmlModel.setCidadeDestinatario(getText(enderDest, "xMun"));
				nfeXmlModel.setEstadoDestinatario(getText(enderDest, "UF"));
				nfeXmlModel.setCepDestinatario(getText(enderDest, "CEP"));
			}

			NodeList totalList = doc.getElementsByTagName("ICMSTot");
			if (totalList != null && totalList.getLength() > 0) {
				Element total = (Element) totalList.item(0);
				nfeXmlModel.setTotalNota(toBigDecimal(getText(total, "vNF")));
				nfeXmlModel.setTotalProdutos(toBigDecimal(getText(total, "vProd")));
			}

			NodeList prodList = doc.getElementsByTagName("prod");
			if (prodList != null && prodList.getLength() > 0) {
				for (int i = 0; i < prodList.getLength(); i++) {
					Element prod = (Element) prodList.item(i);
					NfeItemXmlModel itemNfeXmlModel = new NfeItemXmlModel();

					itemNfeXmlModel.setCode(getText(prod, "cProd"));
					itemNfeXmlModel.setDescription(getText(prod, "xProd"));
					itemNfeXmlModel.setQuantity(toBigDecimal(getText(prod, "qCom")));
					itemNfeXmlModel.setPrice(toBigDecimal(getText(prod, "vUnCom")));
					itemNfeXmlModel.setTotal(toBigDecimal(getText(prod, "vProd")));

					nfeXmlModel.addItem(itemNfeXmlModel);
				}
			}

			return nfeXmlModel;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private BigDecimal toBigDecimal(String text) {
		return text.equals(STRING_DEFAULT) ? BigDecimal.ZERO : new BigDecimal(text.replace(",", "."));
	}

	private String getText(Element element, String tagName) {

		if (element != null) {
			NodeList nodeList = element.getElementsByTagName(tagName);
			if (nodeList != null && nodeList.getLength() > 0) {
				if (nodeList.item(0) != null) {
					return nodeList.item(0).getTextContent();
				}
			}
		}

		return STRING_DEFAULT;
	}

}
