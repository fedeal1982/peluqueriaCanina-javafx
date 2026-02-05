package com.mycompany.pelucanina.persistencia;

import com.mycompany.pelucanina.logica.Mascota;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

public class MascotaJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public MascotaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("PeluCaninaPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Mascota mascota) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            // Si el dueño no tiene ID, es nuevo
            if (mascota.getUnduenio() != null && mascota.getUnduenio().getIdDuenio() == null) {
                em.persist(mascota);
            } else {
                em.merge(mascota);
            }

            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Mascota mascota) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            mascota = em.merge(mascota);
            em.getTransaction().commit();
        } catch (Exception ex) {
            Integer id = mascota.getNumCliente();
            if (findMascota(id) == null) {
                throw new Exception("La mascota con id " + id + " ya no existe.");
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Mascota mascota;
            try {
                mascota = em.getReference(Mascota.class, id);
                mascota.getNumCliente();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("La mascota con id " + id + " ya no existe.", enfe);
            }
            em.remove(mascota);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Mascota> findMascotaEntities() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Mascota.class));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Mascota findMascota(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Mascota.class, id);
        } finally {
            em.close();
        }
    }
}