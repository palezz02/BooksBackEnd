package com.betacom.books.be.services.implementations;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.books.be.dto.InventoryDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Inventory;
import com.betacom.books.be.repositories.IInventoryRepository;
import com.betacom.books.be.requests.InventoryReq;
import com.betacom.books.be.services.interfaces.IInventoryServices;
import com.betacom.books.be.utils.UtilsInventory;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class InventoryImpl implements IInventoryServices {

	private IInventoryRepository inventoryRepository;

	public InventoryImpl(IInventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}

	@Override
	public InventoryDTO getById(Integer id) throws BooksException {
		log.debug("Inventory id:" + id);
		Optional<Inventory> inventory = inventoryRepository.findById(id);

		if (inventory.isEmpty())
			throw new BooksException("Inventory with id: " + id + " doesn't exist");

		Inventory i = inventory.get();

		return UtilsInventory.buildInventoryDTO(i);

	}

	@Override
	public void create(InventoryReq req) throws BooksException {
		log.debug("Create: " + req);
		Inventory i = new Inventory();
		Optional<Inventory> inventory = inventoryRepository.findById(req.getId());

		if (inventory.isPresent())
			throw new BooksException("Inventory with id: " + req.getId() + " already exists");

		if (req.getStock() == null)
			throw new BooksException("Stock mandatory");
		if (req.getPrice() == null)
			throw new BooksException("Price mandatory");
		if (req.getUpdatedAt() == null)
			throw new BooksException("Order number mandatory");

		i.setId(req.getId());
		i.setStock(req.getStock());
		i.setPrice(req.getPrice());
		i.setUpdatedAt(req.getUpdatedAt());
		inventoryRepository.save(i);

	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void update(InventoryReq req) throws BooksException {
		log.debug("Update: " + req);

		Optional<Inventory> inventory = inventoryRepository.findById(req.getId());

		if (inventory.isEmpty())
			throw new BooksException("Inventory with id: " + req.getId() + " doesn't exist");

		Inventory i = new Inventory();

		if ((req.getStock() != null)) {
			i.setStock(req.getStock());
		}

		if ((req.getPrice() != null)) {
			i.setPrice(req.getPrice());
		}

		if ((req.getUpdatedAt() != null)) {
			i.setUpdatedAt(req.getUpdatedAt());
		}

		inventoryRepository.save(i);
	}

	@Override
	public void delete(InventoryReq req) throws BooksException {
		log.debug("Delete: " + req);

		Optional<Inventory> inventory = inventoryRepository.findById(req.getId());

		if (inventory.isEmpty())
			throw new BooksException("Inventory with id: " + req.getId() + " doesn't exist");

		if (!inventory.get().getOrderItems().isEmpty())
			throw new BooksException("Inventory with id: " + req.getId() + " doesn't have items");

	}
}
