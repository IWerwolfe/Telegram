package com.telegrambot.app.repositories;

import com.telegrambot.app.model.legalentity.Company;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends LegalEntityRepository<Company> {
}
