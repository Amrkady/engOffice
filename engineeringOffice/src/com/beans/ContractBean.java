package com.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.entities.Bills;
import com.entities.Contracts;
import com.models.CustomerModel;
import com.services.SandService;

import common.util.Utils;

@ManagedBean(name = "contractBean")
@ViewScoped
public class ContractBean {
	@ManagedProperty(value = "#{sandServiceImpl}")
	private SandService sandServiceImpl;
	private Contracts contract;
	private Date contractDate;
	private CustomerModel cm = new CustomerModel();

	@PostConstruct
	public void init() {
		contract = new Contracts();
		HttpServletRequest httprequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession httpSession = httprequest.getSession(false);
		cm = (CustomerModel) httpSession.getAttribute("CustomerObject");
		if (cm != null) {
			contract.setCustomerId(cm.getCustomerId());
			contract.setDeptId(cm.getDeptId());
			contract.setNat_no(cm.getNatNo());

		}
	}

	public boolean addContract() {
		try {
			String strDate = "";
			SimpleDateFormat sdfDate = new SimpleDateFormat("dd/mm/yyyy");
			if (contractDate != null) {
				strDate = sdfDate.format(contractDate);
			}
			contract.setContractDate(strDate);
			return sandServiceImpl.addContract(contract);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public String save() {

		if (addContract()) {
			String reportName = "/reports/contractReport.jasper";
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("day", contract.getDayLetter());
			parameters.put("date", contract.getContractDate());
			parameters.put("recordNo", cm.getNatNo().toString());
			parameters.put("custName", cm.getCustomerName());
			parameters.put("address", cm.getAddress());
			parameters.put("phone", cm.getPhone());
			parameters.put("onwerNo", contract.getOwnerNo());
			parameters.put("buildDetails", contract.getLicenseType());
			parameters.put("from", contract.getOutFrom());
			parameters.put("outDate", contract.getOutHijridate());
			parameters.put("costByLet", contract.getAmountByLetter());
			parameters.put("cost", contract.getAmount().toString());

			String footerPath = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/reports/footer.png");
			parameters.put("footer", footerPath);
			String headerPath = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/reports/header.png");
			parameters.put("header", headerPath);
			Utils.printPdfReport(reportName, parameters);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "��� ����� ����� ��� ��� ����", "");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}
		return "";
	}

	public SandService getSandServiceImpl() {
		return sandServiceImpl;
	}

	public void setSandServiceImpl(SandService sandServiceImpl) {
		this.sandServiceImpl = sandServiceImpl;
	}

	public Date getContractDate() {
		return contractDate;
	}

	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}

	public Contracts getContract() {
		return contract;
	}

	public void setContract(Contracts contract) {
		this.contract = contract;
	}

	public CustomerModel getCm() {
		return cm;
	}

	public void setCm(CustomerModel cm) {
		this.cm = cm;
	}

}