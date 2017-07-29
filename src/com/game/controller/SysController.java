package com.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.game.service.SysService;
import com.game.utils.common.BaseAction;

@Controller
@RequestMapping("/interface/sys")
public class SysController extends BaseAction {
	@Autowired
	SysService sysService;
	 
	 
}
