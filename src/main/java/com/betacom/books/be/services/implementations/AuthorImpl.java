package com.betacom.books.be.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.books.be.dto.AuthorDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Author;
import com.betacom.books.be.repositories.IAuthorRepository;
import com.betacom.books.be.requests.AuthorReq;
import com.betacom.books.be.services.interfaces.IAuthorService;
import com.betacom.books.be.utils.UtilsAddressAuthor;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class AuthorImpl extends UtilsAddressAuthor implements IAuthorService {

    private final IAuthorRepository authorR;

    public AuthorImpl(IAuthorRepository authorR) {
        this.authorR = authorR;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer create(AuthorReq req) throws BooksException {
        log.debug("create :" + req);

        if (req.getFullName() == null || req.getFullName().isBlank()) {
            throw new BooksException("Full name is required");
        }

        Author author = new Author();
        author.setFullName(req.getFullName());
        author.setBiography(req.getBiography());
        author.setBirthDate(req.getBirthDate());
        author.setDeathDate(req.getDeathDate());
        author.setCoverImageUrl(req.getCoverImageUrl());

        return authorR.save(author).getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(AuthorReq req) throws BooksException {
        log.debug("delete :" + req);

        Optional<Author> authorOpt = authorR.findById(req.getId());
        if (authorOpt.isEmpty()) {
            throw new BooksException("Author not found with id " + req.getId());
        }

        Author author = authorOpt.get();

        if (author.getBooks() != null && !author.getBooks().isEmpty()) {
            throw new BooksException("Author has associated books and cannot be deleted");
        }

        authorR.delete(author);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(AuthorReq req) throws BooksException {
        log.debug("update :" + req);

        Optional<Author> authorOpt = authorR.findById(req.getId());
        if (authorOpt.isEmpty()) {
            throw new BooksException("Author not found with id " + req.getId());
        }

        Author author = authorOpt.get();

        if (req.getFullName() != null) {
            author.setFullName(req.getFullName());
        }
        if (req.getBiography() != null) {
            author.setBiography(req.getBiography());
        }
        if (req.getBirthDate() != null) {
            author.setBirthDate(req.getBirthDate());
        }
        if (req.getDeathDate() != null) {
            author.setDeathDate(req.getDeathDate());
        }
        if (req.getCoverImageUrl() != null) {
            author.setCoverImageUrl(req.getCoverImageUrl());
        }

        authorR.save(author);
    }

    @Override
    public List<AuthorDTO> getAll() {
        List<Author> authors = authorR.findAll();
        return buildListAuthorDTO(authors);
    }

//	@Override
//	public AuthorDTO getById(Integer id) throws BooksException {
//		log.debug("getById: ", id);
//		Author author = authorR.findById(id)
//				.orElseThrow(() -> new BooksException("Author with ID " + id + " not found."));
//		return UtilsAddressAuthor.buildListAuthorDTO(author);
//	}

	@Override
	public AuthorDTO getById(Integer id) throws BooksException {
		log.debug("getById Publisher");
		Optional<Author> author = authorR.findById(id);

		if(author.isEmpty()) {
			throw new BooksException("Publisher non trovato");
		}

		Author a = author.get();

		return buildAuthorDTO(a);
	}
}
