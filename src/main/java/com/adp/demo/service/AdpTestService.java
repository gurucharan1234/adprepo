package com.adp.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adp.demo.beans.ChangeBean;
import com.adp.demo.exception.NotEnoughCoinsException;

@Service
public class AdpTestService {

	@Value("${adp.demo.quarters}")
	private Integer quarters;
	
	@Value("${adp.demo.dimes}")
	private Integer dimes;
	
	@Value("${adp.demo.nickels}")
	private Integer nickels;
	
	@Value("${adp.demo.pennys}")
	private Integer pennys;
	
	private enum Coins {
		QUARTERS(25),
		DIMES(10),
		NICKELS(5),
		PENNYS(1);
		
		private int value;
		private Coins(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
	}
	
	
	public ChangeBean getChangeForBill(Integer billValue, boolean moreChange) throws NotEnoughCoinsException {
		
//		System.out.println("moreChange "+ moreChange);
		return getChange(billValue, moreChange);
	}
	
	private synchronized ChangeBean 
			getChange(Integer billValue, boolean moreChange) throws NotEnoughCoinsException {
		billValue = billValue * 100;
		ChangeBean bean = new ChangeBean();
		if (moreChange) {
			billValue = getCoins(billValue, bean, Coins.PENNYS);
			billValue = getCoins(billValue, bean, Coins.NICKELS);
			billValue = getCoins(billValue, bean, Coins.DIMES);
			billValue = getCoins(billValue, bean, Coins.QUARTERS);
		} else {
			billValue = getCoins(billValue, bean, Coins.QUARTERS);
			billValue = getCoins(billValue, bean, Coins.DIMES);
			billValue = getCoins(billValue, bean, Coins.NICKELS);
			billValue = getCoins(billValue, bean, Coins.PENNYS);
		}
		
		if (billValue != 0) {
			throw new NotEnoughCoinsException("Not Enough Coins to make change");
		}
		return bean;
	}
	
	private Integer getCoins(Integer billValue, ChangeBean bean, Coins coins) {
		int tmp;
		int denomination = coins.getValue();
		int numberOfCoinsLeft = getNumberOfCoins(coins);
		int numberOfCoins = 0;
		if (billValue % denomination == 0 ) {
			tmp = billValue/denomination;
			if (tmp < numberOfCoinsLeft) {
				numberOfCoinsLeft = numberOfCoinsLeft - tmp;
				numberOfCoins = tmp;
				billValue = (billValue - (denomination * tmp));
			} 
			// if we have more billValue than coins left
			else if (tmp > numberOfCoinsLeft && numberOfCoinsLeft != 0){
				billValue = (billValue - (denomination * numberOfCoinsLeft));
				numberOfCoins = numberOfCoinsLeft;
				numberOfCoinsLeft = 0;
			}
		}
		setNumberOfCoinsInResponseBean(numberOfCoins, bean, coins);
		updateNumberOfCoins(numberOfCoinsLeft, coins);
		return billValue;
	}

	@SuppressWarnings("static-access")
	private void setNumberOfCoinsInResponseBean(int numberOfCoins, ChangeBean bean, Coins coins) {
		if (coins == Coins.QUARTERS) {
			bean.setQuarters(numberOfCoins);
		} else if (coins == Coins.DIMES) {
			bean.setDimes(numberOfCoins);
		} else if (coins == coins.NICKELS) {
			bean.setNickels(numberOfCoins);
		} else if (coins == coins.PENNYS) {
			bean.setPennys(numberOfCoins);
		}
	}

	@SuppressWarnings("static-access")
	private void updateNumberOfCoins(int numberOfCoinsLeft, Coins coins) {
		if (coins == Coins.QUARTERS) {
			quarters = numberOfCoinsLeft;
		} else if (coins == Coins.DIMES) {
			dimes = numberOfCoinsLeft;
		} else if (coins == coins.NICKELS) {
			nickels = numberOfCoinsLeft;
		} else if (coins == coins.PENNYS) {
			pennys = numberOfCoinsLeft;
		}
	}

	@SuppressWarnings("static-access")
	private int getNumberOfCoins(Coins coins) {
		if (coins == Coins.QUARTERS) {
			return quarters;
		} else if (coins == Coins.DIMES) {
			return dimes;
		} else if (coins == coins.NICKELS) {
			return nickels;
		} else if (coins == coins.PENNYS) {
			return pennys;
		}
		return 0;
	}
	
	
	

}
