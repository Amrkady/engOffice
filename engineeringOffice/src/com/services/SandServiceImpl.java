package com.services;

import java.util.List;

import com.common.CommonDao;
import com.entities.Bills;
import com.entities.Contracts;
import com.entities.Users;

public class SandServiceImpl implements SandService {
	private CommonDao commonDao;

	public CommonDao getCommonDao() {
		return commonDao;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	@Override
	public boolean addSand(Bills bill) {
		return commonDao.saveObject(bill);

	}

	@Override
	public boolean addContract(Contracts contract) {
		return commonDao.saveObject(contract);
	}

}