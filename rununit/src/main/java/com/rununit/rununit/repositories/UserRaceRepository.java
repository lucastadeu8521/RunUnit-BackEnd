package com.rununit.rununit.repositories;

import com.rununit.rununit.entities.UserRace;
import com.rununit.rununit.entities.UserRaceId;
import com.rununit.rununit.entities.enums.UserRaceTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para a entidade UserRace, responsável pela comunicação com o banco de dados.
 * Utiliza a entidade UserRace e a chave primária composta UserRaceId.
 */
@Repository
public interface UserRaceRepository extends JpaRepository<UserRace, UserRaceId> {

    /**
     * Encontra todas as participações (UserRace) de um usuário específico.
     * @param userId O ID do usuário (parte da chave composta).
     * @return Lista de UserRace.
     */
    List<UserRace> findByIdUserId(Long userId);

    /**
     * Encontra todas as participações (UserRace) em uma corrida específica.
     * @param raceId O ID da corrida (parte da chave composta).
     * @return Lista de UserRace.
     */
    List<UserRace> findByIdRaceId(Long raceId);

    /**
     * Encontra todas as participações (UserRace) de um usuário que estão ativas.
     * @param userId O ID do usuário.
     * @param active O status de atividade (deve ser 'true' para ativos).
     * @return Lista de UserRace.
     */
    List<UserRace> findByIdUserIdAndActive(Long userId, Boolean active);

    /**
     * Encontra participações com uma Tag específica para uma dada corrida.
     * @param raceId O ID da corrida.
     * @param tag A tag de participação (ex: INSCRITO, CONCLUIDO).
     * @return Lista de UserRace.
     */
    List<UserRace> findByIdRaceIdAndTag(Long raceId, UserRaceTag tag);

    /**
     * Encontra uma participação específica por ID de usuário e ID de corrida.
     * @param userId O ID do usuário.
     * @param raceId O ID da corrida.
     * @return Um Optional contendo UserRace, se encontrado.
     */
    Optional<UserRace> findByIdUserIdAndIdRaceId(Long userId, Long raceId);
}