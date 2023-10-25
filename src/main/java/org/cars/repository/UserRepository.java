package org.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.cars.model.User;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;

    public User create(User user) {
        try (Session session = sf.openSession()) {
            session.save(user);
            return user;
        }
    }

    public void update(User user) {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("UPDATE User SET login = :login, password = :password WHERE id = :id")
                    .setParameter("password", user.getPassword())
                    .setParameter("login", user.getLogin())
                    .setParameter("id", user.getId())
                    .executeUpdate();
            transaction.commit();
        }
    }

    public void delete(int userId) {
        try (Session session = sf.openSession()) {
            User user = session.get(User.class, userId);
            if (user != null) {
                session.beginTransaction();
                session.delete(user);
                session.getTransaction().commit();
            }
        }
    }

    public List<User> findAllOrderById() {
        try (Session session = sf.openSession()) {
            List<User> users = session.createQuery("FROM User ORDER BY id", User.class).list();
            return users;
        }
    }

    public Optional<User> findById(int userId) {
        try (Session session = sf.openSession()) {
            User user = session.get(User.class, userId);
            return Optional.ofNullable(user);
        }
    }

    public List<User> findByLikeLogin(String key) {
        try (Session session = sf.openSession()) {
            List<User> users = session.createQuery("FROM User WHERE login LIKE :key", User.class)
                    .setParameter("key", "%" + key + "%")
                    .list();
            return users;
        }
    }

    public Optional<User> findByLogin(String login) {
        try (Session session = sf.openSession()) {
            User user = session.createQuery("FROM User WHERE login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResult();
            return Optional.ofNullable(user);
        }
    }
}