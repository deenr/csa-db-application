package be.kuleuven.csa.jdbi;

import be.kuleuven.csa.domain.*;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class VerkooptRepositoryJdbi3Impl implements VerkooptRepository {

    private final Jdbi jdbi;

    public VerkooptRepositoryJdbi3Impl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }


    @Override
    public List<Verkoopt> getVerkooptByBoerAndPakket(int auteur_id, int pakket_id) {
        var query = "SELECT * FROM Verkoopt v WHERE v.auteur_id = " + auteur_id + " AND v.pakket_id = " + pakket_id + ";";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Verkoopt.class)
                    .list();
        });
    }

    @Override
    public void wijzigSchrijftIn(SchrijftIn schrijftIn) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("UPDATE SchrijftIn SET verkoopt_id = ? WHERE auteur_id = ? AND schrijftIn_id = ?")
                    .bind(0, schrijftIn.getVerkoopt_id())
                    .bind(1, schrijftIn.getAuteur_id())
                    .bind(2, schrijftIn.getSchrijftIn_id())
                    .execute();
        });
    }

    @Override
    public void wijzigHaaltAf(HaaltAf haaltAf) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("UPDATE HaaltAf SET verkoopt_id = ?, pakket_weeknr = ?, pakket_afgehaald = ? WHERE auteur_id = ? AND haaltAf_id = ?")
                    .bind(0, haaltAf.getVerkoopt_id())
                    .bind(1, haaltAf.getPakket_weeknr())
                    .bind(2, haaltAf.getPakket_afgehaald())
                    .bind(3, haaltAf.getAuteur_id())
                    .bind(4, haaltAf.getHaaltAf_id())
                    .execute();
        });
    }

    public List<HaaltAf> getHaaltAfByKlantEnVerkoopt(int auteur_id, int verkoopt_id) {
        var query = "SELECT * FROM HaaltAf h WHERE h.verkoopt_id = " + verkoopt_id + " AND h.auteur_id = " + auteur_id + ";";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(HaaltAf.class)
                    .list();
        });
    }

    public List<SchrijftIn> getSchrijftInByKlantEnVerkoopt(int auteur_id, int verkoopt_id) {
        var query = "SELECT * FROM SchrijftIn s WHERE s.verkoopt_id = " + verkoopt_id + " AND s.auteur_id = " + auteur_id + ";";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(SchrijftIn.class)
                    .list();
        });
    }

    @Override
    public List<Verkoopt> getVerkooptByKlantName(String naam) {
        var query = "SELECT v.verkoopt_id, v.auteur_id, v.pakket_id, v.verkoopt_prijs FROM Auteur a, Klant k, SchrijftIn s, Verkoopt v, Pakket p WHERE auteur_naam = '" + naam + "' AND a.auteur_id = k.auteur_id AND k.auteur_id = s.auteur_id AND s.verkoopt_id = v.verkoopt_id AND v.pakket_id = p.pakket_id";
        return jdbi.withHandle(handle -> {
            return handle.createQuery(query)
                    .mapToBean(Verkoopt.class)
                    .list();
        });
    }

    @Override
    public void voegHaaltAfToe(HaaltAf haaltAf) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO HaaltAf(auteur_id, verkoopt_id, pakket_weeknr, pakket_afgehaald) VALUES (?,?,?,?);")
                    .bind(0, haaltAf.getAuteur_id())
                    .bind(1, haaltAf.getVerkoopt_id())
                    .bind(2, haaltAf.getPakket_weeknr())
                    .bind(3, haaltAf.getPakket_afgehaald())
                    .execute();
        });
    }

    @Override
    public void voegSchijftInToe(SchrijftIn schrijftIn) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO SchrijftIn(auteur_id,verkoopt_id) VALUES (?,?);")
                    .bind(0, schrijftIn.getAuteur_id())
                    .bind(1, schrijftIn.getVerkoopt_id())
                    .execute();
        });
    }
}
