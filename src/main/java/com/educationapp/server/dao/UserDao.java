package com.educationapp.server.dao;

import com.educationapp.server.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserDao {

    protected EntityManager entityManager;
    protected final Class<User> entityClass;

    protected UserDao() {
        this.entityClass = User.class;
    }

    public User selectById(final Long id) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        final Root<User> root = criteriaQuery.from(entityClass);

        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get("id"), id));

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    public List<User> getUserByFilter(final String name, final String lastName) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        final Root<User> root = criteriaQuery.from(entityClass);

        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get("name"), name),
                        criteriaBuilder.equal(root.get("lastName"), lastName));

        return entityManager.createQuery(criteriaQuery)
                .getResultList();
    }

    public List<User> getAllUsers() {
        return entityManager.createQuery(entityManager.getCriteriaBuilder()
                .createQuery(entityClass))
                .getResultList();
    }

    private void insert(User user) {
        entityManager.persist(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public User createEntity(final User newEntity) {

        final User existingEntity = selectById(newEntity.getId());

        if (existingEntity != null) {
            return newEntity;
        }

        insert(newEntity);

        return newEntity;
    }
}
