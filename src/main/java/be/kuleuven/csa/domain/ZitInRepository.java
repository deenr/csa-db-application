package be.kuleuven.csa.domain;

import java.util.List;

public interface ZitInRepository {

    List<ZitIn> getAlleZitInByVerkoopID(int verkoop_id);
}
