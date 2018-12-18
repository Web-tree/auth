package org.webtree.auth.controller;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(value = "#{AuthPropertiesBean.frontendOrigin}")
public abstract class AbstractController {}