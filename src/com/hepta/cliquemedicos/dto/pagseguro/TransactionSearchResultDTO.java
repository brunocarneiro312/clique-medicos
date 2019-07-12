package com.hepta.cliquemedicos.dto.pagseguro;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "transactionSearchResult")
public class TransactionSearchResultDTO {
	private String date;
	private String currentPage;
	private String resultsInThisPage;
	private String totalPages;
	private List<TransactionDTO> transactions;

	public TransactionSearchResultDTO() {
		super();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getResultsInThisPage() {
		return resultsInThisPage;
	}

	public void setResultsInThisPage(String resultInThisPage) {
		this.resultsInThisPage = resultInThisPage;
	}

	public String getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(String totalPages) {
		this.totalPages = totalPages;
	}

	public List<TransactionDTO> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionDTO> transactions) {
		this.transactions = transactions;
	}

}
