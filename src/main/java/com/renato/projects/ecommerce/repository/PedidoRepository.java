package com.renato.projects.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.renato.projects.ecommerce.domain.Cliente;
import com.renato.projects.ecommerce.domain.Pedido;
import com.renato.projects.ecommerce.domain.enums.Status;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

	@Query("SELECT p FROM Pedido p "
			+ "JOIN FETCH p.produtosPedidos pr "
			+ "JOIN p.cliente c "
			+ "JOIN c.user u "
			+ "WHERE u.id = :userId AND p.status = 'INICIADO'")
	Optional<Pedido> findCarrinhoByUserId(@Param("userId") Long userId);

	List<Pedido> findByCliente(Cliente cliente);

	Optional<Pedido> findByClienteAndStatus(Cliente cliente, Status status);

//	Optional<Pedido> findByStatus(Status iniciado);

}
