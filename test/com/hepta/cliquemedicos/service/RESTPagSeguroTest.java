package com.hepta.cliquemedicos.service;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.xml.XmlMapper;
import com.hepta.cliquemedicos.dto.CompraDTO;
import com.hepta.cliquemedicos.dto.pagseguro.AddressDTO;
import com.hepta.cliquemedicos.dto.pagseguro.BillingAddressDTO;
import com.hepta.cliquemedicos.dto.pagseguro.CreditCardDTO;
import com.hepta.cliquemedicos.dto.pagseguro.DocumentDTO;
import com.hepta.cliquemedicos.dto.pagseguro.HolderDTO;
import com.hepta.cliquemedicos.dto.pagseguro.InstallmentDTO;
import com.hepta.cliquemedicos.dto.pagseguro.ItemDTO;
import com.hepta.cliquemedicos.dto.pagseguro.PaymentDTO;
import com.hepta.cliquemedicos.dto.pagseguro.PhoneDTO;
import com.hepta.cliquemedicos.dto.pagseguro.SenderDTO;
import com.hepta.cliquemedicos.dto.pagseguro.SessionDTO;
import com.hepta.cliquemedicos.dto.pagseguro.ShippingDTO;
import com.hepta.cliquemedicos.dto.pagseguro.TransactionDTO;
import com.hepta.cliquemedicos.dto.pagseguro.TransactionSearchResultDTO;
import com.hepta.cliquemedicos.entity.Compra;
import com.hepta.cliquemedicos.job.PagSeguroJob;
import com.hepta.cliquemedicos.persistence.CompraDAO;
import com.hepta.cliquemedicos.util.AmbienteUtil;

class RESTPagSeguroTest {
	CompraDTO compra = new CompraDTO();
	Client client = ClientBuilder.newClient();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

	}

	@Test
	final void testSession() {
		Response responseAMHP = client.target("https://ws.sandbox.pagseguro.uol.com.br/v2/").path("sessions")
				.queryParam("email", "davi.diniz@hepta.com.br")
				.queryParam("token", "896CA31D8EA148B795285F3A36263015")
				.request().post(null);
		//System.out.println(responseAMHP.readEntity(String.class));
		SessionDTO teste = responseAMHP.readEntity(SessionDTO.class);
		System.out.println(teste.id);
		assert (teste.id.length() != 0);
	}

	@Test
	final void testCartaoDeCredito() {
		PaymentDTO payment = new PaymentDTO();

		BillingAddressDTO billing = new BillingAddressDTO();
		billing.setCity("Brasilia");
		billing.setNumber("254");
		billing.setDistrict("Asa Norte");
		billing.setState("DF");
		billing.setComplement("bloco b");
		billing.setPostalCode("70790123");
		billing.setCountry("Brasil");
		billing.setStreet("rua");

		DocumentDTO document = new DocumentDTO();
		document.setType("CPF");
		document.setValue("22111944785");
		List<DocumentDTO> documents = new ArrayList<>();
		documents.add(document);

		PhoneDTO phone = new PhoneDTO();
		phone.setAreaCode("61");
		phone.setNumber("984526975");

		HolderDTO holder = new HolderDTO();
		holder.setName("Ana Maria");
		holder.setBirthDate("17/05/1990");
		holder.setDocument((List<DocumentDTO>) documents);
		holder.setPhone(phone);

		InstallmentDTO installment = new InstallmentDTO();
		installment.setQuantity("1");
		installment.setNoInterestInstallmentQuantity("2");
		installment.setValue("10.00");

		CreditCardDTO card = new CreditCardDTO();
		card.setToken("7187336a82c24bff8794dc5a04a79f20");
		card.setBillingAddress(billing);
		card.setHolder(holder);
		card.setInstallment(installment);

		SenderDTO sender = new SenderDTO();
		sender.setHash("f854fc5a8901ab1e6cc23af5dd0fe1135914af84e2c3875e4805eedadbccae02");
		sender.setPhone(phone);
		sender.setDocument(documents);
		sender.setEmail("c34714638577360291262@sandbox.pagseguro.com.br");
		sender.setName(holder.getName());

		ItemDTO item = new ItemDTO();
		item.setId("1");
		item.setDescription("descricao");
		item.setAmount("10.00");
		item.setQuantity("1");
		List<ItemDTO> items = new ArrayList<>();
		items.add(item);

		AddressDTO address = new AddressDTO();
		address.setCity("a");
		address.setComplement("a");
		address.setCountry("a");
		address.setDistrict("a");
		address.setNumber("1");
		address.setPostalCode("71503505");
		address.setState("DF");
		address.setStreet("a");

		ShippingDTO shipping = new ShippingDTO();
		shipping.setAddress(address);
		shipping.setCost("0.00");
		shipping.setType("3");

		payment.setCurrency("BRL");
		payment.setMethod("creditCard");
		payment.setMode("default");
		payment.setItem(items);
		payment.setSender(sender);
		payment.setCreditCard(card);
		payment.setShippingAddressRequired("false");
		payment.setShipping(shipping);

		try {
			XmlMapper xmlMapper = new XmlMapper();
			String xml = xmlMapper.writeValueAsString(payment);

			Response responseAMHP = client.target("https://ws.sandbox.pagseguro.uol.com.br/v2/").path("transactions")
					.queryParam("email", "davi.diniz@hepta.com.br")
					.queryParam("token", "896CA31D8EA148B795285F3A36263015").request().post(Entity.xml(xml));
			System.out.println(Entity.xml(xml).toString());
			System.out.println("");
			System.out.println(responseAMHP.readEntity(String.class));
			TransactionDTO transaction = responseAMHP.readEntity(TransactionDTO.class);
			System.out.println(transaction.getCode());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	final void testChecarStatusCompras() {
		PagSeguroJob job = new PagSeguroJob();
		try {
			job.rodaJob();
			assert (true);
		}catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
	}

}