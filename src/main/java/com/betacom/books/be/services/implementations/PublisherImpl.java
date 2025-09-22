package com.betacom.books.be.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.books.be.dto.PublisherDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Publisher;
import com.betacom.books.be.repositories.IPublisherRepository;
import com.betacom.books.be.requests.PublisherReq;
import com.betacom.books.be.services.interfaces.IPublisherServices;
import com.betacom.books.be.utils.UtilsPublisher;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PublisherImpl extends UtilsPublisher implements IPublisherServices {
	private IPublisherRepository publishRep;
	
	public PublisherImpl(IPublisherRepository publishRep) {
		this.publishRep = publishRep;
	}
	
	@Override
	public List<PublisherDTO> getAll() throws BooksException {
		log.debug("getAll Publisher");
		List<Publisher> publisherL = publishRep.findAll();
		return buildPublisherDTOList(publisherL);
	}
	
	@Override
	public PublisherDTO getById(Integer id) throws BooksException {
		log.debug("getById Publisher");
		Optional<Publisher> publisher = publishRep.findById(id);
		
		if(publisher.isEmpty()) {
			throw new BooksException("Publisher non trovato");
		}
		
		Publisher p = publisher.get();
		
		return buildPublisherDTO(p);
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public PublisherDTO create(PublisherReq req) throws BooksException {
		log.debug("create Publisher");
		log.debug(req);
		Publisher p = new Publisher();
		Optional<Publisher> publisher = publishRep.findByName(req.getName());
		
		if(publisher.isPresent()) {
			throw new BooksException("OrderItem esistente");
		}
		
		if (req.getName() == null)
			throw new BooksException("Name obbligatorio");
		if (req.getDescription() == null)
			throw new BooksException("Description obbligatorio");
		
		p.setName(req.getName());
		p.setDescription(req.getDescription());
		
		publishRep.save(p);
		return buildPublisherDTO(p);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void update(PublisherReq req) throws BooksException {
		log.debug("update Publisher");
		log.debug(req);
		Optional<Publisher> publisher = publishRep.findById(req.getId());
		
		if(publisher.isEmpty()) {
			throw new BooksException("Publisher non trovato");
		}
		Publisher p = publisher.get();
		
		if (req.getName() != null)
			p.setName(req.getName());
		if (req.getDescription() != null)
			p.setDescription(req.getDescription());

		publishRep.save(p);
		
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(PublisherReq req) throws BooksException {
		log.debug("delete Publisher");
		Optional<Publisher> publisher = publishRep.findById(req.getId());
		
		if(publisher.isEmpty()) {
			throw new BooksException("Publisher non trovato");
		}
		
		publishRep.delete(publisher.get());
		
	}

	

}
