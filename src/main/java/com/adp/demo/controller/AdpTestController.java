/**
 * 
 */
package com.adp.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adp.demo.beans.ChangeBean;
import com.adp.demo.exception.NotEnoughCoinsException;
import com.adp.demo.service.AdpTestService;

/**
 * @author gurucharan
 *
 */
@RestController
public class AdpTestController {
	
	@Autowired
	private AdpTestService service;
	
	@GetMapping("/change/getChange/{billValue}")
	public ChangeBean getChange(@PathVariable("billValue") Integer billValue,
			@RequestParam(name = "moreChange", defaultValue = "false") 
	boolean moreChange) throws Exception {
		// validate input
		return service.getChangeForBill(billValue, moreChange);
	}

}
