package com.betacom.books.be.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.books.be.dto.AddressDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Address;
import com.betacom.books.be.models.User;
import com.betacom.books.be.repositories.IAddressRepository;
import com.betacom.books.be.repositories.IUserRepository;
import com.betacom.books.be.requests.AddressReq;
import com.betacom.books.be.services.interfaces.IAddressService;
import com.betacom.books.be.utils.Utilities;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class AddressImpl extends Utilities implements IAddressService {

    private final IAddressRepository addressR;
    private final IUserRepository userR;

    public AddressImpl(IAddressRepository addressR, IUserRepository userR) {
        this.addressR = addressR;
        this.userR = userR;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer create(AddressReq req) throws BooksException {
        log.debug("create :" + req);

        // validate user
        Optional<User> userOpt = userR.findById(req.getUserId());
        if (userOpt.isEmpty()) {
            throw new BooksException("User not found for id " + req.getUserId());
        }

        Address addr = new Address();
        addr.setStreet(req.getStreet());
        addr.setCity(req.getCity());
        addr.setRegion(req.getRegion());
        addr.setCap(req.getCap());
        addr.setCountry(req.getCountry());
        addr.setUser(userOpt.get());

        return addressR.save(addr).getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(AddressReq req) throws BooksException {
        log.debug("delete :" + req);
        Optional<Address> addrOpt = addressR.findById(req.getId());

        if (addrOpt.isEmpty()) {
			throw new BooksException("Address not found with id " + req.getId());
		}

        Address addr = addrOpt.get();

        // prevent deletion if address has orders
        if (addr.getOrders() != null && !addr.getOrders().isEmpty()) {
            throw new BooksException("Address has associated orders and cannot be deleted");
        }

        addressR.delete(addr);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(AddressReq req) throws BooksException {
        log.debug("update :" + req);

        Optional<Address> addrOpt = addressR.findById(req.getId());
        if (addrOpt.isEmpty()) {
			throw new BooksException("Address not found with id " + req.getId());
		}

        Address addr = addrOpt.get();

        if (req.getStreet() != null) {
			addr.setStreet(req.getStreet());
		}
        if (req.getCity() != null) {
			addr.setCity(req.getCity());
		}
        if (req.getRegion() != null) {
			addr.setRegion(req.getRegion());
		}
        if (req.getCap() != null) {
			addr.setCap(req.getCap());
		}
        if (req.getCountry() != null) {
			addr.setCountry(req.getCountry());
		}

        if (req.getUserId() != null &&
            !req.getUserId().equals(addr.getUser().getId())) {
            Optional<User> userOpt = userR.findById(req.getUserId());
            if (userOpt.isEmpty()) {
				throw new BooksException("User not found for id " + req.getUserId());
			}
            addr.setUser(userOpt.get());
        }

        addressR.save(addr);
    }

    @Override
    public List<AddressDTO> getAll() {
        List<Address> lA = addressR.findAll();
        return buildListAddressDTO(lA);
    }
}
