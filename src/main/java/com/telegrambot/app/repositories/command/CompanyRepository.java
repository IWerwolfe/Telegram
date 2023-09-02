package com.telegrambot.app.repositories.command;

import com.telegrambot.app.model.reference.legalentity.Company;
import com.telegrambot.app.repositories.reference.LegalEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends LegalEntityRepository<Company> {
}
