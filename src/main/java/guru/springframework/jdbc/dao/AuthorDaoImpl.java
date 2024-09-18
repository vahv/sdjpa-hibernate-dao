package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jt on 8/28/21.
 */
@Component
public class AuthorDaoImpl implements AuthorDao {

    private final EntityManagerFactory emf;

    public AuthorDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Author findAuthorByNameNative(String firstName, String lastName) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createNativeQuery(
                    "SELECT * FROM author a where a.first_name = ? AND a.last_name = ?",
                    Author.class);
            query.setParameter(1, firstName);
            query.setParameter(2, lastName);
            return (Author) query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public Author findAuthorByNameCriteria(String firstName, String lastName) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Author> criteriaQuery = criteriaBuilder.createQuery(Author.class);

            Root<Author> root = criteriaQuery.from(Author.class);

            ParameterExpression<String> firstNameParameter = criteriaBuilder.parameter(String.class);
            ParameterExpression<String> lastNameParameter = criteriaBuilder.parameter(String.class);

            Predicate firstNamePredicate = criteriaBuilder.equal(root.get("firstName"), firstNameParameter);
            Predicate lastNamePredicate = criteriaBuilder.equal(root.get("lastName"), lastNameParameter);

            criteriaQuery.select(root).where(criteriaBuilder.and(firstNamePredicate, lastNamePredicate));

            TypedQuery<Author> typedQuery = em.createQuery(criteriaQuery);
            typedQuery.setParameter(firstNameParameter, firstName);
            typedQuery.setParameter(lastNameParameter, lastName);

            return typedQuery.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Author> findAll() {
        EntityManager em = getEntityManager();
        try {
            var theQuery = em.createNamedQuery(
                    "find_all_author", Author.class);
            return theQuery.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Author> listAuthorByLastNameLike(String lastName) {
        EntityManager em = getEntityManager();
        try {
            Query theQuery = em.createQuery("select a from Author a where a.lastName like :last_name");
            theQuery.setParameter("last_name", "%" + lastName + "%");
            List<Author> results = theQuery.getResultList();
            return results;
        } finally {
            em.close();
        }
    }

    @Override
    public Author getById(Long id) {
        EntityManager em = getEntityManager();
        Author author = getEntityManager().find(Author.class, id);
        em.close();
        return author;
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        EntityManager em = getEntityManager();
//        TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a " +
//                "WHERE a.firstName = :first_name and a.lastName = :last_name", Author.class);
        TypedQuery<Author> query = em.createNamedQuery("author_find_by_name", Author.class);
        query.setParameter("first_name", firstName);
        query.setParameter("last_name", lastName);

        Author author = query.getSingleResult();
        em.close();
        return author;
    }

    @Override
    public Author saveNewAuthor(Author author) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(author);
        em.flush();
        em.getTransaction().commit();
        em.close();
        return author;
    }

    @Override
    public Author updateAuthor(Author author) {
        EntityManager em = getEntityManager();

        try {
            em.joinTransaction();
            em.merge(author);
            em.flush();
            em.clear();
            Author saveAuthor = em.find(Author.class, author.getId());
            return saveAuthor;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteAuthorById(Long id) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Author author = em.find(Author.class, id);
        em.remove(author);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    private EntityManager getEntityManager(){
        return emf.createEntityManager();
    }
}
















