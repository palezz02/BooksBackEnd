package com.betacom.books.be.controller;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.books.be.dto.AddressDTO;
import com.betacom.books.be.requests.AddressReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
import com.betacom.books.be.services.interfaces.IAddressService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/rest/address")
public class AddressController {

    private IAddressService addressS;

    public AddressController(IAddressService addressS) {
        this.addressS = addressS;
    }

    @GetMapping("/listAll")
    public ResponseList<AddressDTO> listAll() {
        ResponseList<AddressDTO> r = new ResponseList<>();
        try {
            r.setDati(addressS.getAll());
            r.setRc(true);
        } catch (Exception e) {
            r.setRc(false);
            r.setMsg(e.getMessage());
        }
        return r;
    }

    @PostMapping("/create")
    public ResponseBase create(@RequestBody(required = true) AddressReq req) {
        ResponseBase r = new ResponseBase();
        try {
            addressS.create(req);
            r.setRc(true);
        } catch (Exception e) {
            r.setRc(false);
            r.setMsg(e.getMessage());
        }
        return r;
    }

    @DeleteMapping("/delete")
    public ResponseBase delete(@RequestBody(required = true) AddressReq req) {
        ResponseBase r = new ResponseBase();
        try {
            addressS.delete(req);
            r.setRc(true);
        } catch (Exception e) {
            r.setRc(false);
            r.setMsg(e.getMessage());
        }
        return r;
    }

    @PutMapping("/update")
    public ResponseBase update(@RequestBody(required = true) AddressReq req) {
        ResponseBase r = new ResponseBase();
        try {
            addressS.update(req);
            r.setRc(true);
        } catch (Exception e) {
            r.setRc(false);
            r.setMsg(e.getMessage());
        }
        return r;
    }
}
