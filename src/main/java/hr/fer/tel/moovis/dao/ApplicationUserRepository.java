package hr.fer.tel.moovis.dao;

import hr.fer.tel.moovis.model.ApplicationUser;

import javax.persistence.Table;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Table(name = "application_users")
public interface ApplicationUserRepository extends
		JpaRepository<ApplicationUser, Long> {
	public ApplicationUser findByFacebookId(String facebookId);
}